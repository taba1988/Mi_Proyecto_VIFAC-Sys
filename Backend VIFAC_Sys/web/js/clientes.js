

/* La variable `myModal` para acceder al modal de Bootstrap debe ser global global bootstrap*/

/* global bootstrap */

/* global bootstrap */

let myModal = null;

// Espera a que el DOM esté completamente cargado antes de ejecutar el código
document.addEventListener("DOMContentLoaded", () => {
    // Inicializa el modal
    myModal = new bootstrap.Modal(document.getElementById("modalClientes"));
    
    // Carga la lista de clientes al iniciar la página
    listarClientes();

    // Evento para el botón de Guardar
    document.getElementById("btnguardarCliente").addEventListener("click", guardarCliente);

    // Evento para el formulario de búsqueda
    document.getElementById("formBusqueda").addEventListener("submit", buscarCliente);
    
    // --- CÓDIGO DE DROPDOWNS MOVIDO AQUÍ DENTRO ---
    
    // Manejo del dropdown para el estado
    const dropdownButton = document.getElementById("dropdownEstado");
    const hiddenInput = document.getElementById("estado");
    const dropdownItems = document.querySelectorAll("#dropdownEstado + .dropdown-menu .dropdown-item");

    dropdownItems.forEach(item => {
        item.addEventListener("click", function (e) {
            e.preventDefault();
            const value = this.getAttribute("data-value");
            dropdownButton.textContent = value;
            hiddenInput.value = value;
        });
    });

    // Manejo del dropdown para responsabilidad iva
    const dropdownButtonIva = document.getElementById("dropdownResponsabilidad_iva");
    const hiddenInputIva = document.getElementById("responsableIva");
    const dropdownItemsIva = document.querySelectorAll("#dropdownResponsabilidad_iva + .dropdown-menu .dropdown-item");

    dropdownItemsIva.forEach(item => {
        item.addEventListener("click", function (e) {
            e.preventDefault();
            const value = this.getAttribute("data-value");
            dropdownButtonIva.textContent = value;
            hiddenInputIva.value = value;
        });
    });
    // --- FIN DEL CÓDIGO DE DROPDOWNS ---
});

/**
 * Limpia la validación de los campos del formulario.
 */
function clearValidations() {
    const campos = [
        "razonSocial", "documentoNit", "telefono", "email",
        "direccion", "actividadEconomica", "responsableIva" // CAMBIADO: 'responsabilidadIva' a 'responsableIva'
    ];
    campos.forEach(id => {
        const elemento = document.getElementById(id);
        if (elemento) {
            elemento.classList.remove("is-invalid");
        }
    });
}


//Abre el modal de clientes y limpia los campos.
function abrirModalCliente() {
    document.getElementById("formCliente").reset();
    clearValidations();
    document.getElementById("clienteId").value = "";
    document.getElementById("modalClienteLabel").textContent = "Agregar Cliente";
    myModal.show();
}

// Realiza la validación del formato de correo electrónico.
function validarCorreo(input) {
    const valor = input.value.trim();
    const patronCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (valor === "" || !patronCorreo.test(valor)) {
        input.classList.add("is-invalid");
    } else {
        input.classList.remove("is-invalid");
    }
}

// Guarda un cliente nuevo o edita uno existente.
async function guardarCliente() {
    clearValidations();

    const razonSocial = document.getElementById("razonSocial").value.trim();
    const documentoNit = document.getElementById("documentoNit").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const email = document.getElementById("email").value.trim();
    const estado = document.getElementById("estado").value;
    const idCliente = document.getElementById("clienteId").value;
    const direccion = document.getElementById("direccion").value.trim();
    const actividadEconomica = document.getElementById("actividadEconomica").value.trim();
    // CAMBIADO: 'responsabilidadIva' a 'responsableIva' para obtener el valor del input correcto
    const responsabilidadIva = document.getElementById("responsableIva").value.trim(); 

    let valido = true;
    if (!razonSocial) { valido = false; document.getElementById("razonSocial").classList.add("is-invalid"); }
    if (!documentoNit) { valido = false; document.getElementById("documentoNit").classList.add("is-invalid"); }
    if (!telefono) { valido = false; document.getElementById("telefono").classList.add("is-invalid"); }
    if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { valido = false; document.getElementById("email").classList.add("is-invalid"); }
    if (!estado) { valido = false; document.getElementById("estado").classList.add("is-invalid"); }

    if (!valido) {
        alert("Por favor, complete todos los campos obligatorios correctamente.");
        return;
    }

    const formData = new URLSearchParams();
    formData.append("accion", idCliente ? "editar" : "agregar");
    if (idCliente) {
        formData.append("idCliente", idCliente);
    }
    formData.append("razon_social", razonSocial);
    formData.append("documento_NIT", documentoNit);
    formData.append("telefono", telefono);
    formData.append("direccion", direccion);
    formData.append("email", email);
    formData.append("actividad_economica", actividadEconomica);
    formData.append("responsabilidad_iva", responsabilidadIva);
    formData.append("estado", estado);

    try {
        const response = await fetch("ClientesServlet", {
            method: "POST",
            body: formData
        });

        const data = await response.json();
        if (data.status === "success") {
            alert(data.message);
            myModal.hide();
            listarClientes();
        } else {
            alert("Error: " + data.message);
        }
    } catch (error) {
        console.error("Error al guardar cliente:", error);
        alert("Ocurrió un error al intentar guardar el cliente.");
    }
}

/**
 * Lista todos los clientes obteniéndolos del servidor.
 */
async function listarClientes() {
    try {
        const response = await fetch("ClientesServlet?accion=listar");
        const clientes = await response.json();
        
        const tbody = document.getElementById("tablaClientes");
        tbody.innerHTML = "";

        if (clientes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center">No hay clientes registrados.</td></tr>';
            return;
        }

        clientes.forEach((cliente, index) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${cliente.razon_social}</td>
                <td>${cliente.documento_NIT}</td>
                <td>${cliente.telefono}</td>
                <td>${cliente.direccion}</td>
                <td>${cliente.email}</td>
                <td>${cliente.actividad_economica}</td>
                <td>${cliente.responsabilidad_iva}</td>
                <td>${cliente.estado}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary" onclick="editarCliente(${cliente.idClientes})">
                        <i class="bi bi-pencil-fill"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="eliminarCliente(${cliente.idClientes})">
                        <i class="bi bi-trash-fill"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });

    } catch (error) {
        console.error("Error al listar clientes:", error);
        document.getElementById("tablaClientes").innerHTML = '<tr><td colspan="10" class="text-center">Error al cargar los clientes.</td></tr>';
    }
}


// Rellena el modal con los datos de un cliente para su edición.
async function editarCliente(id) {
    try {
        const response = await fetch("ClientesServlet?accion=listar");
        const clientes = await response.json();
        const cliente = clientes.find(c => c.idClientes === id);

        if (cliente) {
            document.getElementById("modalClienteLabel").textContent = "Editar Cliente";
            document.getElementById("clienteId").value = cliente.idClientes;
            document.getElementById("razonSocial").value = cliente.razon_social;
            document.getElementById("documentoNit").value = cliente.documento_NIT;
            document.getElementById("telefono").value = cliente.telefono;
            document.getElementById("direccion").value = cliente.direccion;
            document.getElementById("email").value = cliente.email;
            document.getElementById("actividadEconomica").value = cliente.actividad_economica;
            // CAMBIADO: 'responsabilidadIva' a 'responsableIva' para rellenar el input correcto
            document.getElementById("responsableIva").value = cliente.responsabilidad_iva; 
            document.getElementById("estado").value = cliente.estado;
            document.getElementById("dropdownEstado").textContent = cliente.estado;
            
            clearValidations();
            myModal.show();
        } else {
            // AGREGADO: Mensaje de error para cuando el cliente no se encuentra
            alert("No se pudo encontrar el valor del cliente para editar.");
        }
    } catch (error) {
        console.error("Error al obtener datos para editar:", error);
        alert("No se pudo cargar la información del cliente para editar.");
    }
}

// Elimina un cliente de la base de datos.
async function eliminarCliente(id) {
    if (!confirm("¿Está seguro que desea eliminar este cliente? Esta acción no se puede deshacer.")) {
        return;
    }

    const formData = new URLSearchParams();
    formData.append("accion", "eliminar");
    formData.append("id", id);

    try {
        const response = await fetch("ClientesServlet", {
            method: "POST",
            body: formData
        });

        const data = await response.json();
        if (data.status === "success") {
            alert(data.message);
            listarClientes();
        } else {
            alert("Error: " + data.message);
        }
    } catch (error) {
        console.error("Error al eliminar cliente:", error);
        alert("Ocurrió un error al intentar eliminar el cliente.");
    }
}


// Realiza una búsqueda de clientes.
async function buscarCliente(event) {
    if (event) event.preventDefault();

    const filtro = document.getElementById("busquedaCliente").value.trim();
    if (!filtro) {
        listarClientes();
        return;
    }

    try {
        const response = await fetch(`ClientesServlet?accion=buscar&busqueda=${encodeURIComponent(filtro)}`);
        const clientes = await response.json();

        const tbody = document.getElementById("tablaClientes");
        tbody.innerHTML = "";

        if (clientes.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="text-center">No se encontraron clientes.</td></tr>';
            return;
        }

        clientes.forEach((cliente, index) => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${cliente.razon_social}</td>
                <td>${cliente.documento_NIT}</td>
                <td>${cliente.telefono}</td>
                <td>${cliente.direccion}</td>
                <td>${cliente.email}</td>
                <td>${cliente.actividad_economica}</td>
                <td>${cliente.responsabilidad_iva}</td>
                <td>${cliente.estado}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary" onclick="editarCliente(${cliente.idClientes})">
                        <i class="bi bi-pencil-fill"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="eliminarCliente(${cliente.idClientes})">
                        <i class="bi bi-trash-fill"></i>
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
        
    } catch (error) {
        console.error("Error al buscar cliente:", error);
    }
}