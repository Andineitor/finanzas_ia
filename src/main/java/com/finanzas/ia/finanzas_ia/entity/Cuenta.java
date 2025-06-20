package com.finanzas.ia.finanzas_ia.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private String nombre;
	private String tipoCuenta;
	private String descripcion;
	private Integer ingreso;
	private LocalDate fecha;
	
	
	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "user_id", nullable = false)
	private Usuario usuario;

	@JsonIgnore
	@OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
	@OrderBy("fecha DESC")
    private List<Transaccion> transacciones;

}
