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
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restablecer Contraseña</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/recuperar.css">
</head>
<body class="bg-white">

  <!-- Encabezado -->
  <div class="header-container d-flex justify-content-between align-items-center py-3 px-3 border-bottom">
      <div class="left-text fs-5 fw-semibold">Restablecer Contraseña</div>
      <div class="center-text fs-3 fw-bold">MAXI-LIMPIEZA</div>
      <div class="right-text fs-5 fw-medium">Calidad que se siente</div>
  </div>

  <!-- Contenedor principal -->
  <div id="layoutAuthentication" class="d-flex flex-column">
      <div id="layoutAuthentication_content">
          <main>
              <!-- Sección de formulario -->
              <div class="container">
                  <div class="row justify-content-start mt-4">
                      <div class="col-sm-10 col-md-7 col-lg-5">
                          <!-- Tarjeta de recuperación -->
                          <div class="card shadow-lg border-7 rounded-lg mt-2">
                              <div class="card-header">
                                  <h3 class="text-center font-weight-secondary my-3">Restablecer Contraseña</h3>
                              </div>
                              <div class="card-body mx-2">
                                  <!-- Formulario de restablecimiento -->
                                  <form action="EnviarTokenRecuperacionServlet" method="post">
                                      <div class="small mb-3 text-muted">
                                          Ingrese su dirección de correo electrónico y le enviaremos un enlace para restablecer su contraseña.
                                      </div>
                                      <!-- Campo de correo -->
                                      <div class="form-floating mb-3">
                                          <input class="form-control" id="inputEmail" name="email" type="email" placeholder="name@example.com" required />
                                          <label for="inputEmail">Correo Electrónico</label>
                                      </div>
                                      <!-- Botones de acción -->
                                      <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                          <a class="small" href="logoutServlet">Volver al inicio de sesión</a>
                                          <button class="btn btn-outline-primary" type="submit">Restablecer</button>
                                      </div>
                                  </form>
                              </div>
                              <!-- Texto de ayuda -->
                              <div class="form-group mb-0 mt-4">
                                  <div class="card-footer text-auto py-3">
                                      <div class="col-sm-12" style="font-size:12px;">
                                          <figure class="text-center">
                                              <p style="text-align: justify;">
                                                  Este es el sistema <b>VIFAC-Sys</b>. Si olvidó su contraseña, ingrese su correo electrónico registrado.
                                                  Se le enviará un enlace seguro para restablecerla. Si no recibe el correo en unos minutos,
                                                  revise su carpeta de spam o contáctese con el administrador del sistema. 
                                                  Recuerde que su contraseña debe tener como mínimo 8 caracteres que contenga (Mayúsculas, Minúsculas, Números y Caracteres especiales).
                                                  Su contraseña es única e intransferible.
                                              </p>
                                          </figure>
                                      </div>
                                  </div>
                              </div>
                          </div>
                          <!-- Fin tarjeta -->
                      </div>
                  </div>
              </div>
          </main>
      </div>

      <!-- Footer -->
      <div id="layoutAuthentication_footer" style="color: #D9D9D9;">
          <footer class="py-3 bg-light mt-auto">
              <div class="container-fluid px-3">
                  <div class="d-flex align-items-center justify-content-between">
                      <div class="text-muted"> &copy; 2025</div>
                      <div>
                          <a href="#" class="link-dark text-muted">Política de privacidad</a>
                          &middot;
                          <a href="#" class="link-dark text-muted">Diseñado por O.T.G “VIFAC-Sys”</a>
                      </div>
                  </div>
              </div>
          </footer>
      </div>
  </div>

  <!-- Scripts de Bootstrap y archivo JS propio -->
  <script src="js/bootstrap.bundle.min.js"></script>
  <script src="js/EnviarToken.js"></script>
</body>
</html>
