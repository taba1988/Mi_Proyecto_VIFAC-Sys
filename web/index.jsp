<%-- 
    Document  : index
    Created on : 8/09/2025, 09:46:21 PM
    Author   : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>HOME-MAXI-LIMPIEZA</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"/>
    <link rel="stylesheet" href="css/index.css"/>      
</head>
<body class="bg-white">
    <div class="container-fluid mt-2 mb-3" style="flex-grow: 1;">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center px-4 py-3 rounded me-auto header-container">
                    <div class="fw-bold text-truncate left-text me-2">MAXI-LIMPIEZA</div>
                    <div class="fw-bold text-truncate text-end right-text d-none d-sm-block">Calidad que se siente</div>
                    <div class="d-flex align-items-end header-icons">
                        <button type="button" class="btn btn-link p-0 text-white me-3 position-relative" 
                            data-bs-toggle="modal" data-bs-target="#modalNotificaciones" aria-label="Notificaciones">
                            <i class="bi bi-bell-fill fs-4"></i>
                            <span id="badgeNotificaciones" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">3</span>
                        </button>
                        <button type="button" class="btn btn-link p-0 text-white position-relative" 
                            data-bs-toggle="modal" data-bs-target="#modalMensajes" aria-label="Mensajes">
                           <i class="bi bi-chat-dots-fill fs-4"></i>
                            <span id="badgeMensajes" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">36</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="profile-block col-12 col-sm-8 col-md-10 col-lg-6 position-relative d-flex align-items-center mt-2 me-0 flex-nowrap">
            <div class="col-12 col-md-10 d-flex align-items-center flex-nowrap">
                <div class="rounded-circle border border-primary mb-2 overflow-hidden" style="width: 70px; height: 100px;">
                    <img src="img/avatarperfil.png" alt="Foto de usuario ${nombre}" class="w-100 h-100 object-fit-cover">
                </div>
                <div class="ms-3">
                    <p class="mb-0"><strong>Usuario:</strong> ${nombre}</p>
                    <p class="mb-1"><strong>Cargo:</strong> ${cargo}</p>
                    <p class="mb-1"><strong>ID:</strong>${documento}</p>
                </div>
            </div>
        </div>

        <div class="row mt-3 d-md-none">
            <div class="text-start">
                <button class="btn btn-outline-primary offcanvas-button" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvas" aria-controls="offcanvas">
                    <i class="bi bi-list fs-4"></i>
                </button>
            </div>
        </div>
        
        <div class="offcanvas offcanvas-start d-md-none" tabindex="-1" id="offcanvas" aria-labelledby="offcanvaslabel">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvaslabel">Navegación</h5>
                <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="close"></button>
            </div>
            <div class="offcanvas-body d-flex flex-column">
                <div class="accordion mt-0" id="accordionMenusOffcanvas">
                    <%-- Menú de Comercial (Administrador y Vendedor) --%>
                    <c:if test="${idRol eq 1 || idRol eq 2}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingComercialOffcanvas">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseComercialOffcanvas">
                                    <i class="bi bi-bar-chart-fill me-2"></i>Comercial
                                </button>
                            </h2>
                            <div id="collapseComercialOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="Vender.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-cart-fill me-2"></i>Vender
                                        </a>
                                        <a href="Inventario.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-boxes me-2"></i>Gestión Inventario
                                        </a>
                                        <a href="Clientes.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-lines-fill me-2"></i>Clientes
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- Menú de Administración (Solo Administrador) --%>
                    <c:if test="${idRol eq 1}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingInternaOffcanvas">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseInternaOffcanvas">
                                    <i class="bi bi-gear-fill me-2"></i>Administración
                                </button>
                            </h2>
                            <div id="collapseInternaOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="GestorUsuarios.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-circle me-2"></i>Usuarios
                                        </a>
                                        <a href="Contabilidad.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-graph-up me-2"></i>Contabilidad
                                        </a>
                                        <a href="proveedores.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-truck me-2"></i>Proveedores
                                        </a>
                                        <a href="404.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-journal-check me-2"></i>Movimientos
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingCuentaOffcanvas">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseCuentaOffcanvas">
                                <i class="bi bi-person-badge-fill me-2"></i>Cuenta
                            </button>
                        </h2>
                        <div id="collapseCuentaOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                            <div class="accordion-body p-0">
                                <ul class="list-group list-group-flush border-0">
                                    <a href="index.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-house-door-fill me-2 align-middle"></i>Inicio
                                    </a>
                                    <a href="500.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi-sliders me-2 align-middle"></i>Configuraciones
                                    </a>
                                    <a href="cambiar_contrasena.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-key-fill me-2 align-middle"></i>Cambiar contraseña
                                    </a>
                                    <a href="ayuda.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-question-circle-fill me-2 align-middle"></i>Ayuda
                                    </a>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="fixed-icons d-md-none mb-5">  
                    <div class="d-flex align-items-center gap-4 px-2">
                        <a href="index.jsp" class="menu-link fixed-icon-link d-flex flex-column align-items-center">
                            <i class="bi bi-house-door-fill fs-4"></i>
                            <span class="menu-text-vertical">Inicio</span>
                        </a>
                        <a href="perfil.jsp" class="menu-link fixed-icon-link d-flex flex-column align-items-center">
                            <i class="bi bi-person-vcard-fill fs-4"></i>
                            <span class="menu-text-vertical">Mi Perfil</span>
                        </a>
                        <a href="logoutServlet" class="menu-link fixed-icon-link d-flex flex-column align-items-center">
                            <i class="bi bi-box-arrow-right fs-4"></i>
                            <span class="menu-text-vertical">Cerrar sesión</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row mt-3 flex-nowrap dashboard-content-row">
            <div class="col-md-3 col-lg-2 d-none d-md-block sidebar-left">
                <div class="accordion mt-0" id="accordionMenusDesktop">
                    <%-- Menú de Comercial (Administrador y Vendedor) --%>
                    <c:if test="${idRol eq 1 || idRol eq 2}">
                        <div class="accordion-item mt-5">
                            <h2 class="accordion-header" id="headingComercialDesktop">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseComercialDesktop">
                                    <i class="bi bi-bar-chart-fill me-2"></i>Comercial
                                </button>
                            </h2>
                            <div id="collapseComercialDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="Vender.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-cart-fill me-2"></i>Vender
                                        </a>
                                        <a href="Inventario.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-boxes me-2"></i>Gestión Inventario
                                        </a>
                                        <a href="Clientes.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-lines-fill me-2"></i>Clientes
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- Menú de Administración (Solo Administrador) --%>
                    <c:if test="${idRol eq 1}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingInternaDesktop">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseInternaDesktop">
                                    <i class="bi bi-gear-fill me-2"></i>Administración
                                </button>
                            </h2>
                            <div id="collapseInternaDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="GestorUsuarios.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-circle me-2"></i>Usuarios
                                        </a>
                                        <a href="Contabilidad.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-graph-up me-2"></i>Contabilidad
                                        </a>
                                        <a href="proveedores.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-truck me-2"></i>Proveedores
                                        </a>
                                        <a href="404.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-journal-check me-2"></i>Movimientos
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingCuentaDesktop">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseCuentaDesktop">
                                <i class="bi bi-person-badge-fill me-2"></i>Cuenta
                            </button>
                        </h2>
                        <div id="collapseCuentaDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                            <div class="accordion-body p-0">
                                <ul class="list-group list-group-flush border-0">
                                    <a href="index.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-house-door-fill me-2 align-middle"></i>Inicio
                                    </a>
                                    <a href="500.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi-sliders me-2 align-middle"></i>Configuraciones
                                    </a>
                                    <a href="cambiar_contrasena.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-key-fill me-2 align-middle"></i>Cambiar contraseña
                                    </a>
                                    <a href="ayuda.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-question-circle-fill me-2 align-middle"></i>Ayuda
                                    </a>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="fixed-icons mb-2">
                    <div class="d-flex justify-content-center gap-5 mt-2">
                        <a href="index.jsp" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Inicio">
                            <i class="bi bi-house-door-fill fs-4"></i>
                        </a>
                        <a href="perfil.jsp" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Mi Perfil">
                            <i class="bi bi-person-vcard-fill fs-4"></i>
                        </a>
                        <a href="logoutServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Cerrar sesión">
                            <i class="bi bi-box-arrow-right fs-4"></i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-9 col-lg-8 dashboard-main-area">
                <h4 class="mt-1 mb-3">Nuestras Soluciones Destacadas</h4>
                <div id="dynamicCarousel" class="carousel slide mt-2" data-bs-ride="carousel" data-bs-touch="true">
                    <div class="carousel-indicators">
                        <button type="button" data-bs-target="#dynamicCarousel" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
                        <button type="button" data-bs-target="#dynamicCarousel" data-bs-slide-to="1" aria-label="Slide 2"></button>
                        <button type="button" data-bs-target="#dynamicCarousel" data-bs-slide-to="2" aria-label="Slide 3"></button>
                    </div>
                    <div class="carousel-inner">
                        <div class="carousel-item active">
                            <img src="img/slide 1.svg" class="d-block w-100" alt="Slide 1">
                        </div>
                        <div class="carousel-item">
                            <img src="img/slide 2.svg" class="d-block w-100" alt="Slide 2">
                        </div>
                        <div class="carousel-item">
                            <img src="img/slide 3.svg" class="d-block w-100" alt="Slide 3">
                        </div>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#dynamicCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                   <button class="carousel-control-next" type="button" data-bs-target="#dynamicCarousel" data-bs-slide="next">
                       <span class="carousel-control-next-icon" aria-hidden="true"></span>
                       <span class="visually-hidden">Next</span>
                   </button>
               </div>

                <h4 class="mt-5 mb-3">Indicadores Clave</h4>
               <div class="row card-section">
                   <div class="col-md-4 mb-4">
                        <div class="card h-100">
                            <img src="img/Card_Ventas.svg" class="card-img-top" alt="Ventas">
                            <div class="card-body">
                                <h5 class="card-title">Ventas del Mes</h5>
                                <p class="card-text">Total de ventas realizadas hasta la fecha.</p>
                                <div class="chart-container-placeholder">
                                    <div class="bar-chart d-flex align-items-end justify-content-between h-100">
                                        <div class="bar w-25" style="height: 80%;"></div>
                                        <div class="bar w-25" style="height: 60%; background-color: #0093DD;"></div>
                                        <div class="bar w-25" style="height: 75%; background-color: #333652;"></div>
                                        <div class="bar w-25" style="height: 90%;"></div>
                                    </div>
                                </div>
                           </div>
                       </div>
                   </div>
                   <div class="col-md-4 mb-4">
                       <div class="card h-100">
                            <img src="img/card_inventario.svg" class="card-img-top" alt="Inventario">
                           <div class="card-body">
                                <h5 class="card-title">Nivel de Inventario</h5>
                               <p class="card-text">Estado actual de tus productos en almacén.</p>
                               <div class="chart-container-placeholder">
                                    <div class="bar-chart d-flex align-items-end justify-content-between h-100">
                                        <div class="bar w-25" style="height: 30%; background-color: #fc7a1e;"></div>
                                        <div class="bar w-25" style="height: 45%; background-color: #fc7a1e;"></div>
                                        <div class="bar w-25" style="height: 20%; background-color: #dc3545;"></div>
                                        <div class="bar w-25" style="height: 60%; background-color: #333652;"></div>
                                    </div>
                               </div>
                           </div>
                       </div>
                   </div>
                   <div class="col-md-4 mb-4">
                       <div class="card h-100">
                           <img src="img/card_clientes.svg" class="card-img-top" alt="Clientes">
                           <div class="card-body">
                               <h5 class="card-title">Clientes Activos</h5>
                               <p class="card-text">Número de clientes con actividad reciente.</p>
                               <div class="chart-container-placeholder">
                                   <div class="bar-chart d-flex align-items-end justify-content-between h-100">
                                        <div class="bar w-25" style="height: 50%; background-color: #198754;"></div>
                                        <div class="bar w-25" style="height: 70%; background-color: #198754;"></div>
                                        <div class="bar w-25" style="height: 85%; background-color: #198754;"></div>
                                        <div class="bar w-25" style="height: 65%; background-color: #198754;"></div>
                                   </div>
                               </div>
                           </div>
                       </div>
                   </div>

               </div>

                <h4 class="mt-5 mb-3">Accesos Rápidos</h4>
               <div class="list-group link-section mb-5">
                   <a href="Vender.jsp" class="list-group-item list-group-item-action"><i class="bi bi-cart-fill me-2"></i>Ir a Ventas</a>
                   <a href="Inventario.jsp" class="list-group-item list-group-item-action"><i class="bi bi-box-seam-fill me-2"></i>Ver Inventario</a>
                   <a href="Clientes.jsp" class="list-group-item list-group-item-action"><i class="bi bi-people-fill me-2"></i>Gestionar Clientes</a>
                   <a href="Contabilidad.jsp" class="list-group-item list-group-item-action"><i class="bi bi-graph-up-arrow me-2"></i>Reportes Contables</a>
               </div>
           </div>  
           <div class="col-2 d-none d-md-flex flex-column justify-content-start align-items-center p-0 mt-4 ms-auto logo-right-col">
               <img src="img/Empresa.png" alt="Logo empresa" class="rounded-circle border border-secondary" style="width: 150px; height: 150px;">
                <p class="text-center mt-2" style="font-family: 'Bad Script', cursive; font-style: italic; font-weight: bold; font-size: 14px; color: #333652;">Calidad que se siente</p>
           </div>
        </div>
        
        <footer class="py-0 bg-light mt-3">
           <div class="d-flex justify-content-between align-items-center px-4 py-3 rounded me-auto bg-transparent" style="white-space: nowrap; overflow: hidden; width: 100%;">
               <div class="text-muted"> &copy; 2025</div>
               <div class="mt-2 mt-md-0">
                    <a href="#" class="link-dark text-muted">Diseñado por O.T.G “VIFAC-Sys”</a>
               </div>
           </div>
        </footer>
    </div>
    <div class="modal fade" id="modalNotificaciones" tabindex="-1" aria-labelledby="modalNotificacionesLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
               <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalNotificacionesLabel"><i class="bi bi-bell-fill me-2"></i> Notificaciones</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
               </div>
               <div class="modal-body">
                    <ul id="listaNotificaciones" class="list-group list-group-flush">
                    </ul>
               </div>
               <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
               </div>
           </div>
        </div>
    </div>

    <div class="modal fade" id="modalMensajes" tabindex="-1" aria-labelledby="modalMensajesLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
               <div class="modal-header bg-success text-white">
                    <h5 class="modal-title" id="modalMensajesLabel"><i class="bi bi-chat-dots-fill me-2"></i> Mensajes del sistema</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
               </div>
               <div class="modal-body">
                    <ul id="listaMensajes" class="list-group list-group-flush">
                    </ul>
               </div>
               <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
               </div>
           </div>
        </div>
    </div>
    <script src="js/bootstrap.bundle.min.js"></script>
    <script src="js/index.js"></script>
</body>
</html>