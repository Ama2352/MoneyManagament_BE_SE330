package JavaProject.MoneyManagement_BE_SE330.user;

import JavaProject.MoneyManagement_BE_SE330.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByEmail(String email);
    boolean existsByEmail(String email);
    List<ApplicationUser> findByRole(Role role);
    void deleteByRoleNot(Role role);
}
