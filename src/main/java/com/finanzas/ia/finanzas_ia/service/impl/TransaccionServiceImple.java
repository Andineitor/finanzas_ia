package com.finanzas.ia.finanzas_ia.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finanzas.ia.finanzas_ia.entity.Categoria;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Transaccion;
import com.finanzas.ia.finanzas_ia.repository.CategoriaRepository;
import com.finanzas.ia.finanzas_ia.repository.CuentaRepositoy;
import com.finanzas.ia.finanzas_ia.repository.TransaccionRepository;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.TransaccionService;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;

@Service
public class TransaccionServiceImple implements TransaccionService {

    @Autowired
	private final CuentaRepositoy cuentaRepo;
    @Autowired
    private final TransaccionRepository transRepo;
    private final CuentaService cuentaServ;
    @Autowired
    private final CategoriaRepository categoriaRepo;

    @Autowired 
    public TransaccionServiceImple(CuentaRepositoy cuentaRepo,
                                    TransaccionRepository transRepo,
                                    CuentaService cuentaServ,
                                    CategoriaRepository cateRepo) {
        this.cuentaRepo = cuentaRepo;
        this.transRepo = transRepo;
        this.cuentaServ = cuentaServ;
		this.categoriaRepo = cateRepo;
    }
	
	
	@Override
	@Transactional
    public void agregarIngreso(String username, Integer cantidad) {
        Cuenta cuenta = cuentaServ.obtenerCuentaDelUsuario(username).orElseThrow();
        cuenta.setIngreso(cuenta.getIngreso() + cantidad);
        cuentaRepo.save(cuenta);

        Transaccion t = new Transaccion();
        t.setDescripcion("Ingreso adicional");
        t.setCantidad(cantidad);
        t.setFecha(new Date());
        t.setTipo("INGRESO");
        t.setUsuario(cuenta.getUsuario());
        t.setTransaccionfFamiliar(false);
        t.setCuenta(cuenta);

        transRepo.save(t);
    }

    @Override
    public void registrarGasto(String username, String descripcion, Integer cantidad, Integer categoriId, @Nullable Date fecha) {
        Cuenta cuenta = cuentaServ.obtenerCuentaDelUsuario(username).orElseThrow();
        cuenta.setIngreso(cuenta.getIngreso() - cantidad);
        cuentaRepo.save(cuenta);

        Transaccion t = new Transaccion();
        t.setDescripcion(descripcion);
        t.setCantidad(cantidad);
        t.setFecha(fecha != null ? fecha : new Date());
        t.setTipo("EGRESO");
        t.setUsuario(cuenta.getUsuario());

        t.setTransaccionfFamiliar(false);
        t.setCuenta(cuenta);
        Categoria categoria = categoriaRepo.findById(categoriId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + categoriId));
            t.setCategoria(categoria);
        transRepo.save(t);
    }
	
	
		
    @Override
    public Integer calcularTotalGastos(Cuenta cuenta) {
        // Calcular los gastos totales solo de las transacciones de tipo "gasto"
        return cuenta.getTransacciones().stream()
            .filter(t -> "EGRESO".equalsIgnoreCase(t.getTipo())) // Solo gastos
            .mapToInt(Transaccion::getCantidad)
            .sum();
    }



    @Override
    public Map<String, Integer> obtenerGastosPorCategoria(Cuenta cuenta) {
        // Filtrar las transacciones de tipo "gasto" y que tengan una categoría
        return cuenta.getTransacciones().stream()
            .filter(t -> "EGRESO".equalsIgnoreCase(t.getTipo()) && t.getCategoria() != null)
            .collect(Collectors.groupingBy(
                t -> t.getCategoria().getNombre(), // Agrupar por nombre de categoría
                Collectors.summingInt(Transaccion::getCantidad) // Sumar las cantidades
            ));
    }


    @Override
    public Map<String, Object> obtenerIngresosYGastosFormateadosPorFecha(Integer usuarioId) {
        List<Object[]> rawData = transRepo.obtenerIngresosYGastosPorFecha(usuarioId);

        List<String> fechas = new ArrayList<>();
        List<Integer> ingresosPorDia = new ArrayList<>();
        List<Integer> gastosPorDia = new ArrayList<>();

        if (rawData != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");

            for (Object[] fila : rawData) {
                if (fila[0] != null) {
                    Date fecha = (Date) fila[0];
                    LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechas.add(localDate.format(formatter));
                } else {
                    fechas.add("N/A");
                }

                ingresosPorDia.add(fila[1] != null ? ((Number) fila[1]).intValue() : 0);
                gastosPorDia.add(fila[2] != null ? ((Number) fila[2]).intValue() : 0);
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("fechas", fechas);
        resultado.put("ingresosPorDia", ingresosPorDia);
        resultado.put("gastosPorDia", gastosPorDia);

        return resultado;
    }


    @Override
    public List<Object[]> obtenerIngresosYGastosPorFecha(Integer usuarioId) {
        return transRepo.obtenerIngresosYGastosPorFecha(usuarioId);
    }


    @Override
    public void editarTransaccion(Transaccion tActualizada) {
        Transaccion t = transRepo.findById(tActualizada.getId()).orElseThrow();
        t.setDescripcion(tActualizada.getDescripcion());
        t.setCantidad(tActualizada.getCantidad());
        t.setFecha(tActualizada.getFecha());
        transRepo.save(t);
    }

    @Override
    public void eliminarTransaccion(Integer id) {
        Transaccion t = transRepo.findById(id).orElseThrow();
        transRepo.delete(t);
    }



}
