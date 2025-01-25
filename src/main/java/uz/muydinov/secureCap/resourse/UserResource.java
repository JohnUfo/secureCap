package uz.muydinov.secureCap.resourse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.muydinov.secureCap.domain.HttpResponse;
import uz.muydinov.secureCap.domain.User;
import uz.muydinov.secureCap.dto.UserDTO;
import uz.muydinov.secureCap.service.UserService;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid User user) {
        UserDTO userDTO = userService.createUser(user);

        Map<String, Object> data = new HashMap<>();
        data.put("user", userDTO);

        HttpResponse response = new HttpResponse();
        response.setTimeStamp(now().toString());
        response.setData(data);
        response.setMessage("User created");
        response.setStatus(HttpStatus.CREATED);
        response.setStatusCode(HttpStatus.CREATED.value());

        return ResponseEntity.created(getUri()).body(response);
    }

    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
}
