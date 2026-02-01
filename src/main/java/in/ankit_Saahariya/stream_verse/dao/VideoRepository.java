package in.ankit_Saahariya.stream_verse.dao;

import in.ankit_Saahariya.stream_verse.entity.VideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity,Long> {

    @Query(
            "SELECT v FROM VideoEntity v WHERE " +
                    "LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                    "LOWER(v.description) LIKE LOWER(CONCAT('%', :search, '%'))"
    )
    Page<VideoEntity> searchVideos(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(v) FROM VideoEntity v WHERE v.published = true")
    long countPublishedVideo();

    @Query("SELECT COALESCE(SUM(v.duration), 0) FROM VideoEntity v")
    long getTotalDuration();

    @Query(
            "SELECT v FROM VideoEntity v " +
                    "WHERE v.published = true AND (" +
                    "LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                    "LOWER(v.description) LIKE LOWER(CONCAT('%', :search, '%'))" +
                    ")"
    )
    Page<VideoEntity> searchPublishedVideos(
            @Param("search") String search,
            Pageable pageable
    );

    Page<VideoEntity> findByPublishedTrueOrderByCreatedAtDesc(Pageable pageable);


    @Query(
            "SELECT v FROM VideoEntity v " +
                    "WHERE v.published = true " +
                    "ORDER BY function('RAND')"
    )
    List<VideoEntity> findRandomPublishedVideos(Pageable pageable);

}
