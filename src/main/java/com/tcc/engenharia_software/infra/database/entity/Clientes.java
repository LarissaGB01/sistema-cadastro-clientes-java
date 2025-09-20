package com.tcc.engenharia_software.infra.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Clientes {

    @Id
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, length = 30)
    private String nome;

    @Column(nullable = false, length = 10)
    private String sistema;
}
