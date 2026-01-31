package in.ankit_Saahariya.stream_verse.dao;

import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<VideoEntity,Long> {

    @Query(
            "SELECT v FROM VideoEntity v WHERE " +
                    "LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                    "LOWER(v.description) LIKE LOWER(CONCAT('%', :search, '%'))"
    )
    Page<VideoEntity> searchVideos(@Param("search") String search, Pageable pageable);
}
