let usuarios = [];
let usuarioEditandoId = null;

const modalUsuarios = new bootstrap.Modal(
  document.getElementById("modalUsuarios")
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

// Abrir modal vacío para agregar usuario
function abrirModalUsuario() {
  usuarioEditandoId = null;
  document.getElementById("formUsuario").reset();
  document.getElementById("item").value = usuarios.length + 1;
  document.getElementById("usuarioId").value = "";
  document.getElementById("dropdownRol").textContent = "Seleccionar rol";
  document.getElementById("rolInput").value = "";
  document.getElementById("dropdownEstado").textContent = "Seleccionar";
  document.getElementById("estadoInput").value = "";
  limpiarValidaciones();
  modalUsuarios.show();
}

// Limpiar validaciones
function limpiarValidaciones() {
  const inputs = document.querySelectorAll("#formUsuario input");
  inputs.forEach((input) => {
    input.classList.remove("is-invalid", "is-valid");
  });
  document.querySelectorAll(".invalid-feedback").forEach(el => el.textContent = "");
}

// Validar contraseña segura
function esContrasenaSegura(contrasena) {
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
  return regex.test(contrasena);
}

// Validar formulario
function validarFormulario() {
  limpiarValidaciones();
  let valido = true;

  const nombre = document.getElementById("nombre").value.trim();
  const documento = document.getElementById("documento").value.trim();
  const telefono = document.getElementById("telefono").value.trim();
  const correo = document.getElementById("correoElectronico").value.trim();
  const usuario = document.getElementById("usuario").value.trim();
  const contrasena = document.getElementById("contrasena").value;
  const rol = document.getElementById("rolInput").value;
  const estado = document.getElementById("estadoInput").value;

  if (!nombre) {
    document.getElementById("nombre").classList.add("is-invalid");
    valido = false;
  }

  if (!/^\d{10}$/.test(documento)) {
    document.getElementById("documento").classList.add("is-invalid");
    document.getElementById("documentoFeedback").textContent = "Documento no válido";
    valido = false;
  } else {
    const docDuplicado = usuarios.some(
      (u) => u.documento === documento && u.id !== usuarioEditandoId
    );
    if (docDuplicado) {
      document.getElementById("documento").classList.add("is-invalid");
      document.getElementById("documentoFeedback").textContent = "El documento ya existe";
      valido = false;
    }
  }

  if (!/^\d{10}$/.test(telefono)) {
    document.getElementById("telefono").classList.add("is-invalid");
    document.getElementById("telefonoFeedback").textContent = "Teléfono no válido";
    valido = false;
  }

  const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!regexCorreo.test(correo)) {
    document.getElementById("correoElectronico").classList.add("is-invalid");
    valido = false;
  }

  if (!usuario) {
    document.getElementById("usuario").classList.add("is-invalid");
    document.getElementById("usuarioFeedback").textContent = "Debe ingresar un nombre de usuario";
    valido = false;
  } else {
    const usuarioDuplicado = usuarios.some(
      (u) => u.usuario.toLowerCase() === usuario.toLowerCase() && u.id !== usuarioEditandoId
    );
    if (usuarioDuplicado) {
      document.getElementById("usuario").classList.add("is-invalid");
      document.getElementById("usuarioFeedback").textContent = "El nombre de usuario ya existe";
      valido = false;
    }
  }

  if (!usuarioEditandoId && !contrasena) {
    document.getElementById("contrasena").classList.add("is-invalid");
    document.getElementById("contrasenaFeedback").textContent = "Debe ingresar una contraseña";
    valido = false;
  } else if (contrasena) {
    const contrasenaDuplicada = usuarios.some(
      (u) => u.contrasena === contrasena && u.id !== usuarioEditandoId
    );
    if (contrasenaDuplicada) {
      document.getElementById("contrasena").classList.add("is-invalid");
      document.getElementById("contrasenaFeedback").textContent = "Esta contraseña ya fue usada por otro usuario";
      valido = false;
    } else if (!esContrasenaSegura(contrasena)) {
      document.getElementById("contrasena").classList.add("is-invalid");
      document.getElementById("contrasenaFeedback").textContent =
        "Debe contener mayúscula, minúscula, número y carácter especial (mín. 8)";
      valido = false;
    }
  }

  if (!rol) {
    document.getElementById("rolInput").classList.add("is-invalid");
    document.getElementById("rolFeedback").textContent = "Por favor, seleccione un rol";
    valido = false;
  }

  if (!estado) {
    document.getElementById("estadoInput").classList.add("is-invalid");
    valido = false;
  }

  return valido;
}

// Guardar usuario
function guardarUsuario() {
  if (!validarFormulario()) return;

  const confirmacion = confirm("¿Está seguro de guardar los datos del usuario?");
  if (!confirmacion) return;

  const nombre = document.getElementById("nombre").value.trim();
  const documento = document.getElementById("documento").value.trim();
  const telefono = document.getElementById("telefono").value.trim();
  const correo = document.getElementById("correoElectronico").value.trim();
  const usuario = document.getElementById("usuario").value.trim();
  const contrasena = document.getElementById("contrasena").value;
  const rol = document.getElementById("rolInput").value;
  const estado = document.getElementById("estadoInput").value;

  if (usuarioEditandoId !== null) {
    const indice = usuarios.findIndex((u) => u.id === usuarioEditandoId);
    if (indice !== -1) {
      usuarios[indice].nombre = nombre;
      usuarios[indice].documento = documento;
      usuarios[indice].telefono = telefono;
      usuarios[indice].correo = correo;
      usuarios[indice].usuario = usuario;
      usuarios[indice].rol = rol;
      usuarios[indice].estado = estado;
      if (contrasena) usuarios[indice].contrasena = contrasena;
    }
  } else {
    const nuevoUsuario = {
      id: Date.now(),
      nombre,
      documento,
      telefono,
      correo,
      usuario,
      contrasena,
      rol,
      estado
    };
    usuarios.push(nuevoUsuario);
  }

  modalUsuarios.hide();
  actualizarTablaUsuarios();
}

// Actualizar tabla
function actualizarTablaUsuarios(filtro = "") {
  const tbody = document.getElementById("tablaUsuarios");
  tbody.innerHTML = "";

  const usuariosFiltrados = usuarios.filter((u) =>
    u.nombre.toLowerCase().includes(filtro.toLowerCase()) ||
    u.documento.includes(filtro) ||
    u.usuario.toLowerCase().includes(filtro.toLowerCase())
  );

  usuariosFiltrados.forEach((usuario, index) => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${index + 1}</td>
      <td>${usuario.nombre}</td>
      <td>${usuario.documento}</td>
      <td>${usuario.telefono}</td>
      <td>${usuario.correo}</td>
      <td>${usuario.usuario}</td>
      <td>${usuario.rol}</td>
      <td>${usuario.estado}</td>
      <td>
        <button class="btn btn-sm btn-warning me-1" onclick="editarUsuario(${usuario.id})">
          <i class="bi bi-pencil-square"></i>
        </button>
        <button class="btn btn-sm btn-danger" onclick="eliminarUsuario(${usuario.id})">
          <i class="bi bi-trash"></i>
        </button>
      </td>
    `;
    tbody.appendChild(fila);
  });
}

// Editar usuario
function editarUsuario(id) {
  const usuario = usuarios.find((u) => u.id === id);
  if (!usuario) return;

  usuarioEditandoId = id;
  document.getElementById("item").value = usuarios.findIndex(u => u.id === id) + 1;
  document.getElementById("nombre").value = usuario.nombre;
  document.getElementById("documento").value = usuario.documento;
  document.getElementById("telefono").value = usuario.telefono;
  document.getElementById("correoElectronico").value = usuario.correo;
  document.getElementById("usuario").value = usuario.usuario;
  document.getElementById("contrasena").value = "";

  document.getElementById("rolInput").value = usuario.rol;
  document.getElementById("dropdownRol").textContent = usuario.rol;

  document.getElementById("estadoInput").value = usuario.estado;
  document.getElementById("dropdownEstado").textContent = usuario.estado;

  document.getElementById("usuarioId").value = usuario.id;
  limpiarValidaciones();
  modalUsuarios.show();
}

// Eliminar usuario
function eliminarUsuario(id) {
  if (!confirm("¿Está seguro de eliminar este usuario?")) return;
  usuarios = usuarios.filter((u) => u.id !== id);
  actualizarTablaUsuarios();
}

// Buscar usuario
function buscarUsuario(event) {
  if (event) event.preventDefault();
  const filtro = document.getElementById("busquedaUsuario").value.trim();
  actualizarTablaUsuarios(filtro);
}

// Función genérica para manejar dropdowns
function setupDropdown(dropdownButtonId, hiddenInputId) {
    const button = document.getElementById(dropdownButtonId);
    const hiddenInput = document.getElementById(hiddenInputId);
    const items = button.nextElementSibling.querySelectorAll(".dropdown-item");

    items.forEach(item => {
        item.addEventListener("click", function (e) {
            e.preventDefault();
            const value = this.getAttribute("data-value");
            button.textContent = value;        // Actualiza el botón
            hiddenInput.value = value;         // Guarda en el hidden input
        });
    });
}

// Inicialización
document.addEventListener("DOMContentLoaded", () => {
  actualizarTablaUsuarios();
  setupDropdown("dropdownRol", "rolInput");
  setupDropdown("dropdownEstado", "estadoInput");
});
// Función genérica para conectar un dropdown con un input hidden
function setupDropdown(dropdownButtonId, hiddenInputId) {
    const button = document.getElementById(dropdownButtonId);
    const hiddenInput = document.getElementById(hiddenInputId);
    const items = button.nextElementSibling.querySelectorAll(".dropdown-item");

    items.forEach(item => {
        item.addEventListener("click", function (e) {
            e.preventDefault();
            const value = this.getAttribute("data-value");
            // Actualiza el texto del botón
            button.textContent = value;
            // Guarda el valor en el hidden input
            hiddenInput.value = value;
        });
    });
}