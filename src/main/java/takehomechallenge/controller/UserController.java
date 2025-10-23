package takehomechallenge.controller;
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
@AllArgsConstructor
public class UserController {

//    @Autowired
//    private IUserService userService;

//    public ResponseEntity<List<User>> getAllUsers() {
//        return new ResponseEntity<>(this.userService.findAll(), HttpStatus.OK);
//    }

    private final IUserService userService;


    @GetMapping("/findall")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> user = userService.findAll();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        userService.save(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable int id, @RequestBody UserDto userDto) {
        Optional<UserDto> op = userService.findById(id);
        if(op.isEmpty()){
            return ResponseEntity.notFound().build(); // 404
        }
        userService.update(id,userDto);

        return ResponseEntity.ok(op.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> delete(@PathVariable int id) {
        Optional<UserDto> user = userService.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build(); // http 404
        }

        userService.deleteById(user.get().getId());
        return ResponseEntity.noContent().build(); //http 204
    }


}
