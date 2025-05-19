package com.finanzas.ia.finanzas_ia.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.finanzas.ia.finanzas_ia.entity.Categoria;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.repository.CategoriaRepository;
import com.finanzas.ia.finanzas_ia.repository.CuentaRepositoy;
import com.finanzas.ia.finanzas_ia.repository.UsuarioRepository;
import com.finanzas.ia.finanzas_ia.service.CategoriaService;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService{


	private final CategoriaRepository categoriaRepo;

	@Override
	public List<Categoria> listarCategorias() {
	    return categoriaRepo.findAll();
	}


	

}
