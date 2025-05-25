package com.finanzas.ia.finanzas_ia.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipDto {

	private UsuarioDto usuario;
	private CuentaDto cuenta;
    private String pregunta;
    private String respuesta;    
}
