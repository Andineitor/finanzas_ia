package com.finanzas.ia.finanzas_ia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            model.addAttribute("usuario", request);
            return "register";
        }
    }



	@GetMapping("/dashboard")
	public String mostrarDashboard(Model model, Principal principal) {
	    String username = principal.getName();

	    Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaDelUsuario(username);
	    Cuenta cuenta = cuentaOpt.orElseGet(() -> {
	        Cuenta c = new Cuenta();
	        c.setIngreso(0);
	        return c;
	    });
	    model.addAttribute("cuenta", cuenta);

	    Integer totalGastos = cuentaOpt.isPresent()
	            ? transServ.calcularTotalGastos(cuenta)
	            : 0;
	    model.addAttribute("totalGastos", totalGastos);

	    Map<String, Integer> gastoCategorias = cuentaOpt.isPresent()
	            ? transServ.obtenerGastosPorCategoria(cuenta)
	            : new HashMap<>();
	    model.addAttribute("gastoCategorias", gastoCategorias);

	    List<Map.Entry<String, Integer>> categoriasMayores40 = gastoCategorias.entrySet()
	            .stream()
	            .filter(e -> e.getValue() > 40)
	            .sorted((a, b) -> b.getValue() - a.getValue())
	            .toList();
	    model.addAttribute("categoriasMayores40", categoriasMayores40);
	    model.addAttribute("categoriasMayores40Chunks", partirLista(categoriasMayores40, 4));

	    Usuario usuario = userService.findByUsername(username);
	    model.addAttribute("usuario", usuario);

	    Integer usuarioId = usuario.getId();

	    List<Object[]> ingresosYGastos = transServ.obtenerIngresosYGastosPorFecha(usuarioId);

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd", new Locale("es", "ES"));

	    List<String> fechas = new ArrayList<>();
	    List<Integer> ingresosPorDia = new ArrayList<>();
	    List<Integer> gastosPorDia = new ArrayList<>();

	    for (Object[] row : ingresosYGastos) {
	        LocalDate fecha = null;

	        if (row[0] instanceof java.sql.Date) {
	            fecha = ((java.sql.Date) row[0]).toLocalDate();
	        } else if (row[0] instanceof LocalDate) {
	            fecha = (LocalDate) row[0];
	        } else if (row[0] instanceof String) {
	            try {
	                fecha = LocalDate.parse((String) row[0]);
	            } catch (Exception e) {
	                fecha = null;
	            }
	        }

	        if (fecha != null) {
	            String formateada = fecha.format(formatter);
	            // Capitalizar primera letra (porque algunos locales devuelven en minúsculas)
	            formateada = formateada.substring(0, 1).toUpperCase() + formateada.substring(1);
	            fechas.add(formateada);
	        } else {
	            fechas.add(row[0].toString());
	        }

	        ingresosPorDia.add(row[1] != null ? ((Number) row[1]).intValue() : 0);
	        gastosPorDia.add(row[2] != null ? ((Number) row[2]).intValue() : 0);
	    }

	    model.addAttribute("fechas", fechas);
	    model.addAttribute("ingresosPorDia", ingresosPorDia);
	    model.addAttribute("gastosPorDia", gastosPorDia);

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
