package br.com.alura.AluraFake.dto.task;

import jakarta.validation.constraints.Size;

public record TaskOptionDTO(
        boolean isCorrect,
        @Size(min = 4, max = 80, message = "A questão deve ter de 4 à 80 caracteres")
        String option
) {
}
