function abrirModalProducto() {
    document.getElementById('formProducto').reset();
    document.getElementById('productoId').value = '';
    const modal = new bootstrap.Modal(document.getElementById('modalProducto'));
    modal.show();
}

function guardarProducto() {
    const sku = document.getElementById('sku').value.trim();
    const nombre = document.getElementById('nombre').value.trim();
    const categoria = document.getElementById('categoria').value.trim();
    const stock = parseInt(document.getElementById('stock').value, 10);
    const precioCompra = parseFloat(document.getElementById('precioCompra').value);
    const precioVenta = parseFloat(document.getElementById('precioVenta').value);
    const estado = document.getElementById('estado').value;

    // VALIDACIONES
    if (!sku) {
        alert('⚠️ Ingrese un SKU válido (código de barras).');
        return;
    }
    if (!nombre) {
        alert('⚠️ Ingrese un nombre válido.');
        return;
    }
    if (isNaN(stock) || stock < 0) {
        alert('⚠️ Ingrese un stock válido.');
        return;
    }
    if (isNaN(precioCompra) || precioCompra < 0) {
        alert('⚠️ Ingrese un precio de compra válido.');
        return;
    }
    if (isNaN(precioVenta) || precioVenta < 0) {
        alert('⚠️ Ingrese un precio de venta válido.');
        return;
    }

    const tabla = document.getElementById('tablaInventario');
    const productoExistente = document.querySelector(`#tablaInventario tr[data-sku="${sku}"]`);

    // Si es un nuevo producto y ya existe el SKU
    if (productoExistente && !document.getElementById('productoId').value) {
        alert('⚠️ Ya existe un producto con ese SKU (código de barras).');
        return;
    }

    if (document.getElementById('productoId').value) {
        // Editar producto existente
        productoExistente.querySelector('.td-nombre').textContent = nombre;
        productoExistente.querySelector('.td-categoria').textContent = categoria;
        productoExistente.querySelector('.td-stock').textContent = stock;
        productoExistente.querySelector('.td-precio-compra').textContent = `$${precioCompra.toFixed(2)}`;
        productoExistente.querySelector('.td-precio-venta').textContent = `$${precioVenta.toFixed(2)}`;
        productoExistente.querySelector('.td-estado').textContent = estado;

        alert('✅ Producto actualizado correctamente.');
    } else {
        // Agregar nuevo producto
        const fila = document.createElement('tr');
        fila.setAttribute('data-sku', sku);
        fila.innerHTML = `
            <td class="td-sku">${sku}</td>
            <td class="td-nombre">${nombre}</td>
            <td class="td-categoria">${categoria}</td>
            <td class="td-stock">${stock}</td>
            <td class="td-precio-compra">$${precioCompra.toFixed(2)}</td>
            <td class="td-precio-venta">$${precioVenta.toFixed(2)}</td>
            <td class="td-estado">${estado}</td>
            <td>
                <button class="btn btn-sm btn-outline-primary me-1" title="Editar" onclick="editarProducto('${sku}')">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-outline-danger" title="Eliminar" onclick="eliminarProducto('${sku}')">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        `;
        tabla.appendChild(fila);

        alert('✅ Producto agregado correctamente.');
    }

    bootstrap.Modal.getInstance(document.getElementById('modalProducto')).hide();
}

function editarProducto(sku) {
    const fila = document.querySelector(`#tablaInventario tr[data-sku="${sku}"]`);
    if (!fila) return;

    document.getElementById('sku').value = fila.querySelector('.td-sku').textContent;
    document.getElementById('sku').disabled = true;
    document.getElementById('nombre').value = fila.querySelector('.td-nombre').textContent;
    document.getElementById('categoria').value = fila.querySelector('.td-categoria').textContent;
    document.getElementById('stock').value = fila.querySelector('.td-stock').textContent;
    document.getElementById('precioCompra').value = fila.querySelector('.td-precio-compra').textContent.replace('$', '');
    document.getElementById('precioVenta').value = fila.querySelector('.td-precio-venta').textContent.replace('$', '');
    document.getElementById('estado').value = fila.querySelector('.td-estado').textContent;
    document.getElementById('productoId').value = sku;

    const modal = new bootstrap.Modal(document.getElementById('modalProducto'));
    modal.show();
}

function eliminarProducto(sku) {
    if (confirm('¿Está seguro que desea eliminar este producto?')) {
        const fila = document.querySelector(`#tablaInventario tr[data-sku="${sku}"]`);
        if (fila) fila.remove();
        alert('✅ Producto eliminado correctamente.');
    }
}

document.getElementById('modalProducto').addEventListener('show.bs.modal', () => {
    if (!document.getElementById('productoId').value) {
        document.getElementById('sku').disabled = false;
    }
});

function buscarProducto() {
    const query = document.getElementById('busquedaInventario').value.toLowerCase();
    const filas = document.querySelectorAll('#tablaInventario tr');
    filas.forEach(fila => {
        const texto = fila.textContent.toLowerCase();
        fila.style.display = texto.includes(query) ? '' : 'none';
    });
}

function seleccionarMetodoLectura(metodo) {
    const btnCodigo = document.getElementById('btnMetodoCodigo');
    const btnTeclado = document.getElementById('btnMetodoTeclado');

    if (metodo === 'codigo') {
        btnCodigo.classList.add('active');
        btnTeclado.classList.remove('active');
    } else if (metodo === 'teclado') {
        btnTeclado.classList.add('active');
        btnCodigo.classList.remove('active');
    }
}

// Actualiza el botón y el input hidden al seleccionar una opción
document.querySelectorAll('.dropdown-item').forEach(item => {
    item.addEventListener('click', function(e) {
        e.preventDefault();
        const value = this.getAttribute('data-value');
        const button = document.getElementById('dropdownEstado');
        button.textContent = this.textContent; // Cambia el texto del botón
        document.getElementById('estado').value = value; // Actualiza hidden
    });
});
