package com.tcc.engenharia_software.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroTratado {
    private String motivo;

    public ErroTratado(String motivo) {
        this.motivo = motivo;
    }
}