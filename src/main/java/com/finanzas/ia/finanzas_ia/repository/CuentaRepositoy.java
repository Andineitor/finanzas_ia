package com.finanzas.ia.finanzas_ia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Usuario;

@Repository
public interface CuentaRepositoy extends JpaRepository<Cuenta, Integer>{

    boolean existsByUsuario(Usuario usuario);
    
    Optional<Cuenta> findByUsuario(Usuario usuario);

    
}
