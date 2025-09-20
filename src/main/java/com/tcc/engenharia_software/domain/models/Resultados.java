package com.tcc.engenharia_software.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Resultados {
    private List<ClienteResponseDTO> resultados;

    @Getter
    @Setter
    public static class ClienteResponseDTO {
        private String cpf;
        private String nome;
        private String sistema;
        private String status;
        private String error;
    }
}
