package in.ankit_Saahariya.stream_verse.dto.response;

import in.ankit_Saahariya.stream_verse.dto.request.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoStatsResponse {

    private long totalVideos;
    private  long publishedVideos;
    private long totalDuration;


}

