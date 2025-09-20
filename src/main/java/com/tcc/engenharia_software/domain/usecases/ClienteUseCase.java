package com.tcc.engenharia_software.domain.usecases;

import com.tcc.engenharia_software.common.exception.ApiException;
import com.tcc.engenharia_software.domain.models.ClienteDTO;
import com.tcc.engenharia_software.domain.models.Resultados.ClienteResponseDTO;
import com.tcc.engenharia_software.domain.models.Resultados;
import com.tcc.engenharia_software.infra.database.entity.Clientes;
import com.tcc.engenharia_software.infra.mq.ClientePublisher;
import com.tcc.engenharia_software.infra.database.repository.ClienteRepository;
import com.tcc.engenharia_software.infra.services.CpfValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteUseCase {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClientePublisher clientePublisher;

    @Autowired
    private CpfValidatorService cpfValidator;

    public Clientes salvarClienteV1(ClienteDTO clienteDTO) {
        return persistirCliente(clienteDTO);
    }

    public Clientes salvarClienteV2(ClienteDTO clienteDTO) throws URISyntaxException {
        cpfValidator.validarCpf(clienteDTO.getCpf());
        return persistirCliente(clienteDTO);
    }

    public Clientes salvarClienteV3(ClienteDTO clienteDTO) throws URISyntaxException {
        cpfValidator.validarCpf(clienteDTO.getCpf());
        var cliente = persistirCliente(clienteDTO);
        clientePublisher.enviarParaFila(cliente);
        return cliente;
    }

    public Resultados salvarClienteV4(List<ClienteDTO> listaClientes) throws URISyntaxException {
        var resultados = new Resultados();

        resultados.setResultados(listaClientes.parallelStream()
                .map(clienteDTO -> {
                    ClienteResponseDTO resposta = new ClienteResponseDTO();
                    resposta.setCpf(clienteDTO.getCpf());

                    try {
                        cpfValidator.validarCpf(clienteDTO.getCpf());
                        var cliente = persistirCliente(clienteDTO);
                        clientePublisher.enviarParaFila(cliente);

                        resposta.setNome(cliente.getNome());
                        resposta.setSistema(cliente.getSistema());
                        resposta.setStatus("Cliente cadastrado");
                    } catch (Exception e) {
                        System.err.println("Erro ao processar cliente " + clienteDTO.getCpf() + ": " + e.getMessage());
                        resposta.setStatus("Erro ao cadastrar cliente");
                        resposta.setError(e.getMessage());
                    }

                    return resposta;
                })
                .collect(Collectors.toList()));

        return resultados;
    }

    private Clientes persistirCliente(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new ApiException("CPF já cadastrado.");
        }

        Clientes clientes = new Clientes();
        clientes.setCpf(clienteDTO.getCpf());
        clientes.setNome(clienteDTO.getNome());
        clientes.setSistema("JAVA");

        return clienteRepository.save(clientes);
    }
}