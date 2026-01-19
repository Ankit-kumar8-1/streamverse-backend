package in.ankit_Saahariya.stream_verse.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class EmailValidationResponse {

    private boolean exists;
    private boolean available;
}
