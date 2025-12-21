package br.com.alura.AluraFake.dto.course;

import br.com.alura.AluraFake.dto.user.UserDTO;

public record CourseDTO(
        Long id,
        String title,
        UserDTO userDTO

) {
}
