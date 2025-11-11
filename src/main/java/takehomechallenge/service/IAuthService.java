package takehomechallenge.service;

import takehomechallenge.dto.UserDto;
import takehomechallenge.model.User;

public interface IAuthService {
    UserDto register(String email, String password);
    String login(String email, String password);
}
