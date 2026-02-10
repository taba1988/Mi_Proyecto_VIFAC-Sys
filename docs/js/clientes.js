function validarCorreo(input) {
  const valor = input.value.trim();
  const patronCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (valor === "" || !patronCorreo.test(valor)) {
    input.classList.add("is-invalid");
  } else {
    input.classList.remove("is-invalid");
  }
}

function abrirModalCliente() {
  document.getElementById("formCliente").reset();
  clearValidations();
  document.getElementById("clienteId").value = "";
  const modal = new bootstrap.Modal(
    document.getElementById("modalClientes")
  );
  modal.show();
}

function clearValidations() {
  ["documento", "telefono", "correoElectronico"].forEach((id) => {
    document.getElementById(id).classList.remove("is-invalid");
  });
  document.getElementById("documentoFeedback").textContent = "";
  document.getElementById("telefonoFeedback").textContent = "";
}

function guardarCliente() {
  clearValidations();

  const nombre = document.getElementById("nombre").value.trim();
  const documento = document.getElementById("documento").value.trim();
  const telefono = document.getElementById("telefono").value.trim();
  const correoElectronico = document
    .getElementById("correoElectronico")
    .value.trim();
  const estado = document.getElementById("estado").value;
  const clienteId = document.getElementById("clienteId").value;

  let valido = true;

  if (!nombre) {
    valido = false;
    alert("Por favor ingrese el nombre completo.");
  }

  // Validar documento (10 dígitos)
  if (documento.length !== 10) {
    valido = false;
    document.getElementById("documento").classList.add("is-invalid");
    document.getElementById("documentoFeedback").textContent =
      "El documento debe tener exactamente 10 dígitos.";
  }

  // Validar teléfono (10 dígitos)
  if (telefono.length !== 10) {
    valido = false;
    document.getElementById("telefono").classList.add("is-invalid");
    document.getElementById("telefonoFeedback").textContent =
      "El teléfono debe tener exactamente 10 dígitos.";
  }

  // Validar correo
  const patronCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!patronCorreo.test(correoElectronico)) {
    valido = false;
    document.getElementById("correoElectronico").classList.add("is-invalid");
  }

  if (!valido) {
    return;
  }

  // Obtener clientes guardados en localStorage
  let clientes = JSON.parse(localStorage.getItem("clientes")) || [];

  if (clienteId) {
    // Editar cliente existente
    const index = clientes.findIndex(
      (cliente) => cliente.id === clienteId
    );
    if (index !== -1) {
      clientes[index] = {
        id: clienteId,
        nombre,
        documento,
        telefono,
        correoElectronico,
        estado,
      };
    }
  } else {
    // Agregar nuevo cliente
    const nuevoCliente = {
      id: crypto.randomUUID(),
      nombre,
      documento,
      telefono,
      correoElectronico,
      estado,
    };
    clientes.push(nuevoCliente);
  }

  localStorage.setItem("clientes", JSON.stringify(clientes));
  listarClientes();

  // Cerrar modal
  const modal = bootstrap.Modal.getInstance(
    document.getElementById("modalClientes")
  );
  modal.hide();
}

function listarClientes(clientesFiltrados) {
  const tbody = document.getElementById("tablaClientes");
  tbody.innerHTML = "";

  // Usar clientes filtrados o todos
  let clientes =
    clientesFiltrados ||
    JSON.parse(localStorage.getItem("clientes")) ||
    [];

  clientes.forEach((cliente, index) => {
    const tr = document.createElement("tr");

    tr.innerHTML = `
      <td>${index + 1}</td>
      <td>${cliente.nombre}</td>
      <td>${cliente.documento}</td>
      <td>${cliente.telefono}</td>
      <td>${cliente.correoElectronico}</td>
      <td>${cliente.estado}</td>
      <td>
        <button class="btn btn-sm btn-outline-primary" onclick="editarCliente('${cliente.id}')">
          <i class="bi bi-pencil-fill"></i>
        </button>
        <button class="btn btn-sm btn-outline-danger" onclick="eliminarCliente('${cliente.id}')">
          <i class="bi bi-trash-fill"></i>
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

function editarCliente(id) {
  const clientes = JSON.parse(localStorage.getItem("clientes")) || [];
  const cliente = clientes.find((c) => c.id === id);
  if (!cliente) return;

  document.getElementById("nombre").value = cliente.nombre;
  document.getElementById("documento").value = cliente.documento;
  document.getElementById("telefono").value = cliente.telefono;
  document.getElementById("correoElectronico").value = cliente.correoElectronico;
  document.getElementById("estado").value = cliente.estado;
  document.getElementById("clienteId").value = cliente.id;

  clearValidations();

  const modal = new bootstrap.Modal(
    document.getElementById("modalClientes")
  );
  modal.show();
}

function eliminarCliente(id) {
  if (
    !confirm(
      "¿Está seguro que desea eliminar este cliente? Esta acción no se puede deshacer."
    )
  )
    return;

  let clientes = JSON.parse(localStorage.getItem("clientes")) || [];
  clientes = clientes.filter((cliente) => cliente.id !== id);
  localStorage.setItem("clientes", JSON.stringify(clientes));
  listarClientes();
}

function buscarCliente(event) {
  if (event) event.preventDefault();

  const filtro = document
    .getElementById("busquedaCliente")
    .value.trim()
    .toLowerCase();

  const clientes = JSON.parse(localStorage.getItem("clientes")) || [];
  if (!filtro) {
    listarClientes(clientes);
    return;
  }

  const clientesFiltrados = clientes.filter(
    (cliente) =>
      cliente.nombre.toLowerCase().includes(filtro) ||
      cliente.documento.includes(filtro)
  );

  listarClientes(clientesFiltrados);
}

// Inicializar tabla al cargar página
document.addEventListener("DOMContentLoaded", () => {
  listarClientes();
});
document.addEventListener("DOMContentLoaded", function () {
  const dropdownButton = document.getElementById("dropdownEstado");
  const hiddenInput = document.getElementById("estado");
  const dropdownItems = document.querySelectorAll("#dropdownEstado + .dropdown-menu .dropdown-item");

  dropdownItems.forEach(item => {
    item.addEventListener("click", function (e) {
      e.preventDefault();
      const value = this.getAttribute("data-value");
      dropdownButton.textContent = value;  // cambia el texto del botón
      hiddenInput.value = value;           // asigna el valor al input oculto
    });
  });
});
