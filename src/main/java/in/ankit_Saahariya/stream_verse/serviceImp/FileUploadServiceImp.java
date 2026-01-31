package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.service.FileUploadService;
import in.ankit_Saahariya.stream_verse.util.FileHandlerUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.access.HttpStatusAccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileUploadServiceImp implements FileUploadService {

    private Path videoStorageLocation;
    private  Path imageStorageLocation;

    @Value("${file.upload.video-dir}")
    private String videoDir;

    @Value(("${file.upload.image-dir}"))
    private String ImageDir;

    @PostConstruct
    public  void init(){
        this.videoStorageLocation = Paths.get(videoDir).toAbsolutePath().normalize();
        this.imageStorageLocation = Paths.get(ImageDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.videoStorageLocation);
            Files.createDirectories(this.imageStorageLocation);
        }catch (Exception ex){
            throw new RuntimeException
                    ("Could not create the directory where the uploaded files will be stored",ex);
        }
    }

    @Override
    public String storeVideoFile(MultipartFile file) {
        return storeFile(file,videoStorageLocation);
    }

    @Override
    public String storeImageFile(MultipartFile file) {
        return storeFile(file,imageStorageLocation);
    }

    @Override
    public ResponseEntity<Resource> serveVideo(String uuid, String rangeHeader) {
        try {
            Path filePath = FileHandlerUtil.findFileByUuid(videoStorageLocation,uuid);
            Resource resource = FileHandlerUtil.createFullResource(filePath);

            String filename = resource.getFilename();
            String contentType = FileHandlerUtil.detectVideoContentType(filename);
            long fileLength = resource.contentLength();

            if(isFullContentRequest(rangeHeader)){
                return buildFullVideoResponse(resource,contentType,filename,fileLength);
            }

            return buildPartialVideoResponse(filePath,rangeHeader,contentType,filename,fileLength);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    private ResponseEntity<Resource> buildPartialVideoResponse(
            Path filePath, String rangeHeader, String contentType, String filename, long fileLength) throws IOException{

        long[] range = FileHandlerUtil.parseRangeHeader(rangeHeader, fileLength);
        long rangeStart = range[0];
        long rangeEnd = range[1];
        
        if(!isValidRange(rangeStart,rangeEnd,fileLength)){
            return buildRangeNotSatisfiableResponse(fileLength);
        }

        long contentLength = rangeEnd - rangeStart +1;
        Resource rangeResource = FileHandlerUtil.createRangeResource(filePath,rangeStart,contentLength);

        return ResponseEntity.status(206)
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename\""+filename+"\"")
                .header(HttpHeaders.ACCEPT_RANGES,"bytes")
                .header(
                        HttpHeaders.CONTENT_RANGE,
                        "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength
                )
                .header(HttpHeaders.CONTENT_LENGTH,String.valueOf(contentLength))
                .body(rangeResource);
        
    }

    @Override
    public ResponseEntity<Resource> serveImage(String uuid) {
        try {
            Path filePath = FileHandlerUtil.findFileByUuid(imageStorageLocation,uuid);
            Resource resource = FileHandlerUtil.createFullResource(filePath);

            String filename = resource.getFilename();
            String contentType = FileHandlerUtil.detectImageContentType(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\""+
                            filename +"\"")
                    .body(resource);
        }catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<Resource> buildRangeNotSatisfiableResponse(long fileLength) {
        return ResponseEntity.status(416)
                .header(HttpHeaders.CONTENT_RANGE,"bytes */"+fileLength)
                .build();
    }

    private boolean isValidRange(long rangeStart, long rangeEnd, long fileLength) {
        return rangeStart <= rangeEnd && rangeStart >=0 &&  rangeEnd < fileLength;
    }

    private ResponseEntity<Resource> buildFullVideoResponse(Resource resource, String contentType, String filename, long fileLength) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename\""+filename+"\"")
                .header(HttpHeaders.ACCEPT_RANGES,"bytes")
                .header(HttpHeaders.CONTENT_LENGTH,String.valueOf(fileLength))
                .body(resource);
    }


    private boolean isFullContentRequest(String rangeHeader) {
        return rangeHeader == null || rangeHeader.isEmpty();
    }

    private String storeFile(MultipartFile file, Path storeLocation) {
        String fileExtension = FileHandlerUtil.extractFileExtension(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();

        String filename = uuid + fileExtension;

        try {
            if(file.isEmpty()){
                throw  new RuntimeException("Failed to store empty file"+filename);
            }

            Path targetLocation = storeLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uuid;
        }catch (IOException ex){
            throw  new RuntimeException("Filed to store file "+ filename,ex);
        }
    }
}
