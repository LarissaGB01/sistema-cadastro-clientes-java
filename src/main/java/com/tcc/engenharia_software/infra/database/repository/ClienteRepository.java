package com.tcc.engenharia_software.infra.database.repository;

import com.tcc.engenharia_software.infra.database.entity.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Clientes, Long> {
    boolean existsByCpf(String cpf);
}
