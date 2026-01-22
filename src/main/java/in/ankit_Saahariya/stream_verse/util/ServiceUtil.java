package in.ankit_Saahariya.stream_verse.util;

import in.ankit_Saahariya.stream_verse.dao.UserRepository;
import in.ankit_Saahariya.stream_verse.dao.VideoRepository;
import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import in.ankit_Saahariya.stream_verse.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceUtil {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public UserEntity getUserByEmailOrThrow(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email:"+email));
    }

    public UserEntity getUserByIdOrThrow(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id:"+id));
    }

    public VideoEntity getVideoByIdOrThrow(Long id){
        return videoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Video not found with id:"+id));
    }
}
