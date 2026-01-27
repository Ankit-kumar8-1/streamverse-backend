package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dto.request.EmailRequest;
import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.EmailValidationResponse;
import in.ankit_Saahariya.stream_verse.dto.response.ForgotPasswordResponse;
import in.ankit_Saahariya.stream_verse.dto.response.LoginResponse;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.enums.Role;
import in.ankit_Saahariya.stream_verse.exception.*;
import in.ankit_Saahariya.stream_verse.security.JwtUtil;
import in.ankit_Saahariya.stream_verse.service.AuthService;
import in.ankit_Saahariya.stream_verse.service.EmailService;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final  EmailService emailService;
    private final JwtUtil jwtUtil;
    private final  ServiceUtil serviceUtil;

    @Override
    public MessageResponse signup(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists !");
        }
        UserEntity user = new UserEntity();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        user.setRole(Role.USER);
        user.setActive(true);
        user.setEmailVerified(false);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpire(Instant.now().plusSeconds(3));
        userRepository.save(user);
        emailService.sendVerificationEmail(userRequest.getEmail(),verificationToken);

        return new MessageResponse("Registration successful ! Please check you email to verify your account");
    }


    @Override
    public LoginResponse login(String email, String password) {
        UserEntity user = userRepository.
                findByEmail(email)
                .filter(u-> passwordEncoder.matches(password,u.getPassword()))
                .orElseThrow(()-> new BadCredentialsException("Invalid Email or Password"));

        if(!user.isActive()){
            throw new AccountDeactivatedException("Your account has been deactivated, Please contact support for assistance.");
        }
        if(!user.isEmailVerified()){
            throw new EmailNotVerifiedException("Please verify you email address before log_in. Check your inbox for the verification link .");
        }

        final String token = jwtUtil.generateToken(user.getEmail(),user.getRole().name());
        return new LoginResponse(token,user.getEmail(), user.getFullName(),user.getRole().name());
    }

    @Override
    public EmailValidationResponse validateEmail(String email) {
        boolean exists =  userRepository.existsByEmail(email);
        return new EmailValidationResponse(exists,!exists);
    }

    @Override
    public MessageResponse resendVerification(EmailRequest emailRequest) {
        UserEntity user = userRepository.findByEmail(emailRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User not Found in database!"));

        if(user.isEmailVerified()){
            throw new EmailAlreadyExistsException("Email Already Verified ! ");
        }

        // Token abhi bhi valid hai → resend mat karo
        if (user.getVerificationTokenExpire() != null &&
                user.getVerificationTokenExpire().isAfter(Instant.now())) {

            throw new RuntimeException(
                    "Verification link is already sent and still valid"
            );
        }

        String newToken = UUID.randomUUID().toString();

        user.setVerificationToken(newToken);
        user.setVerificationTokenExpire(Instant.now().plusSeconds(43200));

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(),newToken);
        return new MessageResponse("Verification link has been resent to your email");
    }

    @Override
    public MessageResponse forgotPassword(String email) {
        UserEntity user = serviceUtil.getUserByEmailOrThrow(email);
        String resetToken = UUID.randomUUID().toString();

        user.setPasswordRestToken(resetToken);
        user.setPasswordRestTokenExpire(Instant.now().plusSeconds(3600));
        user.setPasswordResetVerified(false);

        userRepository.save(user);

        emailService.sendPasswordResetEmail(email,resetToken);
        return new MessageResponse("Password reset email sent successfully !  Please check your Email box");
    }

    @Override
    public ForgotPasswordResponse verifyResetToken( String token) {
        UserEntity user = userRepository.findByPasswordRestToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid or expired password reset token"));

        if (user.getPasswordRestTokenExpire()==null || Instant.now().isAfter(user.getPasswordRestTokenExpire())){
            throw new TokenExpiredException("Password reset token has expired. Please request a new one.");
        }

        user.setPasswordResetVerified(true);
        userRepository.save(user);
        return new ForgotPasswordResponse(true,"Password reset token is valid. You can now reset your password.");
    }

    @Override
    public MessageResponse resetPassword(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordRestToken(token)
                .orElseThrow(()-> new InvalidTokenException("Invalid or expired password reset token"));

        if (user.getPasswordRestTokenExpire() ==null
                || user.getPasswordRestTokenExpire().isBefore(Instant.now()) ){
            throw new InvalidTokenException("Password reset token has expired. Please request a new one.");
        }

        if (!user.getPasswordResetVerified()) {
            throw new RuntimeException("Please verify reset link first");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordRestTokenExpire(null);
        user.setPasswordRestToken(null);
        user.setPasswordResetVerified(false);

        userRepository.save(user);

        return  new MessageResponse("Password reset successfully ! , You can now login with your new Password !");
    }

    @Override
    public MessageResponse changePassword(String email, String currentPassword, String newPassword) {
        UserEntity user=  serviceUtil.getUserByEmailOrThrow(email);

        if(!passwordEncoder.matches(currentPassword,user.getPassword())){
            throw new InValidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password change successfully !");
    }

    @Override
    public LoginResponse currentUser(String email) {
        UserEntity user = serviceUtil.getUserByEmailOrThrow(email);
        return new LoginResponse(null,user.getEmail(),user.getFullName(),user.getRole().name());
    }
}
