package in.ankit_Saahariya.stream_verse.serviceImp;

import in.ankit_Saahariya.stream_verse.service.FileUploadService;
import in.ankit_Saahariya.stream_verse.util.FileHandlerUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
