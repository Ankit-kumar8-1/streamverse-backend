package in.ankit_Saahariya.stream_verse.controller;

import in.ankit_Saahariya.stream_verse.dto.response.MessageResponse;
import in.ankit_Saahariya.stream_verse.dto.response.PageResponse;
import in.ankit_Saahariya.stream_verse.service.WatchListService;
import jakarta.mail.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchList")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;

    @PostMapping("/{videoId}")
    public ResponseEntity<MessageResponse> addToWatchList(
            @PathVariable Long videoId, Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(watchListService.addToWatchList(email,videoId));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<MessageResponse> removeFromWatchList(
            @PathVariable Long videoId, Authentication authentication
    ){
        String email = authentication.getName();
        return  ResponseEntity.ok(watchListService.removeFromWatchList(email,videoId));
    }

    @GetMapping
    public ResponseEntity<PageResponse> getWatchList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Authentication authentication
    ){
        String email = authentication.getName();
        return ResponseEntity.ok(watchListService.getWatchListPaginated(page,size,search,email));
    }
}

