<%-- 
    Document   : proveedores
    Created on : 11/09/2025, 06:51:46 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Proveedor</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/proveedores.css">
</head>
<body>
    <!-- HEADER -->
    <header class="bg-light py-3">
        <div class="container">
            <!-- Ícono y título principal -->
            <div class="row align-items-center mb-2">
                <div class="col-12 d-flex align-items-center justify-content-start flex-wrap gap-2">
                    <img src="img/proveedor.png" alt="Proveedor" class="header-logo">
                    <h4 class="mb-0">Proveedores</h4>
                </div>
            </div>
        
            <!-- Nombre empresa + Barra de búsqueda -->
            <div class="row align-items-center">
                <!-- Nombre empresa -->
                <div class="col-12 col-md-6 mb-2 mb-md-0">
                    <div class="nombre-empresa p-2 text-center rounded-3">
                        <div class="fw-bold nombre-empresa-title">MAXI-LIMPIEZA</div>
                        <div class="fst-italic nombre-empresa-slogan">Calidad que se siente</div>
                    </div>
                </div>
        
               <!-- Barra de búsqueda -->
                <div class="col-12 col-md-6 d-flex justify-content-start justify-content-md-end">
                    <form id="formBusqueda" class="input-group w-100 w-md-auto">
                        <input 
                            type="text" 
                            id="busquedaProveedores" 
                            name="busqueda" 
                            class="form-control" 
                            placeholder="Buscar por nombre o documento"
                        />
                        <input type="hidden" name="accion" value="buscar"/>
                        <button type="submit" class="btn btn-outline-secondary">
                            <i class="bi bi-search"></i>
                        </button>
                    </form>
                </div>                
            </div>
        </div>
    </header>
    
    <hr class="mt-0"/>
    
       <!-- MAIN -->
    <main class="container my-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h4>Lista de Proveedores</h4>
            <button class="btn btn-outline-primary" onclick="abrirModalProveedores()">
            + Agregar Proveedores</button>
        </div>

        <div class="table-responsive justify-content">
            <table class="table table-bordered table-striped">
                <thead class="table-info">
                    <tr>
                        <th>ITEM</th>
                        <th>NOMBRE EMPRESA</th>
                        <th>DOCUMENTO/NIT</th>
                        <th>ASESOR</th>
                        <th>TELÉFONO</th>
                        <th>CORREO ELECTRONICO</th>
                        <th>DIA VISITA</th>
                        <th>ESTADO</th>                       
                        <th>ACCIONES</th>
                    </tr>
                </thead>
                <!-- Aquí se cargan dinámicamente los proveedores -->
                <tbody id="tablaProveedores" class="table-group-divider"></tbody>
            </table>
        </div>
    </main>
       
      <!-- Botones de navegación -->
    <div class="container mb-1 d-flex justify-content-between gap-1">
        <button class="btn btn-outline-secondary" onclick="location.href='index.jsp'">Inicio</button>
        <button class="btn btn-outline-danger" onclick="location.href='logoutServlet'">Cerrar sesión</button>
    </div>
      <!-- FOOTER -->      
    <footer>
        <div class="container d-flex justify-content-between flex-wrap">
            <small>© 2025</small>
            <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
        </div>
    </footer>

      <!-- MODAL Proveedores -->
    <div
    class="modal fade"
    id="modalProveedores"
    data-bs-backdrop="static"
    data-bs-keyboard="false"
    tabindex="-1"
    aria-labelledby="modalProveedoresLabel"
    aria-hidden="true"
>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="modalProveedoresLabel">
                    Agregar / Editar Proveedor
                </h5>
                <button
                    type="button"
                    class="btn-close btn-close-white"
                    data-bs-dismiss="modal"
                    aria-label="Cerrar">
                </button>
            </div>
            
            <!-- Cuerpo del modal -->
            <div class="modal-body">
                <form id="formProveedores" method="post" action="ProveedoresServlet" novalidate>

                    <!-- Fila 1: Ítem, Empresa, Documento -->
                    <div class="row mb-3">
                        <div class="col-12 col-md-2">
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
                        <div class="col-12 col-md-5">
                            <label for="nombreEmpresa" class="form-label">Nombre Empresa</label>
                            <input
                                type="text"
                                id="nombreEmpresa"
                                name="nombreEmpresa"
                                class="form-control"
                                required
                                placeholder="Ingrese Nombre Empresa / Razón Social"
                            />
                        </div>
                        <div class="col-12 col-md-5">
                            <label for="documento_NIT" class="form-label">Documento/NIT</label>
                            <input
                                type="text"
                                id="documento_NIT"
                                name="documento_NIT"
                                class="form-control"
                                oninput="this.value=this.value.replace(/[^0-9]/g,'')"
                                required
                                placeholder="Ingrese documento (10 dígitos)"
                                maxlength="10"
                            />
                            <div class="invalid-feedback" id="documentoFeedback"></div>
                        </div>
                    </div>

                    <!-- Fila 2: Asesor, Teléfono, Email -->
                    <div class="row mb-3">
                        <div class="col-12 col-md-4">
                            <label for="asesor" class="form-label">Asesor</label>
                            <input
                                type="text"
                                id="asesor"
                                name="asesor"
                                class="form-control"
                                required
                                placeholder="Ingrese nombres y apellidos"
                            />
                            <div class="invalid-feedback" id="asesorFeedback"></div>
                        </div>
                        <div class="col-12 col-md-4">
                            <label for="telefono" class="form-label">Teléfono</label>
                            <input
                                type="text"
                                id="telefono"
                                name="telefono"
                                class="form-control"
                                oninput="this.value=this.value.replace(/[^0-9]/g,'')"
                                required
                                placeholder="Ingrese teléfono (10 dígitos)"
                                maxlength="10"
                            />
                            <div class="invalid-feedback" id="telefonoFeedback"></div>
                        </div>
                        <div class="col-12 col-md-4">
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

                    <!-- Fila 3: Fecha Registro indica la fecha en que se relaciono 
                    o registro el proveedor, para tener presente el tiempo 
                    del que esta vinculado a la empresa , luego esta Día visita, y Estado activo o inactivo -->
                    <div class="row mb-3">
                           <div class="col-12 col-md-3">
                            <label class="form-label">Día de Visita</label>
                            <div class="dropdown w-100">
                                <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" id="dropdownDiaVisita" data-bs-toggle="dropdown" aria-expanded="false">
                                    Seleccionar día
                                </button>
                                <ul class="dropdown-menu w-100" aria-labelledby="dropdownDiaVisita">
                                    <li><a class="dropdown-item" role="button" data-value="Lunes">Lunes</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Martes">Martes</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Miércoles">Miércoles</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Jueves">Jueves</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Viernes">Viernes</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Sábado">Sábado</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Domingo">Domingo</a></li>
                                </ul>
                            </div>
                            <div class="invalid-feedback" id="diaVisitaFeedback">
                                Por favor, seleccione el día de visita.
                            </div>
                            <input type="hidden" id="diaVisita" name="diaVisita">
                        </div>
                        
                        
                        <div class="col-12 col-md-3">
                            <label class="form-label">Estado</label>
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

                    <!-- Ocultos -->
                    <input type="hidden" name="accion" id="accionInput" value="agregar" />
                    <input type="hidden" id="idProveedor" name="idProveedor" />

                    <!-- Botones -->
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-outline-primary" id="btnguardarProveedor">Guardar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

    <!-- Scripts de Bootstrap y archivo JS propio -->
   <script src="js/bootstrap.bundle.min.js"></script>
   <script src="js/proveedores.js"></script>
</body>
</html>