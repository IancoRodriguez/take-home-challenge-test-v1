package takehomechallenge.service.db;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import takehomechallenge.dto.UserDto;
import takehomechallenge.exception.UserNotFoundException;
import takehomechallenge.mapper.IUserMapper;
import takehomechallenge.model.User;
import takehomechallenge.repository.IUserRepository;
import takehomechallenge.security.jwt.JwtUtil;
import takehomechallenge.service.IUserService;

import java.util.List;
import java.util.Optional;

@Service

public class UserServiceJpa implements IUserService {

//    @Autowired
//    private IUsuarioRepository userRepository;

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;



    public UserServiceJpa(IUserRepository userRepository,
                          IUserMapper userMapper,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

    }

    @Override
    public UserDto save(UserDto dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toDto(user);
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
        // search user by id
        Optional<User> existingUser  = userRepository.findById(id);
        if(existingUser .isEmpty()){
             throw  new UserNotFoundException("User not found");
        }

        // update existing user
        User user = existingUser.get();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        // save
        User updatedUser = userMapper.toEntity(userDto);
        userRepository.save(updatedUser);

        return Optional.of(userMapper.toDto(updatedUser));

    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto);
    }


}
