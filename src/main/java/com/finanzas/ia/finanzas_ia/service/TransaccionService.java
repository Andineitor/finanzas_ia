package com.finanzas.ia.finanzas_ia.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Transaccion;

public interface TransaccionService {

    
    void agregarIngreso(String username, Integer cantidad);
    void registrarGasto(String username, String descripcion, Integer cantidad, Integer categoriaId, Date fecha);
    Integer calcularTotalGastos(Cuenta cuenta); // Método para calcular los gastos
    Map<String, Integer> obtenerGastosPorCategoria(Cuenta cuenta); // Método para los gastos por cate
    List<Object[]> obtenerIngresosYGastosPorFecha(Integer usuarioId);
    Map<String, Object> obtenerIngresosYGastosFormateadosPorFecha(Integer usuarioId);

    void editarTransaccion(Transaccion t);
    void eliminarTransaccion(Integer id);
}