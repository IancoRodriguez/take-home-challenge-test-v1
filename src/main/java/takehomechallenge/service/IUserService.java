package takehomechallenge.service;

import takehomechallenge.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    void save(UserDto userDto);
    void deleteById(Integer id);
    List<UserDto> findAll();
    Optional<UserDto> findById(Integer id);
    Optional<UserDto> update(Integer id, UserDto userDto);

}
