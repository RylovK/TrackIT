package org.example.trackit.Mapper;

import org.example.trackit.dto.LoginDTO;
import org.example.trackit.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(LoginDTO loginDTO);
    LoginDTO toLoginDTO(User user);


}
