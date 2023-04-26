package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.payload.request.LoginRequest;
import cu.yoel.projectFULL.payload.request.PaswordRequest;
import cu.yoel.projectFULL.payload.request.SignupRequest;
import cu.yoel.projectFULL.payload.response.JwtResponse;
import cu.yoel.projectFULL.payload.response.TokenRefreshResponse;

import java.io.IOException;


public interface AuthService {
    JwtResponse login(LoginRequest loginRequest) throws IOException;
    JwtResponse register(SignupRequest signupRequest);
    void recoveryPassword(PaswordRequest email) throws IOException;
    boolean verify (String verificationCode);

    boolean verifyPassword(String verificationCode);
    TokenRefreshResponse refreshToken (String requestRefreshToken);
}
