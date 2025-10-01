
<%-- 
    Página: bienvenida.jsp
    Propósito: Muestra al usuario un mensaje de ingreso exitoso
               con recomendaciones de seguridad en el uso del sistema.

    Buenas prácticas:
    - Informar al usuario sobre el cierre automático por inactividad.
    - Fomentar el cierre de sesión manual al terminar la jornada.
    - Separar lógica de negocio del diseño (MVC).
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Bienvenida</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="alert alert-success text-center">
            <h4 class="alert-heading">¡Has ingresado con éxito!</h4>
            <hr>
            <p class="mb-1">
                ✅ Recuerda seguir las <strong>buenas prácticas de seguridad</strong> en la información que se almacena, de acuerdo a tu rol.
            </p>
            <p class="mb-1">
                🔒 No olvides <strong>cerrar sesión</strong> al finalizar tu jornada laboral.
            </p>
            <p class="mb-0">
                ⏱️ Por seguridad, el sistema se cerrará automáticamente tras <strong>10 minutos de inactividad</strong>.
            </p>
        </div>

        <div class="text-center mt-4">
            <a href="index.jsp" class="btn btn-primary">Aceptar</a>
            <a href="logoutServlet" class="btn btn-secondary">Rechazar</a>
        </div>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>