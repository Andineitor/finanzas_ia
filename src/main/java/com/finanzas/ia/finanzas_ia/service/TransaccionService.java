package com.finanzas.ia.finanzas_ia.service;

import java.util.Map;

import com.finanzas.ia.finanzas_ia.entity.Cuenta;

public interface TransaccionService {

    
    void agregarIngreso(String username, Integer cantidad);
    void registrarGasto(String username, String descripcion, Integer cantidad, Integer categoriaId);
    Integer calcularTotalGastos(Cuenta cuenta); // Método para calcular los gastos
    Map<String, Integer> obtenerGastosPorCategoria(Cuenta cuenta); // Método para los gastos por cate
}