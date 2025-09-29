/* global bootstrap, eval, cedula, nombreClienteParrafo, clientesRegistrados, resultadoDiv, btnAceptarCliente, cliente, productosVenta, encontrados */

// --- Variables de Estado Global ---
window.clienteSeleccionado = null;
window.inventario = [];
window.carrito = [];
window.productosVenta = [];
window.totalVentaFinal = 0;
window.subtotalVenta = 0;
window.descuentoVenta = 0;
window.cantidadProductos = 0;
window.descuentoAplicadoPorcentaje = 0;
window.valorDescuentoAplicado = 0;
window.metodoDePagoSeleccionado = 'Efectivo';
window.metodoLecturaSeleccionado = 'teclado';
window.calcValor = '';
window.usuarioActivo = null;

// --- Función para cargar inventario ---
async function cargarProductosDesdeBD() {
    console.log('Iniciando la carga del inventario desde la base de datos...');
    try {
        const resp = await fetch('VenderServlet?accion=listarInventarioJson');
        const data = await resp.json();
        console.log('Datos de inventario recibidos exitosamente:', data);
        window.inventario = data;
        mostrarTodosLosProductos();
    } catch (error) {
        console.error('Error fatal al cargar el inventario:', error);
    }
}

// --- Función para actualizar totales en la UI ---
function actualizarTotalesUI() {
    window.subtotalVenta = 0;
    window.descuentoVenta = 0;
    window.totalVentaFinal = 0;

    if (window.productosVenta.length > 0) {
        window.productosVenta.forEach(p => {
            const subtotalProducto = p.precio_unitario * p.cantidad;
            const descuentoProducto = subtotalProducto * (p.descuento_porcentaje / 100 || 0);
            const totalConDescuento = subtotalProducto - descuentoProducto;

            p.subtotal = subtotalProducto;
            p.descuento = descuentoProducto;
            p.total_con_descuento = totalConDescuento;

            window.subtotalVenta += subtotalProducto;
            window.descuentoVenta += descuentoProducto;
            window.totalVentaFinal += totalConDescuento;
        });
    }

    const subtotalEl = document.getElementById('subtotalVenta');
    const descuentoEl = document.getElementById('descuentoVenta');
    const totalEl = document.getElementById('totalVenta');

    if (subtotalEl) subtotalEl.textContent = `$${window.subtotalVenta}`;
    if (descuentoEl) descuentoEl.textContent = `$${window.descuentoVenta}`;
    if (totalEl) totalEl.textContent = `$${window.totalVentaFinal}`;
}

// --- Configuración Inicial de la UI ---
document.getElementById('totalVenta').textContent = '$0';
document.getElementById('btnFinalizarVenta').disabled = true;
document.getElementById('metodoPagoSeleccionado').textContent = window.metodoDePagoSeleccionado;
document.querySelectorAll('.metodo-pago-btn').forEach(btn => btn.classList.remove('active'));
const botonefectivoInicial = Array.from(document.querySelectorAll('.metodo-pago-btn'))
    .find(btn => btn.getAttribute('data-metodo') === 'Efectivo');
if (botonefectivoInicial) botonefectivoInicial.classList.add('active');

const descuentoInput = document.getElementById('inputDescuento');
if (descuentoInput) descuentoInput.value = '';

actualizarTotalesUI();

function crearTarjetaProducto(p) {
    const col = document.createElement('div');
    col.className = 'col-sm-6 col-md-4 col-lg-3 mb-3';
    col.innerHTML = `
        <div class="card h-100 shadow-sm" style="font-size: 0.85rem; cursor: pointer;">
            <div class="card-body">
                <h6 class="card-title">${p.nombre}</h6>
                <p class="card-text mb-1">
                    <strong>Stock:</strong> ${p.stock}<br>
                    <i class="bi bi-upc-scan"></i> ${p.sku}<br>
                    <strong>Precio:</strong> $${formatNumberWithCommas(p.precio_venta)}
                </p>
            </div>
        </div>
    `;
    col.querySelector('.card').addEventListener('click', () => {
        agregarAlCarrito(p);
    });
    return col;
}

//Muestra todos los productos del inventario global en el área de visualización.
function mostrarTodosLosProductos() {
    console.log('Mostrando todos los productos en la vista.');
    const contenedorCards = document.getElementById('contenedorCards');
    contenedorCards.innerHTML = '';
    const fila = document.createElement('div');
    fila.className = 'row';
    if (Array.isArray(window.inventario)) {
        window.inventario.forEach(p => {
            fila.appendChild(crearTarjetaProducto(p));
        });
    } else {
        console.warn('El inventario no es un array. No se pueden mostrar productos.');
    }
    contenedorCards.appendChild(fila);
}

//Filtra y muestra productos según un término de búsqueda.
function buscarProducto(event) {
    if (event) event.preventDefault();
    const input = document.getElementById('campoBuscarDesktop');
    const valor = (input && input.value || '').trim().toLowerCase();
    console.log(`Iniciando búsqueda de producto con el término: "${valor}"`);
    
    const contenedorCards = document.getElementById('contenedorCards');
    contenedorCards.innerHTML = '';

    if (!valor) {
        console.log('Término de búsqueda vacío, mostrando todos los productos.');
        mostrarTodosLosProductos();
        return;
    }

    // Filtrado seguro con ?. para evitar errores de undefined
    let encontrados = window.inventario.filter(p =>
        (p.nombre?.toLowerCase().includes(valor)) ||
        (p.codigo?.toLowerCase().includes(valor)) ||
        (p.categoria?.toLowerCase().includes(valor))
    );

    console.log(`Se encontraron ${encontrados.length} productos que coinciden con "${valor}".`);
    if (encontrados.length === 0) {
        contenedorCards.innerHTML = '<div class="alert alert-warning">No se encontraron productos.</div>';
        return;
    }

    const fila = document.createElement('div');
    fila.className = 'row';
    encontrados.forEach(p => fila.appendChild(crearTarjetaProducto(p)));
    contenedorCards.appendChild(fila);

    // Deselecciona categorías activas
    document.querySelectorAll('#listaCategoriasDesktop li, #listaCategoriasOffcanvas li').forEach(li => li.classList.remove('seleccionado'));
}

//Limpia el campo de entrada de búsqueda.
function limpiarBusqueda() {
    const input = document.getElementById('campoBuscarDesktop');
    if (input) input.value = '';
    console.log('Campo de búsqueda limpio.');
}

// Agrega un producto al carrito de compras o incrementa su cantidad si ya existe.
function agregarAlCarrito(producto) {
    console.log('Producto a agregar:', producto);
    const carritoLista = document.getElementById('carritoLista');

    // Busca si el producto ya está en el carrito
    const productoExistente = carritoLista.querySelector(`li[data-id-producto="${producto.idProducto}"]`);

    if (productoExistente) {
        // Incrementa cantidad en input
        const cantidadInput = productoExistente.querySelector('.cantidad-input');
        cantidadInput.value = parseInt(cantidadInput.value) + 1;

        // Actualiza producto en array
        let productoPrevio = window.productosVenta.find(p => p.idProducto === producto.idProducto);
        if (productoPrevio) {
            productoPrevio.cantidad += 1;
            productoPrevio.subtotal = productoPrevio.cantidad * productoPrevio.precio_unitario * (1 - (productoPrevio.descuento_porcentaje || 0) / 100);
        }

        // Actualiza UI
        productoExistente.querySelector('.total-item').textContent = `$${formatNumberWithCommas(productoPrevio.subtotal)}`;
    } 
    else {
        // Validación
        if (!producto.idProducto) {
            console.error(`Producto "${producto.nombre}" no tiene idProducto definido. No se puede agregar al carrito.`);
            alert(`Error: el producto "${producto.nombre}" no tiene ID y no puede ser agregado.`);
            return;
        }

        // Crear nodo en carrito
        const li = document.createElement('li');
        li.className = 'd-flex justify-content-between align-items-center border-bottom py-2';
        li.setAttribute('data-id-producto', producto.idProducto);
        li.innerHTML = `
          <div class="me-2">
              <input type="number" min="1" value="1" class="form-control form-control-sm text-center cantidad-input"
                     style="width: 60px;" data-precio_venta="${producto.precio_venta ?? 0}" />
          </div>
          <div class="flex-fill px-2">
              <div class="fw-bold">${producto.nombre}</div>
              <div class="text-success fw-bold total-item">$${formatNumberWithCommas(producto.precio_venta ?? 0)}</div>
              <div class="mt-1">
                  <button class="btn btn-sm btn-outline-warning aplicar-descuento-btn" title="Aplicar descuento a este producto">
                    <i class="bi bi-tag"></i> Descuento
                  </button>
                  <span class="badge bg-warning text-dark descuento-producto" style="display:none;"></span>
              </div>
          </div>
          <div class="text-end px-2">
             <small class="text-muted">Unidad: <span class="text-primary fw-semibold">$${formatNumberWithCommas(producto.precio_venta ?? 0)}</span></small>
          </div>
          <button class="btn btn-sm btn-outline-danger ms-2" title="Eliminar" data-action="eliminar-item">
             <i class="bi bi-trash"></i>
          </button>
        `;

        carritoLista.appendChild(li);

        // Eventos
        const cantidadInput = li.querySelector('.cantidad-input');
        cantidadInput.addEventListener('input', () => {
            actualizarTotalItem(cantidadInput);
            actualizarMensajeDescuentoProducto();
        });
        li.querySelector('[data-action="eliminar-item"]').addEventListener('click', e => eliminarItemCarrito(e.target));

        // Agregar al array productosVenta
        window.productosVenta.push({
            idProducto: producto.idProducto,
            nombre: producto.nombre,
            cantidad: 1,
            precio_unitario: producto.precio_venta ?? 0,
            impuesto_porcentaje: 0,
            descuento_porcentaje: producto.descuento_porcentaje || 0,
            subtotal: producto.precio_venta ?? 0
        });
    }

    // Recalcular y actualizar
    console.log('Array final productosVenta:', window.productosVenta);
    actualizarCantidadProductos();
    calcularTotalVentaFinal();
    actualizarBotonFinalizarVenta();
    actualizarMensajeDescuentoProducto();
}

// Función que se asegura de actualizar subtotal al cambiar cantidad
function actualizarTotalItem(cantidadInput) {
    const li = cantidadInput.closest('li');
    const idProducto = parseInt(li.getAttribute('data-id-producto'));
    let cantidad = parseInt(cantidadInput.value);
    if (isNaN(cantidad) || cantidad < 1) cantidad = 1;
    cantidadInput.value = cantidad;
    const precio_unitario = parseFloat(cantidadInput.getAttribute('data-precio_venta') || '0');
s
    let productoPrevio = window.productosVenta.find(p => p.idProducto === idProducto);
    let descuento_porcentaje = productoPrevio ? productoPrevio.descuento_porcentaje : 0;
    const subtotal = parseFloat((cantidad * precio_unitario * (1 - descuento_porcentaje / 100)).toFixed(2));

    // Actualiza array y UI
    if (productoPrevio) {
        productoPrevio.cantidad = cantidad;
        productoPrevio.subtotal = subtotal;
    }
    li.querySelector('.total-item').textContent = `$${formatNumberWithCommas(subtotal)}`;
    calcularTotalVentaFinal();
}

function eliminarItemCarrito(botonEliminar) {
    const itemCarrito = botonEliminar.closest('li');
    if (itemCarrito) {
        const idProducto = parseInt(itemCarrito.getAttribute('data-id-producto'));

        // Eliminar del array global productosVenta
        window.productosVenta = window.productosVenta.filter(p => p.idProducto !== idProducto);

        const nombreProducto = itemCarrito.querySelector('.fw-bold').textContent;
        console.log(`Eliminando producto del carrito: ${nombreProducto}`);
        itemCarrito.remove();

        verificarCarritoVacio();
        calcularTotalVentaFinal();
        actualizarCantidadProductos();
    } else {
        console.warn('No se encontró el elemento <li> para eliminar.');
    }
}

    //Actualiza el precio total de un solo artículo en el carrito cuando su cantidad cambia.
    function actualizarTotalItem(input) {
        const cantidad = Math.max(1, parseInt(input.value) || 1);
        input.value = cantidad;
        const precioUnidad = parseInt(input.getAttribute('data-precio_venta'));
    const li = input.closest('li');
    const idProducto = parseInt(li.getAttribute('data-id-producto'));
    const productoPrevio = window.productosVenta.find(p => p.idProducto === idProducto);
    const descuento_porcentaje = productoPrevio ? productoPrevio.descuento_porcentaje : 0;
    const subtotal = parseFloat((cantidad * precioUnidad * (1 - descuento_porcentaje / 100)).toFixed(2));;

    if (productoPrevio) {
        productoPrevio.cantidad = cantidad;
        productoPrevio.subtotal = subtotal;
    }
    li.querySelector('.total-item').textContent = `$${formatNumberWithCommas(subtotal)}`;
    
    console.log(`Ítem actualizado. Cantidad: ${cantidad}, Precio: ${precioUnidad}, Total Ítem: ${subtotal}`);
        calcularTotalVentaFinal();
        actualizarCantidadProductos();
    }

//Calcula y muestra el total final de toda la venta, incluyendo descuentos.
function calcularTotalVentaFinal() {
    let sumaTotal = 0;

    if (window.productosVenta && window.productosVenta.length > 0) {
        window.productosVenta.forEach(p => {
        let subtotalProducto = (p.subtotal !== undefined && p.descuento_porcentaje !== undefined) 
            ? parseFloat(p.subtotal) 
            : parseInt(p.cantidad) * parseFloat(p.precio_unitario || 0);
            sumaTotal += subtotalProducto;
        });
    }
    window.totalVentaFinal = sumaTotal;

    // Si hay descuento general sobre toda la venta
    let totalConDescuento = sumaTotal - (sumaTotal * (window.descuentoAplicadoPorcentaje / 100));
    window.valorDescuentoAplicado = 0;

    // Calcula el descuento total aplicado (individual + general)
    if (window.productosVenta && window.productosVenta.length > 0) {
    let subtotalSinDescuento = 0;
    window.productosVenta.forEach(p => {
        subtotalSinDescuento += p.subtotal !== undefined ? parseFloat(p.subtotal) : (Number(p.cantidad) * parseFloat(p.precio_unitario || 0));
    });
    window.valorDescuentoAplicado = subtotalSinDescuento - totalConDescuento;
}

    document.getElementById('totalVenta').textContent = `$${formatNumberWithCommas(totalConDescuento)}`;
    document.getElementById('totalVentaHidden').value = totalConDescuento;

    // Actualiza el texto de % descuento en el botón cobrar
    const descuentoAplicadoTextoElement = document.getElementById('descuentoAplicadoTexto');
    if (descuentoAplicadoTextoElement) {
        descuentoAplicadoTextoElement.textContent = window.valorDescuentoAplicado > 0
        ? `-${formatNumberWithCommas(window.valorDescuentoAplicado)}`: '';
    }
    console.log(`Total Venta: $${totalConDescuento}`);
}

function verificarCarritoVacio() {
    const carritoLista = document.getElementById('carritoLista');
    if (carritoLista.children.length === 0) {
        console.log('El carrito está vacío. Reseteando totales.');
        window.totalVentaFinal = 0;
        window.cantidadProductos = 0;
        window.productosVenta = []; // limpia el array global
        document.getElementById('totalVenta').textContent = '$0';
        document.getElementById('btnFinalizarVenta').disabled = true;
        actualizarCantidadProductos();
        eliminarDescuento();
    } else {
        console.log('El carrito todavía tiene productos.');
        actualizarBotonFinalizarVenta();
    }
}

// Habilita o deshabilita el botón "Finalizar Venta" según el contenido del carrito.
function actualizarBotonFinalizarVenta() {
    const isDisabled = document.getElementById('carritoLista').children.length === 0;
    document.getElementById('btnFinalizarVenta').disabled = isDisabled;
    console.log(`Botón 'Finalizar Venta' estado: ${isDisabled ? 'deshabilitado' : 'habilitado'}`);
}

//Aplica un descuento al total de la venta desde el modal de descuento.
function aplicarDescuentoModal() {
    const descuentoInput = document.getElementById('inputDescuento');
    const porcentaje = parseFloat(descuentoInput.value);
    console.log(`Intentando aplicar un descuento del ${porcentaje}%.`);

    if (!isNaN(porcentaje) && porcentaje >= 0 && porcentaje <= 100) {
        window.descuentoAplicadoPorcentaje = porcentaje;
        calcularValorDescuento();
        actualizarMensajeDescuento();
        calcularTotalVentaFinal();
        console.log(`Descuento del ${porcentaje}% aplicado correctamente.`);
    } else {
        console.warn('Intento de aplicar un descuento inválido.');
        alert('Por favor, ingrese un porcentaje de descuento válido (0-100).');
    }
}

//Calcula el valor monetario del descuento porcentual actual.
function calcularValorDescuento() {
    window.valorDescuentoAplicado = window.totalVentaFinal * (window.descuentoAplicadoPorcentaje / 100);
    console.log(`Valor del descuento calculado: $${window.valorDescuentoAplicado}`);
}

//Actualiza la interfaz de usuario para mostrar la información del descuento aplicado.

// General
function actualizarMensajeDescuento() {
  console.log('Actualizando descuento general');

  const porcentajeEl = document.getElementById('valorDescuentoPorcentaje');
  const monedaEl = document.getElementById('valorDescuentoMoneda');
  const mensajeEl = document.getElementById('mensajeDescuento');

  if (porcentajeEl && monedaEl && mensajeEl) {
    porcentajeEl.textContent = `${window.descuentoAplicadoPorcentaje}%`;
    monedaEl.textContent = `$${formatNumberWithCommas(window.valorDescuentoAplicado)}`;
    mensajeEl.style.display = 'block';
  }
}

// Producto

function actualizarMensajeDescuentoProducto() {
    const mensajeDescuentoElement = document.getElementById('mensajeDescuentoProducto');
    const valorDescuentoPorcentajeElement = document.getElementById('valorDescuentoPorcentajeProducto');
    const valorDescuentoMonedaElement = document.getElementById('valorDescuentoMonedaProducto');

    if (mensajeDescuentoElement && valorDescuentoPorcentajeElement && valorDescuentoMonedaElement) {
        // Sumar todos los descuentos del carrito
        const totalDescuento = window.productosVenta.reduce((acc, p) => {
            return acc + ((p.precio_unitario * p.cantidad) - p.subtotal);
        }, 0);
        
        // mostrar porcentaje 
        //valorDescuentoPorcentajeElement.textContent = totalDescuento > 0 ? `${(totalDescuento / totalCompraOriginal)*100}%` : '';

        valorDescuentoMonedaElement.textContent = `$${formatNumberWithCommas(totalDescuento)}`;
        mensajeDescuentoElement.style.display = totalDescuento > 0 ? 'block' : 'none';

        // Actualizar botón "Cobrar" con la promoción aplicada
        const descuentoBoton = document.getElementById('descuentoAplicadoTexto');
        if (descuentoBoton) {
            if (totalDescuento > 0) {
                descuentoBoton.textContent = ` - $${formatNumberWithCommas(totalDescuento)}`;
                descuentoBoton.style.display = 'inline';
            } else {
                descuentoBoton.textContent = '';
                descuentoBoton.style.display = 'none';
            }
        }
    }

    // Actualiza total en la UI
    const totalElement = document.getElementById('totalVenta');
    if (totalElement) totalElement.textContent = `$${formatNumberWithCommas(window.totalVentaFinal)}`;
}

function eliminarDescuento() {
    console.log('Eliminando descuento aplicado.');
    window.descuentoAplicadoPorcentaje = 0;
    window.valorDescuentoAplicado = 0;

    const mensajeDescuentoElement = document.getElementById('mensajeDescuento');
    const descuentoAplicadoTextoElement = document.getElementById('descuentoAplicadoTexto');
    const totalVentaElement = document.getElementById('totalVenta');

    mensajeDescuentoElement.style.display = 'none';
    descuentoAplicadoTextoElement.textContent = '';
    totalVentaElement.textContent = `$${formatNumberWithCommas(window.totalVentaFinal)}`;
    document.getElementById('inputDescuento').value = '';

    calcularTotalVentaFinal();
}

//Formatea un número con separadores de miles para la configuración regional colombiana.
function formatNumberWithCommas(number) {
    if (typeof number !== 'number' || isNaN(number)) {
        return '0';
    }
    return number.toLocaleString('es-CO', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
}

//Actualiza la visualización del contador de productos según las cantidades en el carrito.
function actualizarCantidadProductos() {
    const inputs = document.querySelectorAll('#carritoLista .cantidad-input');
    let totalCantidad = 0;
    inputs.forEach(input => {
        totalCantidad += parseInt(input.value) || 0;
    });
    const texto = totalCantidad === 1 ? "01 producto" : totalCantidad.toString().padStart(2, '0') + " productos";
    const cantidadProductosElement = document.getElementById('productosCantidad');
    if (cantidadProductosElement) {
        cantidadProductosElement.textContent = texto;
    }
    console.log(`Cantidad total de productos en el carrito actualizada a: ${totalCantidad}`);
}

//Establece el método de pago seleccionado y actualiza la interfaz de usuario.
function seleccionarMetodoPago(metodo) {
    window.metodoDePagoSeleccionado = metodo;
    document.getElementById('metodoPagoSeleccionado').textContent = metodo;
    const botonesPago = document.querySelectorAll('.metodo-pago-btn');
    botonesPago.forEach(boton => {
        boton.classList.remove('active');
    });
    const botonSeleccionado = document.querySelector(`.metodo-pago-btn[data-metodo="${metodo}"]`);
    if (botonSeleccionado) {
        botonSeleccionado.classList.add('active');
    }
    console.log(`Método de pago seleccionado: ${metodo}`);
}

//Inicia el proceso de venta final abriendo el modal de pago.
function finalizarVenta() {
    console.log('Iniciando proceso de finalización de venta...');
    const totalVentaElement = document.getElementById('totalVenta');
    const totalVentaTexto = totalVentaElement.textContent.replace('$', '').replace(/\./g, '').trim();
    const totalVenta = parseFloat(totalVentaTexto);

    if (isNaN(totalVenta) || totalVenta <= 0) {
        console.error('Intento de finalizar venta sin productos o con total inválido.');
        alert("No hay productos en el carrito o el total es inválido.");
        return;
    }
    console.log(`Total a pagar: $${totalVenta}. Método de pago: ${window.metodoDePagoSeleccionado}`);

    document.getElementById('totalAPagarEfectivo').value = formatNumberWithCommas(totalVenta);
    document.getElementById('totalVentaHidden').value = totalVenta;
    document.getElementById('efectivoRecibido').value = '';
    document.getElementById('cambioDevolver').value = formatNumberWithCommas(0);
    document.getElementById('pagoEfectivo').style.display = 'none';
    document.getElementById('pagoTarjeta').style.display = 'none';
    document.getElementById('pagoQR').style.display = 'none';

    if (window.metodoDePagoSeleccionado === 'Efectivo') {
        console.log('Mostrando panel de pago en Efectivo.');
        document.getElementById('pagoEfectivo').style.display = 'block';
        
    } else if (window.metodoDePagoSeleccionado === 'Crédito' || window.metodoDePagoSeleccionado === 'Tarjeta') {
        console.log('Mostrando panel de pago con Tarjeta.');
        document.getElementById('pagoTarjeta').style.display = 'block';
        
    } else if (window.metodoDePagoSeleccionado === 'QR') {
        console.log('Mostrando panel de pago con QR.');
        document.getElementById('pagoQR').style.display = 'block';
    }
    const modalCobrar = new bootstrap.Modal(document.getElementById('modalCobrar'));
    modalCobrar.show();
}

// ---  botón de cobrar ---
document.addEventListener('DOMContentLoaded', () => {
    const btnCobrar = document.getElementById('btnCobrar');
    if (btnCobrar) {
        btnCobrar.addEventListener('click', (e) => {
    e.preventDefault(); 
    enviarVentaAlBackend();
    });
  }
});

//Calcula el cambio a devolver en una transacción en efectivo.
function calcularCambio() {
    const totalAPagar = parseFloat(document.getElementById('totalVentaHidden').value);
    const efectivoRecibido = parseFloat(document.getElementById('efectivoRecibido').value);
    const cambioDevolverInput = document.getElementById('cambioDevolver');

    if (!isNaN(totalAPagar) && !isNaN(efectivoRecibido)) {
        const cambio = efectivoRecibido - totalAPagar;
        cambioDevolverInput.value = formatNumberWithCommas(cambio);
        console.log(`Cálculo de cambio: Total: ${totalAPagar}, Recibido: ${efectivoRecibido}, Cambio: ${cambio}`);
    } else {
        cambioDevolverInput.value = formatNumberWithCommas(0);
    }
}

// --- Función para procesar el pago y registrar la venta ---
function procesarPago(metodoPago) {
    console.log("Procesando pago con el método:", metodoPago);
    console.log("Antes de procesarPago, cliente seleccionado:", window.clienteSeleccionado);

    if (!window.productosVenta || window.productosVenta.length === 0) {
        alert("No hay productos en el carrito para procesar el pago.");
        return;
    }
    
    if (!window.clienteSeleccionado || !window.clienteSeleccionado.idClientes) {
        alert("Selecciona un cliente antes de vender.");
        console.error("No hay cliente seleccionado.");
        return;
    }

    if (!window.idUsuarioSesion) {
        alert('Debe iniciar sesión para realizar la venta.');
        return;
    }

    // --- Calcular subtotal, total y valor de descuento ---
    let subtotalVenta = 0;
    let valorDescuentoTotal = 0;
    let totalVentaFinal = 0;

    window.productosVenta.forEach(p => {
        const subtotalProducto = p.precio_unitario * p.cantidad;
        const descuentoProducto = subtotalProducto * (p.descuento_porcentaje / 100 || 0);
        subtotalVenta += subtotalProducto;
        valorDescuentoTotal += descuentoProducto;
        totalVentaFinal += subtotalProducto - descuentoProducto;

        // Guardar subtotal y total con descuento en cada producto
        p.subtotal = subtotalProducto;
        p.total_con_descuento = subtotalProducto - descuentoProducto;
        p.descuento = descuentoProducto;
    });
    window.totalVentaFinal = totalVentaFinal;

    // --- ✅ Validación del pago según método ---
    const totalAPagar = parseFloat(window.totalVentaFinal || 0);

    if (metodoPago === "Efectivo") {
        const efectivoRecibido = parseFloat(document.getElementById('efectivoRecibido').value);
        if (isNaN(efectivoRecibido)) {
            alert("Por favor, ingrese el efectivo recibido.");
            return;
        }
        if (efectivoRecibido < totalAPagar) {
            alert("El efectivo recibido es insuficiente. Venta cancelada.");
            return;
        }
    }

    // --- Crear objeto de venta para enviar al backend ---
    const datosVenta = {
        accion: "vender",
        idCliente: parseInt(window.clienteSeleccionado.idClientes),
        idUsuario: window.idUsuarioSesion,
        subtotal_venta: subtotalVenta,
        descuento_venta: valorDescuentoTotal,
        total_venta: totalVentaFinal,
        productosData: window.productosVenta.map(p => ({
            idProducto: p.idProducto,
            cantidad: p.cantidad,
            precio_unitario: p.precio_unitario,
            descuento: p.descuento,
            total_con_descuento: p.total_con_descuento,
            impuesto_porcentaje: p.impuesto_porcentaje || 0,
            descuento_porcentaje: p.descuento_porcentaje || 0
        })),
        metodoPago: metodoPago,
        valorDescuento: valorDescuentoTotal
    };

    console.log("Array productosVenta:", window.productosVenta);
    console.log("🔥 JSON que se enviará al backend:", JSON.stringify(datosVenta, null, 2));

    if (!datosVenta.idCliente || datosVenta.productosData.length === 0) {
        alert("Error: No se ha seleccionado un cliente o no hay productos en el carrito.");
        console.error("Intento de procesar venta sin cliente o productos.", datosVenta);
        return;
    }

    // --- Enviar al servidor ---
    fetch("VenderServlet", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(datosVenta)
    })
    .then(response => response.json())
// --- Procesar respuesta de venta exitosa ---
.then(data => {
    console.log("✅ Respuesta del servidor:", data);
    if (data.status === "true") {

        // Obtener valores de la UI
        const totalAPagar = parseFloat(document.getElementById('totalVentaHidden').value) || 0;
        const efectivoRecibido = parseFloat(document.getElementById('efectivoRecibido').value) || 0;
        const cambioDevolver = efectivoRecibido - totalAPagar;

        // Alerta con detalle de pago
        const mensaje = 
            `Venta registrada con éxito.\n\n` +
            `Total a pagar: $${formatNumberWithCommas(totalAPagar)}\n` +
            `Efectivo recibido: $${formatNumberWithCommas(efectivoRecibido)}\n` +
            `Cambio a devolver: $${formatNumberWithCommas(cambioDevolver)}\n\n` +
            `¿Desea imprimir la factura?`;

        if (confirm(mensaje)) {
            const imprimirTicket = document.getElementById('toggleTicket').checked;
            abrirFactura(data.idVenta, imprimirTicket);
        }

        limpiarCarrito();
        window.location.reload(true);

        } else {
        alert("Error al registrar la venta: " + (data.message || "desconocido"));
      }
   })
}

// --- Función para abrir la factura ---
function abrirFactura(idVenta, ticket) {
    if (ticket) {
        window.open(`FacturaPOSServlet?idVenta=${idVenta}`, '_blank');
    } else {
        window.open(`FacturaElectronicaServlet?idVenta=${idVenta}`, '_blank');
    }
}

//Establece el método de lectura de productos (ej. teclado, escáner).
function seleccionarMetodoLectura(metodo) {
    window.metodoLecturaSeleccionado = metodo;
    document.querySelectorAll('.lectura-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    const boton = document.querySelector(`.lectura-btn[data-metodo-lectura="${metodo}"]`);
    if (boton) boton.classList.add('active');
    console.log(`Modo de lectura cambiado a: ${metodo}`);
}

//  Limpia todo el carrito de compras y resetea el estado de la venta.
function limpiarCarrito() {
    console.log('Limpiando todo el carrito y reseteando la venta.');

    // Limpiar interfaz
    document.getElementById('carritoLista').innerHTML = '';
    document.getElementById('productosCantidad').textContent = '00 productos';
    document.getElementById('totalVenta').textContent = '$0';
    document.getElementById('totalVentaHidden').value = 0;
    document.getElementById('clienteNombre').innerHTML = `<i class="bi bi-person"></i> Cliente`;
    document.getElementById('clienteSeleccionado').value = '';

    // Limpiar arrays y variables globales
    // window.carrito = [];
    window.productosVenta = [];
    window.clienteSeleccionado = null;
    window.totalVentaFinal = 0;
    window.cantidadProductos = 0;
    window.subtotalVenta = 0;
    window.valorDescuento = 0;
    window.descuentoAplicadoPorcentaje = 0;
    window.valorDescuentoAplicado = 0;

    // Limpiar mensaje de descuento general
    const mensajeDescuentoElement = document.getElementById('mensajeDescuento');
    if (mensajeDescuentoElement) {
        mensajeDescuentoElement.style.display = 'none';
        mensajeDescuentoElement.innerHTML = '';
    }
    const descuentoAplicadoTextoElement = document.getElementById('descuentoAplicadoTexto');
    if (descuentoAplicadoTextoElement) descuentoAplicadoTextoElement.textContent = '';

    // Limpiar mensaje de descuento por producto
    const mensajeDescuentoProducto = document.getElementById('mensajeDescuentoProducto');
    if (mensajeDescuentoProducto) mensajeDescuentoProducto.innerHTML = '';

    // Resetear inputs de modal
    const inputDescuento = document.getElementById('inputDescuento');
    if (inputDescuento) inputDescuento.value = '';
    const inputDescuentoProducto = document.getElementById('inputDescuentoProducto');
    if (inputDescuentoProducto) inputDescuentoProducto.value = '';

    // Deshabilitar botón de finalizar venta y cobrar
    const btnFinalizar = document.getElementById('btnFinalizarVenta');
    if (btnFinalizar) btnFinalizar.disabled = true;
    const btnCobrar = document.getElementById('btnCobrar');
    if (btnCobrar) btnCobrar.disabled = true;
    
    // --- Cerrar modal de cobro si está abierto ---
    const modalCobrarEl = document.getElementById('modalCobrar');
    if (modalCobrarEl) {
    const modalCobrar = bootstrap.Modal.getInstance(modalCobrarEl) 
        || new bootstrap.Modal(modalCobrarEl);
    modalCobrar.hide();
}
    // --- Restaurar foco en la búsqueda ---
    const inputBusqueda = document.getElementById('buscarProducto');
    if (inputBusqueda) inputBusqueda.focus();
    console.log('Carrito limpio y botones deshabilitados.');
}

//Busca un cliente por su número de cédula a través de una llamada a la API.
async function buscarClientePorCedula() {
    const cedula = document.getElementById('cedulaCliente').value.trim();
    if (!cedula) return;
    console.log(`Buscando cliente con cédula: ${cedula}`);

    try {
        const resp = await fetch(`ClientesServlet?accion=buscar&busqueda=${cedula}`);
        const clientes = await resp.json();
        const resultadoDiv = document.getElementById('resultadoBusquedaCliente');
        const nombreClienteParrafo = document.getElementById('nombreClienteEncontrado');
        const btnAceptarCliente = document.getElementById('btnAceptarCliente');

        if (clientes.length > 0) {
            window.clienteSeleccionado = clientes[0]; 
            console.log('Cliente encontrado:', clienteSeleccionado);
            nombreClienteParrafo.textContent = clienteSeleccionado.razon_social;
            resultadoDiv.style.display = 'block';
            btnAceptarCliente.disabled = false;
        } else {
            console.log('No se encontró ningún cliente con esa cédula.');
            window.clienteSeleccionado = null;
            nombreClienteParrafo.textContent = 'No se encontró ningún cliente con esa cédula.';
            resultadoDiv.style.display = 'block';
            btnAceptarCliente.disabled = true;
        }
    } catch (error) {
        console.error('Error al buscar el cliente:', error);
        window.clienteSeleccionado = null;
        document.getElementById('btnAceptarCliente').disabled = true;
    }
}

//Confirma el cliente seleccionado y lo asocia con la venta.
function aceptarCliente() {
    if (!window.clienteSeleccionado) {
        console.warn('Intento de aceptar sin un cliente seleccionado.');
        alert("No hay cliente seleccionado");
        return;
    }
// Guarda el cliente seleccionado para usarlo al enviar la venta
console.log(`Cliente aceptado: ${window.clienteSeleccionado.razon_social} (ID: ${window.clienteSeleccionado.idClientes})`);
document.getElementById('clienteSeleccionado').value = window.clienteSeleccionado.idClientes;
document.getElementById('clienteNombre').innerHTML = `<i class="bi bi-person"></i> ${window.clienteSeleccionado.razon_social}`;
const modal = bootstrap.Modal.getInstance(document.getElementById('modalSeleccionarCliente'));
if (modal) modal.hide();

}

// --- Funciones de la Calculadora ---
window.calcValor = '';
function calcClick(valor) {
    if (valor === '=' || valor === 'C') {
        return;
    }
    window.calcValor += valor;
    document.getElementById('calcPantalla').value = window.calcValor;
    console.log(`Calculadora: clic en '${valor}'. Pantalla: ${window.calcValor}`);
}
function calcularResultado() {
    try {
        const resultado = eval(window.calcValor);
        window.calcValor = resultado.toString();
        document.getElementById('calcPantalla').value = window.calcValor;
        console.log(`Calculadora: evaluando '${window.calcValor}'. Resultado: ${resultado}`);
    } catch (e) {
        document.getElementById('calcPantalla').value = 'Error';
        console.error('Calculadora: Error de evaluación.', e);
        window.calcValor = '';
    }
}
function calcClear() {
    window.calcValor = '';
    document.getElementById('calcPantalla').value = '';
    console.log('Calculadora: Pantalla limpiada.');
}
function cerrarCalculadora() {
    document.getElementById('calculadoraContainer').style.display = 'none';
    console.log('Calculadora cerrada.');
}

// --- Funciones de Caja ---
function abrirCaja() {
    const monto = parseFloat(document.getElementById('montoInicialCaja').value);
    if (!isNaN(monto)) {
        fetch('VenderServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ accion: 'abrirCaja', montoInicial: monto })
        })
        .then(res => res.json())
        .then(data => {
            if (data.status === "ok") {
                alert(`Caja abierta con un monto inicial de $${formatNumberWithCommas(monto)}.`);
                const modal = bootstrap.Modal.getInstance(document.getElementById('modalAbrirCaja'));
                modal.hide();
            } else {
                alert("Error al abrir caja: " + data.message);
            }
        })
        .catch(err => console.error("Error al abrir caja:", err));
    } else {
        alert("Por favor, ingrese el monto inicial.");
    }
}

// --- Función para cerrar caja ---
function cerrarCaja() {
    const monto = parseFloat(document.getElementById('montoFinalCaja').value);
    const observaciones = document.getElementById('observacionesCierre').value;

    if (isNaN(monto) || monto < 0) {
        alert("Por favor, ingrese un monto final válido.");
        return;
    }

    // Primero obtenemos la caja activa del servidor
    fetch('VenderServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ accion: 'obtenerCajaActiva' })
    })
    .then(res => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        return res.json();
    })
    .then(data => {
        if (!data || !data.idCaja) {
            alert("No se encontró la caja activa en la base de datos.");
            return;
        }

        // cerrar la caja con ID
        fetch('VenderServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                accion: 'cerrarCaja', 
                idCaja: data.idCaja, 
                montoFinal: monto, 
                observaciones: observaciones 
            })
        })
        .then(res => {
            if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
            return res.json();
        })
        .then(resCerrar => {
            if (resCerrar.status === "ok") {
                alert(`Caja cerrada correctamente: $${formatNumberWithCommas(monto)}`);
                const modal = bootstrap.Modal.getInstance(document.getElementById('modalCerrarCaja'));
                modal.hide();
            } else {
                alert(`Error al cerrar caja: ${resCerrar.message}`);
            }
        })
        .catch(err => {
            console.error('Error al cerrar caja:', err);
            alert('No se pudo cerrar la caja. Revisa tu conexión o el servidor.');
        });
    })
    .catch(err => {
        console.error('Error al obtener caja activa:', err);
        alert('No se pudo obtener la caja activa.');
    });
}

// --- Función para aplicar descuento a un producto ---
function aplicarDescuentoProducto() {
    const inputModal = document.getElementById('inputDescuentoProducto');
    const porcentaje = parseFloat(inputModal.value);
    const idProducto = parseInt(inputModal.dataset.idProducto);

    if (isNaN(porcentaje) || porcentaje < 0 || porcentaje > 100) {
        alert('Ingrese un porcentaje válido (0-100)');
        return;
    }

    const li = document.querySelector(`#carritoLista li[data-id-producto='${idProducto}']`);
    if (!li) return;

    const cantidadInput = li.querySelector('.cantidad-input');
    const precioUnitario = parseFloat(cantidadInput.dataset.precio_venta || 0);
    const cantidad = parseInt(cantidadInput.value);
    const descuento = precioUnitario * (porcentaje / 100);
    const totalConDescuento = (precioUnitario - descuento) * cantidad;

    li.querySelector('.total-item').textContent = `$${formatNumberWithCommas(totalConDescuento)}`;

    // --- Actualiza badge de descuento ---
    let badge = li.querySelector('.descuento-producto');
    if (!badge) {
        // Crear badge si no existe
        badge = document.createElement('span');
        badge.className = 'badge bg-warning text-dark descuento-producto ms-1';
        li.querySelector('.mt-1').appendChild(badge);
    }
    if (porcentaje > 0) {
        badge.textContent = `${porcentaje}%`;
        badge.style.display = 'inline-block';
    } else {
        badge.style.display = 'none';
    }

    // Actualiza array productosVenta
    const productoEnArray = window.productosVenta.find(p => p.idProducto === idProducto);
    if (productoEnArray) {
        productoEnArray.descuento_porcentaje = porcentaje;
        productoEnArray.subtotal = totalConDescuento;
    }

    // Recalcula total de la venta
    calcularTotalVentaFinal();

    // Actualiza el valor del descuento global y el mensaje en el botón
    const totalDescuento = window.productosVenta.reduce((acc, p) => {
        return acc + ((p.precio_unitario * p.cantidad) - p.subtotal);
    }, 0);
    window.valorDescuentoAplicado = totalDescuento;

    // Actualiza mensaje promocional y botón
    actualizarMensajeDescuentoProducto();

    // Cierra modal de descuento
    const modal = bootstrap.Modal.getInstance(document.getElementById('modalDescuentoProducto'));
    if (modal) modal.hide();
}

// --- Event Listener para DOMContentLoaded ---
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM completamente cargado y analizado. Adjuntando eventos.');
    cargarProductosDesdeBD();

    function mostrarProductos(categoria) {
        console.log(`Filtrando productos por categoría: "${categoria}"`);
        const contenedorCards = document.getElementById('contenedorCards');
        contenedorCards.innerHTML = '';

        const productosFiltrados = inventario.filter(p =>
            p.categoria && p.categoria.trim().toLowerCase() === categoria.trim().toLowerCase()
        );

        console.log(`Se encontraron ${productosFiltrados.length} productos en esta categoría.`);
        if (productosFiltrados.length === 0) {
            contenedorCards.innerHTML = '<div class="alert alert-warning">No hay productos en esta categoría.</div>';
            return;
        }

        const fila = document.createElement('div');
        fila.className = 'row';
        productosFiltrados.forEach(p => fila.appendChild(crearTarjetaProducto(p)));
        contenedorCards.appendChild(fila);
    }

    // Listeners de Categorías
    document.querySelectorAll('#listaCategoriasDesktop li').forEach(li => {
        li.addEventListener('click', () => mostrarProductos(li.dataset.categoria));
    });

    document.querySelectorAll('#listaCategoriasOffcanvas li').forEach(li => {
        li.addEventListener('click', () => {
            mostrarProductos(li.dataset.categoria);
            const offcanvasEl = document.getElementById('offcanvasCategorias');
            const offcanvas = bootstrap.Offcanvas.getInstance(offcanvasEl);
            if (offcanvas) offcanvas.hide();
        });
    });

    // Listener de Búsqueda
    const formBuscar = document.getElementById('formBuscar');
    if (formBuscar) {
        formBuscar.addEventListener('submit', buscarProducto);
    }
    const buscarInputDesktop = document.getElementById('campoBuscarDesktop');
    if (buscarInputDesktop) {
        buscarInputDesktop.addEventListener('input', buscarProducto);
    }

    // --- botón Finalizar Venta ---
    document.getElementById('btnFinalizarVenta').addEventListener('click', (e) => {
        e.preventDefault(); 
        finalizarVenta();
    });
    
    // --- botón Limpiar Carrito ---
    document.getElementById('btnLimpiarCarrito').addEventListener('click', (e) => {
        e.preventDefault();
        limpiarCarrito();
    });
    
    document.querySelectorAll('.metodo-pago-btn').forEach(btn => {
        btn.addEventListener('click', () => seleccionarMetodoPago(btn.dataset.metodo));
    });

    // Listeners del Modal de Cobro
    const efectivoRecibidoInput = document.getElementById('efectivoRecibido');
    if (efectivoRecibidoInput) {
        efectivoRecibidoInput.addEventListener('input', calcularCambio);
    }
    document.getElementById('btnPagar').addEventListener('click', () => procesarPago(window.metodoDePagoSeleccionado));

    // Listeners del Modal de Cliente
    const cedulaInput = document.getElementById('cedulaCliente');
    if (cedulaInput) {
        cedulaInput.addEventListener('input', buscarClientePorCedula);
    }
    const aceptarBtn = document.getElementById('btnAceptarCliente');
    if (aceptarBtn) {
        aceptarBtn.addEventListener('click', aceptarCliente);
    }

    // Listeners del Modal de Descuento
    document.getElementById('btnAplicarDescuento').addEventListener('click', aplicarDescuentoModal);
    document.getElementById('btnEliminarDescuento').addEventListener('click', eliminarDescuento);

    // Listeners del Modal de Caja
    document.getElementById('btnConfirmarAbrirCaja').addEventListener('click', abrirCaja);
    document.getElementById('btnConfirmarCerrarCaja').addEventListener('click', cerrarCaja);

    // Listeners de la Calculadora
    document.getElementById('btnCalculadora').addEventListener('click', function() {
        const calc = document.getElementById('calculadoraContainer');
        const isDisplayed = calc.style.display === 'block';
        calc.style.display = isDisplayed ? 'none' : 'block';
        console.log(`Calculadora ${isDisplayed ? 'ocultada' : 'mostrada'}.`);
    });

    document.getElementById('btnCerrarCalculadora').addEventListener('click', cerrarCalculadora);

    const calcButtons = document.querySelectorAll('#calculadoraContainer button:not([data-calc-equals]):not([data-calc-clear])');
    calcButtons.forEach(btn => {
        btn.addEventListener('click', () => calcClick(btn.dataset.calcValue));
    });

    const equalsBtn = document.querySelector('[data-calc-equals]');
    if (equalsBtn) {
        equalsBtn.addEventListener('click', calcularResultado);
    }

    const clearBtn = document.querySelector('[data-calc-clear]');
    if (clearBtn) {
        clearBtn.addEventListener('click', calcClear);
    }

    // Listeners de Selección de método de lectura
    document.querySelectorAll('.lectura-btn').forEach(btn => {
        btn.addEventListener('click', () => seleccionarMetodoLectura(btn.dataset.metodoLectura));
    });

    // Inicializar el estado de los botones de lectura
    seleccionarMetodoLectura('teclado');
    
    // --- Botón de descuento por producto ---
    document.addEventListener('click', function(e) {
    if (e.target.closest('.aplicar-descuento-btn')) {
        const li = e.target.closest('li');
        const idProducto = li.getAttribute('data-id-producto');
        const inputModal = document.getElementById('inputDescuentoProducto');
        inputModal.dataset.idProducto = idProducto;
        inputModal.value = '';
        const modal = new bootstrap.Modal(document.getElementById('modalDescuentoProducto'));
        modal.show();
    }
});

    // --- Listener del botón del modal descuento producto ---
    document.getElementById('btnAplicarDescuentoProducto')?.addEventListener('click', aplicarDescuentoProducto);
    document.getElementById('btnEliminarDescuentoProducto')?.addEventListener('click', () => {
    // Obtener ID del producto desde el input del modal
    const inputModal = document.getElementById('inputDescuentoProducto');
    const idProducto = parseInt(inputModal.dataset.idProducto);
    const producto = window.productosVenta.find(p => p.idProducto === idProducto);
    if (producto) {
        producto.subtotal = producto.precio_unitario * producto.cantidad;
        producto.descuento_porcentaje = 0;
    }
    const li = document.querySelector(`#carritoLista li[data-id-producto='${idProducto}']`);
    if (li) {
        const badge = li.querySelector('.descuento-producto');
        if (badge) badge.style.display = 'none';
        
        li.querySelector('.total-item').textContent = `$${formatNumberWithCommas(producto.subtotal)}`;
    }
    calcularTotalVentaFinal();
   
    actualizarMensajeDescuentoProducto();
  });
});

