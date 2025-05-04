package com.finanzas.ia.finanzas_ia.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.service.CuentaService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;


    @PostMapping("/register")
    public ResponseEntity<?> crearCuenta(@RequestBody CuentaDto cuentaDTO) {
        try {
            cuentaService.registro(cuentaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cuenta creada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    
    @GetMapping("/mi-cuenta")
    public ResponseEntity<Map<String, Object>> obtenerMiCuenta() {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaDelUsuario();

            if (cuentaOpt.isPresent()) {
                Cuenta cuenta = cuentaOpt.get();
                CuentaDto dto = new CuentaDto(
                    cuenta.getId(),
                    cuenta.getNombre(),
                    cuenta.getDescripcion(),
                    cuenta.getIngreso(),
                    cuenta.getFecha()
                );
                response.put("status", "ok");
                response.put("data", dto);
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", "No se encontró una cuenta para el usuario.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", " Error al obtener la cuenta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
