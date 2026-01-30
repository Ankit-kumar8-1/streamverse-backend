package in.ankit_Saahariya.stream_verse.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    String storeVideoFile(MultipartFile file);

    String storeImageFile(MultipartFile file);
}
