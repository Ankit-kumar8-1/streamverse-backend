package in.ankit_Saahariya.stream_verse.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 6,message = "New password must be a least 6 characters long")
    private  String newPassword;
}
