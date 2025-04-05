package vn.trongquy.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "songs")
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private ArtistEntity album;

    private Integer durationMs;

    private String genre;

    private Integer popularity = 0;

    @Column(name = "spotify_id", unique = true)
    private String spotifyId;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private SongSource source = SongSource.DATASET;

    @Column(name ="created_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name ="updated_at", length = 255)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    public enum SongSource {
        SPOTIFY, DATASET
    }

    // Getters, Setters, Constructors
}
