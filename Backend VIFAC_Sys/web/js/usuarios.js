/* global usuarios, bootstrap, fetch */

let usuarios = []; // Arreglo para almacenar usuarios
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
  // El item se llenará cuando la tabla se actualice desde el servidor
  document.getElementById("item").value = "";
  document.getElementById("usuarioId").value = "";
  document.getElementById("dropdownRol").textContent = "Seleccionar rol";
  document.getElementById("rolInput").value = "";
  document.getElementById("dropdownCargo").textContent = "Seleccionar cargo";
  document.getElementById("cargoInput").value = "";
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
  const email = document.getElementById("email").value.trim();
  const nombreUsuarioInput = document.getElementById("nombreUsuario").value.trim();
  const contrasena = document.getElementById("contrasena").value;
  const rol = document.getElementById("rolInput").value;
  const cargo = document.getElementById("cargoInput").value;
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
      (u) => u.documento === documento && u.idUsuario !== usuarioEditandoId
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
  if (!regexCorreo.test(email)) {
    document.getElementById("email").classList.add("is-invalid");
    valido = false;
  }

  if (!nombreUsuarioInput) {
    document.getElementById("nombreUsuario").classList.add("is-invalid");
    document.getElementById("usuarioFeedback").textContent = "Debe ingresar un nombre de usuario";
    valido = false;
  } else {
    const usuarioDuplicado = usuarios.some(
      (u) => u.nombreUsuario && u.nombreUsuario.toLowerCase() === nombreUsuarioInput.toLowerCase() && u.idUsuario != usuarioEditandoId
    );
    if (usuarioDuplicado) {
      document.getElementById("nombreUsuario").classList.add("is-invalid");
      document.getElementById("usuarioFeedback").textContent = "El nombre de usuario ya existe";
      valido = false;
    }
  }

  if (!usuarioEditandoId && !contrasena) {
    document.getElementById("contrasena").classList.add("is-invalid");
    document.getElementById("contrasenaFeedback").textContent = "Debe ingresar una contraseña";
    valido = false;
  } else if (contrasena && !esContrasenaSegura(contrasena)) {
    document.getElementById("contrasena").classList.add("is-invalid");
    document.getElementById("contrasenaFeedback").textContent =
      "Debe contener mayúscula, minúscula, número y carácter especial (mín. 8)";
    valido = false;
  }

  if (!rol) {
    document.getElementById("rolInput").classList.add("is-invalid");
    document.getElementById("rolFeedback").textContent = "Por favor, seleccione un rol";
    valido = false;
  }

  if (!cargo) {
    document.getElementById("cargoInput").classList.add("is-invalid");
    document.getElementById("cargoFeedback").textContent = "Por favor, seleccione un cargo";
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

  const form = document.getElementById("formUsuario");
  const formData = new FormData(form);

  let accion = "agregar";
  if (usuarioEditandoId !== null) {
    accion = "editar";
    formData.append("usuarioId", usuarioEditandoId);
  }
  formData.append("accion", accion);

  fetch("UsuariosServlet", {
    method: "POST",
    body: new URLSearchParams(formData),
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error('La respuesta de la red no fue exitosa. Código: ' + response.status);
      }
      return response.json();
    })
    .then((data) => {
      if (data.status === "success") {
        alert(data.message);
        modalUsuarios.hide();
        cargarUsuariosDelServidor(); // Recargar la tabla
      } else {
        alert("Error: " + data.message);
      }
    })
    .catch((error) => {
      console.error("Error al guardar el usuario:", error);
      alert("Ocurrió un error al conectar con el servidor. Revise la consola para más detalles.");
    });
}

// Actualizar tabla (se mantiene igual, ya que es la que se encarga de pintar los datos del arreglo local 'usuarios')
function actualizarTablaUsuarios(filtro = "") {
  const tbody = document.getElementById("tablaUsuarios");
  tbody.innerHTML = "";

  const usuariosFiltrados = usuarios.filter((u) =>
    u.nombre.toLowerCase().includes(filtro.toLowerCase()) ||
    u.documento.includes(filtro) ||
    u.nombreUsuario.toLowerCase().includes(filtro.toLowerCase())
  );

  // Mapeo de IDs de rol a nombres para la visualización
  const rolesMap = {
    1: "Administrador",
    2: "Ventas",
    3: "Logística (Bodega)",
    4: "Contabilidad"
  };

  usuariosFiltrados.forEach((usuario, index) => {
    const rolNombre = rolesMap[usuario.idRol] || "Desconocido"; // Obtener el nombre del rol
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${index + 1}</td>
      <td>${usuario.nombre}</td>
      <td>${usuario.documento}</td>
      <td>${usuario.telefono}</td>
      <td>${usuario.email}</td>
      <td>${usuario.nombreUsuario}</td>
      <td>${rolNombre}</td>
      <td>${usuario.cargo}</td>
      <td>${usuario.estado}</td>
      <td>
        <button class="btn btn-sm btn-warning me-1" onclick="editarUsuario(${usuario.idUsuario})">
          <i class="bi bi-pencil-square"></i>
        </button>
        <button class="btn btn-sm btn-danger" onclick="eliminarUsuario(${usuario.idUsuario})">
          <i class="bi bi-trash"></i>
        </button>
      </td>
    `;
    tbody.appendChild(fila);
  });
}

// Editar usuario (se unificó con una sola función)
function editarUsuario(idUsuario) {
  const usuario = usuarios.find((u) => u.idUsuario == idUsuario);
  if (!usuario) {
    alert("Usuario no encontrado.");
    return;
  }

  usuarioEditandoId = usuario.idUsuario;
  document.getElementById("item").value = usuarios.findIndex(u => u.idUsuario == idUsuario) + 1;
  document.getElementById("nombre").value = usuario.nombre;
  document.getElementById("documento").value = usuario.documento;
  document.getElementById("telefono").value = usuario.telefono;
  document.getElementById("email").value = usuario.email;
  document.getElementById("nombreUsuario").value = usuario.nombreUsuario;
  document.getElementById("contrasena").value = ""; // No se carga la contraseña por seguridad

  // Mapeo de IDs de rol a nombres para la visualización
  const rolesMapInvertido = {
    1: "Administrador",
    2: "Ventas",
    3: "Logística (Bodega)",
    4: "Contabilidad"
  };

  const rolNombre = rolesMapInvertido[usuario.idRol] || "Seleccionar rol";
  document.getElementById("rolInput").value = usuario.idRol; // Se guarda el ID del rol en el input
  document.getElementById("dropdownRol").textContent = rolNombre;

  document.getElementById("cargoInput").value = usuario.cargo;
  document.getElementById("dropdownCargo").textContent = usuario.cargo;

  document.getElementById("estadoInput").value = usuario.estado;
  document.getElementById("dropdownEstado").textContent = usuario.estado;

  document.getElementById("usuarioId").value = usuario.idUsuario;
  limpiarValidaciones();
  modalUsuarios.show();
}

// Eliminar usuario
function eliminarUsuario(idUsuario) {
  if (!confirm("¿Está seguro de eliminar este usuario?")) return;

  const formData = new FormData();
  formData.append("accion", "eliminar");
  formData.append("id", idUsuario);

  fetch("UsuariosServlet", {
    method: "POST",
    body: new URLSearchParams(formData)
  })
    .then(response => response.json())
    .then(data => {
      if (data.status === "success") {
        alert(data.message);
        cargarUsuariosDelServidor(); // Recargar la tabla
      } else {
        alert("Error: " + data.message);
      }
    })
    .catch(error => {
      console.error("Error al eliminar el usuario:", error);
    });
}

// Buscar usuario
function buscarUsuario(event) {
  if (event) event.preventDefault();

  const filtroInput = document.getElementById("busquedaUsuario");
  const filtro = filtroInput.value.trim();

  fetch("UsuariosServlet?accion=buscar&busqueda=" + encodeURIComponent(filtro))
    .then(response => response.json())
    .then(data => {
      usuarios = data;
      actualizarTablaUsuarios();

      // Limpiar y enfocar la barra después de buscar
      filtroInput.value = "";
      filtroInput.focus();
    })
    .catch(error => {
      console.error("Error en la búsqueda:", error);
    });
}

// Función genérica para conectar dropdown con input hidden
function setupDropdown(dropdownButtonId, hiddenInputId) {
  const button = document.getElementById(dropdownButtonId);
  const hiddenInput = document.getElementById(hiddenInputId);
  const items = button.nextElementSibling.querySelectorAll(".dropdown-item");

  items.forEach(item => {
    item.addEventListener("click", function (e) {
      e.preventDefault();
      const value = this.getAttribute("data-value");
      button.textContent = this.textContent; // Usar el texto del item para el botón
      hiddenInput.value = value;
    });
  });
}

// Función para obtener y cargar los usuarios del servidor
function cargarUsuariosDelServidor() {
  return new Promise((resolve, reject) => {
    fetch("UsuariosServlet")
      .then((response) => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const clonedResponse = response.clone();
        return response.json();
      })
      .then((data) => {
        usuarios = data;
        actualizarTablaUsuarios();
        resolve();
      })
      .catch((error) => {
        error.response&&text().then(text => {
          console.error("Error al cargar usuarios:", error);
          console.error("Contenido de la respuesta de error:", text);
        }).catch(() => {
          console.error("Error al cargar usuarios:", error);
        });

        document.getElementById("tablaUsuarios").innerHTML = '<tr><td colspan="9" class="text-center text-danger">Error al cargar usuarios del servidor.</td></tr>';
        reject(error);
      });
  });
}

// Inicialización al cargar el DOM
document.addEventListener("DOMContentLoaded", () => {
  cargarUsuariosDelServidor()
    .then(() => {
      setupDropdown("dropdownRol", "rolInput");
      setupDropdown("dropdownCargo", "cargoInput");
      setupDropdown("dropdownEstado", "estadoInput");

      document.getElementById("formBusqueda").addEventListener("submit", buscarUsuario);
      document.getElementById("btnGuardarUsuario").addEventListener("click", guardarUsuario);
    })
    .catch(() => {
      console.log("No se pudo inicializar los dropdowns y el formulario de búsqueda debido a un error de carga de usuarios.");
    });
});

