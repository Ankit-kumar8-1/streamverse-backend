package in.ankit_Saahariya.stream_verse.dto.response;

import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoResponse {

    private Long id;
    private String title;
    private String description;
    private Integer year;
    private  String rating;

    private Integer duration;
    private String src;
    private  String poster;
    private boolean published;

    private List<String> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isInWatchList;

    public static VideoResponse fromEntity(VideoEntity video) {
        return VideoResponse.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .year(video.getYear())
                .rating(video.getRating())
                .duration(video.getDuration())
                .src(video.getSrc())
                .poster(video.getPoster())
                .published(video.isPublished())
                .categories(video.getCategories()) // agar String list hai
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .isInWatchList(
                        video.getIsInWatchList() != null ? video.getIsInWatchList() : false) // default
                .build();
    }


}
