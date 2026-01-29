package in.ankit_Saahariya.stream_verse.dao;

import in.ankit_Saahariya.stream_verse.entity.UserEntity;
import in.ankit_Saahariya.stream_verse.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByPasswordRestToken(String token);


    long countByRoleAndIsActive(Role role, boolean isActive);

    @Query("""
    SELECT u FROM UserEntity u
    WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
""")
    Page<UserEntity> searchUsers(@Param("search") String search, Pageable pageable);

    long countByRole(Role role);
}
