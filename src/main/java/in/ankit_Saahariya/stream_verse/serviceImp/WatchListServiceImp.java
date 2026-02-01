package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dao.VideoRepository;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoResponse;
import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import in.ankit_Saahariya.stream_verse.service.WatchListService;
import in.ankit_Saahariya.stream_verse.util.PaginationUtils;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WatchListServiceImp implements WatchListService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ServiceUtil serviceUtil;

    @Override
    public MessageResponse addToWatchList(String email, Long videoId) {
        UserEntity user = serviceUtil.getUserByEmailOrThrow(email);

        VideoEntity video = serviceUtil.getVideoByIdOrThrow(videoId);
        user.addToWatchList(video);
        userRepository.save(user);
        return new MessageResponse("Video added to watchList successfully !");
    }

    @Override
    public MessageResponse removeFromWatchList(String email, Long videoId) {
        UserEntity user = serviceUtil.getUserByEmailOrThrow(email);

        VideoEntity video = serviceUtil.getVideoByIdOrThrow(videoId);
        user.removeFromWatchList(video);
        userRepository.save(user);
        return new MessageResponse("Video Remove from watchlist successfully !");
    }

    @Override
    public PageResponse getWatchListPaginated(int page, int size, String search, String email) {
        UserEntity user = serviceUtil.getUserByEmailOrThrow(email);
        Pageable pageable = PaginationUtils.createPageRequest(page,size);
        Page<VideoEntity> videoPage;

        if(search!=null && !search.trim().isEmpty()){
            videoPage = userRepository.searchWatchListByUserId(user.getId(),search.trim(),pageable);
        }else {
            videoPage = userRepository.findWatchListByUserId(user.getId(),pageable);
        }

        return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
    }
}
