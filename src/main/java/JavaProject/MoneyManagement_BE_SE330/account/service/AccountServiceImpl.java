package JavaProject.MoneyManagement_BE_SE330.account.service;

import JavaProject.MoneyManagement_BE_SE330.account.dto.SignInDTO;
import JavaProject.MoneyManagement_BE_SE330.account.dto.SignUpDTO;
import JavaProject.MoneyManagement_BE_SE330.authentication.AuthenticationResponse;
import JavaProject.MoneyManagement_BE_SE330.category.CategoryRepository;
import JavaProject.MoneyManagement_BE_SE330.config.JwtUtils;
import JavaProject.MoneyManagement_BE_SE330.message.MessageRepository;
import JavaProject.MoneyManagement_BE_SE330.role.Role;
import JavaProject.MoneyManagement_BE_SE330.role.RoleRepository;
import JavaProject.MoneyManagement_BE_SE330.token.RefreshToken;
import JavaProject.MoneyManagement_BE_SE330.token.RefreshTokenDTO;
import JavaProject.MoneyManagement_BE_SE330.token.RefreshTokenRepository;
import JavaProject.MoneyManagement_BE_SE330.transaction.TransactionRepository;
import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import JavaProject.MoneyManagement_BE_SE330.user.UserRepository;
import JavaProject.MoneyManagement_BE_SE330.wallet.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public AccountServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                              JwtUtils jwtUtils, RefreshTokenRepository refreshTokenRepository,
                              TransactionRepository transactionRepository, WalletRepository walletRepository,
                              CategoryRepository categoryRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public boolean signUp(SignUpDTO signUpDTO) {
        try {
            if (!signUpDTO.getPassword().equals(signUpDTO.getConfirmPassword())) {
                logger.warn("Passwords do not match for email: {}", signUpDTO.getEmail());
                return false;
            }
            if (userRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
                logger.warn("Email {} already exists", signUpDTO.getEmail());
                return false;
            }

            ApplicationUser user = new ApplicationUser();
            user.setFirstName(signUpDTO.getFirstName());
            user.setLastName(signUpDTO.getLastName());
            user.setEmail(signUpDTO.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));
            user.setRole(userRole);

            userRepository.save(user);
            logger.info("User {} signed up successfully", signUpDTO.getEmail());
            return true;
        } catch (Exception e) {
            logger.error("Error during sign-up for {}: {}", signUpDTO.getEmail(), e.getMessage());
            return false;
        }
    }

    @Override
    public AuthenticationResponse signIn(SignInDTO signInDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            ApplicationUser user = userRepository.findByEmail(signInDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshToken = UUID.randomUUID().toString();

            RefreshToken tokenEntity = new RefreshToken();
            tokenEntity.setToken(refreshToken);
            tokenEntity.setUser(user);
            tokenEntity.setJwtId(jwtUtils.getJwtId(jwt));
            tokenEntity.setCreationDate(LocalDateTime.now());
            tokenEntity.setExpiryDate(LocalDateTime.now().plusDays(7));
            tokenEntity.setInvalidated(false);
            refreshTokenRepository.save(tokenEntity);

            return new AuthenticationResponse(true, "Login successful", jwt, null);

        } catch (Exception e) {
            logger.error("Sign-in failed for email: {}. Error: {}", signInDTO.getEmail(), e.getMessage());
            return new AuthenticationResponse(false, "Invalid login credentials", null, List.of("Invalid login credentials"));

        }
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO) {
        String expiredToken = refreshTokenDTO.getExpiredToken();
        String userEmail = jwtUtils.getUserNameFromJwtToken(expiredToken);

        if (!jwtUtils.validateJwtToken(expiredToken, true)) {
            return new AuthenticationResponse(false, "Invalid token", null, List.of("Invalid token"));
        }

        ApplicationUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jti = jwtUtils.getJwtId(expiredToken);
        RefreshToken refreshToken = refreshTokenRepository.findByJwtIdAndUserIdAndInvalidatedFalse(jti, user.getId())
                .orElse(null);

        if (refreshToken == null || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new AuthenticationResponse(false, "No valid refresh token found", null, List.of("No valid refresh token found"));
        }

        String newJwt = jwtUtils.generateJwtTokenForUser(user);
        String newRefreshToken = UUID.randomUUID().toString();

        RefreshToken newTokenEntity = new RefreshToken();
        newTokenEntity.setToken(newRefreshToken);
        newTokenEntity.setUser(user);
        newTokenEntity.setJwtId(jwtUtils.getJwtId(newJwt));
        newTokenEntity.setCreationDate(LocalDateTime.now());
        newTokenEntity.setExpiryDate(LocalDateTime.now().plusDays(7));
        newTokenEntity.setInvalidated(false);
        refreshTokenRepository.save(newTokenEntity);

        return new AuthenticationResponse(true, "Token refreshed successfully", newJwt, null);
    }

    @Override
    @Transactional
    public boolean clearDatabase() {
        logger.info("Starting to clear database data.");
        boolean success = true;

        try {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            List<ApplicationUser> adminUsers = adminRole != null
                    ? userRepository.findByRole(adminRole)
                    : new ArrayList<>();

            logger.info("Deleting transactions...");
            transactionRepository.deleteAll();

            logger.info("Deleting wallets...");
            walletRepository.deleteAll();

            logger.info("Deleting categories...");
            categoryRepository.deleteAll();

            logger.info("Deleting messages...");
            messageRepository.deleteAll();

            logger.info("Deleting refresh tokens for non-admin users...");
            if (!adminUsers.isEmpty()) {
                refreshTokenRepository.deleteByUserNotIn(adminUsers);
            } else {
                refreshTokenRepository.deleteAll();
            }

            logger.info("Deleting non-admin users...");
            if (!adminUsers.isEmpty()) {
                userRepository.deleteByRoleNot(adminRole);
            } else {
                userRepository.deleteAll();
            }

            logger.info("Deleting non-admin roles...");
            roleRepository.deleteByNameNot("ROLE_ADMIN");

            logger.info("Database clear operation completed.");
        } catch (Exception ex) {
            logger.error("Exception occurred during database clear operation: {}", ex.getMessage());
            success = false;
        }

        return success;
    }
}
