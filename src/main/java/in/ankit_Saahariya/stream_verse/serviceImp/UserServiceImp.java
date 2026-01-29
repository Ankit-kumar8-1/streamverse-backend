package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dto.request.UserRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.UserResponse;
import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.enums.Role;
import in.ankit_Saahariya.stream_verse.exception.EmailAlreadyExistsException;
import in.ankit_Saahariya.stream_verse.exception.InvalidRoleException;
import in.ankit_Saahariya.stream_verse.service.EmailService;
import in.ankit_Saahariya.stream_verse.service.UserService;
import in.ankit_Saahariya.stream_verse.util.PaginationUtils;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
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

    @Override
    public PageResponse<UserResponse> getUsers(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page,size,"id");
        Page<UserEntity> userPage ;

        if(search != null && !search.trim().isEmpty()){
            userPage = userRepository.searchUsers(search.trim(),pageable);
        }else {
            userPage = userRepository.findAll(pageable);
        }

        return PaginationUtils.toPageResponse(userPage,UserResponse::fromEntity);

    }

    @Override
    public MessageResponse deleteUser(Long id, String currentUserEmail) {
        UserEntity existingUser = serviceUtil.getUserByIdOrThrow(id);

        if(existingUser.getEmail().equals(currentUserEmail)){
            throw  new RuntimeException("You can not delete your own account ");
        }

        ensureNotLastAdmin(existingUser,"delete");

        userRepository.deleteById(id);
        return new MessageResponse("User deleted successfully !");
    }

    @Override
    public MessageResponse toggleUserStatus(Long id, String currentUserEmail) {
        UserEntity user = serviceUtil.getUserByIdOrThrow(id);

        if(user.getEmail().equals(currentUserEmail)){
            throw new RuntimeException("You cannot deactivate your own account");
        }

        ensureNotLastActiveAdmin(user);
        user.setActive(!user.isActive());
        userRepository.save(user);
        return new MessageResponse("User status updated successfully !");
    }

    @Override
    public MessageResponse changeUserRole(Long id, UserRequest userRequest) {
        UserEntity user = serviceUtil.getUserByIdOrThrow(id);
        validateRole(userRequest.getRole());

        Role newRole = Role.valueOf(userRequest.getRole().toUpperCase());
        if(user.getRole() == Role.ADMIN && newRole == Role.USER){
            ensureNotLastAdmin(user,"change the role of");
        }

        user.setRole(newRole);
        userRepository.save(user);
        return new MessageResponse("user role updated successfully");
    }

    private void ensureNotLastAdmin(UserEntity existingUser, String operation) {
        if(existingUser.getRole() == Role.ADMIN){
            long adminCount = userRepository.countByRole(Role.ADMIN);
            if(adminCount<= 1){
                throw  new RuntimeException("Cannot"+ operation + "the last admin user");
            }
        }
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
