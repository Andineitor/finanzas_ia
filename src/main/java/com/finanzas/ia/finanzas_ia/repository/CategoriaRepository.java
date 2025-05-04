package com.finanzas.ia.finanzas_ia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finanzas.ia.finanzas_ia.entity.Categoria;

import jakarta.transaction.Transactional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{


}
