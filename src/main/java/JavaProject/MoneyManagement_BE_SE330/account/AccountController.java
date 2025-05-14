package JavaProject.MoneyManagement_BE_SE330.account;

import JavaProject.MoneyManagement_BE_SE330.account.dto.SignInDTO;
import JavaProject.MoneyManagement_BE_SE330.account.dto.SignUpDTO;
import JavaProject.MoneyManagement_BE_SE330.account.service.AccountService;
import JavaProject.MoneyManagement_BE_SE330.authentication.AuthenticationResponse;
import JavaProject.MoneyManagement_BE_SE330.token.RefreshTokenDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        boolean success = accountService.signUp(signUpDTO);
        if (success) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDTO signInDTO) {
        AuthenticationResponse response = accountService.signIn(signInDTO);
        if (response.getToken() == null || response.getToken().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(response.getToken());
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            AuthenticationResponse response = accountService.refreshToken(refreshTokenDTO);
            if (response.getToken() == null || response.getToken().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthenticationResponse(false, "Token refresh failed", null, response.getErrors()));
            }
            return ResponseEntity.ok(new AuthenticationResponse(true, "Token refreshed successfully", response.getToken(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponse(false, ex.getMessage(), null, null));
        }
    }

    @DeleteMapping("/cleardatabase")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> clearDatabase() {
        boolean success = accountService.clearDatabase();
        if (success) {
            return ResponseEntity.ok("Database cleared successfully.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to clear database.");
    }
}
