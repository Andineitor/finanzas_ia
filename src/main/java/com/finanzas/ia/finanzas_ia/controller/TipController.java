package com.finanzas.ia.finanzas_ia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.dto.TipDto;
import com.finanzas.ia.finanzas_ia.dto.TipResponseDto;
import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Tip;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.TipService;

@Controller
@RequestMapping("/tip")
public class TipController {

    private final TipService tipService;
    private final CuentaService cuentaService;

    public TipController(TipService tipService, CuentaService cuentaService) {
        this.tipService = tipService;
        this.cuentaService = cuentaService;
    }

    @PostMapping
    @ResponseBody
    public TipResponseDto enviarPregunta(
        @RequestBody TipDto tipRequest,
        @AuthenticationPrincipal Usuario usuarioAuth) {

        UsuarioDto usuarioDto = new UsuarioDto(
            usuarioAuth.getId(),
            usuarioAuth.getUsername(),
            null, // no envíes la password
            usuarioAuth.getNombre(),
            usuarioAuth.getApellido(),
            usuarioAuth.getEdad(),
            usuarioAuth.getEmail(),
            usuarioAuth.getSexo(),
            null,
            usuarioAuth.getRole()
        );

        // Obtén la cuenta del usuario
     // Obtiene la entidad Cuenta (no DTO)
        Cuenta cuenta = cuentaService.obtenerCuentaDelUsuario(usuarioAuth.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));

        // Convertir Cuenta (entidad) a CuentaDto
        CuentaDto cuentaDto = new CuentaDto(
            cuenta.getId(),
            cuenta.getNombre(),
            cuenta.getDescripcion(),
            cuenta.getIngreso(),
            cuenta.getFecha()
        );


        TipDto tipCompleto = new TipDto(
            usuarioDto,
            cuentaDto,
            tipRequest.getPregunta(),
            tipRequest.getRespuesta()
        );

        return tipService.procesarPregunta(tipCompleto);
    }

    @GetMapping("/dashboard")
    public String mostrarChatFinanzas(Model model, @AuthenticationPrincipal Usuario usuario) {
        model.addAttribute("usuario", usuario);

        // Cargar historial
        List<Tip> historial = tipService.obtenerHistorial(usuario.getId());
        model.addAttribute("historial", historial);

        return "tips";
    }


}
