<%-- 
    Document   : FacturaElectronica
    Created on : 28/11/2025, 9:32:15 p. m.
    Author     : ORLANDUVALIE TABARES GUTIERREZ
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-compatible" content="IE-edge">
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Factura Electrónica MAXI-LIMPIEZA</title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/facturaelectronica.css">
</head>
<body>
    <!-- Encabezado con branding, datos de factura y logo -->
    <header class="bg-light py-3">
        <div class="container-fluid d-flex align-items-top">
            <!-- Branding -->
            <div style="border-radius: 5px; background-color: #0093DD; padding: 0.5rem 1rem; text-align: center; display: inline-block; margin-right: 1rem;">
                <div class="fw-bold" style="font-size: calc(1rem + 0.5vw); color: #333652;">MAXI-LIMPIEZA</div>
                <div class="fst-italic" style="font-size: 16px; color: #333652;">Calidad que se Siente</div>
            </div>
            <!-- Datos de factura -->
            <div class="mt-2 invoice-details">
                <strong>Factura Electrónica</strong><br>
                Nro. Documento/Factura: <span id="nroFactura">${venta.nroDocumentoFactura}</span><br>
                FECHA DE EMISIÓN: ${fechaEmision} ${horaEmision} Hrs<br>
                FECHA DE VALIDACIÓN: ${fechaEmision} ${horaEmision} Hrs<br>
                FECHA DE VENCIMIENTO: ${fechaVencimiento} ${horaVencimiento} Hrs
            </div>
            <!-- Logo y QR -->
            <div class="d-flex align-items-top justify-content-end" style="flex-grow: 1;">
                <img src="img/empresa (2).png" alt="Logo empresa" class="rounded-circle border border-secondary" style="width: 100px; height: 100px; margin-right: 1rem;">
                <div class="qr-code" id="qrcode-container">
                    <img src="${venta.qrCodeUrl}" alt="QR" style="width:100px;height:100px;">
                </div>
            </div>
        </div>
        <!-- Resoluciones legales -->
        <div class="container-fluid bg-secondary text-white py-1 mt-2 d-flex justify-content-between">
            <p class="footer-info mb-0 d-flex justify-content-between w-100">
                <span>Resolución Mercantil Nro. ${empresa.resolucion_mercantil}</span>
                <span>Fecha de Registro: ${empresa.fecha_registro_res}</span>
                <span>Fecha de Vencimiento: ${empresa.fecha_vencimiento_res}</span>
            </p>
        </div>
    </header>

    <!-- Contenido principal -->
    <main class="container-fluid my-1">
        <div class="row">
            <!-- Datos del emisor -->
            <div class="col-md-6 mb-3">
                <div class="card">
                    <div class="card-header bg-light">
                        <strong>Datos de Emisor</strong>
                    </div>
                    <div class="card-body">
                        <p><strong>Razón Social:</strong> ${empresa.razon_social}</p>
                        <p><strong>CC/NIT:</strong> ${empresa.cc_nit}</p>
                        <p><strong>Actividad Económica:</strong> ${empresa.actividad_economica}</p>
                        <p><strong>Responsabilidad:</strong> ${empresa.responsabilidad_iva}</p>
                        <p><strong>Dirección:</strong> ${empresa.direccion}</p>
                        <p><strong>Teléfono:</strong> ${empresa.telefono}</p>
                        <p><strong>E-mail:</strong> ${empresa.email}</p>
                        <p class="mt-2"><strong>Nota:</strong> Regimen Simplificado</p>
                    </div>
                </div>
            </div>
            <!-- Datos del cliente -->
            <div class="col-md-6 mb-3">
                <div class="card">
                    <div class="card-header bg-light">
                        <strong>Datos del Cliente</strong>
                    </div>
                    <div class="card-body">
                        <p><strong>Razón Social:</strong> ${cliente.razon_social}</p>
                        <p><strong>CC/NIT:</strong> ${cliente.documento_NIT}</p>
                        <p><strong>Actividad Económica:</strong> ${cliente.actividad_economica}</p>
                        <p><strong>Responsabilidad:</strong>${cliente.responsabilidad_iva}</p>
                        <p><strong>Dirección:</strong> ${cliente.direccion}</p>
                        <p><strong>Teléfono:</strong> ${cliente.telefono}</p>
                        <p><strong>E-mail:</strong> <span id="emailCliente">${cliente.email}</span></p>
                        <p class="mt-2"><strong>Nota:</strong>${cliente.estado}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabla de detalle de factura -->
        <div class="table-responsive mb-3">
            <table class="table table-bordered">
                <thead class="table-light">
                    <tr>
                        <th>Item</th>
                        <th>Código/SKU</th>
                        <th>Cant</th>
                        <th>U.M</th>
                        <th>Descripción</th>
                        <th>Valor Unitario</th>
                        <th>V = IVA %</th>
                        <th>Impuesto</th>
                        <th>Desct %</th>
                        <th>Valor Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${detalle}" varStatus="i">
                        <tr>
                            <td>${i.index + 1}</td>
                            <td>${d.sku}</td>
                            <td>${d.cantidad}</td>
                            <td>UND</td>
                            <td>${d.nombreProducto}</td>
                            <td>$${d.precioUnitario}</td>
                            <td>N/A</td>
                            <td>N/A</td>
                            <td>${d.descuento_porcentaje}%</td>
                            <td>$${d.totalConDescuento}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        <!-- Totales de unidades y referencias -->
        <hr class="mt-2 mb-1" style="border-top: 1px dashed black;">
        <p><strong>Total Unidades en la compra:</strong> ${totalUnidades} 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>Total Referencias Selecionadas:</strong> ${totalReferencias}</p>
        <hr class="mt-1 mb-1" style="border-top: 1px dashed black;">

        </div>

        <div class="row">
            <!-- Impuestos -->
            <div class="col-md-6 mb-3">
                <div class="card">
                    <div class="card-header bg-light">
                        <strong>Impuestos</strong>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Tipo</th>
                                        <th>Monto Base</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>${empresa.responsabilidad_iva}</td>
                                        <td>${empresa.responsabilidad_iva}</td>
                                        <td>${empresa.responsabilidad_iva}</td>
                                    </tr>
                                </tbody>
                            </table>
                <p class="mt-2 mb-0" style="font-size: 0.85em; color: #555;">
                No somos responsables de IVA – empresa bajo régimen simplificado, según Artículo 437 3° del Estatuto Tributario
                        </div>
                    </div>
                </div>
            </div>
            <!-- Valores totales -->
            <div class="col-md-6 mb-3">
                <div class="card">
                    <div class="card-header bg-light">
                        <strong>Valores Totales</strong>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-sm">
                                <tbody>
                                    <tr>
                                        <th scope="row">SUBTOTAL:</th>
                                        <td>$${venta.subtotalVenta}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">BASE DISPONIBLE:</th>
                                        <td>$${venta.subtotalVenta}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">DESCUENTO:</th>
                                        <td>$${venta.descuentoVenta}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">IVA:</th>
                                        <td>${empresa.responsabilidad_iva}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row">TOTAL:</th>
                                        <td><strong class="total-amount">$${venta.totalVenta}</strong></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Medios de pago -->
    <div class="container-fluid bg-secondary text-white text-center py-1 mt-2">
        <strong>Medios de Pago</strong>
    </div>
    <div class="container-fluid mt-2">
        <div class="table-responsive">
            
            <!-- Tabla de métodos de pago-->
            <div class="table-responsive">
                <table class="table table-bordered table-sm">
                    <thead>
                        <tr class="table-light">
                            <th>Metodo de Pago</th>
                            <th>Fecha</th>
                            <th>Recibido</th>
                            <th>Cambio</th>
                            <th>Referencia</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${transaccion.metodoPago}</td>
                        <td>${transaccion.fecha}</td>
                        <td>$${transaccion.recibido}</td>
                        <td>$${transaccion.cambio}</td>
                        <td>${transaccion.referencia}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Nota adicional -->
    <div class="container-fluid bg-secondary text-white py-1 mt-2 d-flex justify-content-between">
        <p class="footer-info mb-0 d-flex justify-content-center w-100">
            <span>Nota</span>
        </p>
    </div>

    <!-- Información de pagos y contactos -->
    <div class="container-fluid mt-2">
        <p class="mb-1"><strong>Responsable IVA:</strong> ${empresa.responsabilidad_iva}</p>
        <p class="mb-1"><strong>EFECTUAR SUS PAGOS A:</strong></p>
        <p class="mb-1">Banco Caja Social: cuenta de ahorros Nro. 000000000</p>
        <p class="mb-1">Bancolombia: Cuenta de ahorros Nro. 0000000</p>
        <p class="mb-1">Cuenta Nequi: 1234567890</p>
        <p class="mb-1"><strong>Girar Cheque a nombre de:</strong> ${empresa.razon_social}</p>
        <p class="mb-1">Enviar consignacion a ${empresa.email}</p>
        <p class="mb-1">Tomar contacto a ${empresa.telefono}</p>
        <p class="mb-0">correo para atención al cliente: ${empresa.email}</p>
    </div>

    <!-- Botones de acción -->
    <div class="buttons-container mt-4 gap-2 d-flex justify-content-center">
        <button class="btn btn-outline-primary" onclick="location.href='VenderServlet'">Continuar</button>
        <button class="btn btn-outline-secondary btn-sm" onclick="window.print()">Imprimir</button>
       <!-- <button class="btn btn-outline-success btn-sm">Enviar</button> -->
    </div>

    <!-- Representación CUFE -->
    <hr class="mt-2">    
    <div class="col mb-1">
        <figure class="text-center">        
            <figcaption class="blockquote-footer">
                <p>CUFE N/A</p>
                <p>[Representación impresa de Factura Electrónica de venta]</p>
            </figcaption>
        </figure>
    </div>

    <hr>
    <!-- Footer -->
    <footer class="bg-light py-3 mt-1"> 
        <div class="container-fluid d-flex justify-content-between flex-wrap">
            <small>© 2025</small>
            <small>Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</small>
        </div>
    </footer>

    <!-- Scripts de Bootstrap y archivo JS propio -->
    <script src="js/bootstrap.bundle.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="js/facturaelectronica.js"></script>
</body>
</html>
