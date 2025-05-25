package com.finanzas.ia.finanzas_ia.service;

import java.util.Optional;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;

public interface CuentaService {
	
	Cuenta registroParaUsuario(CuentaDto request, String username);
    Optional<Cuenta> obtenerCuentaDelUsuario(String username);
    void actualizarCuenta(CuentaDto cuenta);


	
}
