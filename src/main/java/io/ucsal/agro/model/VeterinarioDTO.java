package io.ucsal.agro.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VeterinarioDTO {

    private Integer veterinarioId;

    @NotNull
    @Size(max = 255)
    private String nome;

    private String qualificacoes;

    @Size(max = 255)
    private String contato;

}
