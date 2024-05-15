package io.ucsal.agro.domain;

import io.ucsal.agro.model.AnimalRaca;
import io.ucsal.agro.model.AnimalSituacao;
import io.ucsal.agro.model.AnimalTipo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Animal {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer animalId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalTipo tipo;

    @Column
    @Enumerated(EnumType.STRING)
    private AnimalRaca raca;

    @Column
    private LocalDate dataNascimento;

    @Column
    private String origem;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalSituacao situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parto_id")
    private Parto parto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mae_id")
    private Animal mae;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pai_id")
    private Animal pai;

}
