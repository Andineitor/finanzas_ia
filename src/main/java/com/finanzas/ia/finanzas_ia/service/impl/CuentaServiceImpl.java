package com.finanzas.ia.finanzas_ia.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.repository.CuentaRepositoy;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService{
	
	private final CuentaRepositoy cuentaRepo;
	private final UsuarioService usuarioServ;
	
	
	@Override
	public Cuenta registro(CuentaDto request) {
		Usuario usuario = usuarioServ.getCurrentAuthenticatedUser();
		if (cuentaRepo.existsByUsuario(usuario)) {
            throw new RuntimeException("El usuario ya tiene una cuenta asociada.");
        }
		Cuenta cuenta = new Cuenta();
	    cuenta.setNombre(request.getNombre());
	    cuenta.setDescripcion(request.getDescripcion());
	    cuenta.setIngreso(request.getIngreso());
	    cuenta.setFecha(request.getFecha());
	    cuenta.setUsuario(usuario);
	    return cuentaRepo.save(cuenta);
	}


	@Override
	public Optional<Cuenta> obtenerCuentaDelUsuario() {
	    Usuario usuario = usuarioServ.getCurrentAuthenticatedUser();
	    return cuentaRepo.findByUsuario(usuario);
	}

	
}
