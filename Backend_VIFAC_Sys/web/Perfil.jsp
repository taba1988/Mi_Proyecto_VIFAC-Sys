<%-- 
    Document   : Perfil
    Created on : 26/10/2025, 10:54:04 AM
    Author     : duval
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Perfil de Usuario</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/perfil.css" />
</head>
<body>
<header class="bg-light py-3">
    <div class="container d-flex flex-column gap-3">
        <div class="d-flex align-items-center justify-content-between">
            <div class="d-flex align-items-center gap-2">
                <i class="bi bi-person-vcard-fill fs-1 text-info"></i>
                <h4 class="mb-0">Perfil de Usuario</h4>
            </div>
            <div class="d-lg-none">
                <button class="btn p-0 border-0 bg-transparent ms-3" type="button" 
                        data-bs-toggle="offcanvas" data-bs-target="#offcanvasProfile" 
                        aria-controls="offcanvasProfile">
                    <i class="bi bi-list fs-4"></i>
                </button>
            </div>
        </div>
        <div class="d-flex align-items-center justify-content-between">
            <div class="bloque-azul flex-grow-1 px-4 py-4 d-flex justify-content-between align-items-center">
                <div class="bloque-azul-texto fw-bold">
                    MAXI-LIMPIEZA
                </div>
                <div class="bloque-azul-slogan fst-italic">
                    Calidad que se siente
                </div>
            </div>
        </div>
    </div>
</header>

<hr />

<main class="container profile-container">
    <div class="profile-card">
        <div class="profile-image-section d-none d-lg-flex flex-column align-items-center">
            <div class="profile-image-container rounded-circle overflow-hidden mt-5 avatar-grande">
              <img 
                  src="${pageContext.request.contextPath}/ImagenPerfilServlet?nombreArchivo=${usuarioLogeado.fotoPerfil != null && !usuarioLogeado.fotoPerfil.isEmpty() ? usuarioLogeado.fotoPerfil : 'avatarperfil.png'}" 
                  alt="Foto de usuario ${usuarioLogeado.nombre}" 
                  class="w-100 h-100 object-fit-cover"
              />
            </div>
            <div class="profile-name mt-3 text-center">${nombre}</div>
            <div class="profile-role text-center">${cargo}</div>
        </div>

        <div class="profile-details-section w-100 flex-grow-1">
            <div class="profile-details-header bg-light">DATOS DE PERFIL</div>
            
            <form id="perfilForm" method="POST" action="${pageContext.request.contextPath}/PerfilServlet">
                <input type="hidden" name="accion" value="actualizarPerfil" />
                
<ul class="profile-details-list">
    <li><strong>Nombres y Apellidos:</strong> ${nombre}</li>
    <li><strong>Empresa:</strong> ${empresa}</li>
    <li><strong>Dependencia:</strong> ${dependencia}</li>
    <li><strong>Celular:</strong> 
        <input type="tel" class="form-control form-control-sm" 
               id="telefono" name="telefono" value="${telefono}">
    </li>
    <li><strong>Situación Laboral:</strong> ${situacionLaboral}</li>
    <li><strong>Identificación:</strong> 
        <input type="text" class="form-control form-control-sm" 
               value="${documento}" name="documento" readonly>
    </li>
    <li><strong>Usuario:</strong> ${usuarioAutenticado}</li>
    <li><strong>Nota:</strong> ${notaSistema}</li>
    <li><strong>Cargo:</strong> ${cargo}</li>
    <li><strong>Dirección:</strong> 
        <textarea class="form-control form-control-sm" 
                  id="direccion" name="direccion">${direccion}</textarea>
    </li>
    <li><strong>Correo Electrónico:</strong> 
        <input type="email" class="form-control form-control-sm" 
               value="${email}" name="email" readonly>
    </li>
</ul>

                <div class="d-flex justify-content-between align-items-center mt-3">          
                    <div class="d-flex gap-4">
                        <button class="btn btn-outline-secondary" type="button" onclick="location.href='${pageContext.request.contextPath}/indexServlet'">Inicio</button>
                        <button class="btn btn-outline-danger" type="button" onclick="location.href='${pageContext.request.contextPath}/logoutServlet'">Cerrar</button>
                    </div>
                    <button type="button" class="btn btn-outline-primary" onclick="confirmarGuardar()">Guardar Cambios</button>
                </div>
            </form>
        </div>
    </div>
</main>

<div class="offcanvas offcanvas-start d-lg-none" tabindex="-1" id="offcanvasProfile" aria-labelledby="offcanvasProfileLabel">
    <div class="offcanvas-header">
        <h5 class="offcanvas-title" id="offcanvasProfileLabel">Perfil</h5>
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
    </div>
    <div class="offcanvas-body text-center">
        <div class="profile-image-container rounded-circle overflow-hidden mx-auto avatar-grande">
            <img src="${pageContext.request.contextPath}/img/avatarperfil.png" 
                 alt="Foto de usuario ${nombre}" 
                 class="w-100 h-100 object-fit-cover">
        </div>
        <div class="profile-name mt-3">${nombre}</div>
        <div class="profile-role">${cargo}</div>
    </div>
    <div class="d-flex justify-content-center align-items-center mb-5 gap-4"> 
        <button class="btn btn-outline-secondary" type="button" onclick="location.href='${pageContext.request.contextPath}/indexServlet'">Inicio</button>
        <button class="btn btn-outline-danger" type="button" onclick="location.href='${pageContext.request.contextPath}/logoutServlet'">Cerrar</button>
    </div>
</div>

<footer class="bg-light py-3 mt-4">
    <div class="container d-flex justify-content-between flex-wrap">
        <small>© 2025</small>
        <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
    </div>
</footer>

<div class="modal fade" id="confirmarGuardarModal" tabindex="-1" aria-labelledby="confirmarGuardarModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmarGuardarModalLabel">Confirmar Guardar Cambios</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                ¿Está seguro de que desea guardar los cambios realizados en su perfil?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-outline-primary" onclick="guardarCambios()">Aceptar</button>
            </div>
        </div>
    </div>
</div>

<%@ include file="temporizador.jsp" %>
    
<script src="js/bootstrap.bundle.min.js"></script>
<script src="js/perfil.js"></script>
</body>
</html>
