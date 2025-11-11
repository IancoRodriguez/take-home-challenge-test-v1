package takehomechallenge.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import takehomechallenge.dto.UserDto;
import takehomechallenge.service.IUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "Users", description = "User crud")
@AllArgsConstructor
public class UserController {

//    @Autowired
//    private IUserService userService;

//    public ResponseEntity<List<User>> getAllUsers() {
//        return new ResponseEntity<>(this.userService.findAll(), HttpStatus.OK);
//    }

    private final IUserService userService;


    @GetMapping("/findall")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> user = userService.findAll();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/save")
    @Operation(summary = "Create new user")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.save(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing user")
    public ResponseEntity<UserDto> update(@PathVariable int id, @RequestBody UserDto userDto) {
        System.out.println("🟢 Controller - ID: " + id);

        Optional<UserDto> updatedUser = userService.update(id, userDto);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get()); // ✅ 200 OK
        } else {
            return ResponseEntity.notFound().build(); // ✅ 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<UserDto> delete(@PathVariable int id) {
        Optional<UserDto> user = userService.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build(); // http 404
        }

        userService.deleteById(user.get().getId());
        return ResponseEntity.noContent().build(); //http 204
    }


}
