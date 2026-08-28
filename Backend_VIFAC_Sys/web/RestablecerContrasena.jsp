<%-- 
    Document   : Restablecer Contraseña
    Created on : 22/10/2025, 07:46:43 PM
    Author     : Orlanduvalie Tabares Gutierrez 
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"> <!-- Codificación de caracteres -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge"> <!-- Compatibilidad con IE -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Adaptabilidad responsive -->
    <title>Restablecer Contraseña</title>
    <link rel="stylesheet" href="css/bootstrap.min.css"> <!-- Bootstrap -->
    <link rel="stylesheet" href="css/cambiar_contrasena.css"> <!-- Estilos personalizados -->
</head>
<body class="bg-white">
    <div class="header-container">
        <div class="left-text">Restablecer Contraseña</div>
        <div class="center-text">MAXI-LIMPIEZA</div>
        <div class="right-text">Calidad que se siente</div>
    </div>

    <div id="layoutAuthentication">
        <div id="layoutAuthentication_content">
            <main>
                <div class="container">
                    <div class="row justify-content-start mt-3">
                        <div class="col-sm-10 col-md-7 col-lg-5">
                            <div class="card shadow-lg border-7 rounded-lg mt-2">
                                <div class="card-header">
                                    <h3 class="text-center font-weight-secondary my-3">Restablecer Contraseña</h3>
                                </div>
                                <div class="card-body mx-2">
                                    <!-- Formulario que mantiene el JS pero envia token al servlet -->
                                    <form action="RestablecerContrasenaServlet" method="post">
                                        <input type="hidden" name="token" value="<%= request.getParameter("token") %>">
                                    
                                        <div class="form-floating mb-3">
                                            <input class="form-control" name="nuevaContrasena" type="password" placeholder="Nueva Contraseña" required />
                                            <label for="inputNewPassword">Nueva Contraseña</label>
                                        </div>
                                    
                                        <div class="form-floating mb-3">
                                            <input class="form-control" name="confirmarContrasena" type="password" placeholder="Repetir Nueva Contraseña" required />
                                            <label for="inputConfirmNewPassword">Confirmar Nueva Contraseña</label>
                                            <div id="password-match-error" class="error-message d-none">Las contraseñas no coinciden.</div>
                                            <div id="password-strength-error" class="error-message d-none">La contraseña debe tener mínimo 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales.</div>
                                        </div>                                       
                                    
                                        <div class="d-flex justify-content-center mt-4 mb-0">
                                            <button type="submit" class="btn btn-outline-primary">Guardar Contraseña</button>
                                        </div>
                                    </form>
                                </div>
                                <div class="form-group mb-0">
                                    <div class="card-footer text-auto py-3">
                                        <div class="col-sm-12" style="font-size:12px;">
                                            <figure class="text-center">
                                                <p style="text-align: justify;">
                                                    Este es el sistema <b>VIFAC-Sys</b>. Para recuperar su contraseña asegúrese de que cumpla con los requisitos de seguridad:
                                                    Recuerde que su contraseña debe tener como mínimo 8 caracteres que contenga Mayúsculas, Minúsculas, Números y Caracteres especiales.
                                                    Su contraseña es única e intransferible.
                                                </p>
                                            </figure>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>

        <div id="layoutAuthentication_footer" style="color: #D9D9D9;">
            <footer class="py-3 bg-light mb-5">
                <div class="container-fluid px-3">
                    <div class="d-flex align-items-center justify-content-between">
                        <div class="text-muted"> &copy; 2025</div>
                        <div>
                            <a href="#" class="link-dark text-muted">Política de privacidad</a>
                            &middot;
                            <a href="#" class="link-dark text-muted">Diseñado por O.T.G “VIFAC-Sys</a>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    </div>

    <!-- Modal de confirmación -->
    <div class="modal fade" id="confirmChangePasswordModal" tabindex="-1" aria-labelledby="confirmChangePasswordModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="confirmChangePasswordModalLabel">Confirmar la recuperación de Contraseña</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body">
                    ¿Está seguro de que desea guardar esta contraseña?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                    <button type="button" class="btn btn-primary" onclick="submitNewPassword()">Aceptar</button>
                </div>
            </div>
        </div>
    </div>
    
    <c:if test="${not empty mensaje}">
     <div class="modal fade show" id="responseModal" tabindex="-1" style="display:block;" aria-modal="true" role="dialog">
         <div class="modal-dialog">
             <div class="modal-content">
                 <div class="modal-header ${tituloModal == 'ERROR' ? 'bg-warning text-dark' : 'bg-info text-white'}">
                     <h5 class="modal-title">${tituloModal}</h5>
                 </div>
                 <div class="modal-body">
                     <p>${mensaje}</p>
                 </div>
                 <div class="modal-footer justify-content-center">
                     <a href="login.jsp" class="btn btn-outline-primary">Aceptar</a>
                 </div>
             </div>
         </div>
     </div>
    </c:if>
    <%@ include file="Bubbles.jsp" %>
    <!-- Scripts originales -->
    <script src="js/bootstrap.bundle.min.js"></script>
    <script src="js/recuperar.js"></script>
    
</body>
</html>
