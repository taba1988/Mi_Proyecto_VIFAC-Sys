# Backend_Mi_Proyecto_VIFAC-Sys

## Descripción
Repositorio del **backend dinámico** del proyecto **VIFAC-Sys**.  
Desarrollado en **Java con JSP y Servlets**, adaptando los archivos HTML, CSS y JS del frontend para funcionamiento dinámico con conexión a **base de datos centralizada**.

## Funcionalidades principales
- Gestión de **Usuarios**, **Clientes**, **Proveedores**, **Inventario** y **Ventas**.
- Formularios dinámicos utilizando **GET** y **POST**.
- Validaciones de datos y manejo de errores con **mensajes en la consola**.
- Reutilización de CSS y JS del frontend, integrando **Bootstrap**.
- Generación de reportes y visualización de datos en tiempo real.

## Tecnologías y librerías
- **IDE:** NetBeans 8.2  
- **Servidor:** Apache Tomcat  
- **Base de datos:** MySQL (centralizada), gestionada con **XAMPP y phpMyAdmin**  
- **Usuario DB:** `root` con contraseña definida en la configuración  
- **Librerías:** JAR externas necesarias, manejo de **JSON**  
- **Frontend:** HTML, CSS, JS adaptados, Bootstrap  

## Contenido del proyecto
- `src/` → Servlets y clases Java  
- `Web Pages/` → JSP y HTML adaptados  
  - `css/` → estilos (Bootstrap y personalizados)  
  - `js/` → scripts adaptados/dinámicos  
  - `images/` → imágenes del sistema  
- `lib/` → librerías externas (JARs)  
- `script_db.sql` → script de la base de datos  
- `README.md` → descripción del proyecto  

## Pruebas y validaciones
- Módulos codificados y probados en **Tomcat**.  
- Conexión a la **base de datos centralizada** verificada usando **XAMPP y phpMyAdmin**.  
- Usuario de base de datos: `root` con contraseña configurada en `config` de Servlets.  
- Validaciones de formularios y errores visibles en la **consola de NetBeans**.  
- Pruebas de carga y consistencia de datos realizadas.

## Cómo usar
1. Abrir **XAMPP** y activar **Apache** y **MySQL**.  
2. Abrir **phpMyAdmin** y crear la base de datos usando `script_db.sql`.  
3. Configurar el usuario y contraseña (`root`) en los Servlets para la conexión a MySQL.  
4. Importar el proyecto en **NetBeans 8.2**.  
5. Ejecutar el proyecto en **Tomcat**.  
6. Acceder al sistema desde un navegador web.
