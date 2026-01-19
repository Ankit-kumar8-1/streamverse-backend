package in.ankit_Saahariya.stream_verse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Current Password is required !")
    private String currentPassword;

    @NotNull(message = "New Password is required !")
    private String newPassword;
}
