/* global bootstrap, fetch */

let proveedores = [];
let proveedorEditandoId = null;

const modalProveedores = new bootstrap.Modal(
  document.getElementById("modalProveedores")
);

// Validar correo electrónico en tiempo real
function validarCorreo(input) {
  const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (regexCorreo.test(input.value)) {
    input.classList.remove("is-invalid");
    input.classList.add("is-valid");
  } else {
    input.classList.add("is-invalid");
    input.classList.remove("is-valid");
  }
}

// Abrir modal vacío para agregar proveedor
function abrirModalProveedores() {
  proveedorEditandoId = null;
  document.getElementById("formProveedores").reset();
  document.getElementById("item").value = proveedores.length + 1;
  document.getElementById("idProveedor").value = "";
  document.getElementById("accionInput").value = "agregar";
  limpiarValidacionesProveedores();
  modalProveedores.show();
}

// Limpiar validaciones de proveedores
function limpiarValidacionesProveedores() {
  const inputs = document.querySelectorAll("#formProveedores input, #formProveedores select");
  inputs.forEach((input) => {
    input.classList.remove("is-invalid", "is-valid");
  });
  document.querySelectorAll("#formProveedores .invalid-feedback").forEach(el => el.textContent = "");
}

// Validar formulario de proveedores
function validarFormularioProveedores() {
  limpiarValidacionesProveedores();
  let valido = true;

  const nombreEmpresa = document.getElementById("nombreEmpresa").value.trim();
  const documento_NIT = document.getElementById("documento_NIT").value.trim();
  const asesor = document.getElementById("asesor").value.trim();
  const telefono = document.getElementById("telefono").value.trim();
  const email = document.getElementById("email").value.trim();
  const diaVisita = document.getElementById("diaVisita").value;
  const estado = document.getElementById("estado").value;

  if (!nombreEmpresa) {
    document.getElementById("nombreEmpresa").classList.add("is-invalid");
    valido = false;
  }

  if (!/^\d{1,10}$/.test(documento_NIT)) {
    document.getElementById("documento_NIT").classList.add("is-invalid");
    document.getElementById("documentoFeedback").textContent =
      "Documento/NIT no válido (máximo 10 dígitos)";
    valido = false;
  }

  if (!asesor) {
    document.getElementById("asesor").classList.add("is-invalid");
    document.getElementById("asesorFeedback").textContent =
      "Ingrese el nombre del asesor";
    valido = false;
  }

  if (!/^\d{10}$/.test(telefono)) {
    document.getElementById("telefono").classList.add("is-invalid");
    document.getElementById("telefonoFeedback").textContent =
      "Teléfono no válido (10 dígitos)";
    valido = false;
  }

  const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!regexCorreo.test(email)) {
    document.getElementById("email").classList.add("is-invalid");
    valido = false;
  }

  if (!diaVisita) {
    document.getElementById("diaVisita").classList.add("is-invalid");
    document.getElementById("diaVisitaFeedback").textContent =
      "Seleccione el día de visita.";
    valido = false;
  }

  if (!estado) {
    document.getElementById("estado").classList.add("is-invalid");
    valido = false;
  }

  return valido;
}

// Función para calcular la próxima visita
function calcularProximaVisita(diaSemana) {
  const diasSemana = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
  const hoy = new Date();
  const hoyDia = hoy.getDay();
  const diaObjetivo = diasSemana.indexOf(diaSemana);
  let diferencia = diaObjetivo - hoyDia;
  if (diferencia <= 0) diferencia += 7;
  hoy.setDate(hoy.getDate() + diferencia);
  return hoy.toISOString().split('T')[0];
}

// Guardar proveedor (Agregar o Editar)
async function guardarProveedor() {
  if (!validarFormularioProveedores()) return;

  const confirmacion = confirm("¿Guardar los datos del proveedor?");
  if (!confirmacion) return;

  const form = document.getElementById("formProveedores");
  const formData = new FormData(form);

  const diaVisita = document.getElementById("diaVisita").value;
  const proximaVisita = calcularProximaVisita(diaVisita);

  formData.append("accion", proveedorEditandoId ? "editar" : "agregar");
  if (proveedorEditandoId) {
    formData.append("id", proveedorEditandoId);
  }
  formData.append("proximaVisita", proximaVisita);

  try {
    const res = await fetch("ProveedoresServlet", {
      method: "POST",
      body: new URLSearchParams(formData)
    });
    const data = await res.json();

    if (data.status === "success") {
      alert(data.message);
      modalProveedores.hide();
      cargarProveedoresDelServidor();
    } else {
      alert("Error: " + data.message);
    }
  } catch (err) {
    console.error("Error al guardar:", err);
    alert("No se pudo guardar el proveedor.");
  }
}

// Editar proveedor
function editarProveedor(id) {
  const proveedor = proveedores.find(p => p.idProveedor === id);
  if (!proveedor) return;
  
  proveedorEditandoId = id;

  document.getElementById("nombreEmpresa").value = proveedor.nombreEmpresa;
  document.getElementById("documento_NIT").value = proveedor.documento_NIT;
  document.getElementById("asesor").value = proveedor.asesor;
  document.getElementById("telefono").value = proveedor.telefono;
  document.getElementById("email").value = proveedor.email;
  document.getElementById("diaVisita").value = proveedor.diaVisita;
  document.getElementById("dropdownDiaVisita").textContent = proveedor.dia_visita;
  document.getElementById("estado").value = proveedor.estado;
  document.getElementById("dropdownEstado").textContent = proveedor.estado;
  document.getElementById("idProveedor").value = proveedor.idProveedor;
  document.getElementById("accionInput").value = "editar";

  modalProveedores.show();
}

// Eliminar proveedor
async function eliminarProveedor(id) {
  if (!confirm("¿Eliminar este proveedor?")) return;

  const formData = new FormData();
  formData.append("accion", "eliminar");
  formData.append("id", id);

  try {
    const res = await fetch("ProveedoresServlet", {
      method: "POST",
      body: new URLSearchParams(formData)
    });
    const data = await res.json();

    if (data.status === "success") {
      alert(data.message);
      cargarProveedoresDelServidor();
    } else {
      alert("Error al eliminar: " + data.message);
    }
  } catch (err) {
    console.error("Error al eliminar proveedor:", err);
  }
}

// Buscar proveedores
async function buscarProveedores(event) {
  if (event) event.preventDefault();

  const filtro = document.getElementById("busquedaProveedores").value.trim();
  try {
    const res = await fetch(`ProveedoresServlet?accion=buscar&busqueda=${encodeURIComponent(filtro)}`);
    const data = await res.json();
    proveedores = data;
    actualizarTablaProveedores();
  } catch (err) {
    console.error("Error al buscar proveedores:", err);
  }
}

async function cargarProveedoresDelServidor() {
  try {
    const res = await fetch("ProveedoresServlet?accion=listar&_=" + new Date().getTime());
    const data = await res.json();
    proveedores = data;
    actualizarTablaProveedores();
  } catch (err) {
    console.error("Error al cargar proveedores:", err);
  }
}

// Función para actualizar la tabla de proveedores
function actualizarTablaProveedores() {
  const tbody = document.getElementById("tablaProveedores");
  tbody.innerHTML = "";

  proveedores.forEach((p) => {
    const fila = document.createElement("tr");

    const proxima = new Date(p.proximaVisita);
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    const alerta = proxima <= hoy ? '<span class="text-danger fw-bold">¡Próxima visita!</span><br>' : "";

    fila.innerHTML = `
      <td>${p.idProveedor}</td>
      <td>${p.nombreEmpresa}</td>
      <td>${p.documento_NIT}</td>
      <td>${p.asesor}</td>
      <td>${p.telefono}</td>
      <td>${p.email}</td>
      <td>${p.diaVisita}</td>
      <td>${p.estado}</td>
      <td>
        <button class="btn btn-sm btn-outline-primary me-1" onclick="editarProveedor(${p.idProveedor})"><i class="bi bi-pencil-square"></i></button>
        <button class="btn btn-sm btn-outline-danger" onclick="eliminarProveedor(${p.idProveedor})"><i class="bi bi-trash"></i></button>
      </td>
    `;
    tbody.appendChild(fila);
  });
}

// Evento para buscar proveedores cuando se escribe
document.getElementById("busquedaProveedores").addEventListener("input", buscarProveedores);

// Cargar proveedores al iniciar la página
document.addEventListener("DOMContentLoaded", cargarProveedoresDelServidor);

// Manejo del dropdown Día de Visita
document.querySelectorAll("#dropdownDiaVisita + .dropdown-menu .dropdown-item").forEach(item => {
  item.addEventListener("click", function () {
    const valor = this.getAttribute("data-value");
    document.getElementById("dropdownDiaVisita").textContent = valor;
    document.getElementById("diaVisita").value = valor;
  });
});

// Manejo del dropdown Estado
document.querySelectorAll("#dropdownEstado + .dropdown-menu .dropdown-item").forEach(item => {
  item.addEventListener("click", function () {
    const valor = this.getAttribute("data-value");
    document.getElementById("dropdownEstado").textContent = valor;
    document.getElementById("estado").value = valor;
  });
});

// Previene que el formulario recargue la página al hacer submit
document.getElementById("formProveedores").addEventListener("submit", function (event) {
  event.preventDefault();
});

// Conecta el botón "Guardar" con la función guardarProveedor
document.getElementById("btnguardarProveedor").addEventListener("click", guardarProveedor);