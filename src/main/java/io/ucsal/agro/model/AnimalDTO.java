package io.ucsal.agro.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnimalDTO {

    private Integer animalId;

    @NotNull
    @Size(max = 255)
    private String nome;

    @NotNull
    private AnimalTipo tipo;

    private AnimalRaca raca;

    private LocalDate dataNascimento;

    @Size(max = 255)
    private String origem;

    @NotNull
    private AnimalSituacao situacao;

    private Integer parto;

    private Integer mae;

    private Integer pai;

}
