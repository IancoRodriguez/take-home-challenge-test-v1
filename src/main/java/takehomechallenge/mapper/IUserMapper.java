package takehomechallenge.mapper;

import takehomechallenge.dto.UserDto;
import takehomechallenge.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
