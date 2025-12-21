package br.com.alura.AluraFake.dto.task;

import br.com.alura.AluraFake.dto.course.CourseDTO;

import java.util.List;

public record TaskDTO(
        Long id,
        String statement,
        Integer order,
        CourseDTO course,
        List<TaskOptionDTO> taskOptionDTOS
) {
}
