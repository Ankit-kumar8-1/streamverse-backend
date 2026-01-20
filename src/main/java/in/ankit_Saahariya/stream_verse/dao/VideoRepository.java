package in.ankit_Saahariya.stream_verse.dao;

import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoEntity,Long> {
}
