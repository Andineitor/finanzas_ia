package com.finanzas.ia.finanzas_ia.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.repository.UsuarioRepository;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsuarioController {
	
    private final UsuarioService usuarioService;

    private final CuentaService cuentaService;
    
    private final UsuarioRepository usuarioRepository;
    
    @GetMapping("/profile")
    public String mostrarProfile(Model model, Principal principal) {
        String username = principal.getName();
        Usuario usuario = usuarioService.findByUsername(username);
        model.addAttribute("usuario", usuario);

        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaDelUsuario(username);
        model.addAttribute("cuenta", cuentaOpt.orElse(null));

        if (cuentaOpt.isEmpty()) {
            model.addAttribute("mensaje", "No tienes una cuenta creada aún.");
        }

        return "user";
    }


	
	@PostMapping("/actualizar")
	public String actualizarUsuario(
	        @ModelAttribute UsuarioDto usuarioDto,
	        @RequestParam(value = "imagenPerfil", required = false) MultipartFile imagen,
	        Model model, Principal principal) {
		try {
	        usuarioService.actualizarUsuario(usuarioDto, imagen);
	        return "redirect:/dashboard";
	    } catch (IOException e) {
	        model.addAttribute("error", "Error al subir la imagen: " + e.getMessage());
	    } catch (RuntimeException e) {
	        model.addAttribute("error", e.getMessage());
	    }
        	Usuario usuario = usuarioService.findByUsername(principal.getName());
	        model.addAttribute("usuario", usuarioDto);
	        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaDelUsuario(principal.getName());
	        model.addAttribute("cuenta", cuentaOpt.orElse(null));
	        return "user";
	    
	}


	///heasder foto perfil
	@GetMapping("/{id}/foto")
	public ResponseEntity<String> obtenerFotoPerfil(@PathVariable Integer id) {
	    Usuario usuario = usuarioRepository.findById(id).orElse(null);
	    if (usuario == null || usuario.getFotoPerfilBase64() == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(usuario.getFotoPerfilBase64());
	}




}
