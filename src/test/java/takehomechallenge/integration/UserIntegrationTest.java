package takehomechallenge.integration;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import takehomechallenge.dto.UserDto;
import takehomechallenge.model.User;
import takehomechallenge.repository.IUserRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles("test") // usa application-test.properties
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IUserRepository  userRepository;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();// limpia la DB antes de cada test
        // agregamos un usuario de prueba
    }

    @Test
    void findAll_usersExist_returnsList(){
        User user = new User();
        user.setUsername("juan123");
        user.setEmail("juan@mail.com");
        user.setPassword("12345");
        userRepository.save(user);
        //Hacemos geta la api/finall
        ResponseEntity<UserDto[]> response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/api/findall",
                UserDto[].class
        );
        //Validamos que devuelva 200 ok
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //convertimos a lista
        List<UserDto> users = Arrays.asList(response.getBody());

        assertFalse(users.isEmpty());
        assertEquals("juan123", users.get(0).getUsername());
        assertEquals("juan@mail.com", users.get(0).getEmail());
    }


    // ---------------------------------------------
    // 2️⃣ POST /api/save
    // --------------------------------------------
    @Test
    void save_validUser_returnsCreated(){
        UserDto userDto = new UserDto();
        userDto.setUsername("maria123");
        userDto.setEmail("maria@mail.com");

        ResponseEntity<UserDto> response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/api/save",
                userDto, //body
                UserDto.class //type of class to turn into
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("maria123", response.getBody().getUsername());

        // verify that it was saved in the DB

        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 1);
        assertEquals("maria123", users.get(0).getUsername());
    }

    // ---------------------------------------------
    // 3️⃣ PUT /api/{id}
    // ---------------------------------------------

    @Test
    void updateExistingUser_returnsUpdated(){
        //user
        User user = new User();
        user.setUsername("olduser");
        user.setEmail("old@mail.com");
        user.setPassword("123");
        User savedUser = userRepository.save(user);

        //dto whit new data
        UserDto userDto = new UserDto();
        userDto.setUsername("newuser");
        userDto.setEmail("new@mail.com");

        //http request put
        HttpEntity<UserDto> request = new HttpEntity<>(userDto);
        ResponseEntity<UserDto> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/api/" + savedUser.getId(),
                HttpMethod.PUT,
                request,
                UserDto.class
        );

        // Validations
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newuser", response.getBody().getUsername());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals("new@mail.com", updatedUser.getEmail());

    }

    @Test
    void updatedNonExistingUser_returns404(){
        UserDto dto = new UserDto();
        dto.setUsername("fake");
        dto.setEmail("@fake");
        dto.setId(1);


        HttpEntity<UserDto> request = new HttpEntity<>(dto);
        ResponseEntity<UserDto> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/api/" + dto.getId(),
                HttpMethod.PUT,
                request,
                UserDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    // ---------------------------------------------
    // 4️⃣ DELETE /api/{id}
    // ---------------------------------------------

    @Test
    void delete_existingUser_returns204(){ //no content

        User user = new User();
        user.setUsername("juan123");
        user.setPassword("12345");
        user.setEmail("@juan123");
        userRepository.save(user);

        ResponseEntity<Void> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/api/" + user.getId(),
                HttpMethod.DELETE,
                null, // delete has no body
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();


    }

    @Test
    void deleteNonExistentUser_returns404(){
        ResponseEntity<Void> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/api/9999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
