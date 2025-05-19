document.addEventListener("DOMContentLoaded", () => {
  const categorias = categoriasData || [];
  const gastos = gastosData || [];
  const ingreso = ingresoData || 0;
  const gastoTotal = gastoTotalData || 0;
  const fechasData = fechas || [];
  const ingresosData = ingresosPorDia || [];
  const gastosDataLine = gastosPorDia || [];

  // Gráfico pastel ingresos vs egresos
  new Chart(document.getElementById("balancePieChart"), {
    type: 'pie',
    data: {
      labels: ['Ingreso', 'Egreso'],
      datasets: [{
        data: [ingreso, gastoTotal],
        backgroundColor: ['#4CAF50', '#E74C3C'],
        borderColor: '#fff',
        borderWidth: 2,
        hoverOffset: 30,
      }]
    },
    options: {
      responsive: true,
      plugins: {
        legend: { position: 'bottom', labels: { font: { size: 14, weight: '600' }, color: '#34495e' }},
        title: {
          display: true,
          text: 'Balance General: Ingreso vs Egreso',
          font: { size: 20, weight: '700' },
          color: '#2c3e50',
          padding: { bottom: 20 }
        },
        tooltip: {
          backgroundColor: '#2c3e50',
          titleFont: { weight: 'bold', size: 16 },
          bodyFont: { size: 14 },
          padding: 10,
          cornerRadius: 8
        }
      }
    }
  });

  // Gráfico barras horizontal gastos por categoría
  if(categorias.length > 0 && gastos.length > 0) {
    new Chart(document.getElementById("gastoBarChart"), {
      type: 'bar',
      data: {
        labels: categorias,
        datasets: [{
          label: 'Gastos por Categoría',
          data: gastos,
          backgroundColor: '#3498db',
          borderRadius: 6,
          maxBarThickness: 40
        }]
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        plugins: {
          legend: { display: false },
          title: {
            display: true,
            text: 'Gastos por Categoría (Barra Horizontal)',
            font: { size: 18, weight: '700' }
          },
          tooltip: {
            backgroundColor: '#2c3e50',
            titleFont: { weight: 'bold', size: 14 },
            bodyFont: { size: 14 },
            cornerRadius: 6,
            padding: 8
          }
        },
        scales: {
          x: {
            beginAtZero: true,
            grid: { color: '#ecf0f1' },
            ticks: { font: { size: 13 }, color: '#34495e' }
          },
          y: {
            grid: { display: false },
            ticks: { font: { size: 14 }, color: '#34495e' }
          }
        }
      }
    });
  }

  // Gráfico lineal evolución ingresos y egresos
  if(fechasData.length > 0) {
    new Chart(document.getElementById("lineChart"), {
      type: 'line',
      data: {
        labels: fechasData,
        datasets: [
          {
            label: 'Ingresos',
            data: ingresosData,
            borderColor: '#4CAF50',
            backgroundColor: 'rgba(76, 175, 80, 0.2)',
            fill: true,
            tension: 0.4,
            pointRadius: 5,
            pointHoverRadius: 7,
            borderWidth: 3,
          },
          {
            label: 'Egresos',
            data: gastosDataLine,
            borderColor: '#E74C3C',
            backgroundColor: 'rgba(231, 76, 60, 0.2)',
            fill: true,
            tension: 0.4,
            pointRadius: 5,
            pointHoverRadius: 7,
            borderWidth: 3,
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'top', labels: { font: { size: 14, weight: '600' }, color: '#34495e' }},
          tooltip: {
            backgroundColor: '#2c3e50',
            titleFont: { weight: 'bold', size: 16 },
            bodyFont: { size: 14 },
            padding: 10,
            cornerRadius: 8
          },
          title: {
            display: true,
            text: 'Evolución de Ingresos y Egresos',
            font: { size: 20, weight: '700' },
            color: '#2c3e50',
            padding: { bottom: 20 }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { font: { size: 14, weight: '600' }, color: '#34495e' },
            grid: { color: '#ecf0f1', borderColor: '#bdc3c7' }
          },
          x: {
            ticks: { font: { size: 14, weight: '600' }, color: '#34495e' },
            grid: { display: false }
          }
        }
      }
    });
  }
  console.log({
    fechasData,
    ingresosData,
    gastosDataLine
  });

});
