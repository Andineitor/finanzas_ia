package com.finanzas.ia.finanzas_ia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
	
	private Integer id;
	private String username;
    private String password;
    private String nombre;
	private String apellido;
	private Integer edad;
	private String email;
	private String sexo;
    private String role = "USER";
}
