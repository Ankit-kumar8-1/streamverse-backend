package in.ankit_Saahariya.stream_verse.service;

import in.ankit_Saahariya.stream_verse.dto.request.EmailRequest;
import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.EmailValidationResponse;
import in.ankit_Saahariya.stream_verse.dto.response.ForgotPasswordResponse;
import in.ankit_Saahariya.stream_verse.dto.response.LoginResponse;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import jakarta.validation.Valid;

public interface AuthService {

    MessageResponse signup(@Valid UserRequest userRequest);

    LoginResponse login(String email, String password);

    EmailValidationResponse validateEmail(String email);

    MessageResponse resendVerification(@Valid EmailRequest emailRequest);

    MessageResponse forgotPassword( String email);

    ForgotPasswordResponse verifyResetToken( String token);

    MessageResponse resetPassword( String token, String newPassword);

    MessageResponse changePassword(String email, String currentPassword,  String newPassword);

    LoginResponse currentUser(String email);
}
