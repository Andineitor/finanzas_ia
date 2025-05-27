package com.finanzas.ia.finanzas_ia.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finanzas.ia.finanzas_ia.service.TransaccionService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/transaccion")
@RequiredArgsConstructor
public class TransaccionController {
	
	private final TransaccionService transServ;


    @PostMapping("/ingreso")
    public String agregarIngreso(@RequestParam Integer cantidad, Principal principal) {
    	transServ.agregarIngreso(principal.getName(), cantidad);
    	
    	
        return "redirect:/cuenta/dashboard";
    }

    @PostMapping("/gasto")
    public String registrarGasto(
            @RequestParam String descripcion,
            @RequestParam Integer cantidad,
            @RequestParam Integer categoriaId,
            Principal principal,
            RedirectAttributes redirectAttrs
    ) {
        try {
            transServ.registrarGasto(principal.getName(), descripcion, cantidad, categoriaId);
        } catch (IllegalArgumentException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/cuenta/dashboard";
    }
}
