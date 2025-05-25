package com.finanzas.ia.finanzas_ia.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.finanzas.ia.finanzas_ia.dto.CuentaDto;
import com.finanzas.ia.finanzas_ia.dto.TipDto;
import com.finanzas.ia.finanzas_ia.dto.TipResponseDto;
import com.finanzas.ia.finanzas_ia.dto.UsuarioDto;
import com.finanzas.ia.finanzas_ia.entity.Tip;
import com.finanzas.ia.finanzas_ia.entity.Usuario;
import com.finanzas.ia.finanzas_ia.repository.TipRepository;
import com.finanzas.ia.finanzas_ia.repository.TransaccionRepository;
import com.finanzas.ia.finanzas_ia.repository.UsuarioRepository;
import com.finanzas.ia.finanzas_ia.service.TipService;
import com.google.gson.*;

import lombok.RequiredArgsConstructor;
import okhttp3.*;

@Service
@RequiredArgsConstructor
public class TipServiceImpl implements TipService {

    private final TipRepository tipRepo;
    private final UsuarioRepository usuarioRepo;
    private final TransaccionRepository transaccionRepo;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    @Value("${gemini.api.key}")
    private String apiKey;

    @Override
    public TipResponseDto procesarPregunta(TipDto tip) {
        UsuarioDto u = tip.getUsuario();
        CuentaDto c = tip.getCuenta();

        Usuario usuarioEntity = usuarioRepo.findById(u.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Object[]> gastosPorCategoria = transaccionRepo.sumaGastosPorCategoria(u.getId());
        StringBuilder gastosTexto = new StringBuilder();
        for (Object[] g : gastosPorCategoria) {
            gastosTexto.append("- ").append(g[0]).append(": $").append(g[1]).append("\n");
        }

        String prompt = String.format("""
            El siguiente usuario necesita un consejo financiero personalizado.

            Perfil del usuario:
            - Nombre: %s %s
            - Edad: %d
            - Sexo: %s
            - Saldo actual: $%d

            Gastos por categoría:
            %s

            Pregunta del usuario:
            "%s"

            Brinda un consejo claro, útil, aplicable y corto.
        """, u.getNombre(), u.getApellido(), u.getEdad(),
             u.getSexo(), c.getIngreso(), gastosTexto, tip.getPregunta());

        // Armar el body para Gemini
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contents);

        RequestBody body = RequestBody.create(
            gson.toJson(requestBody),
            MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(ENDPOINT + "?key=" + apiKey)
                .post(body)
                .build();

        String respuestaIA;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error de Gemini: " + response);
            }

            String jsonResp = response.body().string();
            JsonObject parsed = gson.fromJson(jsonResp, JsonObject.class);

            respuestaIA = parsed
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        } catch (IOException e) {
            respuestaIA = "Hubo un problema con Gemini: " + e.getMessage();
        }

        tipRepo.save(new Tip(usuarioEntity, "user", tip.getPregunta(), LocalDateTime.now()));
        tipRepo.save(new Tip(usuarioEntity, "ai", respuestaIA.trim(), LocalDateTime.now()));

        System.out.println("Prompt generado: \n" + prompt);
        System.out.println("Request JSON:\n" + gson.toJson(requestBody));

        return new TipResponseDto(respuestaIA.trim());
    }

    @Override
    public List<Tip> obtenerHistorial(Integer usuarioId) {
        Usuario usuarioEntity = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return tipRepo.findByUsuarioOrderByTimestampAsc(usuarioEntity);
    }
}
