package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dao.VideoRepository;
import in.ankit_Saahariya.stream_verse.dto.request.VideoRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoResponse;
import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import in.ankit_Saahariya.stream_verse.service.VideoService;
import in.ankit_Saahariya.stream_verse.util.PaginationUtils;
import in.ankit_Saahariya.stream_verse.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
