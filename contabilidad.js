document.addEventListener('DOMContentLoaded', () => {
  seleccionarProceso('ingresos');
});
let procesoActual = 'ingresos'; 
const datos = {
  ingresos: {
    semana: [5, 150000, 3, 90000, 2, 60000, 10000, 5000], // [Facturas, Total, Vencidas Facturas, Vencidas Total, Vigentes Facturas, Vigentes Total, IVA, Devoluciones Total]
    mes: [20, 600000, 8, 240000, 12, 360000, 40000, 20000],
    trimestre: [60, 1800000, 25, 750000, 35, 1050000, 120000, 60000],
    semestre: [120, 3600000, 50, 1500000, 70, 2100000, 240000, 120000],
    anio: [240, 7200000, 100, 3000000, 140, 4200000, 480000, 240000]
  },
  egresos: {
    semana: [4, 120000, 2, 60000, 2, 60000, 30000, 20000], // [Facturas, Total, Vencidas Facturas, Vencidas Total, Vigentes Facturas, Vigentes Total, Arriendo, Servicios Públicos]
    mes: [18, 540000, 10, 300000, 8, 240000, 120000, 80000],
    trimestre: [55, 1650000, 30, 900000, 25, 750000, 360000, 240000],
    semestre: [110, 3300000, 60, 1800000, 50, 1500000, 720000, 480000],
    anio: [220, 6600000, 120, 3600000, 100, 3000000, 1440000, 960000]
  },
  nomina: {
    semana: [2, 60000, 1, 20000, 0, 0], // [Empleados Activos, Total Nomina, Comisiones, Total Comisiones, Adelantos, Total Adelantos]
    mes: [3, 240000, 2, 80000, 1, 30000],
    trimestre: [3, 720000, 5, 200000, 2, 60000],
    semestre: [5, 1440000, 8, 320000, 3, 90000],
    anio: [5, 2880000, 15, 600000, 5, 150000]
  }
};

function seleccionarProceso(proceso) {
  procesoActual = proceso;

  // Oculta todas las secciones y muestra solo la seleccionada
  document.querySelectorAll('.card-section').forEach(sec => sec.style.display = 'none');
  const seccion = document.getElementById(proceso);
  if (seccion) seccion.style.display = 'block';

  // Actualiza la clase activa del menú
  document.querySelectorAll('.procesos-scroll li').forEach(li => li.classList.remove('active'));
  const liActivo = document.getElementById(`proceso-${proceso}`);
  if (liActivo) liActivo.classList.add('active');

  const periodo = document.getElementById('role-select').value;
  if (periodo && periodo !== "Seleccionar Periodo") {
    actualizarDatos(periodo);
  } else if (proceso === 'ingresos') {
    actualizarDatos('semana'); // Muestra datos por defecto al cargar la sección de ingresos
    document.getElementById('role-select').value = 'semana'; // Selecciona 'Semana' por defecto en el select
  } else if (proceso === 'egresos') {
    actualizarDatos('semana'); // Muestra datos por defecto al cargar la sección de egresos
    document.getElementById('role-select').value = 'semana'; // Selecciona 'Semana' por defecto en el select
  } else if (proceso === 'nomina') {
    actualizarDatos('semana'); // Muestra datos por defecto al cargar la sección de nomina
    document.getElementById('role-select').value = 'semana'; // Selecciona 'Semana' por defecto en el select
  }
}

function actualizarDatos(periodo) {
  if (!procesoActual) return;

  const formatoMoneda = (valor) => `$${valor.toLocaleString(undefined, { minimumFractionDigits: 0 })}`;

  if (procesoActual === 'ingresos') {
    const vals = datos.ingresos[periodo];
    document.getElementById('ingresos-facturas').textContent = `Facturas ${vals[0]}`;
    document.getElementById('ingresos-total').textContent = `total ${formatoMoneda(vals[1])}`;
    document.getElementById('ingresos-cobrar-vencidos-facturas').textContent = `Facturas ${vals[2]}`;
    document.getElementById('ingresos-cobrar-vencidos-total').textContent = `total ${formatoMoneda(vals[3])}`;
    document.getElementById('ingresos-cobrar-vigentes-facturas').textContent = `Facturas ${vals[4]}`;
    document.getElementById('ingresos-cobrar-vigentes-total').textContent = `total ${formatoMoneda(vals[5])}`;
    document.getElementById('iva').textContent = `Iva ${formatoMoneda(vals[6])}`;
    document.getElementById('devoluciones').textContent = `0 Devoluciones Total ${formatoMoneda(vals[7])}`; // Asumiendo 0 devoluciones por ahora
  } else if (procesoActual === 'egresos') {
    const vals = datos.egresos[periodo];
    document.getElementById('egresos-facturas').textContent = `Facturas ${vals[0]}`;
    document.getElementById('egresos-total').textContent = `total ${formatoMoneda(vals[1])}`;
    document.getElementById('egresos-pagar-vencidos-facturas').textContent = `Facturas ${vals[2]}`;
    document.getElementById('egresos-pagar-vencidos-total').textContent = `total ${formatoMoneda(vals[3])}`;
    document.getElementById('egresos-pagar-vigentes-facturas').textContent = `Facturas ${vals[4]}`;
    document.getElementById('egresos-pagar-vigentes-total').textContent = `total ${formatoMoneda(vals[5])}`;
    document.getElementById('egresos-arrendo').textContent = `Arrendo ${formatoMoneda(vals[6])}`;
    document.getElementById('egresos-servicios-publicos').textContent = `Servicios Públicos ${formatoMoneda(vals[7])}`;
  } else if (procesoActual === 'nomina') {
    const vals = datos.nomina[periodo];
    document.getElementById('nomina-empleados-activos').textContent = `Empleados Activos ${vals[0]}`;
    document.getElementById('nomina-total').textContent = `total ${formatoMoneda(vals[1])}`;
    document.getElementById('comisiones-facturas').textContent = `Comisiones ${vals[2]}`;
    document.getElementById('comisiones-total').textContent = `Total ${formatoMoneda(vals[3])}`;
    document.getElementById('adelantos-nomina-facturas').textContent = `Adelantos ${vals[4]}`;
    document.getElementById('adelantos-nomina-total').textContent = `total ${formatoMoneda(vals[5])}`;
  }

  actualizarGraficos(periodo);
  actualizarTablaDatos(periodo);
}

// Gráficos
const ctxArea = document.getElementById('myAreaChart').getContext('2d');
const ctxBar = document.getElementById('myBarChart').getContext('2d');
let areaChart = null;
let barChart = null;

function actualizarGraficos(periodo) {
  if (!procesoActual) return;

  let etiquetas = [];
  let valoresArea = [];
  let valoresBar = [];

  if (procesoActual === 'ingresos') {
    etiquetas = ['Ingresos', 'Por Cobrar Vencidos', 'Por Cobrar Vigentes', 'IVA', 'Devoluciones'];
    const vals = datos.ingresos[periodo];
    valoresArea = [vals[1], vals[3], vals[5], vals[6], vals[7]];
    valoresBar = [vals[1], vals[3], vals[5], vals[6], vals[7]];
  } else if (procesoActual === 'egresos') {
    etiquetas = ['Egresos', 'Por Pagar Vencidos', 'Por Pagar Vigentes', 'Arriendo', 'Servicios Públicos'];
    const vals = datos.egresos[periodo];
    valoresArea = [vals[1], vals[3], vals[5], vals[6], vals[7]];
    valoresBar = [vals[1], vals[3], vals[5], vals[6], vals[7]];
  } else if (procesoActual === 'nomina') {
    etiquetas = ['Nómina', 'Comisiones', 'Adelantos'];
    const vals = datos.nomina[periodo];
    valoresArea = [vals[1], vals[3], vals[5]];
    valoresBar = [vals[1], vals[3], vals[5]];
  }

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: { display: false },
      tooltip: { mode: 'index', intersect: false }
    },
    scales: {
      y: { beginAtZero: true }
    }
  };

  if (areaChart) areaChart.destroy();
  areaChart = new Chart(ctxArea, {
    type: 'line',
    data: {
      labels: etiquetas,
      datasets: [{
        label: 'Monto en $',
        data: valoresArea,
        borderColor: '#0093DD',
        backgroundColor: 'rgba(0,147,221,0.1)',
        tension: 0.4,
        fill: true
      }]
    },
    options: chartOptions
  });

  if (barChart) barChart.destroy();
  barChart = new Chart(ctxBar, {
    type: 'bar',
    data: {
      labels: etiquetas,
      datasets: [{
        label: 'Monto en $',
        data: valoresBar,
        backgroundColor: ['#0093DD', '#66B2FF', '#004080', '#ADD8E6', '#87CEEB'] // Más colores si es necesario
      }]
    },
    options: chartOptions
  });
}

function actualizarTablaDatos(periodo) {
  if (!procesoActual) return;

  const tbody = document.querySelector('#datatablesSimple tbody');
  tbody.innerHTML = '';
  const formatoMoneda = (valor) => `$${valor.toLocaleString(undefined, { minimumFractionDigits: 0 })}`;
  let datosTabla = [];

  if (procesoActual === 'ingresos') {
    const vals = datos.ingresos[periodo];
    datosTabla = [
      { item: 1, factura: 'INV-' + Math.floor(Math.random() * 1000), descripcion: 'Venta (Total)', clienteProveedor: 'Varios', categoria: 'Ingreso', fecha: obtenerFechaAleatoria(), monto: vals[1] },
      { item: 2, factura: 'VENC-' + Math.floor(Math.random() * 1000), descripcion: 'Factura Vencida', clienteProveedor: 'Cliente A', categoria: 'Por Cobrar', fecha: obtenerFechaAleatoria(true), monto: vals[3] },
      { item: 3, factura: 'VIG-' + Math.floor(Math.random() * 1000), descripcion: 'Factura Vigente', clienteProveedor: 'Cliente B', categoria: 'Por Cobrar', fecha: obtenerFechaAleatoria(), monto: vals[5] },
      { item: 4, factura: 'IVA-' + Math.floor(Math.random() * 1000), descripcion: 'IVA Recaudado', clienteProveedor: 'Gobierno', categoria: 'Variable', fecha: obtenerFechaAleatoria(), monto: vals[6] },
      { item: 5, factura: 'DEV-' + Math.floor(Math.random() * 1000), descripcion: 'Devolución', clienteProveedor: 'Cliente C', categoria: 'Variable', fecha: obtenerFechaAleatoria(), monto: vals[7] * -1 },
      // ... más datos estáticos si lo deseas
    ];
  } else if (procesoActual === 'egresos') {
    const vals = datos.egresos[periodo];
    datosTabla = [
      { item: 1, factura: 'EG-' + Math.floor(Math.random() * 1000), descripcion: 'Pago (Total)', clienteProveedor: 'Proveedor X', categoria: 'Egreso', fecha: obtenerFechaAleatoria(), monto: vals[1] },
      { item: 2, factura: 'VENCP-' + Math.floor(Math.random() * 1000), descripcion: 'Pago Vencido', clienteProveedor: 'Acreedor Y', categoria: 'Por Pagar', fecha: obtenerFechaAleatoria(true), monto: vals[3] },
      { item: 3, factura: 'VIGP-' + Math.floor(Math.random() * 1000), descripcion: 'Pago Vigente', clienteProveedor: 'Servicios Z', categoria: 'Por Pagar', fecha: obtenerFechaAleatoria(), monto: vals[5] },
      { item: 4, factura: 'ARR-' + Math.floor(Math.random() * 1000), descripcion: 'Pago Arriendo', clienteProveedor: 'Inmobiliaria', categoria: 'Variable', fecha: obtenerFechaAleatoria(), monto: vals[6] },
      { item: 5, factura: 'SERV-' + Math.floor(Math.random() * 1000), descripcion: 'Pago Servicios', clienteProveedor: 'Empresas Públicas', categoria: 'Variable', fecha: obtenerFechaAleatoria(), monto: vals[7] },
      // ... más datos estáticos si lo deseas
    ];
  } else if (procesoActual === 'nomina') {
    const vals = datos.nomina[periodo];
    datosTabla = [
      { item: 1, factura: 'NOM-' + Math.floor(Math.random() * 1000), descripcion: 'Pago Nómina (Total)', clienteProveedor: 'Empleados', categoria: 'Nómina', fecha: obtenerFechaAleatoria(), monto: vals[1] },
      { item: 2, factura: 'COM-' + Math.floor(Math.random() * 1000), descripcion: 'Pago Comisiones', clienteProveedor: 'Vendedores', categoria: 'Nómina', fecha: obtenerFechaAleatoria(), monto: vals[3] },
      { item: 3, factura: 'ADEL-' + Math.floor(Math.random() * 1000), descripcion: 'Adelanto Nómina', clienteProveedor: 'Empleado W', categoria: 'Nómina', fecha: obtenerFechaAleatoria(), monto: vals[5] },
      // ... más datos estáticos si lo deseas
    ];
  }

  datosTabla.forEach(fila => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td><span class="math-inline">\{fila\.item\}</td\>
<td\></span>{fila.factura}</td>
      <td><span class="math-inline">\{fila\.descripcion\}</td\>
<td\></span>{fila.clienteProveedor}</td>
      <td><span class="math-inline">\{fila\.categoria\}</td\>
<td\></span>{fila.fecha}</td>
      <td>${formatoMoneda(fila.monto)}</td>
    `;
    tbody.appendChild(tr);
  });

  // Reinicializar el DataTable para aplicar los nuevos datos (si estás usando la funcionalidad de búsqueda/paginación de Bootstrap DataTable)
  if (window.bootstrap && window.bootstrap.DataTable) {
    let dataTable = window.bootstrap.DataTable.getOrCreateInstance('#datatablesSimple');
    dataTable.destroy();
    new window.bootstrap.DataTable('#datatablesSimple');
  }
}

function obtenerFechaAleatoria(pasado = false) {
  const hoy = new Date();
  const inicio = pasado ? new Date(hoy.getFullYear(), hoy.getMonth() - 1, 1) : new Date(hoy.getFullYear(), hoy.getMonth(), 1);
  const fin = pasado ? new Date(hoy.getFullYear(), hoy.getMonth(), 0) : hoy;
  const fecha = new Date(inicio.getTime() + Math.random() * (fin.getTime() - inicio.getTime()));
  const dia = fecha.getDate().toString().padStart(2, '0');
  const mes = (fecha.getMonth() + 1).toString().padStart(2, '0');
  const anio = fecha.getFullYear();
  return `${dia}/${mes}/${anio}`;
}

document.addEventListener('DOMContentLoaded', () => {
  const roleSelect = document.getElementById('role-select');

  if (roleSelect) {
    roleSelect.addEventListener('change', (event) => {
      const periodoSeleccionado = event.target.value;
      actualizarDatos(periodoSeleccionado);
    });
  } else {
    console.error('Error: No se encontró el elemento con ID "role-select".');
  }

  // Inicializar la visualización al cargar la página con un periodo por defecto
  const periodoInicial = 'semana';
  actualizarDatos(periodoInicial);
  if (roleSelect) {
    roleSelect.value = periodoInicial;
  }
});