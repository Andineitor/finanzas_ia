package com.finanzas.ia.finanzas_ia.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Usuario;

public interface UsuarioService {

	Usuario register(UsuarioDto request);
    Usuario login(UsuarioDto request);
    
    Usuario findByUsername(String username);
    Usuario findByEmail(String username);

    void actualizarUsuario(UsuarioDto usuarioDto, MultipartFile imagen) throws IOException;

}
