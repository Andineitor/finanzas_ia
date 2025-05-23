package com.finanzas.ia.finanzas_ia.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tips")
@RequiredArgsConstructor
public class TipsController {
	
    private final UsuarioService usuarioService;// ← Asegúrate de tener esto

	
	@GetMapping("/dashboard")
    public String mostrarTips(Model model, Principal principal) {
		String username = principal.getName();
        Usuario usuario = usuarioService.findByUsername(username); // Suponiendo que tienes este método
        model.addAttribute("usuario", usuario);
        return "tips";
    }

}
