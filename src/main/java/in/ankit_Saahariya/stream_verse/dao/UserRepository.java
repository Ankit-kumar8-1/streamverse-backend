package in.ankit_Saahariya.stream_verse.dao;

import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
}
