package com.finanzas.ia.finanzas_ia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finanzas.ia.finanzas_ia.entity.Tip;
import com.finanzas.ia.finanzas_ia.entity.Usuario;

public interface TipRepository extends JpaRepository<Tip, Integer>{

	List<Tip> findByUsuarioOrderByTimestampAsc(Usuario usuario);

}
