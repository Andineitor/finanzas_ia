package com.finanzas.ia.finanzas_ia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finanzas.ia.finanzas_ia.entity.Transaccion;
import com.finanzas.ia.finanzas_ia.entity.Usuario;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer>{

	List<Transaccion> findByUsuarioOrderByFechaDesc(Usuario usuario);
	

	 @Query("SELECT t.categoria.nombre, SUM(t.cantidad) " +
	           "FROM Transaccion t " +
	           "WHERE t.usuario.id = :usuarioId AND t.tipo = 'gasto' " +
	           "GROUP BY t.categoria.nombre")
	    List<Object[]> sumaGastosPorCategoria(@Param("usuarioId") Integer usuarioId);

}
