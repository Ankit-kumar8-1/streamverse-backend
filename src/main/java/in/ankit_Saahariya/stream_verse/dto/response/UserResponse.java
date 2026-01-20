package in.ankit_Saahariya.stream_verse.dto.response;


import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private  String fullName;
    private String role;
    private boolean isActive;
    private Instant createdAt;
    private  Instant updatedAt;

    public static UserResponse fromEntity(UserEntity user){
        return  UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
