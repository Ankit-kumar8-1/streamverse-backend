package in.ankit_Saahariya.stream_verse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.ankit_Saahariya.stream_verse.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_users")
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role =  Role.USER;

    @Column(nullable = false)
    private boolean isActive =true;

    @Column(nullable = false)
    private boolean emailVerified = false;

    @Column(unique = true)
    private  String verificationToken;

    @Column
    private Instant verificationTokenExpire;

    @Column
    private String passwordRestToken;

    @Column
    private Instant passwordRestTokenExpire;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_watchList",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    private Set<VideoEntity> watchList = new HashSet<>();

    private void addToWatchList(VideoEntity video){
        this.watchList.add(video);
    }

    public void removeFromWatchList(VideoEntity video){
        this.watchList.remove(video);
    }


}
