package com.finanzas.ia.finanzas_ia.service.impl;



import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.repository.UsuarioRepository;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
	
	private final UsuarioRepository userRepo;
    private final PasswordEncoder passwordEncoder;

	@Override
	public Usuario register(UsuarioDto request) {
		if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
		}
		Usuario user = new Usuario();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEdad(request.getEdad());
        user.setEmail(request.getEmail());
        user.setSexo(request.getSexo());
        user.setRole("USER"); 
        return userRepo.save(user);
	}

	@Override
	public Usuario login(UsuarioDto request) {
		Usuario user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return user;
    }

	public Usuario findByUsername(String username) {
	    return userRepo.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}
	
	
	

}
