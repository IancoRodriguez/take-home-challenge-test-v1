package takehomechallenge.service.db;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import takehomechallenge.dto.UserDto;
import takehomechallenge.mapper.IUserMapper;
import takehomechallenge.model.User;
import takehomechallenge.repository.IUserRepository;
import takehomechallenge.security.jwt.JwtUtil;
import takehomechallenge.service.IAuthService;
@Service
public class AuthServiceJpa implements IAuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IUserMapper userMapper;

    public AuthServiceJpa(IUserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }



    @Override
    public UserDto register(String email, String password) {
        if(userRepository.existsUserByEmail(email)){
            throw new UsernameNotFoundException("User with email " + email + " already exists");
        }

        User user = User.create(email, password);

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public String login(String email, String password) {
        // Spring security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        return jwtUtil.generateJwtToken(email);
    }
}
