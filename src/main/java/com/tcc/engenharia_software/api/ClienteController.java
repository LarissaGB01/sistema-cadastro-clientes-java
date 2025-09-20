package com.tcc.engenharia_software.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.engenharia_software.domain.models.ClienteDTO;
import com.tcc.engenharia_software.domain.models.Resultados;
import com.tcc.engenharia_software.domain.usecases.ClienteUseCase;
import com.tcc.engenharia_software.infra.database.entity.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ClienteController {

    @Autowired
    private ClienteUseCase clienteUseCase;

    @PostMapping
    @RequestMapping("/v1/clientes")
    public ResponseEntity<Clientes> cadastrarClienteV1(@RequestBody ClienteDTO clienteDTO) throws JsonProcessingException {
        trataRequisicao(clienteDTO);
        Clientes clientes = clienteUseCase.salvarClienteV1(clienteDTO);
        return new ResponseEntity<>(clientes, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/v2/clientes")
    public ResponseEntity<Clientes> cadastrarClienteV2(@RequestBody ClienteDTO clienteDTO) throws JsonProcessingException, URISyntaxException {
        trataRequisicao(clienteDTO);
        Clientes clientes = clienteUseCase.salvarClienteV2(clienteDTO);
        return new ResponseEntity<>(clientes, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/v3/clientes")
    public ResponseEntity<Clientes> cadastrarClienteV3(@RequestBody ClienteDTO clienteDTO) throws JsonProcessingException, URISyntaxException {
        trataRequisicao(clienteDTO);
        Clientes clientes = clienteUseCase.salvarClienteV3(clienteDTO);
        return new ResponseEntity<>(clientes, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/v4/clientes")
    public ResponseEntity<Resultados> cadastrarClienteV4(@RequestBody List<ClienteDTO> clienteDTO) throws JsonProcessingException, URISyntaxException {
        clienteDTO.forEach(cliente -> {
            try {
                trataRequisicao(cliente);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        Resultados clientes = clienteUseCase.salvarClienteV4(clienteDTO);
        return new ResponseEntity<>(clientes, HttpStatus.CREATED);
    }

    private static void trataRequisicao(ClienteDTO clienteDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Requisição recebida: " + mapper.writeValueAsString(clienteDTO));
        clienteDTO.setCpf(clienteDTO.getCpf().replace(".", "").replace("-", ""));
        clienteDTO.setNome(clienteDTO.getNome().substring(0, Math.min(clienteDTO.getNome().length(), 30)));
    }
}