package JavaProject.MoneyManagement_BE_SE330.user;

import JavaProject.MoneyManagement_BE_SE330.category.Category;
import JavaProject.MoneyManagement_BE_SE330.role.Role;
import JavaProject.MoneyManagement_BE_SE330.token.RefreshToken;
import JavaProject.MoneyManagement_BE_SE330.wallet.Wallet;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wallet> wallets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens;
}