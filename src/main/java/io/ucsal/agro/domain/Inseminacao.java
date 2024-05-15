package io.ucsal.agro.domain;

import io.ucsal.agro.model.InseminacaoMetodo;
import io.ucsal.agro.model.InseminacaoSituacao;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Inseminacao {

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
    private Integer inseminacaoId;

    @Column(nullable = false)
    private LocalDate dataCobertura;

    @Column
    private String observacoes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InseminacaoMetodo metodo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InseminacaoSituacao situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaca_id", nullable = false)
    private Animal vaca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "touro_id")
    private Animal touro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id")
    private Veterinario veterinario;

    @OneToMany(mappedBy = "inseminacao")
    private Set<Parto> inseminacaoPartoes;

}
