document.addEventListener("DOMContentLoaded", () => {
  const categorias = categoriasData || [];
  const gastos = gastosData || [];
  const ingreso = ingresoData || 0;
  const gastoTotal = gastoTotalData || 0;
  const fechasData = fechas || [];
  const ingresosData = ingresosPorDia || [];
  const gastosDataLine = gastosPorDia || [];

  // Gráfico DONUT ingresos vs egresos
  new Chart(document.getElementById("balancePieChart"), {
    type: 'doughnut',
    data: {
      labels: ['Ingreso', 'Egreso'],
      datasets: [{
        data: [ingreso, gastoTotal],
        backgroundColor: ['#1cc88a', '#e74a3b'],
        borderColor: '#fff',
        borderWidth: 2,
        hoverOffset: 30,
        hoverBackgroundColor: ['#17a673', '#be2617'],
      }]
    },
    options: {
      responsive: true,
      cutoutPercentage: 75,
      plugins: {
        legend: { display: false },
        tooltip: {
          enabled: true,
          callbacks: {
            label: function(context) {
              const total = context.dataset.data.reduce((a, b) => a + b, 0);
              const value = context.raw;
              const label = context.label;
              const percentage = ((value / total) * 100).toFixed(1);
              return `${label}: $${value} (${percentage}%)`;
            }
          }
        }
      }
    }
  });

  // Generar colores dinámicos para barras
  const generarColores = (cantidad) => {
    const colores = [];
    for (let i = 0; i < cantidad; i++) {
      const r = Math.floor(Math.random() * 256);
      const g = Math.floor(Math.random() * 256);
      const b = Math.floor(Math.random() * 256);
      colores.push(`rgba(${r}, ${g}, ${b}, 0.7)`);
    }
    return colores;
  };

  // Gráfico barras horizontal gastos por categoría
  if(categorias.length > 0 && gastos.length > 0) {
    new Chart(document.getElementById("gastoBarChart"), {
      type: 'horizontalBar',
      data: {
        labels: categorias,
        datasets: [{
          label: 'Egreso total',
          data: gastos,
          backgroundColor: generarColores(categorias.length),
          borderRadius: 6,
          maxBarThickness: 40
        }]
      },
      options: {
        responsive: true,
        legend: { display: false },
        title: {
          display: false,
          text: 'Egreso total',
          fontSize: 18
        },
        scales: {
          xAxes: [{
            ticks: { beginAtZero: true }
          }],
          yAxes: [{
            barPercentage: 0.5
          }]
        }
      }
    });
  }

  // Gráfico lineal evolución ingresos y egresos
  if(fechas.length > 0) {
	// Formateo de fechas para que sean más amigables visualmente
	 fechas = fechas.map(f => {
	   let date = new Date(f);
	   let dia = date.getDate().toString().padStart(2, '0');
	   let mes = date.toLocaleString('es-ES', { month: 'long' });
	   return `${mes.charAt(0).toUpperCase() + mes.slice(1)}-${dia}`;
	 });
    const ctx = document.getElementById("myAreaChart").getContext("2d");

    new Chart(ctx, {
      type: 'line',
      data: {
        labels: fechas,
        datasets: [
          {
            label: "Ingresos",
            data: ingresosPorDia,
            backgroundColor: "rgba(78, 115, 223, 0.05)",
            borderColor: "rgba(78, 115, 223, 1)",
            fill: true,
            tension: 0.3
          },
          {
            label: "Gastos",
            data: gastosPorDia,
            backgroundColor: "rgba(231, 74, 59, 0.05)",
            borderColor: "rgba(231, 74, 59, 1)",
            fill: true,
            tension: 0.3
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
          }
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'Fecha'
            }
          },
          y: {
            title: {
              display: true,
              text: 'Monto ($)'
            },
            beginAtZero: true
          }
        }
      }
    });
  }
});
