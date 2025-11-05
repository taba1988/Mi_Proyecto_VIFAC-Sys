<%-- 
    Documento   : index o dashboard pagina inicial para la navegacion a todos los modulos 
    Creado el   : 8/09/2025, 09:46:21 PM
    Author      : ORLANDUVALIE TABARES GUTIERREZ
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
        <!-- HEADER -->
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center px-4 py-3 rounded me-auto header-container">
                    <div class="fw-bold text-truncate left-text me-2">MAXI-LIMPIEZA</div>
                    <div class="fw-bold text-truncate text-end right-text d-none d-sm-block">Calidad que se siente</div>
                    <!-- ICONOS HEADER -->
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

        <!-- BLOCK PERFIL -->
        <div class="profile-block col-12 col-sm-8 col-md-10 col-lg-6 position-relative d-flex align-items-center mt-2 me-0 flex-nowrap">
            <div class="col-12 col-md-10 d-flex align-items-center flex-nowrap">
                <div class="rounded-circle border border-primary mb-2 overflow-hidden" style="width: 70px; height: 100px;">
                 <img 
                     src="${pageContext.request.contextPath}/ImagenPerfilServlet?nombreArchivo=${usuarioLogeado.fotoPerfil != null && !usuarioLogeado.fotoPerfil.isEmpty() ? usuarioLogeado.fotoPerfil : 'avatarperfil.png'}" 
                     alt="Foto de usuario ${usuarioLogeado.nombre}" 
                     class="w-100 h-100 object-fit-cover"
                 />
                </div>
                <div class="ms-3">
                   <p class="mb-0"><strong>Usuario:</strong> <span class="text-uppercase">${nombre}</span></p>
                   <p class="mb-1"><strong>Cargo:</strong> <span class="text-uppercase">${cargo}</span></p>
                   <p class="mb-1"><strong>ID:</strong> <span class="text-uppercase">${documento}</span></p>
                </div>
            </div>
        </div>

        <!-- BOTÓN OFFCANVAS MOVIL -->
        <div class="row mt-3 d-md-none">
            <div class="text-start">
                <button class="btn btn-outline-primary offcanvas-button" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvas" aria-controls="offcanvas">
                    <i class="bi bi-list fs-4"></i>
                </button>
            </div>
        </div>
        
        <!-- OFFCANVAS BAVEGACIÒN MOVIL PARA PANTALLAS PEQUEÑAS-->
        <div class="offcanvas offcanvas-start d-md-none" tabindex="-1" id="offcanvas" aria-labelledby="offcanvaslabel">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvaslabel">Navegación</h5>
                <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="close"></button>
            </div>
            <div class="offcanvas-body d-flex flex-column">
                
                <!-- Acordeón en Offcanvas (Movil) -->
                <div class="accordion mt-0" id="accordionMenusOffcanvas">
                    <%-- 📦 Sección Comercial --%>
                    <c:if test="${idRol eq 1 || idRol eq 2}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingComercialOffcanvas">
                                <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseComercialOffcanvas">
                                    <i class="bi bi-bar-chart-fill me-2 fs-5"></i><span class="fw-bold">Comercial</span>
                                </button>
                            </h2>
                            <div id="collapseComercialOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="VenderServlet" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-cart-fill me-2 fs-5"></i><span class="fw-bold">Vender</span>
                                        </a>
                                        <a href="Clientes.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-lines-fill me-2 fs-5"></i><span class="fw-bold">Gestion de Clientes</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- ⚙️ Sección Administración --%>
                    <c:if test="${idRol eq 1}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingAdministracionOffcanvas">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseAdministracionOffcanvas">
                                    <i class="bi bi-gear-fill me-2 fs-5"></i><span class="fw-bold">Administración</span>
                                </button>
                            </h2>
                            <div id="collapseAdministracionOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="GestorUsuarios.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-circle me-2 fs-5"></i><span class="fw-bold">Usuarios</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- 🚚 Sección Logística --%>
                    <c:if test="${idRol eq 1 || idRol eq 4}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingLogisticaOffcanvas">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseLogisticaOffcanvas">
                                    <i class="bi bi-truck me-2 fs-5 fw-bold"></i><span class="fw-bold">Logística</span>
                                </button>
                            </h2>
                            <div id="collapseLogisticaOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="Inventario.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-box-seam me-2 fs-5"></i><span class="fw-bold">Gestión de Inventario</span>
                                        </a>
                                        <a href="proveedores.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-workspace me-2 fs-5"></i><span class="fw-bold">Proveedores</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- 💰 Sección Contabilidad --%>
                    <c:if test="${idRol eq 1 || idRol eq 3}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingContabilidadOffcanvas">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseContabilidadOffcanvas">
                                    <i class="bi bi-graph-up me-2 fs-5"></i><span class="fw-bold">Gestion Contable</span>
                                </button>
                            </h2>
                            <div id="collapseContabilidadOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="Contabilidad.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-graph-up me-2 fs-5"></i><span class="fw-bold">Contabilidad</span>
                                        </a>
                                        <a href="404.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-journal-check me-2 fs-5"></i><span class="fw-bold">Movimientos</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- 👤 Sección Cuenta --%>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingCuentaOffcanvas">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseCuentaOffcanvas">
                                <i class="bi bi-person-badge-fill me-2 fs-5"></i><span class="fw-bold">Cuenta</span>
                            </button>
                        </h2>
                        <div id="collapseCuentaOffcanvas" class="accordion-collapse collapse" data-bs-parent="#accordionMenusOffcanvas">
                            <div class="accordion-body p-0">
                                <ul class="list-group list-group-flush border-0">
                                    <a href="indexServlet" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-house-door-fill me-2 align-middle fs-5"></i><span class="fw-bold">Inicio</span>
                                    </a>
                                    <a href="500.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi-sliders me-2 align-middle fs-5"></i><span class="fw-bold">Configuraciones</span>
                                    </a>
                                    <a href="cambiar_contrasena.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-key-fill me-2 align-middle fs-5"></i><span class="fw-bold">Cambiar contraseña</span>
                                    </a>
                                    <a href="ayuda.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-question-circle-fill me-2 align-middle fs-5"></i><span class="fw-bold">Ayuda</span>
                                    </a>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div> <!-- Fin acordeón en OFFCANVAS (Movil) -->

                <!-- Iconos fijos en móvil -->
                <div class="fixed-icons mb-2" style="position: relative; bottom: -140px;">
                    <div class="d-flex justify-content-center gap-5 mt-2">
                        <a href="indexServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Inicio">
                            <i class="bi bi-house-door-fill fs-2"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/PerfilServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Mi Perfil">
                            <i class="bi bi-person-vcard-fill fs-2"></i>
                        </a>
                        <a href="logoutServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Cerrar sesión">
                            <i class="bi bi-box-arrow-right fs-2"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>

       <!-- NAVEGACIÒN PARA PANTALLAS GRANDES-->
        <div class="row mt-3 flex-nowrap dashboard-content-row">
            <!-- Sidebar izquierdo -->
            <div class="col-md-3 col-lg-2 d-none d-md-block sidebar-left">
                <!-- Acordeón en pantallas grandes -->
                <div class="accordion mt-0" id="accordionMenusDesktop">
                    <%-- 📦 Sección Comercial --%>
                    <c:if test="${idRol eq 1 || idRol eq 2}">
                        <div class="accordion-item mt-5">
                            <h2 class="accordion-header" id="headingComercialDesktop">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseComercialDesktop">
                                    <i class="bi bi-bar-chart-fill me-2 fs-5"></i><span class="fw-bold">Comercial</span>
                                </button>
                            </h2>
                            <div id="collapseComercialDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="VenderServlet" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-cart-fill me-2 fs-5"></i><span class="fw-bold">Vender</span>
                                        </a>
                                        <a href="Clientes.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-lines-fill me-2 fs-5"></i><span class="fw-bold">Gestion de Clientes</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- ⚙️ Sección Administración--%>
                    <c:if test="${idRol eq 1}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingAdministracionDesktop">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseAdministracionDesktop">
                                    <i class="bi bi-gear-fill me-2 fs-5"></i><span class="fw-bold">Administración</span>
                                </button>
                            </h2>
                            <div id="collapseAdministracionDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="GestorUsuarios.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-circle me-2 fs-5"></i><span class="fw-bold">Usuarios</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- 🚚 Sección Logística --%>
                    <c:if test="${idRol eq 1 || idRol eq 4}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingLogisticaDesktop">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseLogisticaDesktop">
                                    <i class="bi bi-truck me-2 fs-5"></i><span class="fw-bold">Logística</span>
                                </button>
                            </h2>
                            <div id="collapseLogisticaDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="Inventario.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-box-seam me-2 fs-5"></i><span class="fw-bold">Gestión de Inventario</span>
                                        </a>
                                        <a href="proveedores.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-person-workspace me-2 fs-5"></i><span class="fw-bold">Proveedores</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- 💰 Sección Contabilidad --%>
                    <c:if test="${idRol eq 1 || idRol eq 3}">
                        <div class="accordion-item">
                            <h2 class="accordion-header" id="headingContabilidadDesktop">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseContabilidadDesktop">
                                    <i class="bi bi-graph-up me-2 fs-5"></i><span class="fw-bold">Gestion Contable</span>
                                </button>
                            </h2>
                            <div id="collapseContabilidadDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                                <div class="accordion-body p-0">
                                    <ul class="list-group list-group-flush border-0">
                                        <a href="Contabilidad.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-graph-up me-2 fs-5"></i><span class="fw-bold">Contabilidad</span>
                                        </a>
                                        <a href="404.jsp" class="list-group-item list-group-item-action border-0">
                                            <i class="bi bi-journal-check me-2 fs-5"></i><span class="fw-bold">Movimientos</span>
                                        </a>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%-- 👤 Sección Cuenta --%>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingCuentaDesktop">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseCuentaDesktop">
                                <i class="bi bi-person-badge-fill me-2 fs-5"></i><span class="fw-bold">Cuenta</span>
                            </button>
                        </h2>
                        <div id="collapseCuentaDesktop" class="accordion-collapse collapse" data-bs-parent="#accordionMenusDesktop">
                            <div class="accordion-body p-0">
                                <ul class="list-group list-group-flush border-0">
                                    <a href="indexServlet" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-house-door-fill me-2 align-middle fs-5"></i><span class="fw-bold">Inicio</span>
                                    </a>
                                    <a href="500.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi-sliders me-2 align-middle fs-5"></i><span class="fw-bold">Configuraciones</span>
                                    </a>
                                    <a href="cambiar_contrasena.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-key-fill me-2 align-middle fs-5"></i><span class="fw-bold">Cambiar contraseña</span>
                                    </a>
                                    <a href="ayuda.jsp" class="list-group-item list-group-item-action border-0">
                                        <i class="bi bi-question-circle-fill me-2 align-middle fs-5"></i><span class="fw-bold">Ayuda</span>
                                    </a>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div> <!-- Fin acordeón para pantallas grandes -->

                <!-- Iconos fijos pantalla grande-->
                <div class="fixed-icons mb-2" style="position: relative; bottom: -140px;">
                    <div class="d-flex justify-content-center gap-5 mt-2">
                        <a href="indexServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Inicio">
                            <i class="bi bi-house-door-fill fs-2"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/PerfilServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Mi Perfil">
                            <i class="bi bi-person-vcard-fill fs-2"></i>
                        </a>
                        <a href="logoutServlet" class="menu-link fixed-icon-link" data-bs-toggle="tooltip" data-bs-placement="top" title="Cerrar sesión">
                            <i class="bi bi-box-arrow-right fs-2"></i>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Main contenido central (carrousel, Indicadores claves Accesos rapidos) dinamicos e intuituvos del dashboard -->
            <div class="col-md-9 col-lg-8 dashboard-main-area">
                <h4 class="mt-1 mb-3">Nuestras Soluciones Destacadas</h4>
                <!-- Sección de botones y modal para subir imágenes al carousel (solo admin) -->
                <c:if test="${idRol eq 1}">
                    <!-- Botón para abrir modal -->
                    <div class="d-flex justify-content-end mb-2">
                        <button type="button" class="btn btn-outline-secondary btn-destacado" 
                                data-bs-toggle="modal" data-bs-target="#modalSubirImagen">
                            <i class="bi bi-upload me-1"></i> Agregar Destacado
                        </button>
                    </div>
                    <!-- Modal para subir imágenes destacadas -->
                    <div class="modal fade" id="modalSubirImagen" tabindex="-1" aria-labelledby="modalSubirImagenLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <!-- Encabezado modal -->
                                <div class="modal-header bg-primary text-white">
                                    <h5 class="modal-title" id="modalSubirImagenLabel">Cargar Imágenes Destacadas</h5>
                                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                                </div>
                                <!-- Cuerpo modal con formulario -->
                                <div class="modal-body">
                                    <form action="SubirImagenServlet" method="post" enctype="multipart/form-data">
                                        <!-- Inputs de imágenes -->
                                        <div class="mb-3">
                                            <label for="banner1" class="form-label">Imagen 1</label>
                                            <input type="file" class="form-control" id="banner1" name="banner1" accept="image/*" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="banner2" class="form-label">Imagen 2</label>
                                            <input type="file" class="form-control" id="banner2" name="banner2" accept="image/*" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="banner3" class="form-label">Imagen 3</label>
                                            <input type="file" class="form-control" id="banner3" name="banner3" accept="image/*" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="banner4" class="form-label">Imagen 4</label>
                                            <input type="file" class="form-control" id="banner4" name="banner4" accept="image/*" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="banner5" class="form-label">Imagen 5</label>
                                            <input type="file" class="form-control" id="banner5" name="banner5" accept="image/*" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="banner6" class="form-label">Imagen 6</label>
                                            <input type="file" class="form-control" id="banner6" name="banner6" accept="image/*" required>
                                        </div>
                                        <button type="submit" class="btn btn-success w-100">Cargar</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Sección Carrusel dinámico -->
                <!-- ================================== -->
                <!-- Sección Carrousel: para mostrar imágenes destacadas -->
                <!-- ================================== -->
                <div id="dynamicCarousel" class="carousel slide mt-2" data-bs-ride="carousel" data-bs-touch="true">
                    <!-- Indicadores para el carrousel-->
                    <div class="carousel-indicators">
                        <c:forEach var="img" items="${imagenesDestacadas}" varStatus="status">
                            <button type="button" data-bs-target="#dynamicCarousel" data-bs-slide-to="${status.index}" 
                                    class="${status.first ? 'active' : ''}" aria-current="${status.first ? 'true' : 'false'}"
                                    aria-label="Slide ${status.index + 1}"></button>
                        </c:forEach>
                    </div>
                    <!-- Slides para el carrousel -->
                    <div class="carousel-inner">
                        <c:forEach var="img" items="${imagenesDestacadas}" varStatus="status">
                            <div class="carousel-item ${status.first ? 'active' : ''}">
                                <img src="${pageContext.request.contextPath}/ImagenServlet?nombreArchivo=${img.nombreArchivo}" 
                                     class="d-block w-100" 
                                     alt="Imagen destacada ${status.index + 1}">
                            </div>
                        </c:forEach>
                        <!-- Placeholder si no hay imágenes -->
                        <c:if test="${empty imagenesDestacadas}">
                            <div class="carousel-item active">
                                <img src="${pageContext.request.contextPath}/img/placeholder.png" 
                                     class="d-block w-100" 
                                     alt="Sin imágenes">
                            </div>
                        </c:if>
                    </div>
                    <!-- Controles del carrosel -->
                    <button class="carousel-control-prev" type="button" data-bs-target="#dynamicCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#dynamicCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>

                <!-- Sección Indicadores Clave -->
                <h4 class="mt-5 mb-3">Indicadores Clave</h4>
                <!-- Tarjetas con gráficos simulados -->
                <div class="row card-section">
                    <!-- Ventas del Mes -->
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
                   <!-- Nivel de Inventario -->
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
                   <!-- Clientes Activos -->
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

               <!-- Accesos Rápidos -->
                <h4 class="mt-5 mb-3">Accesos Rápidos</h4>
                <!-- Lista de enlaces rápidos -->
                <div class="list-group link-section mb-5">
                    <a href="indexServlet" class="list-group-item list-group-item-action">
                        <i class="bi bi-house-door-fill me-2"></i>Inicio
                    </a>
                    <c:if test="${idRol eq 1 || idRol eq 2}">
                        <a href="VenderServlet" class="list-group-item list-group-item-action">
                            <i class="bi bi-cart-fill me-2"></i>Ir a Ventas
                        </a>
                    </c:if>
                    <c:if test="${idRol eq 1 || idRol eq 4}">
                        <a href="Inventario.jsp" class="list-group-item list-group-item-action">
                            <i class="bi bi-box-seam-fill me-2"></i>Ver Inventario
                        </a>
                    </c:if>
                    <c:if test="${idRol eq 1 || idRol eq 2}">
                        <a href="Clientes.jsp" class="list-group-item list-group-item-action">
                            <i class="bi bi-people-fill me-2"></i>Gestionar Clientes
                        </a>
                    </c:if>
                    <c:if test="${idRol eq 1 || idRol eq 3}">
                        <a href="Contabilidad.jsp" class="list-group-item list-group-item-action">
                            <i class="bi bi-graph-up-arrow me-2"></i>Reportes Contables
                        </a>
                    </c:if>
                </div>

            </div>

            <!-- Logo lateral derecho -->
            <div class="col-2 d-none d-md-flex flex-column justify-content-start align-items-center p-0 mt-4 ms-auto logo-right-col">
                <img src="img/Empresa.png" alt="Logo empresa" class="rounded-circle border border-secondary" style="width: 150px; height: 150px;">
                <p class="text-center mt-2" style="font-family: 'Bad Script', cursive; font-style: italic; font-weight: bold; font-size: 14px; color: #333652;">Calidad que se siente</p>
            </div>
        </div>
        
        <!-- FOOTER -->
        <footer class="py-0 bg-light mt-3">
            <div class="d-flex justify-content-between align-items-center px-4 py-3 rounded me-auto bg-transparent" style="white-space: nowrap; overflow: hidden; width: 100%;">
                <div class="text-muted"> &copy; 2025</div>
                <div class="mt-2 mt-md-0">
                    <a href="#" class="link-dark text-muted">Diseñado por O.T.G “VIFAC-Sys”</a>
                </div>
            </div>
        </footer>
    </div>

    <!-- Modales -->
    <!-- ================== -->
    <!-- Notificaciones -->
    <!-- ================== -->
    <div class="modal fade" id="modalNotificaciones" tabindex="-1" aria-labelledby="modalNotificacionesLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalNotificacionesLabel"><i class="bi bi-bell-fill me-2"></i> Notificaciones</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <!-- Lista -->
                <div class="modal-body">
                    <ul id="listaNotificaciones" class="list-group list-group-flush"></ul>
                </div>
                <!-- Pie -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- ================== -->
    <!-- Mensajes Sistema -->
    <!-- ================== -->
    <div class="modal fade" id="modalMensajes" tabindex="-1" aria-labelledby="modalMensajesLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
                <!-- Encabezado -->
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title" id="modalMensajesLabel"><i class="bi bi-chat-dots-fill me-2"></i> Mensajes del sistema</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <!-- Lista -->
                <div class="modal-body">
                    <ul id="listaMensajes" class="list-group list-group-flush"></ul>
                </div>
                <!-- Pie -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- ================== -->
    <!-- Bienvenida -->
    <!-- ================== -->
    <div class="modal fade" id="modalBienvenida" tabindex="-1" aria-labelledby="modalBienvenidaLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <!-- Encabezado -->
                <div class="modal-header bg-primary text-white">
                    <h3 class="modal-title" id="modalBienvenidaLabel"><strong>¡Has ingresado con éxito!</strong></h3>
                </div>
                <!-- Cuerpo -->
                <div class="modal-body">
                    <p>✅ Recuerda seguir las <strong>buenas prácticas de seguridad</strong> en la información que se almacena en este sistema.</p>
                    <p>🔒 No olvides <strong>cerrar sesión</strong> al finalizar tu jornada laboral.</p>
                    <p>⏱️ Por seguridad, el sistema se cerrará automáticamente tras <strong>10 minutos de inactividad</strong>.</p>
                    <p>🔑 Recuerda <strong>cambiar tu contraseña periódicamente</strong> para mantener tu cuenta segura.</p>
                </div>
                <!-- Botones -->
                <div class="modal-footer">
                    <a href="indexServlet?skipCarousel=true" class="btn btn-primary">Aceptar</a>
                    <a href="logoutServlet" class="btn btn-secondary">Rechazar</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Include temporizador -->
    <%@ include file="temporizador.jsp" %>

    <!-- Scripts Bootstrap y custom -->
    <script src="js/bootstrap.bundle.min.js"></script>
    <script src="js/index.js"></script>
    <script src="js/Bienvenida.js"></script>
</body>
</html>