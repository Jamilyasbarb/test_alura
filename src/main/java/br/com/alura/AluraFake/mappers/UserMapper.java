package br.com.alura.AluraFake.mappers;

import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.dto.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDtoFromEntity(User user){
        return new UserDTO(
                user.getId(),
                user.getName()
        );
    }
}
