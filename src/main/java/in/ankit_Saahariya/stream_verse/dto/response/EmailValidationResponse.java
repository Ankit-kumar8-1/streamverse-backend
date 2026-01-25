package in.ankit_Saahariya.stream_verse.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
public class EmailValidationResponse {

    private boolean exists;
    private boolean available;
}
