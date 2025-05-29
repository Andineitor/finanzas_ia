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
	           "WHERE t.usuario.id = :usuarioId AND t.tipo = 'INGRESO' " +
	           "GROUP BY t.categoria.nombre")
	    List<Object[]> sumaGastosPorCategoria(@Param("usuarioId") Integer usuarioId);
	    
	    
	    @Query("SELECT t.fecha, " +
	    	       "SUM(CASE WHEN t.tipo = 'INGRESO' THEN t.cantidad ELSE 0 END), " +
	    	       "SUM(CASE WHEN t.tipo = 'EGRESO' THEN t.cantidad ELSE 0 END) " +
	    	       "FROM Transaccion t " +
	    	       "WHERE t.usuario.id = :usuarioId " +
	    	       "GROUP BY t.fecha " +
	    	       "ORDER BY t.fecha ASC")
	    	List<Object[]> obtenerIngresosYGastosPorFecha(@Param("usuarioId") Integer usuarioId);


}
