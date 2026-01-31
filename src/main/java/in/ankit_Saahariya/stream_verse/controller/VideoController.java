package in.ankit_Saahariya.stream_verse.controller;


import in.ankit_Saahariya.stream_verse.dao.VideoRepository;
import in.ankit_Saahariya.stream_verse.dto.request.VideoRequest;
import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.VideoResponse;
import in.ankit_Saahariya.stream_verse.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<MessageResponse> createVideoByAdmin(@Valid @RequestBody VideoRequest videoRequest){
        return ResponseEntity.ok(videoService.createVideoByAdmin(videoRequest));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<PageResponse<VideoResponse>> getAllAdminVideos
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @RequestParam(required = false) String search
            ){

        return ResponseEntity.ok(videoService.getAllAdminVideos(page,size,search));
    }

}
