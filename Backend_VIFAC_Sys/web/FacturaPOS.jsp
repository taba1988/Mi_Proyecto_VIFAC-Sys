<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comprobante POS MAXI-LIMPIEZA</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/facturaPOS.css">
</head>
<body>
    <div class="receipt">

        <!-- Encabezado -->
        <div class="header">
            <div class="logo">MAXI-LIMPIEZA</div>
            <div class="info fst-italic">Calidad que se Siente</div>
        </div>

        <!-- Información general -->
        <div class="transaction-info">
            <p><strong>Factura:</strong> #${venta.nroDocumentoFactura}</p>
            <p><strong>NIT:</strong> ${empresa.cc_nit}</p>
            <p><strong>Dirección:</strong> ${empresa.direccion}</p>
            <p><strong>Teléfono:</strong> ${empresa.telefono}</p>
            <p><strong>Ciudad:</strong> ${empresa.ciudad}</p>
            <p><strong>Responsable IVA:</strong> ${empresa.responsabilidad_iva}</p>
            <p><strong>Fecha:</strong> ${fechaEmision}</p>
            <p><strong>Hora:</strong> ${horaEmision}</p>
            <hr class="mb-1 mt-1" style="border-top: 1px dashed black;">
            <p><strong>Turno</strong> #${venta.nroDocumentoFactura}</p>
            <p><strong>Caja:</strong> ${venta.numeroCaja}</p>
            <p><strong>Vendedor:</strong> ${vendedor.nombre}</p>
            <p><strong>Cliente:</strong> ${cliente.razon_social}</p>
            <p><strong>Email:</strong> ${cliente.email}</p>
            <p><strong>Teléfono:</strong> ${cliente.telefono}</p>
            <p><strong>Dirección:</strong> ${cliente.direccion}</p>
            <p><strong>Fecha Vence:</strong> ${fechaVencimiento}</p>
            <p><strong>Hora Vence:</strong> ${horaVencimiento}</p>
            <p><strong>Plazo:</strong> 0</p>
            <hr class="mb-1 mt-1" style="border-top: 1px dashed black;">
        </div>

        <!-- Tabla productos -->
        <table>
            <thead>
                <tr style="border-bottom: none;">
                    <th>ITEM</th>
                    <th>SKU</th>
                    <th>PRODUCTO</th>
                    <th style="text-align: center;">CANT x UNIT</th>
                    <th style="text-align: right;">TOTAL</th>
                </tr>
            </thead>
            <tbody style="border-top: 1px solid #eee;">
                <c:forEach var="d" items="${detalle}" varStatus="i">
                    <tr>
                        <td>${i.index + 1}</td>
                        <td>${d.sku}</td>
                        <td>${d.nombreProducto}</td>
                        <td style="text-align: center;">${d.cantidad} x $${d.precioUnitario}</td>
                        <td style="text-align: right;">$${d.totalConDescuento}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Totales -->
        <div class="total-section">
            <div>
                <span>Subtotal:</span>
                <span>$${venta.subtotalVenta}</span>
            </div>
            <div>
                <span>Descuento:</span>
                <span>$${venta.descuentoVenta}</span>
            </div>
                
            <div>
                <span>IVA: <strong>No Aplica</strong></span>
                <span></span>
            </div>
           
            <div>
                <span class="total-label"><strong>TOTAL:</strong></span>
                <span class="total-amount"><strong>$${venta.totalVenta}</strong></span>
            </div>
        </div>
           
        <!-- Detalle de forma de pago -->
        <div>
            ---------------------- [ Detalle forma de pago ] --------------------
        </div>
        <div style="line-height: 1.1; margin: 0;">
            <p style="margin: 0;"><strong>Metodo Pago:</strong> ${venta.metodoPago}</p>
            <p style="margin: 0;"><strong>Recibido:</strong> $${recibido}</p>
            <p style="margin: 0;"><strong>Cambio:</strong> $${cambio}</p>
        </div>
        
        <!-- Detalle de impuestos -->
        <div class="text-center mb-2">
            ------------------------- [ Detalle Impuestos ] ----------------------
        </div>

        <div style="font-size: 0.8em; white-space: normal; margin: 0;">
            <pre style="margin: 0; line-height: 1.0; white-space: pre-wrap; word-wrap: break-word;">
        
TIPO %   BASE    IMPUESTO
-------------------------
N/A      N/A     N/A

No somos responsables de IVA – empresa bajo régimen simplificado, según Artículo 437 §3° del Estatuto Tributario
            
            </pre>
        </div>

        <!-- Totales de unidades y referencias -->
        <hr class="mt-2 mb-1" style="border-top: 1px dashed black;">
        <p><strong>Total Und:</strong> ${totalUnidades} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong>Total Ref:</strong> ${totalReferencias}</p>
        <hr class="mt-1 mb-1" style="border-top: 1px dashed black;">

        <!-- QR y CUDE -->
        <div style="text-align: center; margin-top: 8px;">
            <div style="width: 100px; height: 100px; border: 1px solid #ccc; margin: 0 auto 3px auto; display: flex; justify-content: center; align-items: center; font-size: 0.7em; color: #777;">
                QR
            </div>
            <p style="font-size: 0.8em; overflow-wrap: break-word;">
                <strong>CUDE:</strong> N/A
            </p>
        </div>

         <!-- Información de validez del comprobante -->
         <hr class="mt-2 mb-1" style="border-top: 1px dashed black;">
         <div style="text-align: center; font-size: 0.8em; color: #777;">
             <p>Comprobante interno emitido por el sistema VIFAC.</p>
             <p>Este documento respalda su compra y será requerido para hacer efectiva la garantía.</p>
             <p>Fecha de impresión: <strong>${fechaEmision}</strong></p>
         </div>

        <!-- Resolución -->
        <hr class="mt-2 mb-2" style="border-top: 1px dashed black;">
        <div style="text-align: center; font-size: 0.7em; color: #777;">
            <p>RESOLUCIÓN MERCANTIL # ${empresa.resolucion_mercantil}</p>
            <p>Desde ${empresa.fecha_registro_res} hasta ${empresa.fecha_vencimiento_res}</p>
        </div>

        <!-- Pie -->
        <div class="footer mt-2">
            <p style="font-size: 2.2em; font-weight: bold; text-align: center;">¡Gracias por su compra!</p>
            <p style="font-size: 0.9em;">© 2025 Política de privacidad · Diseñado por O.T.G “VIFAC-Sys”</p>
        </div>
    </div>

    <div class="print-button">
        <button onclick="window.print()" class="btn btn-outline-secondary">Imprimir Ticket</button>
    </div>

    <script src="js/bootstrap.bundle.min.js"></script>
</body>
</html>
