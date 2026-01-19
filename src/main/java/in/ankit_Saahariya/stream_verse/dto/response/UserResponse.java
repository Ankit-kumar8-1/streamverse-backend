package in.ankit_Saahariya.stream_verse.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private  String fullName;
    private String role;
    private boolean isActive;
    private Instant createdAt;
    private  Instant updatedAt;

}
