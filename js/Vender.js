const productos = {
    'jabón líquido': [
        { nombre: "Jabón rosita 500ml", stock: 20, codigo: "J001", precio: 5000 },
        { nombre: "Jabón coco 1L", stock: 15, codigo: "J002", precio: 7000 },
        { nombre: "Jabón azul Rey 1L", stock: 12, codigo: "J003", precio: 6500 },
        { nombre: "Jabón Azul+ 500ml", stock: 8, codigo: "J004", precio: 4500 }
    ],
    ambientadores: [
        { nombre: "Ambientador Lavanda 250ml", stock: 10, codigo: "A001", precio: 4000 },
        { nombre: "Ambientador Cítrico 500ml", stock: 7, codigo: "A002", precio: 4500 }
    ],
    esencias: [
        { nombre: "Esencia Floral 100ml", stock: 5, codigo: "E007", precio: 3000 },
        { nombre: "Esencia Vainilla 100ml", stock: 9, codigo: "E008", precio: 3200 }
    ],
    'elementos de aseo': [
        { nombre: "Cepillo multiusos", stock: 20, codigo: "E001", precio: 3000 },
        { nombre: "Esponja abrasiva", stock: 30, codigo: "E002", precio: 1200 }
    ],
    'protección personal': [
        { nombre: "Guantes de látex", stock: 40, codigo: "P001", precio: 2000 },
        { nombre: "Tapabocas N95", stock: 25, codigo: "P002", precio: 5000 }
    ],
    kits: [
        { nombre: "Kit limpieza básica", stock: 10, codigo: "K001", precio: 10000 }
    ],
    limpieza: [
        { nombre: "Desinfectante 1L", stock: 15, codigo: "L001", precio: 6500 }
    ],
    higiénicos: [
        { nombre: "Papel higiénico x4", stock: 50, codigo: "H001", precio: 4000 },
        { nombre: "Toallas x100", stock: 50, codigo: "H002", precio: 4000 }
    ]
};

document.getElementById('totalVenta').textContent = '$0';
document.getElementById('btnFinalizarVenta').disabled = true;
let metodoDePagoSeleccionado = 'efectivo';
document.getElementById('metodoPagoSeleccionado').textContent = metodoDePagoSeleccionado;
document.querySelectorAll('.metodo-pago-btn').forEach(btn => btn.classList.remove('active'));
const botonefectivoInicial = Array.from(document.querySelectorAll('.metodo-pago-btn')).find(btn => btn.getAttribute('data-metodo') === 'efectivo');
if (botonefectivoInicial) {
    botonefectivoInicial.classList.add('active');
}
const descuentoInput = document.getElementById('inputDescuento');
if (descuentoInput) descuentoInput.value = '';
let entradaSeleccionada = 'teclado';
let totalVentaFinal = 0;
let cantidadProductos = 0;
let descuentoAplicadoPorcentaje = 0;
let valorDescuentoAplicado = 0;
let metodoLecturaSeleccionado = null;
let carrito = [];

function crearTarjetaProducto(p) {
    const col = document.createElement('div');
    col.className = 'col-sm-6 col-md-4 col-lg-3 mb-3';
    col.innerHTML = `
        <div class="card h-100 shadow-sm" style="font-size: 0.85rem; cursor: pointer;">
            <div class="card-body">
                <h6 class="card-title">${p.nombre}</h6>
                <p class="card-text mb-1">
                    <strong>Stock:</strong> ${p.stock}<br>
                    <i class="bi bi-upc-scan"></i> ${p.codigo}<br>
                    <strong>Precio:</strong> $${formatNumberWithCommas(p.precio)}
                </p>
            </div>
        </div>
    `;
    col.querySelector('.card').addEventListener('click', () => {
        agregarAlCarritoHTML(p.nombre, '', p.precio);
    });
    return col;
}

function mostrarProductos(categoriaTexto) {
    const contenedorCards = document.getElementById('contenedorCards');
    contenedorCards.innerHTML = '';
    limpiarBusqueda();
    document.querySelectorAll('.categoria-scroll li').forEach(li => {
        li.classList.toggle('seleccionado', li.textContent.trim().toLowerCase() === categoriaTexto.toLowerCase());
    });

    const productosCategoria = productos[categoriaTexto.toLowerCase()];
    if (!productosCategoria) {
        contenedorCards.innerHTML = '<div class="alert alert-warning">No hay productos para esta categoría.</div>';
        return;
    }

    const fila = document.createElement('div');
    fila.className = 'row';
    productosCategoria.forEach(p => fila.appendChild(crearTarjetaProducto(p)));
    contenedorCards.appendChild(fila);
}

function mostrarTodosLosProductos() {
    const contenedorCards = document.getElementById('contenedorCards');
    contenedorCards.innerHTML = '';
    const fila = document.createElement('div');
    fila.className = 'row';
    for (const categoria in productos) {
        productos[categoria].forEach(p => fila.appendChild(crearTarjetaProducto(p)));
    }
    contenedorCards.appendChild(fila);
    document.querySelectorAll('.categoria-scroll li').forEach(li => li.classList.remove('seleccionado'));
}

function buscarProducto(event) {
    if (event) event.preventDefault();
    const input = document.getElementById('campoBuscarDesktop');
    const valor = (input?.value || '').trim().toLowerCase();
    const contenedorCards = document.getElementById('contenedorCards');
    contenedorCards.innerHTML = '';

    if (!valor) {
        mostrarTodosLosProductos();
        return;
    }

    let encontrados = [];
    for (const categoria in productos) {
        encontrados = encontrados.concat(productos[categoria].filter(p =>
            p.nombre.toLowerCase().includes(valor)
        ));
    }

    if (encontrados.length === 0) {
        contenedorCards.innerHTML = '<div class="alert alert-warning">No se encontraron productos.</div>';
        return;
    }

    const fila = document.createElement('div');
    fila.className = 'row';
    encontrados.forEach(p => fila.appendChild(crearTarjetaProducto(p)));
    contenedorCards.appendChild(fila);
    document.querySelectorAll('.categoria-scroll li').forEach(li => li.classList.remove('seleccionado'));
}

function limpiarBusqueda() {
    const input = document.getElementById('campoBuscarHeader');
    if (input) input.value = '';
}

function seleccionarEntrada(tipo) {
    entradaSeleccionada = tipo;
    document.getElementById('btnCodigoBarras')?.classList.toggle('active', tipo === 'codigoBarras');
    document.getElementById('btnTeclado')?.classList.toggle('active', tipo === 'teclado');
}

function agregarAlCarritoHTML(nombre, medida, precio) {
    const carritoLista = document.getElementById('carritoLista');
    const productoExistente = Array.from(carritoLista.querySelectorAll('li')).find(item =>
        item.querySelector('.fw-bold')?.textContent.trim() === nombre
    );

    if (productoExistente) {
        const cantidadInput = productoExistente.querySelector('.cantidad-input');
        cantidadInput.value = parseInt(cantidadInput.value) + 1;
        actualizarTotal(cantidadInput);
    } else {
        const li = document.createElement('li');
        li.className = 'd-flex justify-content-between align-items-center border-bottom py-2';
        li.innerHTML = `
            <div class="me-2">
                <input type="number" min="1" value="1" class="form-control form-control-sm text-center cantidad-input"
                       style="width: 60px;" data-precio="${precio}" oninput="actualizarTotal(this)" />
            </div>
            <div class="flex-fill px-2">
                <div class="fw-bold">${nombre}</div>
                <div class="text-success fw-bold total-item">$${formatNumberWithCommas(precio)}</div>
            </div>
            <div class="text-end px-2">
                <small class="text-muted">Unidad: <span class="text-primary fw-semibold">$${formatNumberWithCommas(precio)}</span></small>
            </div>
            <button class="btn btn-sm btn-outline-danger ms-2" title="Eliminar" onclick="eliminarItemCarrito(this)">
                <i class="bi bi-trash"></i>
            </button>
        `;
        carritoLista.appendChild(li);
        cantidadProductos++;
    }

    actualizarCantidadProductos();
    calcularTotalVentaFinal();
    actualizarBotonFinalizarVenta();
}

function eliminarItemCarrito(botonEliminar) {
    const itemCarrito = botonEliminar.closest('li');
    if (itemCarrito) {
        const cantidadInput = itemCarrito.querySelector('.cantidad-input');
        const cantidadEliminada = parseInt(cantidadInput.value);
        itemCarrito.remove();
        cantidadProductos -= cantidadEliminada;
        actualizarCantidadProductos();
        verificarCarritoVacio();
        calcularTotalVentaFinal();
    }
}

function actualizarTotal(input) {
    const cantidad = Math.max(1, parseInt(input.value) || 1);
    input.value = cantidad;
    const precioUnidad = parseInt(input.getAttribute('data-precio'));
    const total = cantidad * precioUnidad;
    input.closest('li').querySelector('.total-item').textContent = `$${formatNumberWithCommas(total)}`;
    calcularTotalVentaFinal();
    actualizarCantidadProductos();
}

function calcularTotalVentaFinal() {
    let sumaTotal = 0;
    document.querySelectorAll('#carritoLista .cantidad-input').forEach(input => {
        sumaTotal += parseInt(input.value) * parseInt(input.getAttribute('data-precio'));
    });
    totalVentaFinal = sumaTotal;
    document.getElementById('totalVenta').textContent = `$${formatNumberWithCommas(totalVentaFinal)}`;
}

function verificarCarritoVacio() {
    const carritoLista = document.getElementById('carritoLista');
    if (carritoLista.children.length === 0) {
        totalVentaFinal = 0;
        cantidadProductos = 0;
        document.getElementById('totalVenta').textContent = '$0';
        document.getElementById('btnFinalizarVenta').disabled = true;
        actualizarCantidadProductos();
    } else {
        actualizarBotonFinalizarVenta();
    }
}

function actualizarBotonFinalizarVenta() {
    document.getElementById('btnFinalizarVenta').disabled = totalVentaFinal === 0;
}

function aplicarDescuentoModal() {
    const descuentoInput = document.getElementById('inputDescuento');
    const porcentaje = parseFloat(descuentoInput.value);

    if (!isNaN(porcentaje) && porcentaje >= 0 && porcentaje <= 100) {
        descuentoAplicadoPorcentaje = porcentaje;
        calcularValorDescuento();
        actualizarMensajeDescuento();
        aplicarDescuentoVisual();
    } else {
        alert('Por favor, ingrese un porcentaje de descuento válido (0-100).');
    }
}

function calcularValorDescuento() {
    valorDescuentoAplicado = totalVentaFinal * (descuentoAplicadoPorcentaje / 100);
}

function actualizarMensajeDescuento() {
    const valorDescuentoPorcentajeElement = document.getElementById('valorDescuentoPorcentaje');
    const valorDescuentoMonedaElement = document.getElementById('valorDescuentoMoneda');
    const mensajeDescuentoElement = document.getElementById('mensajeDescuento');

    valorDescuentoPorcentajeElement.textContent = `${descuentoAplicadoPorcentaje}%`;
    valorDescuentoMonedaElement.textContent = `$${formatNumberWithCommas(valorDescuentoAplicado)}`;
    mensajeDescuentoElement.style.display = 'block';
}

function aplicarDescuentoVisual() {
    const descuentoAplicadoTextoElement = document.getElementById('descuentoAplicadoTexto');
    const totalVentaElement = document.getElementById('totalVenta');
    const totalConDescuento = totalVentaFinal - valorDescuentoAplicado;

    descuentoAplicadoTextoElement.textContent = `${descuentoAplicadoPorcentaje}% (-$${formatNumberWithCommas(valorDescuentoAplicado)})`;
    totalVentaElement.textContent = `$${formatNumberWithCommas(totalConDescuento)}`;
}

function eliminarDescuento() {
    descuentoAplicadoPorcentaje = 0;
    valorDescuentoAplicado = 0;
    const mensajeDescuentoElement = document.getElementById('mensajeDescuento');
    const descuentoAplicadoTextoElement = document.getElementById('descuentoAplicadoTexto');
    const totalVentaElement = document.getElementById('totalVenta');

    mensajeDescuentoElement.style.display = 'none';
    descuentoAplicadoTextoElement.textContent = '';
    totalVentaElement.textContent = `$${formatNumberWithCommas(totalVentaFinal)}`;
    document.getElementById('inputDescuento').value = '';
}

function formatNumberWithCommas(number) {
    return number.toLocaleString('es-CO', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
}

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
}

document.addEventListener('DOMContentLoaded', () => {
    mostrarTodosLosProductos();
    document.querySelectorAll('.categoria-scroll li').forEach(li => {
        li.addEventListener('click', () => mostrarProductos(li.textContent.trim()));
    });
    const buscarInputDesktop = document.getElementById('campoBuscarDesktop');
    if (buscarInputDesktop) {
        buscarInputDesktop.addEventListener('input', buscarProducto);
    }
});
// JS parte 2 (corregido y unificado - COMPLETO)
function seleccionarMetodoPago(metodo) {
    metodoDePagoSeleccionado = metodo;
    document.getElementById('metodoPagoSeleccionado').textContent = metodo;
    const botonesPago = document.querySelectorAll('.metodo-pago-btn');

    botonesPago.forEach(boton => {
        boton.classList.remove('active');
        boton.style.backgroundColor = '';
        const icono = boton.querySelector('i');
        if (icono) {
            icono.style.color = '';
        }
    });

    botonesPago.forEach(boton => {
        if (boton.getAttribute('data-metodo') === metodo) {
            boton.classList.add('active');
            boton.style.backgroundColor = '#d1ecf1'; // Color info claro
            const icono = boton.querySelector('i');
            if (icono) {
                icono.style.color = '#0c5460'; // Color info oscuro para el icono
            }
        }
    });
}

function finalizarVenta() {
    const totalVentaElement = document.getElementById('totalVenta');
    const totalVentaTexto = totalVentaElement.textContent.replace('$', '').replace(/\./g, '').trim();
    const totalVenta = parseFloat(totalVentaTexto);

    if (isNaN(totalVenta) || totalVenta <= 0) {
        alert("No hay productos en el carrito o el total es inválido.");
        return;
    }

    document.getElementById('totalAPagarEfectivo').value = formatNumberWithCommas(totalVenta);
    document.getElementById('totalVentaHidden').value = totalVenta;
    document.getElementById('efectivoRecibido').value = '';
    document.getElementById('cambioDevolver').value = formatNumberWithCommas(0);

    document.getElementById('pagoEfectivo').style.display = 'none';
    document.getElementById('pagoTarjeta').style.display = 'none';
    document.getElementById('pagoQR').style.display = 'none';

    if (metodoDePagoSeleccionado === 'Efectivo') {
        document.getElementById('pagoEfectivo').style.display = 'block';
        const modalCobrarElement = document.getElementById('modalCobrar');
        modalCobrarElement.addEventListener('shown.bs.modal', () => {
            document.getElementById('efectivoRecibido').focus();
        }, { once: true });
    } else if (metodoDePagoSeleccionado === 'Crédito' || metodoDePagoSeleccionado === 'Tarjeta') {
        document.getElementById('pagoTarjeta').style.display = 'block';
    } else if (metodoDePagoSeleccionado === 'QR') {
        document.getElementById('pagoQR').style.display = 'block';
    }

    const modalCobrarElement = document.getElementById('modalCobrar');
    const modalCobrar = new bootstrap.Modal(modalCobrarElement);
    modalCobrar.show();


   /*
modalCobrar._element.addEventListener('hidden.bs.modal', function () {
    let ticketActivo = document.getElementById('toggleTicket').checked;
    if (!ticketActivo) {
        window.open('Facturaelectronica.html', '_blank');
    }
});
*/
}

function calcularCambio() {
    const totalAPagar = parseFloat(document.getElementById('totalVentaHidden').value);
    const efectivoRecibido = parseFloat(document.getElementById('efectivoRecibido').value);
    const cambioDevolverInput = document.getElementById('cambioDevolver');

    if (!isNaN(totalAPagar) && !isNaN(efectivoRecibido)) {
        const cambio = efectivoRecibido - totalAPagar;
        cambioDevolverInput.value = formatNumberWithCommas(cambio);
    } else {
        cambioDevolverInput.value = formatNumberWithCommas(0);
    }
}

function procesarPago() {
    const imprimirTicket = document.getElementById('toggleTicket').checked;

    if (metodoDePagoSeleccionado === 'Efectivo') {
        const totalAPagar = parseFloat(document.getElementById('totalVentaHidden').value);
        const efectivoRecibido = parseFloat(document.getElementById('efectivoRecibido').value);
        const cambioDevolver = parseFloat(document.getElementById('cambioDevolver').value.replace('$', '').replace('.', '').replace(',', '.'));

        if (isNaN(efectivoRecibido)) {
            alert("Por favor, ingrese el efectivo recibido.");
            return;
        }

        if (efectivoRecibido < totalAPagar) {
            alert("El efectivo recibido es insuficiente.");
            return;
        }

        alert(`Venta en efectivo finalizada. Total pagado: $${formatNumberWithCommas(efectivoRecibido)}, Cambio a devolver: $${formatNumberWithCommas(cambioDevolver)}`);

        const modalCobrar = bootstrap.Modal.getInstance(document.getElementById('modalCobrar'));
        if (modalCobrar) modalCobrar.hide();

        limpiarCarrito();

        if (imprimirTicket) {
            abrirVentanaFactura('facturaPOS.html');
        } else {
            abrirVentanaFactura('Facturaelectronica.html');
        }

    } else if (metodoDePagoSeleccionado === 'Crédito' || metodoDePagoSeleccionado === 'Tarjeta' || metodoDePagoSeleccionado === 'QR') {
        alert(`Pago con ${metodoDePagoSeleccionado} procesado a través del datáfono.`);
        const modalCobrar = bootstrap.Modal.getInstance(document.getElementById('modalCobrar'));
        modalCobrar.hide();

        limpiarCarrito();

        if (imprimirTicket) {
            abrirVentanaFactura('facturaPOS.html');
        } else {
            abrirVentanaFactura('Facturaelectronica.html');
        }
    }
}


function abrirVentanaFactura(url) {
    const ventanaFactura = window.open(url, '_blank');
    if (ventanaFactura) {
        ventanaFactura.onload = function() {
            const itemsFacturaDiv = ventanaFactura.document.getElementById('items-factura');
            const totalFacturaSpan = ventanaFactura.document.getElementById('total-factura');
            const totalVenta = parseFloat(document.getElementById('totalVentaHidden').value);
            const carritoLista = document.getElementById('carritoLista');

            if (itemsFacturaDiv && totalFacturaSpan && carritoLista) {
                itemsFacturaDiv.innerHTML = '';
                carritoLista.querySelectorAll('li').forEach(itemLi => {
                    const nombre = itemLi.querySelector('.fw-bold').textContent;
                    const cantidad = itemLi.querySelector('.cantidad-input').value;
                    const totalItemTexto = itemLi.querySelector('.total-item').textContent.replace('$', '').replace('.', '').replace(',', '.');
                    const totalItem = parseFloat(totalItemTexto);

                    const itemDiv = ventanaFactura.document.createElement('div');
                    itemDiv.classList.add('item');
                    itemDiv.innerHTML = `
                        <span>${cantidad} x ${nombre}</span>
                        <span>$${formatNumberWithCommas(totalItem)}</span>`;
                    itemsFacturaDiv.appendChild(itemDiv);
                });

                totalFacturaSpan.textContent = formatNumberWithCommas(totalVenta);
            }
        };
    }
}



function seleccionarMetodoLectura(metodo) {
    metodoLecturaSeleccionado = metodo;

    document.querySelectorAll('[onclick^="seleccionarMetodoLectura"]').forEach(btn => {
        btn.classList.remove('active');
    });

    const boton = [...document.querySelectorAll('[onclick^="seleccionarMetodoLectura"]')].find(btn =>
        btn.getAttribute('onclick').includes(`'${metodo}'`)
    );
    if (boton) boton.classList.add('active');

    if (metodo === 'codigo') {
        console.log('Modo: Código de barras activado');
    } else if (metodo === 'teclado') {
        console.log('Modo: Teclado activado');
    }
}
function limpiarCarrito() {
    const carritoLista = document.getElementById('carritoLista');
    carritoLista.innerHTML = '';

    carrito = [];

    document.getElementById('productosCantidad').textContent = '00 productos';
    document.getElementById('totalVenta').textContent = '$0';
    document.getElementById('totalVentaHidden').value = 0;
    document.getElementById('clienteNombre').innerHTML = '<i class="bi bi-person"></i> Nombre del cliente';

    eliminarDescuento();
}

function eliminarDescuento() {
    document.getElementById("mensajeDescuento").style.display = "none";
    document.getElementById("inputDescuento").value = "";
    document.getElementById("descuentoAplicadoTexto").textContent = "";
    document.getElementById("valorDescuentoPorcentaje").textContent = "";
    document.getElementById("valorDescuentoMoneda").textContent = "";
}

function buscarClientePorCedula() {
    const cedula = document.getElementById('cedulaCliente').value;
    const resultadoDiv = document.getElementById('resultadoBusquedaCliente');
    const nombreClienteParrafo = document.getElementById('nombreClienteEncontrado');
    const btnAceptarCliente = document.getElementById('btnAceptarCliente');

    const clientesRegistrados = [
        { cedula: '1', nombre: 'Orlan Tabares' },
        { cedula: '2', nombre: 'Yoe Hernandez' },
        { cedula: '3', nombre: 'Luz Gutierrez' }
    ];

    const clienteEncontrado = clientesRegistrados.find(cliente => cliente.cedula === cedula);

    if (clienteEncontrado) {
        nombreClienteParrafo.textContent = clienteEncontrado.nombre;
        resultadoDiv.style.display = 'block';
        btnAceptarCliente.disabled = false;
    } else {
        nombreClienteParrafo.textContent = 'No se encontró ningún cliente con esa cédula.';
        resultadoDiv.style.display = 'block';
        btnAceptarCliente.disabled = true;
    }
}

function aceptarCliente() {
    const nombreCliente = document.getElementById('nombreClienteEncontrado').textContent;
    document.getElementById('clienteSeleccionado').value = nombreCliente;
    const modalSeleccionarCliente = bootstrap.Modal.getInstance(document.getElementById('modalSeleccionarCliente'));
    if (modalSeleccionarCliente) {
        modalSeleccionarCliente.hide();
    }
    const clienteNombreElement = document.getElementById('clienteNombre');
    if (clienteNombreElement) {
        clienteNombreElement.innerHTML = `<i class="bi bi-person"></i> ${nombreCliente}`;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const cedulaInput = document.getElementById('cedulaCliente');
    if (cedulaInput) {
        cedulaInput.addEventListener('input', buscarClientePorCedula);
    }

    const aceptarBtn = document.getElementById('btnAceptarCliente');
    if (aceptarBtn) {
        aceptarBtn.addEventListener('click', aceptarCliente);
    }
});