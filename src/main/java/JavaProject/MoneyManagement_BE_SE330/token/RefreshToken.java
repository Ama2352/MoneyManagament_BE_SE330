package JavaProject.MoneyManagement_BE_SE330.token;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(name = "jwt_id", nullable = false)
    private String jwtId;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean invalidated;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;
}