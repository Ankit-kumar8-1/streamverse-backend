package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.enums.Role;
import in.ankit_Saahariya.stream_verse.exception.EmailAlreadyExistsException;
import in.ankit_Saahariya.stream_verse.exception.InvalidRoleException;
import in.ankit_Saahariya.stream_verse.service.EmailService;
import in.ankit_Saahariya.stream_verse.service.UserService;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final  ServiceUtil serviceUtil ;
    private final  EmailService emailService;

    @Override
    public MessageResponse createUser(UserRequest userRequest) {

        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw  new EmailAlreadyExistsException("Email Already Exists !");
        }
        validateRole(userRequest.getRole());
        UserEntity user = new UserEntity();
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        user.setRole(Role.valueOf(userRequest.getRole().toUpperCase()));
        user.setActive(true);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpire(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(userRequest.getEmail(),verificationToken);


        return new MessageResponse("User Created Successfully !");
    }

    @Override
    public MessageResponse updateUser(Long id, UserRequest userRequest) {
        UserEntity user = serviceUtil.getUserByIdOrThrow(id);

        ensureNotLastActiveAdmin(user);
        validateRole(userRequest.getRole());

        user.setFullName(userRequest.getFullName());
        user.setRole(Role.valueOf(userRequest.getRole()));
        userRepository.save(user);
        return new MessageResponse("User updated successfully !");
    }



    private void ensureNotLastActiveAdmin(UserEntity user) {
        if(user.isActive() && user.getRole()==Role.ADMIN ) {
            long activeAdminCount = userRepository.countByRoleAndIsActive(Role.ADMIN,true);
            if (activeAdminCount <= 1){
                throw  new RuntimeException("cannot deactivate the last active admin user");
            }
        }
    }

    private void validateRole(String role) {
        if(Arrays.stream(Role.values()).noneMatch(r-> r.name().equalsIgnoreCase(role))){
            throw new InvalidRoleException("Invalid role :"+role);
        }
    }
}
