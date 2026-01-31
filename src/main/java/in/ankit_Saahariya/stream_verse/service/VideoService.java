package in.ankit_Saahariya.stream_verse.service;

import in.ankit_Saahariya.stream_verse.dto.request.VideoRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoResponse;
import jakarta.validation.Valid;

public interface VideoService {
    MessageResponse createVideoByAdmin(@Valid VideoRequest videoRequest);


    PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search);
}
