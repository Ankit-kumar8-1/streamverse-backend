package in.ankit_Saahariya.stream_verse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ForgotPasswordResponse {
    private  boolean valid;
    private String message;

    public ForgotPasswordResponse() {

    }
}
