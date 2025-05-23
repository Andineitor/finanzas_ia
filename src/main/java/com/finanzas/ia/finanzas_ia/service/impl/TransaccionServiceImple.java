package com.finanzas.ia.finanzas_ia.service.impl;

import java.util.Date;
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

    public TransaccionServiceImple(CuentaRepositoy cuentaRepo,
                                    TransaccionRepository transRepo,
                                    CuentaService cuentaServ) {
        this.cuentaRepo = cuentaRepo;
        this.transRepo = transRepo;
        this.cuentaServ = cuentaServ;
		this.categoriaRepo = null;
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
        t.setTipo("ingreso");
        t.setTransaccionfFamiliar(false);
        t.setCuenta(cuenta);

        transRepo.save(t);
    }

    @Override
    public void registrarGasto(String username, String descripcion, Integer cantidad, Integer categoriId) {
        Cuenta cuenta = cuentaServ.obtenerCuentaDelUsuario(username).orElseThrow();
        cuenta.setIngreso(cuenta.getIngreso() - cantidad);
        cuentaRepo.save(cuenta);

        Transaccion t = new Transaccion();
        t.setDescripcion(descripcion);
        t.setCantidad(cantidad);
        // ¡positivo! el tipo ahora lo define
        t.setFecha(new Date());
        t.setTipo("gasto");
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
            .filter(t -> "gasto".equalsIgnoreCase(t.getTipo())) // Solo gastos
            .mapToInt(Transaccion::getCantidad)
            .sum();
    }



    @Override
    public Map<String, Integer> obtenerGastosPorCategoria(Cuenta cuenta) {
        // Filtrar las transacciones de tipo "gasto" y que tengan una categoría
        return cuenta.getTransacciones().stream()
            .filter(t -> "gasto".equalsIgnoreCase(t.getTipo()) && t.getCategoria() != null)
            .collect(Collectors.groupingBy(
                t -> t.getCategoria().getNombre(), // Agrupar por nombre de categoría
                Collectors.summingInt(Transaccion::getCantidad) // Sumar las cantidades
            ));
    }


}
