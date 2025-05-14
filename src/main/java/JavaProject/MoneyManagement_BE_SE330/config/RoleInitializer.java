package JavaProject.MoneyManagement_BE_SE330.config;

import JavaProject.MoneyManagement_BE_SE330.role.Role;
import JavaProject.MoneyManagement_BE_SE330.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
        if (!roleRepository.findByName("ROLE_CUSTOMER").isPresent()) {
            Role customerRole = new Role();
            customerRole.setName("ROLE_CUSTOMER");
            roleRepository.save(customerRole);
        }
    }
}
