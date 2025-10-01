<%-- 
    Document   : Clientes
    Created on : 11/09/2025, 06:51:46 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-compatible" content="IE-edge">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Clientes</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/clientes.css" />
</head>
<body>
    <header class="bg-light py-3">
        <div class="container">
            <div class="row align-items-center mb-2">
                <div class="col-12 d-flex align-items-center justify-content-start flex-wrap gap-2">
                    <i class="bi bi-person-lines-fill"></i>
                    <h4 class="mb-0">Clientes</h4>
                </div>
            </div>

            <div class="row align-items-center">
                <div class="col-12 col-md-6 mb-2 mb-md-0">
                    <div class="nombre-empresa">
                        <div class="titulo">MAXI-LIMPIEZA</div>
                        <div class="subtitulo">Calidad que se siente</div>
                    </div>
                </div>

                <div class="col-12 col-md-6 d-flex justify-content-start justify-content-md-end">
                    <form id="formBusqueda" class="input-group w-100 w-md-auto">
                        <input 
                            type="text" 
                            id="busquedaCliente" 
                            name="busqueda" 
                            class="form-control" 
                            placeholder="Buscar por nombre o documento"
                        />
                        <input type="hidden" name="accion" value="buscar"/>
                        <button type="submit" class="btn btn-outline-secondary">Buscar</button>
                    </form>
                </div> 
            </div>
        </div>
    </header>
    <hr />

    <main class="container my-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4>Lista Clientes</h4>
            <button class="btn btn-outline-primary" onclick="abrirModalCliente()">
                + Agregar Cliente
            </button>
        </div>

        <div class="table-responsive justify-content">
            <table class="table table-bordered table-striped">
                <thead class="table-info">
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
                <tbody id="tablaClientes" class="table-group-divider"></tbody>
            </table>
        </div>
    </main>

    <div class="container mb-1 d-flex justify-content-start gap-1">
        <button class="btn btn-outline-secondary" onclick="location.href='index.jsp'">Inicio</button>
        <button class="btn btn-outline-success justify-content-start" onclick="location.href='Vender.jsp'">Continuar Venta</button>
        <button class="btn btn-outline-danger ms-auto" onclick="location.href='logoutServlet'">Cerrar sesión</button>
    </div>

    <footer>
        <div class="container d-flex justify-content-between flex-wrap">
            <small>© 2025</small>
            <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
        </div>
    </footer>

    <div 
        class="modal fade" 
        id="modalClientes" 
        data-bs-backdrop="static"
        data-bs-keyboard="false"
        tabindex="-1" 
        aria-labelledby="modalClienteLabel" 
        aria-hidden="true">
        
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalClienteLabel">
                        Agregar / Editar Cliente</h5>
                    <button 
                        type="button" 
                        class="btn-close btn-close-white" 
                        data-bs-dismiss="modal" 
                        aria-label="Cerrar">   
                    </button>
                </div>

                <div class="modal-body">
                    <form id="formCliente" method="post" action="ClientesServlet" novalidate>
                        <div class="row mb-3">
                            <div class="col-12 col-md-3">
                                <label for="item" class="form-label">Ítem</label>
                                <input
                                    type="text"
                                    id="item"
                                    name="item"
                                    class="form-control"
                                    readonly
                                    placeholder="Ítem"
                                />
                            </div>
                            <div class="col-12 col-md-9">
                                <label for="razonSocial" class="form-label">Razón Social</label>
                                <input
                                    type="text"
                                    id="razonSocial"
                                    name="razon_social"
                                    class="form-control"
                                    required
                                    placeholder="Ingrese Razón Social"
                                />
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-12 col-md-6">
                                <label for="documentoNit" class="form-label">Documento/NIT</label>
                                <input
                                    type="text"
                                    id="documentoNit"
                                    name="documento_NIT"
                                    class="form-control"
                                    oninput="this.value=this.value.replace(/[^0-9]/g,'')"
                                    required
                                    placeholder="Ingrese Documento o NIT"
                                    maxlength="10"
                                />
                                <div class="invalid-feedback" id="documentoNitFeedback"></div>
                            </div>
                            <div class="col-12 col-md-6">
                                <label for="telefono" class="form-label">Teléfono</label>
                                <input
                                    type="text"
                                    id="telefono"
                                    name="telefono"
                                    class="form-control"
                                    oninput="this.value=this.value.replace(/[^0-9]/g,'')"
                                    required
                                    placeholder="Ingrese teléfono"
                                    maxlength="10"
                                />
                                <div class="invalid-feedback" id="telefonoFeedback"></div>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-12 col-md-6">
                                <label for="direccion" class="form-label">Dirección</label>
                                <input
                                    type="text"
                                    id="direccion"
                                    name="direccion"
                                    class="form-control"
                                    required
                                    placeholder="Ingrese dirección"
                                />
                            </div>
                            <div class="col-12 col-md-6">
                                <label for="email" class="form-label">Correo Electrónico</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    class="form-control"
                                    required
                                    placeholder="ejemplo@correo.com"
                                    oninput="validarCorreo(this)"
                                />
                                <div class="invalid-feedback">
                                    Por favor ingrese un correo válido (ejemplo: usuario@dominio.com)
                                </div>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-12 col-md-6">
                                <label for="actividadEconomica" class="form-label">Actividad Económica</label>
                                <input
                                    type="text"
                                    id="actividadEconomica"
                                    name="actividad_economica"
                                    class="form-control"
                                    required
                                    placeholder="Actividad Económica"
                                />
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-12 col-md-6">
                                <label for="responsabilidad_iva" class="form-label">Responsabilidad IVA</label>
                                <div class="dropdown w-100">
                                    <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" id="dropdownResponsabilidad_iva" data-bs-toggle="dropdown" aria-expanded="false">
                                        Seleccionar
                                    </button>
                                    <ul class="dropdown-menu w-100" aria-labelledby="dropdownResponsabilidad_iva">
                                        <li><a class="dropdown-item" role="button" data-value="si">si</a></li>
                                        <li><a class="dropdown-item" role="button" data-value="no">no</a></li>
                                    </ul>
                                </div>
                                <input type="hidden" id="responsableIva" name="responsabilidad_iva">
                            </div>

                            <div class="col-12 col-md-6">
                                <label for="estado" class="form-label">Estado</label>
                                <div class="dropdown w-100">
                                    <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" id="dropdownEstado" data-bs-toggle="dropdown" aria-expanded="false">
                                        Seleccionar
                                    </button>
                                    <ul class="dropdown-menu w-100" aria-labelledby="dropdownEstado">
                                        <li><a class="dropdown-item" role="button" data-value="Activo">Activo</a></li>
                                        <li><a class="dropdown-item" role="button" data-value="Inactivo">Inactivo</a></li>
                                    </ul>
                                </div>
                                <input type="hidden" id="estado" name="estado">
                            </div>
                        </div>

                        <input type="hidden" name="accion" id="accionInput" value="agregar" />
                        <input type="hidden" id="clienteId" name="idCliente" />

                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-outline-primary" id="btnguardarCliente">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
    <script src="js/clientes.js"></script>
</body>
</html>
