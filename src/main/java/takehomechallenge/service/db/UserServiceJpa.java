package takehomechallenge.service.db;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import takehomechallenge.dto.UserDto;
import takehomechallenge.mapper.IUserMapper;
import takehomechallenge.model.User;
import takehomechallenge.repository.IUsuarioRepository;
import takehomechallenge.service.IUserService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceJpa implements IUserService {

//    @Autowired
//    private IUsuarioRepository userRepository;

    private final IUsuarioRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    public void save(UserDto dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toDto(user))
                .toList();
    }

    @Override
    public Optional<UserDto> findById(Integer id) {
        return userRepository.findById(id)
                .map(user -> userMapper.toDto(user));
    }

    @Override
    public Optional<UserDto> update(Integer id, UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);

        return Optional.of(userMapper.toDto(user));
    }


}
