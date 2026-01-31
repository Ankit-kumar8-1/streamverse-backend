package in.ankit_Saahariya.stream_verse.controller;


import in.ankit_Saahariya.stream_verse.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload/video")
    public ResponseEntity<Map<String, String>> uploadVideo(@RequestParam("file")MultipartFile file){
        String uuid = fileUploadService.storeVideoFile(file);
        return ResponseEntity.ok(buildUploadResponse(uuid,file));
    }

    private Map<String, String> buildUploadResponse(String uuid ,MultipartFile file) {
        Map<String,String> response =  new HashMap<>();
        response.put("uuid",uuid);
        response.put("filename",file.getOriginalFilename());
        response.put("size",String.valueOf(file.getSize()));
        return  response;
    }

    @PostMapping("/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file")MultipartFile file){
        String uuid = fileUploadService.storeImageFile(file);
        return ResponseEntity.ok(buildUploadResponse(uuid,file));
    }


    @GetMapping("/video/{uuid}")
    public ResponseEntity<Resource> serveVideo(
            @PathVariable String uuid,
            @RequestHeader(value = "Range", required = false) String range
    ) {
        return fileUploadService.serveVideo(uuid, range);
    }

    @GetMapping("/image/{uuid}")
    public ResponseEntity<Resource> serveImage(@PathVariable String uuid){
        return fileUploadService.serveImage(uuid);
    }
}
