package com.finanzas.ia.finanzas_ia.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto {
	private Integer id;
	private String nombre;
    private String descripcion;
    private Integer ingreso;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;

}
