<%-- 
    Document   : Inventario
    Created on : 11/09/2025, 06:51:46 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Inventario</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="css/inventario.css">
</head>
<body>
    <header class="bg-light py-3">
        <div class="container d-flex flex-column gap-2">
            <div class="logo-titulo">
                <img src="img/inventarios.png" alt="inventario">
                <h4>Inventario</h4>
            </div>
            <div class="d-flex align-items-end justify-content-between">
                <div class="flex-grow-1 me-4 header-bloque-azul justify-content-between">
                    <div class="titulo-principal">MAXI-LIMPIEZA</div>
                    <div class="subtitulo">Calidad que se siente</div>
                </div>

                <div class="metodo-lectura">
                    <h5 class="mb-0 fw-bold d-none d-md-block">Método de Lectura:</h5>
                    <button id="btnMetodoCodigo" class="btn metodo-btn" title="Código de barras" onclick="seleccionarMetodoLectura('codigo')">
                        <i class="bi bi-upc icono-metodo"></i>
                    </button>
                    <button id="btnMetodoTeclado" class="btn metodo-btn" title="Teclado" onclick="seleccionarMetodoLectura('teclado')">
                        <i class="bi bi-keyboard icono-metodo"></i>
                    </button>
                </div>
            </div>
        </div>
    </header>

    <hr class="mt-0">
    <main class="container my-4">
        

    <div class="d-flex align-items-center mb-3">
           <h4 class="mb-0">Gestión de Productos</h4>

        <div class="input-group ms-auto" style="max-width: 400px;">
            <input type="text" id="busquedaInventario" name="busquedaInventario"
                class="form-control" placeholder="Buscar por nombre o categoría">
            <button class="btn btn-outline-secondary" onclick="buscarProducto()">
                <i class="bi bi-search"></i>
            </button>
        </div>
    </div>

        
        <div class="d-flex justify-content-end align-items-center mb-3">
           
            <button class="btn btn-outline-primary" onclick="abrirModalProducto()">+ Agregar Producto</button>
        </div>

        
        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead class="table-info">
                    <tr>
                        <th>ITEM</th>
                        <th>SKU</th>
                        <th>Producto</th>
                        <th>Categoría</th>
                        <th>Stock</th>
                        <th>Precio Compra</th>
                        <th>Precio Venta</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody id="tablaInventario" class="table-group-divider">
                </tbody>
            </table>
        </div>
    </main>

    <div class="container mb-1 d-flex justify-content-between gap-1">
        <button class="btn btn-outline-secondary" onclick="location.href='index.jsp'">Inicio</button>
        <button class="btn btn-outline-danger" onclick="location.href='logout.jsp'">Cerrar sesión</button>
    </div>
    
    <footer>
        <div class="container d-flex justify-content-between flex-wrap">
            <small>© 2025</small>
            <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
        </div>
    </footer>

    <div 
        class="modal fade" 
        id="modalInventario" 
        data-bs-backdrop="static"
        data-bs-keyboard="false"
        tabindex="-1" 
        aria-labelledby="modalInventarioLabel" 
        aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalInventarioLabel">
                        Agregar / Editar Producto</h5>
                    <button 
                        type="button" 
                        class="btn-close btn-close-white" 
                        data-bs-dismiss="modal" 
                        aria-label="Cerrar">
                    </button>
                </div>
                <div class="modal-body modal-select-visible">
                    <form id="formInventario" method="post" action="InventarioServlet" novalidate>
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
            <label for="nombre" class="form-label">Nombre del Producto</label>
            <input
                type="text"
                id="nombre"
                name="nombre"
                class="form-control"
                required
                placeholder="Nombre del producto"
            />
        </div>
    </div>

    <div class="mb-3">
        <label for="descripcion" class="form-label">Descripción</label>
        <textarea
            id="descripcion"
            name="descripcion"
            class="form-control"
            rows="2"
            placeholder="Descripción del producto"
        ></textarea>
    </div>

    <div class="row mb-3">
        <div class="col-12 col-md-4">
            <label for="sku" class="form-label">SKU</label>
            <input
                type="text"
                id="sku"
                name="sku"
                class="form-control"
                required
                placeholder="Código SKU"
            />
        </div>
        <div class="col-12 col-md-4">
            <label for="precio_compra" class="form-label">Precio Compra</label>
            <input
                type="number"
                id="precio_compra"
                name="precio_compra"
                class="form-control"
                step="0.01"
                required
                placeholder="Precio de compra"
            />
        </div>
        
        <div class="col-12 col-md-4">
             <label for="porcentaje_ganancia" class="form-label">Porcentaje Ganancia (%)</label>
             <input
                 type="number"
                 id="porcentaje_ganancia"
                 name="porcentaje_ganancia"
                 class="form-control"
                 step="0.01"
                 placeholder="Ej: 30"
            />
        </div>
        
        <div class="col-12 col-md-4">
            <label for="precio_venta" class="form-label">Precio Venta</label>
            <input
                type="number"
                id="precio_venta"
                name="precio_venta"
                class="form-control"
                step="0.01"
                required
                placeholder="Precio de venta"
            />
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-12 col-md-6">
            <label for="stock" class="form-label">Stock</label>
            <input
                type="number"
                id="stock"
                name="stock"
                class="form-control"
                required
                placeholder="Cantidad disponible"
            />
        </div>
        <div class="col-12 col-md-6">
            <label for="unidad_medida" class="form-label">Unidad de Medida</label>
            <input
                type="text"
                id="unidad_medida"
                name="unidad_medida"
                class="form-control"
                placeholder="Ej: unidad, kg, litro"
            />
        </div>
    </div>

    <div class="row mb-3">
        <div class="col-12 col-md-6">
            <label for="estado" class="form-label">Estado</label>
            <div class="dropdown w-100">
                <button class="btn btn-outline-secondary dropdown-toggle w-100 text-start" type="button" id="dropdownEstado" data-bs-toggle="dropdown" aria-expanded="false">
                  Seleccionar
                </button>
                <ul class="dropdown-menu w-100" aria-labelledby="dropdownEstado">
                  <li><a class="dropdown-item" role="button" href="#" data-value="Activo">Activo</a></li>
                  <li><a class="dropdown-item" role="button" href="#" data-value="Inactivo">Inactivo</a></li>
                </ul>
            </div>
            <input type="hidden" id="estado" name="estado">
        </div>

        <div class="col-12 col-md-6">
            <label for="idCategoria" class="form-label">ID Categoría</label>
            <input
                type="number"
                id="idCategoria"
                name="idCategoria"
                class="form-control"
                placeholder="ID de la categoría"
            />
        </div>
    </div>

    <!-- ✅ Inputs ocultos SOLO una vez -->
    <input type="hidden" id="accionInput" name="accion" value="agregar">
    <input type="hidden" id="idProducto" name="idProducto">

    <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
        <button type="submit" class="btn btn-outline-primary" id="btnguardarInventario">Guardar</button>
    </div>
</form>


                </div>
            </div>
        </div>
    </div>

    <script src="js/inventario.js"></script>
    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>