package JavaProject.MoneyManagement_BE_SE330.message;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private ApplicationUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private ApplicationUser receiver;

    @Column(nullable = false)
    private String content;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
}