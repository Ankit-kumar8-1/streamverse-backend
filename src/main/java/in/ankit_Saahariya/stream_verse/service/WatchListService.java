package in.ankit_Saahariya.stream_verse.service;

import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;

public interface WatchListService {
    public MessageResponse addToWatchList(String email, Long videoId);

    MessageResponse removeFromWatchList(String email, Long videoId);

    PageResponse getWatchListPaginated(int page, int size, String search, String email);
}
