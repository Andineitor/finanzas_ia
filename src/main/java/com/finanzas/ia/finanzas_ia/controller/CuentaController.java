package com.finanzas.ia.finanzas_ia.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.service.CategoriaService;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cuenta")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;// ← Asegúrate de tener esto

    /**
     * Muestra el dashboard con la cuenta del usuario, o el formulario si no tiene cuenta.
     */
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, Principal principal) {
        model.addAttribute("nombreUsuario", principal.getName());

        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaDelUsuario(principal.getName());
        cuentaOpt.ifPresent(c -> {
            model.addAttribute("cuenta", c);
            model.addAttribute("transacciones", c.getTransacciones()); 
        });
        
        Usuario usuario = usuarioService.findByUsername(principal.getName());
        model.addAttribute("usuario", usuario); 

        model.addAttribute("categorias", categoriaService.listarCategorias()); 

        return "count";
    }

    /**
     * Procesa la creación de la cuenta desde el formulario.
     */
    @PostMapping("/registrar")
    public String crearCuentaDesdeFormulario(@ModelAttribute CuentaDto cuentaDTO, Principal principal, Model model) {
        try {
            cuentaService.registroParaUsuario(cuentaDTO, principal.getName());
            return "redirect:/cuenta/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("nombreUsuario", principal.getName());
            model.addAttribute("error", e.getMessage());
            return "count";
        }
    }
    
    
    

}
