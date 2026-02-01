package in.ankit_Saahariya.stream_verse.service;

import in.ankit_Saahariya.stream_verse.dto.request.VideoRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoStatsResponse;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;

import java.util.List;

public interface VideoService {
    MessageResponse createVideoByAdmin(@Valid VideoRequest videoRequest);


    PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search);

    MessageResponse updateVideoByAdmin(Long id, @Valid VideoRequest videoRequest);

    MessageResponse deleteVideo(Long id);

    MessageResponse toggleVideoPublishedStatusByAdmin(Long id, boolean value);

    VideoStatsResponse getAdminStats();

    PageResponse<VideoResponse> getPublishedVideo(int page, int size, String search, String email);

    List<VideoResponse> getFeaturedResponse();
}
