package com.tcc.engenharia_software.infra.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.engenharia_software.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class CpfValidatorService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${invertexto.token}")
    private String token;

    @Value("${invertexto.url}")
    private String baseUrl;

    public void validarCpf(String cpf) {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(baseUrl)
                    .queryParam("token", token)
                    .queryParam("value", cpf)
                    .encode() // <-- importante
                    .build()
                    .toUri();

            System.out.println("Chamando URI: " + uri);

            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            JsonNode json = new ObjectMapper().readTree(response.getBody());
            if (!json.get("valid").asBoolean()) {
                throw new ApiException("CPF inválido");
            }
        } catch (Exception e) {
            throw new ApiException("Erro ao validar CPF: " + e);
        }
    }
}
