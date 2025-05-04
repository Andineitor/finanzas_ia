package com.finanzas.ia.finanzas_ia.service;

import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Usuario;

public interface UsuarioService {

	Usuario register(UsuarioDto request);
    Usuario login(UsuarioDto request);
    
    Usuario getCurrentAuthenticatedUser();
}
