package io.ucsal.agro.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InseminacaoDTO {

    private Integer inseminacaoId;

    @NotNull
    private LocalDate dataCobertura;

    @Size(max = 255)
    private String observacoes;

    @NotNull
    private InseminacaoMetodo metodo;

    @NotNull
    private InseminacaoSituacao situacao;

    @NotNull
    private Integer vaca;

    private Integer touro;

    private Integer veterinario;

}
