package com.finanzas.ia.finanzas_ia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finanzas.ia.finanzas_ia.entity.Transaccion;
import com.finanzas.ia.finanzas_ia.entity.Usuario;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer>{

	List<Transaccion> findByUsuarioOrderByFechaDesc(Usuario usuario);

}
