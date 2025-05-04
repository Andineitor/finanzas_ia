package com.finanzas.ia.finanzas_ia.service;

import java.util.Optional;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;

public interface CuentaService {
	
	//dto por que recibe credenciales
	Cuenta registro(CuentaDto request);
	
	
	Optional<Cuenta> obtenerCuentaDelUsuario();
	
}
