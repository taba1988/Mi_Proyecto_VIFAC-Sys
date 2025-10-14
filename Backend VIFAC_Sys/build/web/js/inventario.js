/* global fetch, bootstrap, estado, nombre, sku */

let metodoLectura = 'teclado'; // valor por defecto

document.addEventListener('DOMContentLoaded', () => {
    cargarProductos();
    
    const form = document.getElementById('formInventario');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            guardarProducto();
        });
    }
    
    // Inicializar método de lectura "Teclado" por defecto
    seleccionarMetodoLectura('teclado');
    
    // --- CÁLCULO AUTOMÁTICO DE PRECIO DE VENTA ---
const inputCompra = document.getElementById('precio_compra');
const inputPorcentaje = document.getElementById('porcentaje_ganancia');
const inputVenta = document.getElementById('precio_venta');

function actualizarPrecioVenta() {
    const compra = parseFloat(inputCompra.value) || 0;
    const porcentaje = parseFloat(inputPorcentaje.value) || 0;
    const venta = compra + (compra * porcentaje / 100);
    inputVenta.value = venta.toFixed(2);
}

if (inputCompra) inputCompra.addEventListener('input', actualizarPrecioVenta);
if (inputPorcentaje) inputPorcentaje.addEventListener('input', actualizarPrecioVenta);


    // BÚSQUEDA EN TIEMPO REAL
    const busquedaInventarioInput = document.getElementById('busquedaInventario');
    let debounceTimer;
    if (busquedaInventarioInput) {
        busquedaInventarioInput.addEventListener('keyup', () => {
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                const busqueda = busquedaInventarioInput.value.trim();
                if (busqueda === '') {
                    cargarProductos();
                    return;
                }
                fetch(`InventarioServlet?accion=buscar&busqueda=${encodeURIComponent(busqueda)}`)
                    .then(response => response.json())
                    .then(productos => {
                        const tabla = document.getElementById('tablaInventario');
                        tabla.innerHTML = '';
                        if (productos.length === 0) {
                            tabla.innerHTML = '<tr><td colspan="9" class="text-center">No se encontraron productos con ese criterio.</td></tr>';
                            return;
                        }
                        productos.forEach((producto, index) => {
                            const fila = document.createElement('tr');
                            fila.innerHTML = `
                                <td>${index + 1}</td>
                                <td>${producto.sku}</td>
                                <td>${producto.nombre}</td>
                                <td>${producto.categoria || 'Sin categoría'}</td>
                                <td>${producto.stock}</td>
                                <td>$${Number(producto.precio_compra).toFixed(2)}</td>
                                <td>$${Number(producto.precio_venta).toFixed(2)}</td>
                                <td>${producto.estado}</td>
                                <td>
                                    <button class="btn btn-sm btn-outline-primary me-1" title="Editar" onclick="editarProducto(${producto.idProducto})">
                                        <i class="bi bi-pencil"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger" title="Eliminar" onclick="eliminarProducto(${producto.idProducto})">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            `;
                            tabla.appendChild(fila);
                        });
                    })
                    .catch(error => console.error('Error al realizar la búsqueda en tiempo real:', error));
            }, 300);
        });
    }

    // Manejo de dropdown de estado
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('dropdown-item')) {
            e.preventDefault();
            const value = e.target.getAttribute('data-value');
            const button = document.getElementById('dropdownEstado');
            if (button) button.textContent = e.target.textContent;
            const hidden = document.getElementById('estado');
            if (hidden) hidden.value = value || e.target.textContent;
        }
    });
});

// Abrir modal de producto (Agregar)
function abrirModalProducto() {
    document.getElementById('formInventario').reset();
    document.getElementById('idProducto').value = '';
    document.getElementById('accionInput').value = 'agregar';
    document.getElementById('sku').disabled = false;
    document.getElementById('modalInventarioLabel').textContent = 'Agregar Producto';
    
    // Inicializar método de lectura
    seleccionarMetodoLectura(metodoLectura);

    const modal = new bootstrap.Modal(document.getElementById('modalInventario'));
    modal.show();
}

// Selección de método de lectura
function seleccionarMetodoLectura(metodo) {
    metodoLectura = metodo;

    const skuInput = document.getElementById('sku');
    if (!skuInput) return;

    const btnCodigo = document.getElementById('btnMetodoCodigo');
    const btnTeclado = document.getElementById('btnMetodoTeclado');

    if (metodo === 'codigo') {
        skuInput.disabled = true;
        skuInput.value = '';
        skuInput.focus();

        btnCodigo.classList.add('metodo-selected');
        btnCodigo.classList.remove('metodo-unselected');

        btnTeclado.classList.add('metodo-unselected');
        btnTeclado.classList.remove('metodo-selected');
    } else {
        skuInput.disabled = false;
        skuInput.value = '';
        skuInput.focus();

        btnTeclado.classList.add('metodo-selected');
        btnTeclado.classList.remove('metodo-unselected');

        btnCodigo.classList.add('metodo-unselected');
        btnCodigo.classList.remove('metodo-selected');
    }
}

// Guardar producto
function guardarProducto() {
    const form = document.getElementById('formInventario');
    const formData = new FormData(form);

    if (!confirm("¿Desea guardar los datos?")) return;

    const accion = document.getElementById('accionInput').value;
    formData.set('accion', accion);

    const sku = formData.get('sku')?.trim() || '';
    const nombre = formData.get('nombre')?.trim() || '';
    const estado = formData.get('estado')?.trim() || '';
    const precioCompraStr = formData.get('precio_compra') || '';
    const precioVentaStr = formData.get('precio_venta') || '';
    const stockStr = formData.get('stock') || '';

    if (!sku || !nombre || !estado || !precioCompraStr || !precioVentaStr || !stockStr) {
        alert('⚠️ Por favor, complete todos los campos requeridos.');
        return;
    }

    const precio_compra = parseFloat(precioCompraStr);
    const precio_venta = parseFloat(precioVentaStr);
    const stock = parseInt(stockStr, 10);

    if (isNaN(precio_compra) || isNaN(precio_venta) || isNaN(stock)) {
        alert('⚠️ Los precios y el stock deben ser números válidos.');
        return;
    }

    formData.set('precio_compra', precio_compra);
    formData.set('precio_venta', precio_venta);
    formData.set('stock', stock);

    const params = new URLSearchParams();
    formData.forEach((value, key) => params.append(key, value));

    fetch('InventarioServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body: params.toString()
    })
    .then(response => response.json())
    .then(data => {
        const ok = data && (data.success === "success" || data.success === true || data.status === "success");
        const msg = data && (data.message || data.msg) || null;

        if (ok) {
            alert('✅ Operación realizada correctamente.');
            const modalInstance = bootstrap.Modal.getInstance(document.getElementById('modalInventario'));
            if (modalInstance) modalInstance.hide();
            cargarProductos();
        } else {
            alert(`❌ Error: ${msg || 'Operación no válida.'}`);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('❌ Error al comunicarse con el servidor.');
    });
}

// Cargar productos
function cargarProductos() {
    fetch('InventarioServlet?accion=listar')
    .then(response => response.json())
    .then(productos => {
        const tabla = document.getElementById('tablaInventario');
        tabla.innerHTML = '';
        productos.forEach((producto, index) => {
            const fila = document.createElement('tr');
            fila.innerHTML = `
                <td>${index + 1}</td>
                <td>${producto.sku}</td>
                <td>${producto.nombre}</td>
                <td>${producto.categoria || 'Sin categoría'}</td>
                <td>${producto.stock}</td>
                <td>$${Number(producto.precio_compra).toFixed(2)}</td>
                <td>$${Number(producto.precio_venta).toFixed(2)}</td>
                <td>${producto.estado}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary me-1" title="Editar" onclick="editarProducto(${producto.idProducto})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" title="Eliminar" onclick="eliminarProducto(${producto.idProducto})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
            tabla.appendChild(fila);
        });
    })
    .catch(error => console.error('Error al cargar productos:', error));
}

// Editar producto
function editarProducto(id) {
    fetch(`InventarioServlet?accion=buscarId&idProducto=${id}`)
    .then(response => response.json())
    .then(producto => {
        document.getElementById('idProducto').value = producto.idProducto || '';
        document.getElementById('sku').value = producto.sku || '';
        document.getElementById('nombre').value = producto.nombre || '';
        document.getElementById('descripcion').value = producto.descripcion || '';
        document.getElementById('precio_compra').value = producto.precio_compra !== null ? producto.precio_compra : '';
        document.getElementById('precio_venta').value = producto.precio_venta !== null ? producto.precio_venta : '';
        document.getElementById('stock').value = producto.stock !== null ? producto.stock : '';
        document.getElementById('unidad_medida').value = producto.unidad_medida || '';
        document.getElementById('estado').value = producto.estado || '';
        document.getElementById('idCategoria').value = producto.idCategoria !== null ? producto.idCategoria : '';

        document.getElementById('accionInput').value = 'editar';
        document.getElementById('dropdownEstado').textContent = producto.estado || 'Seleccionar';
        document.getElementById('modalInventarioLabel').textContent = 'Editar Producto';

        const modal = new bootstrap.Modal(document.getElementById('modalInventario'));
        modal.show();

        // Mantener método de lectura seleccionado
        seleccionarMetodoLectura(metodoLectura);
    })
    .catch(error => console.error('Error al buscar producto:', error));
}

// Listener para método código de barras
document.addEventListener('keydown', (e) => {
    if (metodoLectura !== 'codigo') return;
    const skuInput = document.getElementById('sku');
    if (!skuInput) return;

    if (e.key === 'Enter') {
        e.preventDefault();
        guardarProducto();
    } else {
        skuInput.value += e.key;
    }
});

// Eliminar producto
function eliminarProducto(id) {
    if (!confirm('¿Está seguro que desea eliminar este producto?')) return;

    const body = `accion=eliminar&idProducto=${encodeURIComponent(id)}`;

    fetch('InventarioServlet', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
        body: body
    })
    .then(response => response.json())
    .then(data => {
        const ok = data && (data.success === "success" || data.success === true || data.status === "success");
        if (ok) {
            alert('✅ Producto eliminado correctamente.');
            cargarProductos();
        } else {
            alert(`❌ Error al eliminar: ${data && data.message ? data.message : 'Error desconocido'}`);
        }
    })
    .catch(error => {
        console.error('Error al eliminar:', error);
        alert('❌ Error al comunicarse con el servidor.');
    });
}
