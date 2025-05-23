package com.finanzas.ia.finanzas_ia.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsuarioController {
	
    private final UsuarioService usuarioService;

    private final CuentaService cuentaService;
    
	@GetMapping("/profile")
    public String mostrarProfile(Model model, Principal principal) {
		String username = principal.getName();
        Usuario usuario = usuarioService.findByUsername(username); 
        model.addAttribute("usuario", usuario);
        Cuenta cuenta = cuentaService.obtenerCuentaDelUsuario(principal.getName())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        model.addAttribute("cuenta", cuenta);
        return "user";
    }

}
