package in.ankit_Saahariya.stream_verse.controller;


import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.UserResponse;
import in.ankit_Saahariya.stream_verse.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final  UserService userService;

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)String search
        ){
            return ResponseEntity.ok(userService.getUsers(page,size,search));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id, Authentication authentication){
        String currentUserEmail= authentication.getName();
        return ResponseEntity.ok(userService.deleteUser(id,currentUserEmail));
    }

}
