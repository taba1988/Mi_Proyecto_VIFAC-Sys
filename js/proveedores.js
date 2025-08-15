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
    document.getElementById("ProveedoresId").value = "";
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

    const nombre = document.getElementById("nombre").value.trim();
    const documento = document.getElementById("documento").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const correoElectronico = document.getElementById("correoElectronico").value.trim();
    const empresa = document.getElementById("empresa").value.trim();
    const fechaVisita = document.getElementById("fechaVisita").value;
    const diaVisita = document.getElementById("diaVisita").value;
    const estado = document.getElementById("estado").value;

    if (!nombre) {
        document.getElementById("nombre").classList.add("is-invalid");
        valido = false;
    }

    if (!/^\d{1,10}$/.test(documento)) {
        document.getElementById("documento").classList.add("is-invalid");
        document.getElementById("documentoFeedback").textContent =
            "Documento/NIT no válido (máximo 10 dígitos)";
        valido = false;
    } else {
        const docDuplicado = proveedores.some(
            (p) => p.documento === documento && p.id !== proveedorEditandoId
        );
        if (docDuplicado) {
            document.getElementById("documento").classList.add("is-invalid");
            document.getElementById("documentoFeedback").textContent =
                "El Documento/NIT ya existe";
            valido = false;
        }
    }

    if (!/^\d{10}$/.test(telefono)) {
        document.getElementById("telefono").classList.add("is-invalid");
        document.getElementById("telefonoFeedback").textContent =
            "Teléfono no válido (10 dígitos)";
        valido = false;
    }

    const regexCorreo = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!regexCorreo.test(correoElectronico)) {
        document.getElementById("correoElectronico").classList.add("is-invalid");
        valido = false;
    }

    if (!empresa) {
        document.getElementById("empresa").classList.add("is-invalid");
        valido = false;
    }

    if (!fechaVisita) {
        document.getElementById("fechaVisita").classList.add("is-invalid");
        document.getElementById("fechaVisitaFeedback").textContent = "Por favor, ingrese la fecha de visita.";
        valido = false;
    }

    if (!diaVisita) {
        document.getElementById("diaVisita").classList.add("is-invalid");
        document.getElementById("diaVisitaFeedback").textContent = "Por favor, seleccione el día de visita.";
        valido = false;
    }

    if (!estado) {
        document.getElementById("estado").classList.add("is-invalid");
        valido = false;
    }

    return valido;
}

// Función para calcular la próxima fecha de visita
function calcularProximaVisita(diaSemana) {
    const diasSemana = ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"];
    const hoy = new Date();
    const hoyDiaSemana = hoy.getDay(); // 0 (Domingo) - 6 (Sábado)
    const diaObjetivoIndice = diasSemana.indexOf(diaSemana);

    let diferencia = diaObjetivoIndice - hoyDiaSemana;
    if (diferencia <= 0) {
        diferencia += 7; // Si el día objetivo ya pasó o es hoy, la próxima es la siguiente semana
    }

    const proximaVisita = new Date(hoy);
    proximaVisita.setDate(hoy.getDate() + diferencia);

    const anio = proximaVisita.getFullYear();
    const mes = (proximaVisita.getMonth() + 1).toString().padStart(2, '0');
    const dia = proximaVisita.getDate().toString().padStart(2, '0');

    return `${anio}-${mes}-${dia}`; // Formato YYYY-MM-DD
}

// Guardar proveedor
function guardarProveedor() {
    if (!validarFormularioProveedores()) return;

    const confirmacion = confirm("¿Está seguro de guardar los datos del proveedor?");
    if (!confirmacion) return;

    const nombre = document.getElementById("nombre").value.trim();
    const documento = document.getElementById("documento").value.trim();
    const telefono = document.getElementById("telefono").value.trim();
    const correoElectronico = document.getElementById("correoElectronico").value.trim();
    const empresa = document.getElementById("empresa").value.trim();
    const fechaVisita = document.getElementById("fechaVisita").value;
    const diaVisita = document.getElementById("diaVisita").value;
    const estado = document.getElementById("estado").value;

    const proximaVisitaInicial = calcularProximaVisita(diaVisita);

    if (proveedorEditandoId !== null) {
        const indice = proveedores.findIndex((p) => p.id === proveedorEditandoId);
        if (indice !== -1) {
            proveedores[indice].nombre = nombre;
            proveedores[indice].documento = documento;
            proveedores[indice].telefono = telefono;
            proveedores[indice].correoElectronico = correoElectronico;
            proveedores[indice].empresa = empresa;
            proveedores[indice].fechaVisita = fechaVisita;
            proveedores[indice].diaVisita = diaVisita;
            proveedores[indice].estado = estado;
            proveedores[indice].proximaVisita = proximaVisitaInicial; // Recalcular si se edita el día
        }
    } else {
        const nuevoProveedor = {
            id: Date.now(),
            nombre,
            documento,
            telefono,
            correoElectronico,
            empresa,
            fechaVisita,
            diaVisita,
            estado,
            proximaVisita: proximaVisitaInicial,
            ultimaVisitaCumplida: null // Inicialmente ninguna visita cumplida
        };
        proveedores.push(nuevoProveedor);
    }

    modalProveedores.hide();
    actualizarTablaProveedores();
}

// Función auxiliar para obtener el nombre del día de la semana
function obtenerNombreDia(numeroDia) {
    const dias = ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
    return dias[numeroDia];
}

// Actualizar tabla de proveedores
function actualizarTablaProveedores(filtro = "") {
    const tbody = document.getElementById("tablaProveedores");
    tbody.innerHTML = "";

    const proveedoresFiltrados = proveedores.filter((p) =>
        p.nombre.toLowerCase().includes(filtro.toLowerCase()) ||
        p.documento.includes(filtro) ||
        p.empresa.toLowerCase().includes(filtro.toLowerCase())
    );

    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    proveedoresFiltrados.forEach((proveedor, index) => {
        const fila = document.createElement("tr");

        const proximaVisitaDate = new Date(proveedor.proximaVisita);
        const diaSemanaNumero = proximaVisitaDate.getDay();
        const nombreDia = obtenerNombreDia(diaSemanaNumero);
        const dia = proximaVisitaDate.getDate().toString().padStart(2, '0');
        const mes = (proximaVisitaDate.getMonth() + 1).toString().padStart(2, '0');
        const anio = proximaVisitaDate.getFullYear();
        const fechaProximaVisitaFormateada = `${dia}/${mes}/${anio}`;

        let alerta = "";
        if (proximaVisitaDate <= hoy) {
            alerta = '<span class="text-danger fw-bold">¡Próxima visita!</span><br>';
        }

        fila.innerHTML = `
            <td>${index + 1}</td>
            <td>${proveedor.nombre}</td>
            <td>${proveedor.documento}</td>
            <td>${proveedor.telefono}</td>
            <td>${proveedor.correoElectronico}</td>
            <td>${proveedor.empresa}</td>
            <td>${alerta}${fechaProximaVisitaFormateada} (${nombreDia})</td>
            <td>${proveedor.estado}</td>
            <td>
                <button class="btn btn-sm btn-success me-1" title="Cumplida" onclick="marcarCumplida(${proveedor.id})">
                    <i class="bi bi-check-circle-fill"></i>
                </button>
                <button class="btn btn-sm btn-warning me-1" title="Incumplida" onclick="marcarIncumplida(${proveedor.id})">
                    <i class="bi bi-x-circle-fill"></i>
                </button>
                <button class="btn btn-sm btn-warning me-1" title="Editar" onclick="editarProveedor(${proveedor.id})">
                    <i class="bi bi-pencil-square"></i>
                </button>
                <button class="btn btn-sm btn-danger" title="Eliminar" onclick="eliminarProveedor(${proveedor.id})">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(fila);
    });
}

// Editar proveedor
function editarProveedor(id) {
    const proveedor = proveedores.find((p) => p.id === id);
    if (!proveedor) return;

    proveedorEditandoId = id;
    document.getElementById("item").value = proveedores.findIndex(p => p.id === id) + 1;
    document.getElementById("nombre").value = proveedor.nombre;
    document.getElementById("documento").value = proveedor.documento;
    document.getElementById("telefono").value = proveedor.telefono;
    document.getElementById("correoElectronico").value = proveedor.correoElectronico;
    document.getElementById("empresa").value = proveedor.empresa;
    document.getElementById("fechaVisita").value = proveedor.fechaVisita;
    document.getElementById("diaVisita").value = proveedor.diaVisita;
    document.getElementById("estado").value = proveedor.estado;
    document.getElementById("ProveedoresId").value = proveedor.id;
    limpiarValidacionesProveedores();
    modalProveedores.show();
}

// Marcar visita como cumplida
function marcarCumplida(id) {
    const proveedor = proveedores.find(p => p.id === id);
    if (proveedor) {
        proveedor.ultimaVisitaCumplida = new Date().toISOString().split('T')[0]; // Registrar fecha
        proveedor.proximaVisita = calcularProximaVisita(proveedor.diaVisita); // Calcular siguiente
        actualizarTablaProveedores();
        alert(`Visita de ${proveedor.nombre} marcada como cumplida. Próxima visita: ${proveedor.proximaVisita}`);
        // Aquí iría la lógica para guardar en el backend si lo tuvieras
    }
}

// Marcar visita como incumplida
function marcarIncumplida(id) {
    const proveedor = proveedores.find(p => p.id === id);
    if (proveedor) {
        alert(`Visita de ${proveedor.nombre} marcada como incumplida.`);
        // Aquí podrías añadir lógica para reprogramar o registrar el motivo
        // También la lógica para guardar en el backend
    }
}

// Eliminar proveedor
function eliminarProveedor(id) {
    if (!confirm("¿Está seguro de eliminar este proveedor?")) return;
    proveedores = proveedores.filter((p) => p.id !== id);
    actualizarTablaProveedores();
}

// Buscar proveedores
function buscarProveedores(event) {
    if (event) event.preventDefault();
    const filtro = document.getElementById("busquedaProveedores").value.trim();
    actualizarTablaProveedores(filtro);
}

// Inicial
document.addEventListener("DOMContentLoaded", () => {
    actualizarTablaProveedores();
});