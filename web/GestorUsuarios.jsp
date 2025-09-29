<%-- 
    Document   : GestorUsuarios
    Created on : 10/09/2025, 02:26:31 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Usuarios</title>
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
  <link rel="stylesheet" href="css/usuarios.css">
</head>
<body>
  <!-- HEADER -->
    <header class="bg-light py-3">
        <div class="container">
            <!-- Ícono y título principal -->
                <div class="row align-items-center mb-2">
                  <div class="col-12 d-flex align-items-center justify-content-start flex-wrap gap-2">
                    <i class="bi bi-person-lines-fill fs-1 text-info"></i>
                    <h4 class="mb-0">Usuarios</h4>
                  </div>
                </div>
          
                <!-- Nombre empresa + Barra de búsqueda -->
                <div class="row align-items-center">
                  <!-- Nombre empresa -->
                  <div class="col-12 col-md-6 mb-2 mb-md-0">
                    <div class="nombre-empresa">
                      <div class="nombre-empresa-title">MAXI-LIMPIEZA</div>
                      <div class="nombre-empresa-slogan">Calidad que se siente</div>
                    </div>
                  </div>
          
                <!-- Barra de búsqueda -->
                <div class="col-12 col-md-6 d-flex justify-content-start justify-content-md-end">
                    <form id="formBusqueda" class="input-group w-100 w-md-auto">
                        <input 
                            type="text" 
                            id="busquedaUsuario" 
                            name="busqueda" 
                            class="form-control" 
                            placeholder="Buscar por nombre, documento o usuario"
                        />
                        <input type="hidden" name="accion" value="buscar" />
                        <button type="submit" class="btn btn-outline-secondary">
                            <i class="bi bi-search"></i>
                        </button>
                    </form>
                </div>
                  
            </div>
        </div>
    </header>

  <hr class="mt-0" />

    <!-- MAIN -->
    <main class="container my-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h4>Lista Usuarios</h4>
          <button class="btn btn-outline-primary" onclick="abrirModalUsuario()">+ Agregar Usuario</button>
        </div>
    
        <div class="table-responsive justify-content">
            <table class="table table-bordered table-striped">
                <thead class="table-info">
                  <tr>
                    <th>ITEM</th>
                    <th>NOMBRES Y APELLIDOS</th>
                    <th>DOCUMENTO</th>
                    <th>TELÉFONO</th>
                    <th>CORREO ELECTRÓNICO</th>
                    <th>USUARIO</th>
                    <th>CARGO</th>
                    <th>ROL</th>
                    <th>ESTADO</th>
                    <th>ACCIONES</th>
                  </tr>
                </thead>
              <tbody id="tablaUsuarios" class="table-group-divider"></tbody>
            </table>
        </div>
    </main>

  <!-- BOTONES INFERIORES -->
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

  <!-- MODAL Usuario -->
    <div class="modal fade" id="modalUsuarios" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalUsuarioLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="modalUsuarioLabel">Agregar / Editar Usuario</h5>
                  <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
    
                <div class="modal-body">
                    <form id="formUsuario" method="post" action="UsuariosServlet" novalidate>
                            <div class="row mb-3">
                              <div class="col-md-1">
                                <label class="form-label">Ítem</label>
                                <input 
                                    type="text" 
                                    id="item" 
                                    name="item" 
                                    class="form-control" 
                                    readonly placeholder="Ítem" 
                                />
                              </div>
                                <div class="col-md-5">
                                   <label for="nombre" class="form-label">Nombres y Apellidos</label>
                                   <input 
                                    type="text" 
                                    id="nombre" 
                                    name="nombre" 
                                    class="form-control" 
                                    required placeholder="Ingrese nombres y apellidos" 
                                    />
                                </div>
                                <div class="col-md-6">
                                   <label for="documento" class="form-label">Documento</label>
                                   <input 
                                     type="text" 
                                     id="documento" 
                                     name="documento"
                                     class="form-control" 
                                     oninput="this.value=this.value.replace(/[^0-9]/g,'')" 
                                     required 
                                     placeholder="Ingrese documento (10 dígitos)" 
                                     maxlength="10" 
                                   />
                                   <div class="invalid-feedback" id="documentoFeedback"></div>
                                </div>
                            </div>
                             
                            <div class="row mb-3">
                                <div class="col-md-4">
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
                                <div class="col-md-4">
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
                                 <div class="col-md-4">
                                   <label for="usuario" class="form-label">Usuario</label>
                                   <input 
                                       type="text" 
                                       id="nombreUsuario" 
                                       name="nombreUsuario" 
                                       class="form-control" 
                                       required 
                                       placeholder="Ingrese nombre de usuario" />
                                   <div class="invalid-feedback" id="usuarioFeedback"></div>
                                 </div>
                            </div>
                
                        <div class="row mb-3">
                                <div class="col-md-6">
                                  <label for="contrasena" class="form-label">Contraseña</label>
                                  <input type="password" id="contrasena" name="contrasena" class="form-control" required placeholder="Ingrese contraseña" />
                                  <div class="invalid-feedback" id="contrasenaFeedback"></div>
                                </div>
                
                            <div class="col-12 col-md-3">
                                  <label for="rol" class="form-label">Rol</label>
                                <div class="dropdown w-">
                                    <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" id="dropdownRol" data-bs-toggle="dropdown" aria-expanded="false">
                                      Seleccionar Rol
                                    </button>
                                    <ul class="dropdown-menu w-100" aria-labelledby="dropdownRol">
                                      <li><a class="dropdown-item" role="button" data-value="1">Administrador</a></li>
                                      <li><a class="dropdown-item" role="button" data-value="2">Ventas</a></li>
                                      <li><a class="dropdown-item" role="button" data-value="3">Logística (Bodega)</a></li>
                                      <li><a class="dropdown-item" role="button" data-value="4">Contabilidad</a></li>
                                    </ul>
                                </div>
                                  <div class="invalid-feedback" id="rolFeedback">Por favor, seleccione un rol.</div>
                                  <input type="hidden" id="rolInput" name="idRol">
                            </div>
                            
                             <div class="col-12 col-md-3">
                                <label for="cargo" class="form-label">Cargo</label>
                                <div class="dropdown w-100">
                                  <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" id="dropdownCargo" data-bs-toggle="dropdown" aria-expanded="false">
                                    Seleccionar Cargo
                                  </button>
                                  <ul class="dropdown-menu w-100" aria-labelledby="dropdownCargo">
                                    <li><a class="dropdown-item" role="button" data-value="Administrador">Administrador</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Vendedor">Vendedor</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Logistica">Logística (Bodega)</a></li>
                                    <li><a class="dropdown-item" role="button" data-value="Contabilidad">Contabilidad</a></li>
                                  </ul>
                                </div>
                                <div class="invalid-feedback" id="cargoFeedback">Por favor, seleccione un cargo.</div>
                                <input type="hidden" id="cargoInput" name="cargo">
                              </div>
                            
                            <div class="col-12 col-md-3">
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
                                <input type="hidden" id="estadoInput" name="estado">
                            </div>
                        </div>
                           <input type="hidden" name="accion" id="accionInput" value="agregar" />
                           <input type="hidden" id="usuarioId" name="usuarioId" />
                           <div class="modal-footer">
                             <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                             <button type="button" class="btn btn-outline-primary" id="btnGuardarUsuario">Guardar</button>
                           </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
  <!-- Scripts de Bootstrap y archivo JS propio -->
  <script src="js/bootstrap.bundle.min.js"></script>
  <script src="js/usuarios.js"></script>
</body>
</html>