package com.finanzas.ia.finanzas_ia.service;

import java.util.List;

import com.finanzas.ia.finanzas_ia.dto.TipDto;
import com.finanzas.ia.finanzas_ia.dto.TipResponseDto;
import com.finanzas.ia.finanzas_ia.entity.Tip;

public interface TipService {

	TipResponseDto procesarPregunta(TipDto tip);
    List<Tip> obtenerHistorial(Integer usuarioId);
}
