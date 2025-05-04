package com.finanzas.ia.finanzas_ia.dto;

import java.util.Date;

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
	private Date fecha;
	

    // Agregar un constructor personalizado si lo prefieres
    public CuentaDto(String nombre, String descripcion, Integer ingreso, Date fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ingreso = ingreso;
        this.fecha = fecha;
    }

}
