package io.ucsal.agro.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PartoDTO {

    private Integer partoId;

    @NotNull
    private LocalDate dataParto;

    @Size(max = 255)
    private String observacoes;

    @NotNull
    private Integer inseminacao;

}
