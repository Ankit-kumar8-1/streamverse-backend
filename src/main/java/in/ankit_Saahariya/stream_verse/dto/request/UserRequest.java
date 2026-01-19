package in.ankit_Saahariya.stream_verse.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String email;

    private  String password;

    private String fullName;

    private String role;

    private boolean isActive;
}
