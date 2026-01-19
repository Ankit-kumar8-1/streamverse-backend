package in.ankit_Saahariya.stream_verse.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private  String fullName;
    private String role;
}
