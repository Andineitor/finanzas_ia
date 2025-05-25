package com.finanzas.ia.finanzas_ia.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String rol;
    
    @Column(columnDefinition = "TEXT")
    private String contenido; 

    private LocalDateTime timestamp;

    public Tip() {}

    public Tip(Usuario usuario, String rol, String contenido, LocalDateTime timestamp) {
        this.usuario = usuario;
        this.rol = rol;
        this.contenido = contenido;
        this.timestamp = timestamp;
    }

    // getters y setters generados por Lombok o manuales
}
