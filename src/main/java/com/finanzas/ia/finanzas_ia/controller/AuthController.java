package com.finanzas.ia.finanzas_ia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Cuenta;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.repository.UsuarioRepository;
import com.finanzas.ia.finanzas_ia.service.CuentaService;
import com.finanzas.ia.finanzas_ia.service.TransaccionService;
import com.finanzas.ia.finanzas_ia.service.UsuarioService;
import com.finanzas.ia.finanzas_ia.service.impl.TransaccionServiceImple;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final UsuarioService userService;

	private final UsuarioRepository userRepo;
    private final TransaccionService transServ;
    private final CuentaService cuentaService;

	@GetMapping("/login-user")
    public String showLoginPage() {
        return "login"; // login.html en templates
    }

	@GetMapping("/register-user")
    public String showRegisterPage(Model model) {
        model.addAttribute("usuarioDto", new UsuarioDto());
        return "register";
    }

	@PostMapping("/register-user")
    public String registerUser(@ModelAttribute("usuarioDto") UsuarioDto request, Model model) {
        try {
            userService.register(request);
            return "redirect:/login-user";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }



	@GetMapping("/dashboard")
	public String mostrarDashboard(Model model, Principal principal) {
	    String username = principal.getName();

	    // Obtener la cuenta del usuario (o crear una vacía si no hay)
	    Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaDelUsuario(username);
	    Cuenta cuenta = cuentaOpt.orElseGet(() -> {
	        Cuenta c = new Cuenta();
	        c.setIngreso(0);
	        return c;
	    });
	    model.addAttribute("cuenta", cuenta);

	    // Calcular total de gastos, asegurando que nunca sea null
	    Integer totalGastos = cuentaOpt.isPresent()
	            ? transServ.calcularTotalGastos(cuenta)
	            : 0;
	    model.addAttribute("totalGastos", totalGastos);

	    // Gastos por categoría, vacíos si no hay cuenta
	    Map<String, Integer> gastoCategorias = cuentaOpt.isPresent()
	            ? transServ.obtenerGastosPorCategoria(cuenta)
	            : new HashMap<>();
	    model.addAttribute("gastoCategorias", gastoCategorias);

		// Filtrar categorías con gasto mayor a 40 para las tarjetas
		List<Map.Entry<String, Integer>> categoriasMayores40 = gastoCategorias.entrySet()
				.stream()
				.filter(e -> e.getValue() > 40)
				.sorted((a, b) -> b.getValue() - a.getValue())
				.toList();
		model.addAttribute("categoriasMayores40", categoriasMayores40);
		model.addAttribute("categoriasMayores40Chunks", partirLista(categoriasMayores40, 4));

	    // Usuario
	    Usuario usuario = userService.findByUsername(username);
	    model.addAttribute("usuario", usuario);

	    return "dashboard";
	}
	// MÉTODO AUXILIAR para reemplazar #lists.partition
	private List<List<Map.Entry<String, Integer>>> partirLista(List<Map.Entry<String, Integer>> lista, int tamaño) {
		List<List<Map.Entry<String, Integer>>> resultado = new ArrayList<>();
		for (int i = 0; i < lista.size(); i += tamaño) {
			resultado.add(lista.subList(i, Math.min(i + tamaño, lista.size())));
		}
		return resultado;
	}

}
