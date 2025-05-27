package com.finanzas.ia.finanzas_ia.service.impl;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    private final String UPLOAD_DIR = "uploads/";

	@Override
	public Usuario register(UsuarioDto request) {
		if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Usuario ya existe");
		}
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya existe");
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

	@Override
	public void actualizarUsuario(UsuarioDto dto, MultipartFile imagen) throws IOException {
	    Usuario usuario = userRepo.findById(dto.getId())
	        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + dto.getId()));

	    // Verifica si otro usuario ya usa ese username
	    Optional<Usuario> usuarioConMismoUsername = userRepo.findByUsername(dto.getUsername());
	    if (usuarioConMismoUsername.isPresent() && !usuarioConMismoUsername.get().getId().equals(dto.getId())) {
	        throw new RuntimeException("Username ya lo utiliza otro usuario");
	    }

	    // Verifica si otro usuario ya usa ese email
	    Optional<Usuario> usuarioConMismoEmail = userRepo.findByEmail(dto.getEmail());
	    if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getId().equals(dto.getId())) {
	        throw new RuntimeException("Email ya pertenece a otro usuario");
	    }

	    usuario.setNombre(dto.getNombre());
	    usuario.setApellido(dto.getApellido());
	    usuario.setEdad(dto.getEdad());
	    usuario.setEmail(dto.getEmail());
	    usuario.setSexo(dto.getSexo());
	    usuario.setUsername(dto.getUsername());

	    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }

	    if (imagen != null && !imagen.isEmpty()) {
	        String base64 = Base64.getEncoder().encodeToString(imagen.getBytes());
	        usuario.setFotoPerfilBase64(base64);
	    } else if (dto.getFotoPerfilBase64() != null) {
	        usuario.setFotoPerfilBase64(dto.getFotoPerfilBase64());
	    }

	    userRepo.save(usuario);
	}

	@Override
	public Usuario findByEmail(String username) {
		return userRepo.findByEmail(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}

		
	}
	
