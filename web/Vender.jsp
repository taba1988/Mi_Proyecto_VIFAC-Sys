<%-- 
    Document   : Vender
    Created on : 11/09/2025, 06:51:46 PM
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-compatible" content="IE-edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Vender</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" />
    <link rel="stylesheet" href="css/Vender.css" />
</head>
<body class="d-flex flex-column min-vh-100 mt-2">

<header class="bg-white px-3 my-3 mt-1">
    <div class="container-fluid d-flex flex-wrap justify-content-between align-items-center gap-3">

        <div class="d-flex align-items-center">
            <div class="logo-container">
                <div class="logo-name fw-bold">MAXI-LIMPIEZA</div>
                <div class="logo-slogan fst-italic">Calidad que se siente</div>
            </div>

            <div class="d-flex align-items-center gap-2">
              <img src="img/register.png" alt="vender" class="img-vender">
              <h4 class="mb-0">Vender</h4>
            </div>
        </div>
        
        <div class="d-flex align-items-center gap-2">
    <form class="input-group flex-grow-1" id="formBuscar">
        <input type="text" class="form-control" placeholder="Buscar producto..." id="campoBuscarDesktop">
        <button class="btn btn-outline-primary" type="submit">
            <i class="bi bi-search"></i>
        </button>
    </form>

    <div class="d-flex gap-2 flex-shrink-0">
        <button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#modalAbrirCaja" id="btnAbrirCaja">Abrir Caja</button>
        <button class="btn btn-outline-danger" data-bs-toggle="modal" data-bs-target="#modalCerrarCaja" id="btnCerrarCaja">Cerrar Caja</button>
    </div>
</div>

    </div>
</header>

<div class="d-flex justify-content-end">
  <button class="btn btn-outline d-lg-none me-2" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasCategorias" aria-controls="offcanvasCategorias">
    <i class="bi bi-list fs-3"></i>
  </button>
</div>

<main class="container-fluid flex-grow-1 mt-0 mb-3 bg-light">
    <div class="row flex-column-reverse flex-lg-row">

        <aside class="col-lg-2 mb-3 d-none d-lg-block" aria-label="Barra lateral de categorías">
            <nav class="border rounded p-3 h-100">
                <h5 class="fw-bold mb-3">Categorías</h5>
                    <ul class="categoria-scroll list-unstyled" id="listaCategoriasDesktop">
                        <li role="button" data-categoria="Jabón líquido">Jabón líquido</li>
                        <li role="button" data-categoria="Ambientadores">Ambientadores</li>
                        <li role="button" data-categoria="Esencias">Esencias</li>
                        <li role="button" data-categoria="Elementos de aseo">Elementos de aseo</li>
                        <li role="button" data-categoria="Protección personal">Protección personal</li>
                        <li role="button" data-categoria="Kits">Kits</li>
                        <li role="button" data-categoria="Limpieza">Limpieza</li>
                        <li role="button" data-categoria="Higiénicos">Higiénicos</li>
                    </ul>
                <div class="d-flex justify-content-center align-items-center gap-5 mt-5">
                   <a href="index.jsp" class="menu-link d-flex flex-column align-items-center text-decoration-none text-dark icon-hover-text">
                        <i class="bi bi-house-door-fill fs-4"></i>
                        <span class="menu-text-hover">Inicio</span>
                   </a>
                   <a href="logoutServlet" class="menu-link d-flex flex-column align-items-center text-decoration-none text-dark icon-hover-text">
                        <i class="bi bi-box-arrow-right fs-4"></i>
                        <span class="menu-text-hover">Cerrar Sesión</span>
                   </a>
                </div>
            </nav>
        </aside>

        <section class="col-lg-6 mb-3" id="productos" aria-label="Listado de productos">
            <h5><i class="bi bi-box-seam me-2 fs-1"></i>Productos en Stock</h5>
            <div id="contenedorCards" class="mt-3"></div>
        </section>

        <aside class="col-lg-4 mb-3" aria-label="Carrito de compras">
        <div class="card h-100">

            <div class="d-flex align-items-center p-2 border-bottom justify-content-between">
                <div class="d-flex align-items-center gap-2">
                    <span class="fw-bold me-2">Lectura:</span>
                    <button class="btn btn-light btn-sm border-0 p-0 lectura-btn" title="Código de barras" data-metodo-lectura="codigo">
                        <i class="bi bi-upc fs-5"></i>
                    </button>
                    <button class="btn btn-light btn-sm border-0 p-0 lectura-btn" title="Teclado" data-metodo-lectura="teclado">
                        <i class="bi bi-keyboard fs-5"></i>
                    </button>
                </div>
                <div class="mb-0">
                    <div class="input-group">
                        <button class="btn btn-outline-secondary btn-sm" type="button" data-bs-toggle="modal" data-bs-target="#modalSeleccionarCliente">
                            <i class="bi bi-person-plus-fill"></i> Seleccionar Cliente
                        </button>
                    </div>
                </div>
            </div>
            
            <div class="card-header bg-primary text-white fw-bold d-flex justify-content-between align-items-start">
                Carrito 
              <button class="btn btn-sm btn-light mt-0" style="z-index: 1;" id="btnLimpiarCarrito">
               <i class="bi bi-trash"></i> Limpiar Carrito
              </button>
            </div>
              
            <div class="card-body overflow-auto carrito-body">
                <ul id="carritoLista" class="list-unstyled"></ul>
            </div>

                <div class="d-flex justify-content-around mb-2" id="metodosPago">
                    <button class="btn btn-light btn-sm border-0 metodo-pago-btn metodo-pago-btn-size" 
                            data-metodo="Efectivo" 
                            title="Efectivo">
                        <i class="bi bi-wallet2 fs-5"></i>
                    </button>

                    <button class="btn btn-light btn-sm border-0 metodo-pago-btn metodo-pago-btn-size" 
                            data-metodo="Crédito" 
                            title="Crédito">
                        <i class="bi bi-credit-card fs-5"></i>
                    </button>

                    <button class="btn btn-light btn-sm border-0 metodo-pago-btn metodo-pago-btn-size" 
                            data-metodo="QR" 
                            title="QR">
                        <i class="bi bi-qr-code-scan fs-5"></i>
                    </button>
                
                    <button class="btn btn-light btn-sm border-0 metodo-pago-btn metodo-pago-btn-size" 
                            data-metodo="Tarjeta" 
                            title="Tarjeta">
                        <i class="bi bi-credit-card-2-front fs-5"></i>
                    </button>

                    <button class="btn btn-light btn-sm border-0 metodo-pago-btn-size" 
                            data-bs-toggle="modal" 
                            data-bs-target="#modalDescuento" 
                            title="Descuento" 
                            id="btnDescuento">
                        <i class="bi bi-percent fs-5"></i>
                    </button>
                </div>
            
                <form action="VenderServlet" method="POST" id="formVenta">
                  <input type="hidden" name="accion" value="vender">
                  <input type="hidden" name="clienteSeleccionado" id="clienteSeleccionado">
                  <input type="hidden" name="productosData" id="productosData">
                  <input type="hidden" name="totalVenta" id="totalVentaHidden">
                
                  <!-- Botón de enviar el formulario -->
                  <button type="submit" class="btn btn-success w-100 text-start p-2 btn-venta" id="btnFinalizarVenta">
                    <div class="d-flex justify-content-between align-items-center">
                      <div class="fs-4">COBRAR</div>
                      <div id="productosCantidad">00 productos</div>
                    </div>
                    <div class="d-flex justify-content-between align-items-center btn-venta-total">
                      <div id="promoTexto">Promoción aplicada <span id="descuentoAplicadoTexto"></span></div>
                      <div id="totalVenta" class="fs-2">$0</div>
                    </div>
                    <div id="clienteNombre">
                      <i class="bi bi-person"></i> Nombre del cliente
                    </div>
                  </button>
                </form>
            <div class="d-flex justify-content-between align-items-center mt-0 info-venta-bottom">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="toggleTicket">
                    <label class="form-check-label" for="toggleTicket">Ticket</label>
                </div>
                <div id="metodoPagoSeleccionado"></div>
                <button id="btnCalculadora" class="btn btn-light btn-sm p-0" title="Calculadora">
                    <i class="bi bi-calculator fs-6"></i>
                </button>
            </div>
          </div>
      </aside>
    </div>
</main>

<footer>
    <div class="container d-flex justify-content-between flex-wrap">
      <small>© 2025</small>
      <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
    </div>
  </footer>
  
<div class="modal fade" id="modalAbrirCaja" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalAbrirCajaLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Abrir Caja</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="montoInicialCaja" class="form-label">Monto Inicial en Caja:</label>
                    <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input type="number" class="form-control" id="montoInicialCaja" placeholder="Ingrese el monto inicial" step="0.01">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-outline-success" id="btnConfirmarAbrirCaja">Confirmar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalCerrarCaja" data-bs-backdrop="static"data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalCerrarCajaLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cerrar Caja</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="montoFinalCaja" class="form-label">Monto Final en Caja:</label>
                    <div class="input-group">
                        <span class="input-group-text">$</span>
                        <input type="number" class="form-control" id="montoFinalCaja" placeholder="Ingrese el monto final" step="0.01">
                    </div>
                </div>
                <div class="mb-3">
                    <label for="observacionesCierre" class="form-label">Observaciones (Opcional):</label>
                    <textarea class="form-control" id="observacionesCierre" rows="3"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-outline-danger" id="btnConfirmarCerrarCaja">Confirmar</button>
            </div>
        </div>
    </div>
</div>
    
    //boton descuento % venta general

<div class="modal fade" id="modalDescuento" data-bs-backdrop="static"data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalDescuentoLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalDescuentoLabel">Aplicar Descuento</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="inputDescuento" class="form-label">Porcentaje de Descuento:</label>
                    <div class="input-group">
                        <input type="number" class="form-control" id="inputDescuento" min="0" max="100" placeholder="Ingrese el porcentaje">
                        <span class="input-group-text">%</span>
                    </div>
                </div>
                <div class="alert alert-info mt-3" id="mensajeDescuento" style="display: none;">
                    Descuento aplicado: <span id="valorDescuentoPorcentaje"></span> (<span id="valorDescuentoMoneda"></span>)
                    <button type="button" class="btn-close btn-sm float-end" aria-label="Eliminar Descuento" id="btnEliminarDescuento"></button>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-outline-primary" id="btnAplicarDescuento" data-bs-dismiss="modal">Aceptar</button>
            </div>
        </div>
    </div>
</div>
       
    //Boton descuento producto especifico

<div class="modal fade" id="modalDescuentoProducto" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalDescuentoProductoLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalDescuentoProductoLabel">Aplicar Descuento al Producto</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body">
        <div class="mb-3">
          <label for="inputDescuentoProducto" class="form-label">Porcentaje de Descuento:</label>
          <div class="input-group">
            <input type="number" class="form-control" id="inputDescuentoProducto" min="0" max="100" placeholder="Ingrese el porcentaje">
            <span class="input-group-text">%</span>
          </div>
        </div>
        <div class="alert alert-info mt-3" id="mensajeDescuentoProducto" style="display: none;">
          Descuento aplicado: <span id="valorDescuentoPorcentajeProducto"></span> (<span id="valorDescuentoMonedaProducto"></span>)
          <button type="button" class="btn-close btn-sm float-end" aria-label="Eliminar Descuento" id="btnEliminarDescuentoProducto"></button>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
        <button type="button" class="btn btn-outline-primary" id="btnAplicarDescuentoProducto" data-bs-dismiss="modal">Aceptar</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="modalCobrar" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalCobrarLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="modalCobrarLabel">Finalizar Venta</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body" data-bs-backdrop="static"data-bs-keyboard="false">
                
                <div id="pagoEfectivo" style="display:none;">
                    <div class="mb-3">
                        <label for="totalAPagarEfectivo" class="form-label fw-bold">Total a Pagar:</label>
                        <div class="input-group">
                            <span class="input-group-text">$</span>
                            <input type="text" class="form-control" id="totalAPagarEfectivo" value="0.00" readonly>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="efectivoRecibido" class="form-label fw-bold">Efectivo Recibido:</label>
                        <div class="input-group">
                            <span class="input-group-text">$</span>
                            <input type="number" class="form-control" id="efectivoRecibido" min="0" step="0.01">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="cambioDevolver" class="form-label fw-bold">Cambio a Devolver:</label>
                        <div class="input-group">
                            <span class="input-group-text">$</span>
                            <input type="text" class="form-control" id="cambioDevolver" value="0.00" readonly>
                        </div>
                    </div>
                    </div>
                <div id="pagoTarjeta" style="display:none;">
                    <p class="alert alert-info"><i class="bi bi-credit-card me-2"></i> Procesando pago con datáfono...</p>
                </div>
                <div id="pagoQR" style="display:none;">
                    <p class="alert alert-info"><i class="bi bi-qr-code-scan me-2"></i> Procesando pago con datáfono (Código QR)...</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-outline-primary" id="btnPagar">Pagar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalSeleccionarCliente" data-bs-backdrop="static"data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalSeleccionarClienteLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="modalSeleccionarClienteLabel">Buscar Cliente por Cédula</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <div class="mb-3">
                    <label for="cedulaCliente" class="form-label fw-bold">Cédula del Cliente:</label>
                    <input type="text" class="form-control" id="cedulaCliente" placeholder="Ingrese la cédula">
                </div>
                <div id="resultadoBusquedaCliente" style="display: none;">
                    <p class="mt-2 fw-bold">clientes:</p>
                    <p id="nombreClienteEncontrado"></p>
                </div>
                <div class="mt-3 d-flex justify-content-end gap-2">
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-outline-success" id="btnAceptarCliente" disabled>Aceptar</button>
                </div>
            </div>
            <div class="modal-footer">
                <a href="Clientes.jsp" target="_blank" class="btn btn-info btn-sm">
                    <i class="bi bi-person-plus-fill"></i> Agregar/Editar Cliente
                </a>
            </div>
        </div>
    </div>
</div>

<div class="offcanvas offcanvas-end" tabindex="-1" id="offcanvasCategorias" aria-labelledby="offcanvasCategoriasLabel" class="offcanvas-custom">
  <div class="offcanvas-header">
    <h5 class="offcanvas-title" id="offcanvasCategoriasLabel">Categorías</h5>
    <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Cerrar"></button>
  </div>
  <div class="offcanvas-body d-flex flex-column justify-content-between p-0">
  <ul class="list-unstyled p-3 mb-3 flex-grow-1 categoria-scroll" id="listaCategoriasOffcanvas">
      <li role="button" data-categoria="Jabón líquido" data-bs-dismiss="offcanvas">Jabón líquido</li>
      <li role="button" data-categoria="Ambientadores" data-bs-dismiss="offcanvas">Ambientadores</li>
        <li role="button" data-categoria="Esencias" data-bs-dismiss="offcanvas">Esencias</li>
      <li role="button" data-categoria="Elementos de aseo" data-bs-dismiss="offcanvas">Elementos de aseo</li>
      <li role="button" data-categoria="Protección personal" data-bs-dismiss="offcanvas">Protección personal</li>
      <li role="button" data-categoria="Kits" data-bs-dismiss="offcanvas">Kits</li>
      <li role="button" data-categoria="Limpieza" data-bs-dismiss="offcanvas">Limpieza</li>
      <li role="button" data-categoria="Higiénicos" data-bs-dismiss="offcanvas">Higiénicos</li>
  </ul>

<div class="d-flex justify-content-center align-items-center gap-5 position-relative offcanvas-menu">
    <a href="index.jsp" class="d-flex flex-column align-items-center text-decoration-none text-dark">
        <i class="bi bi-house-door-fill fs-1"></i>
        <span>Inicio</span>
    </a>
    <a href="logoutServlet" class="d-flex flex-column align-items-center text-decoration-none text-dark">
        <i class="bi bi-box-arrow-right fs-1"></i>
        <span>Cerrar Sesión</span>
    </a>
</div>
</div>
</div>

<div id="calculadoraContainer" class="calculadora-flotante" style="display:none;">
    <input type="text" id="calcPantalla" class="form-control mb-2 text-end calc-pantalla" readonly>
    <div class="d-grid gap-1 calculadora-grid">
        <button data-calc-value="7">7</button>
        <button data-calc-value="8">8</button>
        <button data-calc-value="9">9</button>
        <button data-calc-value="/">/</button>

        <button data-calc-value="4">4</button>
        <button data-calc-value="5">5</button>
        <button data-calc-value="6">6</button>
        <button data-calc-value="*">*</button>
        <button data-calc-value="1">1</button>
        <button data-calc-value="2">2</button>
        <button data-calc-value="3">3</button>
        <button data-calc-value="-">-</button>

        <button data-calc-value="0">0</button>
        <button data-calc-value=".">.</button>
        <button data-calc-equals="true">=</button>
        <button data-calc-value="+">+</button>

        <button data-calc-clear="true" class="calc-clear-btn">C</button>
    </div>
    <button class="btn btn-sm btn-secondary mt-2 w-100" id="btnCerrarCalculadora">Cerrar</button>
</div>

<script src="js/bootstrap.bundle.min.js"></script>
<script>
    window.idUsuarioSesion = <%= session.getAttribute("idUsuario") %>;
</script>
<script src="js/Vender.js"></script>

</body>
</html>