package JavaProject.MoneyManagement_BE_SE330.account.service;

import JavaProject.MoneyManagement_BE_SE330.account.dto.SignInDTO;
import JavaProject.MoneyManagement_BE_SE330.account.dto.SignUpDTO;
import JavaProject.MoneyManagement_BE_SE330.authentication.AuthenticationResponse;
import JavaProject.MoneyManagement_BE_SE330.token.RefreshTokenDTO;

public interface AccountService {
    boolean signUp(SignUpDTO signUpDTO);
    AuthenticationResponse signIn(SignInDTO signInDTO);
    AuthenticationResponse refreshToken(RefreshTokenDTO refreshTokenDTO);
    boolean clearDatabase();
}
