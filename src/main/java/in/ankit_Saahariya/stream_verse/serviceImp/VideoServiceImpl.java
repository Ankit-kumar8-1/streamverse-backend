package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dao.VideoRepository;
import in.ankit_Saahariya.stream_verse.dto.request.VideoRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoStatsResponse;
import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import in.ankit_Saahariya.stream_verse.service.VideoService;
import in.ankit_Saahariya.stream_verse.util.PaginationUtils;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  VideoRepository videoRepository;
    @Autowired
    private ServiceUtil serviceUtil;


     @Override
     public MessageResponse createVideoByAdmin(VideoRequest videoRequest) {

           VideoEntity video = new VideoEntity();

         video.setTitle(videoRequest.getTitle());
         video.setDescription(videoRequest.getDescription());
         video.setYear(videoRequest.getYear());
         video.setRating(videoRequest.getRating());
         video.setDuration(videoRequest.getDuration());
         video.setSrcUuid(videoRequest.getSrc());
         video.setPosterUuid(videoRequest.getPosterUuid());
         video.setPublished(videoRequest.isPublished());
         video.setCategories(videoRequest.getCategories() != null? videoRequest.getCategories() : List.of());

         videoRepository.save(video);
         return new MessageResponse("Video Created successfully !");
     }


    @Override
    public PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page,size,"id");
        Page<VideoEntity> videoPage ;

        if(search != null && search.trim().isEmpty()){
            videoPage = videoRepository.searchVideos(search.trim(),pageable);
        }else {
            videoPage = videoRepository.findAll(pageable);
        }

        return PaginationUtils.toPageResponse(videoPage,VideoResponse::fromEntity);
    }

    @Override
    public MessageResponse updateVideoByAdmin(Long id, VideoRequest videoRequest) {
        VideoEntity newVideo = new VideoEntity();
        newVideo.setId(id);
        newVideo.setTitle(videoRequest.getTitle());
        newVideo.setDescription(videoRequest.getDescription());
        newVideo.setRating(videoRequest.getRating());
        newVideo.setSrcUuid(videoRequest.getSrc());
        newVideo.setPublished(videoRequest.isPublished());
        newVideo.setPosterUuid(videoRequest.getPosterUuid());
        newVideo.setCategories(videoRequest.getCategories() !=null ? videoRequest.getCategories() :List.of());
        newVideo.setDuration(videoRequest.getDuration());
        newVideo.setYear(videoRequest.getYear());

        videoRepository.save(newVideo);

        return new MessageResponse("Video updated successfully !");
    }

    @Override
    public MessageResponse deleteVideo(Long id) {
        if(!videoRepository.existsById(id)){
            throw new IllegalArgumentException("Video not found !");
        }
        videoRepository.deleteById(id);
        return new MessageResponse("Video deleted successfully !");
    }

    @Override
    public MessageResponse toggleVideoPublishedStatusByAdmin(Long id, boolean status) {
        VideoEntity video = serviceUtil.getVideoByIdOrThrow(id);
        video.setPublished(status);
        videoRepository.save(video);
        return new MessageResponse("Video Published status updated successfully !");
    }

    @Override
    public VideoStatsResponse getAdminStats() {
        long totalVideos = videoRepository.count();
        long publishedVideos = videoRepository.countPublishedVideo();
        long totalDuration = videoRepository.getTotalDuration();

        return new VideoStatsResponse(totalVideos,publishedVideos,totalDuration);
    }

    @Override
    public PageResponse<VideoResponse> getPublishedVideo(int page, int size, String search, String email) {

        Pageable pageable = PaginationUtils.createPageRequest(page, size, "createdAt");

        Page<VideoEntity> videoPage;

        if (search != null && !search.trim().isEmpty()) {
            videoPage = videoRepository.searchPublishedVideos(search.trim(), pageable);
        } else {
            videoPage = videoRepository.findByPublishedTrueOrderByCreatedAtDesc(pageable);
        }

        List<VideoEntity> videos = videoPage.getContent();

        Set<Long> watchListIds = Set.of();
        if (!videos.isEmpty()) {
            List<Long> videoIds = videos.stream()
                    .map(VideoEntity::getId)
                    .toList();
            watchListIds = userRepository.findWatchListVideoIds(email, videoIds);
        }

        Set<Long> finalWatchListIds = watchListIds;
        videos.forEach(video ->
                video.setIsInWatchList(finalWatchListIds.contains(video.getId()))
        );

        List<VideoResponse> videoResponses =
                videos.stream().map(VideoResponse::fromEntity).toList();

        return PaginationUtils.toPageResponse(videoPage, videoResponses);
    }

    @Override
    public List<VideoResponse> getFeaturedResponse() {
        Pageable pageable = PageRequest.of(0,5);
        List<VideoEntity> videos = videoRepository.findRandomPublishedVideos(pageable);

        return videos.stream().map(VideoResponse::fromEntity).toList();
    }
}
