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
  const precio = parseFloat(document.getElementById('precio').value);
  const estado = document.getElementById('estado').value;

  if (!sku) return alert('Ingrese un SKU válido (código de barras).');
  if (!nombre) return alert('Ingrese un nombre válido.');

  const tabla = document.getElementById('tablaInventario');
  const productoExistente = document.querySelector(`#tablaInventario tr[data-sku="${sku}"]`);

  if (productoExistente && !document.getElementById('productoId').value) {
    return alert('Ya existe un producto con ese SKU (código de barras).');
  }

  if (document.getElementById('productoId').value) {
    productoExistente.querySelector('.td-nombre').textContent = nombre;
    productoExistente.querySelector('.td-categoria').textContent = categoria;
    productoExistente.querySelector('.td-stock').textContent = stock;
    productoExistente.querySelector('.td-precio').textContent = `$${precio.toFixed(2)}`;
    productoExistente.querySelector('.td-estado').textContent = estado;
  } else {
    const fila = document.createElement('tr');
    fila.setAttribute('data-sku', sku);
    fila.innerHTML = `
      <td class="td-sku">${sku}</td>
      <td class="td-nombre">${nombre}</td>
      <td class="td-categoria">${categoria}</td>
      <td class="td-stock">${stock}</td>
      <td class="td-precio">$${precio.toFixed(2)}</td>
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
  document.getElementById('precio').value = fila.querySelector('.td-precio').textContent.replace('$', '');
  document.getElementById('estado').value = fila.querySelector('.td-estado').textContent;
  document.getElementById('productoId').value = sku;

  const modal = new bootstrap.Modal(document.getElementById('modalProducto'));
  modal.show();
}

function eliminarProducto(sku) {
  if (confirm('¿Está seguro que desea eliminar este producto?')) {
    const fila = document.querySelector(`#tablaInventario tr[data-sku="${sku}"]`);
    if (fila) fila.remove();
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
