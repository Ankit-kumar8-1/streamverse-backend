package in.ankit_Saahariya.stream_verse.controller;


import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dto.request.EmailRequest;
import in.ankit_Saahariya.stream_verse.dto.request.LoginRequest;
import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.EmailValidationResponse;
import in.ankit_Saahariya.stream_verse.dto.response.ForgotPasswordResponse;
import in.ankit_Saahariya.stream_verse.dto.response.LoginResponse;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.service.AuthService;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ServiceUtil serviceUtil;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(authService.signup(userRequest));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyAccount(
            @RequestParam String email, @RequestParam String token){
        UserEntity user = serviceUtil.getUserByEmailOrThrow(email);

        if(!token.equals(user.getVerificationToken())){
            throw new RuntimeException("Invalid Verification Token !");
        }

        if(user.getVerificationTokenExpire().isBefore(Instant.now())){
            throw  new RuntimeException("Verification Link iS Expire !");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpire(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Email verified Successfully. you can Login now."));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest.getEmail(),loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-email")
    public ResponseEntity<EmailValidationResponse> validateEmail(@RequestParam String email){
        return ResponseEntity.ok(authService.validateEmail(email));
    }

    @PostMapping("resend/verification")
    public ResponseEntity<MessageResponse> resendVerification(@Valid @RequestBody EmailRequest emailRequest){
        return ResponseEntity.ok(authService.resendVerification(emailRequest));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody  EmailRequest emailRequest){
        return ResponseEntity.ok(authService.forgotPassword(emailRequest.getEmail()));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPasswordAllow(@RequestParam String token){
        return ResponseEntity.ok(authService.verifyResetToken(token));
    }
}
