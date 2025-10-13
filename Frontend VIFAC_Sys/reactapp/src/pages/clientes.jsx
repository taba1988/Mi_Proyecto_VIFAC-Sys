/*
 * Clientes.jsx
 * Componente React que mantiene la visual del JSP original y consume el servlet ClientesServlet.
 * Comentarios a nivel de bloque solo donde es necesario.
 */

import React, { useState, useEffect } from "react";
import "./clientes.css";

function Clientes() {
  /*
   * Estado:
   * - clientes: lista obtenida desde el servlet
   * - busqueda: texto de la barra de búsqueda
   * - mostrarModal: controla visualización del modal
   * - formData: objeto con los campos del formulario (coinciden con parámetros esperados por el servlet)
   * - mostrarExito: controla visualización del modal de éxito
   * - mensajeExito: mensaje a mostrar en el modal de éxito
   */
  const [clientes, setClientes] = useState([]);
  const [busqueda, setBusqueda] = useState("");
  const [mostrarModal, setMostrarModal] = useState(false);
  const [formData, setFormData] = useState({
    idCliente: "",
    item: "",
    razon_social: "",
    documento_NIT: "",
    telefono: "",
    direccion: "",
    email: "",
    actividad_economica: "",
    responsabilidad_iva: "",
    estado: "",
  });
  const [mostrarExito, setMostrarExito] = useState(false);
  const [mensajeExito, setMensajeExito] = useState("");

  /* URL base del servlet (ajusta el host/puerto/context si hace falta) */
  const URL_SERVLET = "http://localhost:8084/Mi_Proyecto_VIFAC-Sys/ClientesServlet";

  /* Cargar lista de clientes desde el backend */
  const fetchClientes = async (query = "") => {
    try {
      const params = new URLSearchParams();
      params.append("accion", query ? "buscar" : "listar");
      if (query) params.append("busqueda", query);
      const res = await fetch(`${URL_SERVLET}?${params.toString()}`, {
        method: "GET",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        credentials: "include"
      });
      if (!res.ok) throw new Error("Error al obtener clientes");
      const data = await res.json();
      setClientes(data);
    } catch (err) {
      console.error("fetchClientes:", err);
    }
  };

  /* Efecto inicial: cargar clientes al montar */
  useEffect(() => {
    fetchClientes();
  }, []);

  /* Modal: abrir vacío para agregar */
  const abrirModalCliente = () => {
    setFormData({
      idCliente: "",
      item: "",
      razon_social: "",
      documento_NIT: "",
      telefono: "",
      direccion: "",
      email: "",
      actividad_economica: "",
      responsabilidad_iva: "",
      estado: "",
    });
    setMostrarModal(true);
  };

  /* Modal: abrir con datos para editar */
  const abrirModalEditar = (cliente) => {
    setFormData({
      idCliente: cliente.idClientes ?? cliente.idCliente ?? "",
      item: cliente.idClientes ?? cliente.idCliente ?? "",
      razon_social: cliente.razon_social ?? "",
      documento_NIT: cliente.documento_NIT ?? "",
      telefono: cliente.telefono ?? "",
      direccion: cliente.direccion ?? "",
      email: cliente.email ?? "",
      actividad_economica: cliente.actividad_economica ?? "",
      responsabilidad_iva: cliente.responsabilidad_iva ?? "",
      estado: cliente.estado ?? "",
    });
    setMostrarModal(true);
  };

  const cerrarModal = () => setMostrarModal(false);

  const cerrarExito = () => setMostrarExito(false);

  /* Manejo de cambios en inputs del formulario */
  const manejarCambio = (e) => {
    const { name, value } = e.target;

    // Validaciones específicas
    if (name === "documento_NIT" || name === "telefono") {
      // Solo números y máximo 10 dígitos
      const soloNumeros = value.replace(/[^0-9]/g, "").slice(0, 10);
      setFormData((prev) => ({ ...prev, [name]: soloNumeros }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  /* Validación de correo electrónico */
  const validarEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  /* Verificar si la cédula ya existe en la lista de clientes (excluye al cliente actual al editar) */
  const cedulaExiste = (cedula) => {
    return clientes.some((c) => {
      const id = c.idCliente ?? c.idClientes;
      return c.documento_NIT === cedula && id !== (formData.idCliente ?? formData.item);
    });
  };

  /* Guardar cliente: agregar o editar */
  const guardarCliente = async () => {
    if (!formData.documento_NIT || formData.documento_NIT.length < 5) {
      alert("Ingrese una cédula válida.");
      return;
    }
    if (!formData.telefono || formData.telefono.length < 7) {
      alert("Ingrese un teléfono válido.");
      return;
    }
    if (!validarEmail(formData.email)) {
      alert("Ingrese un correo válido.");
      return;
    }
    if (cedulaExiste(formData.documento_NIT)) {
      alert("La cédula ya existe. Corrija antes de guardar.");
      return;
    }

    try {
      const accion = formData.idCliente ? "editar" : "agregar";
      const params = new URLSearchParams();
      params.append("accion", accion);
      if (formData.idCliente) params.append("idCliente", formData.idCliente);
      params.append("razon_social", formData.razon_social ?? "");
      params.append("documento_NIT", formData.documento_NIT ?? "");
      params.append("telefono", formData.telefono ?? "");
      params.append("direccion", formData.direccion ?? "");
      params.append("email", formData.email ?? "");
      params.append("actividad_economica", formData.actividad_economica ?? "");
      params.append("responsabilidad_iva", formData.responsabilidad_iva ?? "");
      params.append("estado", formData.estado ?? "");

      const res = await fetch(URL_SERVLET, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString(),
        credentials: "include"
      });

      const result = await res.json();
      if (result.status === "success") {
        await fetchClientes();
        cerrarModal();
        setMensajeExito(formData.idCliente ? "Cliente editado con éxito" : "Cliente agregado con éxito");
        setMostrarExito(true);
      } else {
        alert(result.mensaje || "Error guardando cliente");
      }
    } catch (err) {
      console.error("guardarCliente:", err);
      alert("Error guardando cliente. Revisa consola.");
    }
  };

  /* Eliminar cliente */
  const eliminarCliente = async (id) => {
    if (!window.confirm("¿Desea eliminar este cliente?")) return;
    try {
      const params = new URLSearchParams();
      params.append("accion", "eliminar");
      params.append("id", id);
      const res = await fetch(URL_SERVLET, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString(),
        credentials: "include"
      });
      const result = await res.json();
      if (result.status === "success") {
        await fetchClientes();
        setMensajeExito("Cliente eliminado con éxito");
        setMostrarExito(true);
      } else alert(result.mensaje || "Error eliminando cliente");
    } catch (err) {
      console.error("eliminarCliente:", err);
      alert("Error eliminando cliente. Revisa consola.");
    }
  };

  /* Buscar clientes */
  const handleBuscar = async (e) => {
    if (e) e.preventDefault();
    await fetchClientes(busqueda);
  };

  /* Render */
  return (
    <div className="container mt-4">
      {/* Header con barra de búsqueda */}
      <header className="bg-light py-3">
        <div className="row align-items-center mb-2">
          <div className="col-12 d-flex align-items-center justify-content-start flex-wrap gap-2">
            <i className="bi bi-person-lines-fill"></i>
            <h4 className="mb-0">Clientes</h4>
          </div>
        </div>

        <div className="row align-items-center">
          <div className="col-12 col-md-6 mb-2 mb-md-0">
            <div className="nombre-empresa">
              <div className="titulo">MAXI-LIMPIEZA</div>
              <div className="subtitulo">Calidad que se siente</div>
            </div>
          </div>

          <div className="col-12 col-md-6 d-flex justify-content-start justify-content-md-end">
            <form className="input-group w-100 w-md-auto" onSubmit={handleBuscar}>
              <input
                type="text"
                id="busquedaCliente"
                name="busqueda"
                className="form-control"
                placeholder="Buscar por nombre o documento"
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
              />
              <input type="hidden" name="accion" value="buscar" />
              <button type="submit" className="btn btn-outline-secondary">Buscar</button>
            </form>
          </div>
        </div>
      </header>

      <hr />

      {/* Main: lista y botón agregar */}
      <main className="container my-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h4>Lista Clientes</h4>
          <button className="btn btn-outline-primary" onClick={abrirModalCliente}>+ Agregar Cliente</button>
        </div>

        <div className="table-responsive justify-content">
          <table className="table table-bordered table-striped">
            <thead className="table-info">
              <tr>
                <th>ITEM</th>
                <th>RAZÓN SOCIAL</th>
                <th>DOCUMENTO/NIT</th>
                <th>TELÉFONO</th>
                <th>DIRECCIÓN</th>
                <th>CORREO ELECTRÓNICO</th>
                <th>ACTIVIDAD ECONÓMICA</th>
                <th>RESPONSABILIDAD IVA</th>
                <th>ESTADO</th>
                <th>ACCIONES</th>
              </tr>
            </thead>
            <tbody id="tablaClientes" className="table-group-divider">
              {clientes.length === 0 ? (
                <tr>
                  <td colSpan="10" className="text-center">No hay clientes</td>
                </tr>
              ) : (
                clientes.map((c, i) => (
                  <tr key={i}>
                    <td>{i + 1}</td>
                    <td>{c.razon_social}</td>
                    <td>{c.documento_NIT}</td>
                    <td>{c.telefono}</td>
                    <td>{c.direccion}</td>
                    <td>{c.email}</td>
                    <td>{c.actividad_economica}</td>
                    <td>{c.responsabilidad_iva}</td>
                    <td>{c.estado}</td>
                    <td>
                      <button 
                        className="btn btn-sm btn-outline-primary me-1" 
                        onClick={() => abrirModalEditar(c)}
                      >
                        <i className="bi bi-pencil"></i>
                      </button>
                      <button 
                        className="btn btn-sm btn-outline-danger" 
                        onClick={() => eliminarCliente(c.idClientes ?? c.idCliente)}
                      >
                        <i className="bi bi-trash"></i>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </main>

      {/* Botones inferiores */}
      <div className="container mb-1 d-flex justify-content-start gap-1">
        <button className="btn btn-outline-secondary" onClick={() => (window.location.href = "/index.jsp")}>Inicio</button>
        <button className="btn btn-outline-success" onClick={() => (window.location.href = "/Vender.jsp")}>Continuar Venta</button>
        <button className="btn btn-outline-danger ms-auto" onClick={() => (window.location.href = "/logoutServlet")}>Cerrar sesión</button>
      </div>

      {/* Footer */}
      <footer>
        <div className="container d-flex justify-content-between flex-wrap">
          <small>© 2025</small>
          <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
        </div>
      </footer>

      {/* Modal de Cliente */}
      {mostrarModal && (
        <div className="modal fade show d-block" tabIndex="-1" role="dialog" aria-labelledby="modalClienteLabel" aria-modal="true">
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header bg-primary text-white">
                <h5 className="modal-title" id="modalClienteLabel">Agregar / Editar Cliente</h5>
                <button type="button" className="btn-close btn-close-white" onClick={cerrarModal} aria-label="Cerrar"></button>
              </div>

              <div className="modal-body">
                <form>
                  <div className="row mb-3">
                    <div className="col-12 col-md-3">
                      <label htmlFor="item" className="form-label">Ítem</label>
                      <input type="text" id="item" name="item" className="form-control" value={formData.item} readOnly placeholder="Ingrese ítem" />
                    </div>
                    <div className="col-12 col-md-9">
                      <label htmlFor="razonSocial" className="form-label">Razón Social</label>
                      <input type="text" id="razonSocial" name="razon_social" className="form-control" value={formData.razon_social} onChange={manejarCambio} placeholder="Ingrese razón social" />
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col-12 col-md-6">
                      <label htmlFor="documentoNit" className="form-label">Documento/NIT</label>
                      <input type="text" id="documentoNit" name="documento_NIT" className="form-control" value={formData.documento_NIT} onChange={manejarCambio} placeholder="Ingrese cédula/NIT" required maxLength="10" />
                    </div>
                    <div className="col-12 col-md-6">
                      <label htmlFor="telefono" className="form-label">Teléfono</label>
                      <input type="text" id="telefono" name="telefono" className="form-control" value={formData.telefono} onChange={manejarCambio} placeholder="Ingrese teléfono" required maxLength="10" />
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col-12 col-md-6">
                      <label htmlFor="direccion" className="form-label">Dirección</label>
                      <input type="text" id="direccion" name="direccion" className="form-control" value={formData.direccion} onChange={manejarCambio} placeholder="Ingrese dirección" />
                    </div>
                    <div className="col-12 col-md-6">
                      <label htmlFor="email" className="form-label">Correo Electrónico</label>
                      <input type="email" id="email" name="email" className="form-control" value={formData.email} onChange={manejarCambio} placeholder="Ingrese correo electrónico" />
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col-12 col-md-6">
                      <label htmlFor="actividadEconomica" className="form-label">Actividad Económica</label>
                      <input type="text" id="actividadEconomica" name="actividad_economica" className="form-control" value={formData.actividad_economica} onChange={manejarCambio} placeholder="Ingrese actividad económica" />
                    </div>
                    <div className="col-12 col-md-6">
                      <label className="form-label">Responsabilidad IVA</label>
                      <div className="dropdown w-100">
                        <button className="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                          {formData.responsabilidad_iva || "Seleccionar"}
                        </button>
                        <ul className="dropdown-menu w-100">
                          <li><button type="button" className="dropdown-item" onClick={() => setFormData((p) => ({ ...p, responsabilidad_iva: "si" }))}>si</button></li>
                          <li><button type="button" className="dropdown-item" onClick={() => setFormData((p) => ({ ...p, responsabilidad_iva: "no" }))}>no</button></li>
                        </ul>
                      </div>
                    </div>
                  </div>

                  <div className="row mb-3">
                    <div className="col-12 col-md-6">
                      <label className="form-label">Estado</label>
                      <div className="dropdown w-100">
                        <button className="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                          {formData.estado || "Seleccionar"}
                        </button>
                        <ul className="dropdown-menu w-100">
                          <li><button type="button" className="dropdown-item" onClick={() => setFormData((p) => ({ ...p, estado: "Activo" }))}>Activo</button></li>
                          <li><button type="button" className="dropdown-item" onClick={() => setFormData((p) => ({ ...p, estado: "Inactivo" }))}>Inactivo</button></li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </form>
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-outline-secondary" onClick={cerrarModal}>Cancelar</button>
                <button type="button" className="btn btn-outline-primary" onClick={guardarCliente}>Guardar</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Modal de éxito */}
      {mostrarExito && (
        <div className="modal fade show d-block" tabIndex="-1" role="dialog" aria-modal="true">
          <div className="modal-dialog modal-sm">
            <div className="modal-content">
              <div className="modal-header bg-primary text-white">
                <h5 className="modal-title">Éxito</h5>
                <button type="button" className="btn-close btn-close-white" onClick={cerrarExito}></button>
              </div>
              <div className="modal-body">
                <p>{mensajeExito}</p>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-success" onClick={cerrarExito}>Aceptar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Clientes;
