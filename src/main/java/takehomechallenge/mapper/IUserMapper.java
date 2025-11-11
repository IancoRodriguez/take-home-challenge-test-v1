package takehomechallenge.mapper;

import org.mapstruct.Mapping;
import takehomechallenge.dto.UserDto;
import takehomechallenge.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    // entity -> dto: copy id
    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);

    // dto -> entity: IGNORE id (not try to set)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto userDto);
}