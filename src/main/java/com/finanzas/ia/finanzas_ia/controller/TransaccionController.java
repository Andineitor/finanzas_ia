package com.finanzas.ia.finanzas_ia.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finanzas.ia.finanzas_ia.entity.Transaccion;
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
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha,
            Principal principal,
            RedirectAttributes redirectAttrs
    ) {
        try {
            transServ.registrarGasto(principal.getName(), descripcion, cantidad, categoriaId, fecha);
        } catch (IllegalArgumentException ex) {
            redirectAttrs.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/cuenta/dashboard";
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute Transaccion transaccion, RedirectAttributes redirectAttrs) {
        try {
            transServ.editarTransaccion(transaccion);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("errorMessage", "Error al editar: " + e.getMessage());
            return "redirect:/transaccion/editar";
        }
        return "redirect:/cuenta/dashboard";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer id) {
        transServ.eliminarTransaccion(id);
        return "redirect:/cuenta/dashboard";
    }
}
