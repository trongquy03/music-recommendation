package vn.trongquy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;

@Entity
@Table(name = "likes")
@Data
@IdClass(LikeId.class)
public class LikeEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "song_id")
    private SongEntity song;

    @CreationTimestamp
    private Timestamp likedAt;
}
