package JavaProject.MoneyManagement_BE_SE330.token;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByJwtIdAndUserIdAndInvalidatedFalse(String jwtId, Long userId);
    void deleteByUserNotIn(List<ApplicationUser> users);
}
