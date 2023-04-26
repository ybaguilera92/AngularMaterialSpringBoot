package cu.yoel.projectFULL.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cu.yoel.projectFULL.utils.Operations;
import cu.yoel.projectFULL.exception.EntityNotFoundException;
import cu.yoel.projectFULL.exception.TokenRefreshException;
import cu.yoel.projectFULL.payload.request.LoginRequest;
import cu.yoel.projectFULL.payload.request.PaswordRequest;
import cu.yoel.projectFULL.payload.request.SignupRequest;
import cu.yoel.projectFULL.payload.response.JwtResponse;
import cu.yoel.projectFULL.payload.response.TokenRefreshResponse;
import cu.yoel.projectFULL.repositories.NotificationRepository;
import cu.yoel.projectFULL.repositories.RoleRepository;
import cu.yoel.projectFULL.repositories.UserRepository;
import cu.yoel.projectFULL.service.FileService;
import cu.yoel.projectFULL.service.impl.security.jwt.JwtUtils;
import cu.yoel.projectFULL.service.impl.security.services.UserDetailsImpl;
import cu.yoel.projectFULL.service.AuthService;
import cu.yoel.projectFULL.service.EmailService;
import cu.yoel.projectFULL.service.RefreshTokenService;
import cu.yoel.projectFULL.models.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    @Value("${asktravel.app.url}")
    private String url;

    @Value("${asktravel.app.name}")
    private String nameapp;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    FileService fileService;




    @Override
    public JwtResponse login(LoginRequest loginRequest)  throws IOException {

            UsernamePasswordAuthenticationToken aux=new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
//        System.out.println(aux);
            Authentication authentication = authenticationManager.authenticate( aux);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getEmail()).orElse(null);
//        System.out.println(userDetails.getName());
            String fileEncode="";
            if (user != null && user.isEnabled()){
                if(user.getAvatar()!=null){
                    user.setFileEncode(Base64.getEncoder().encodeToString(fileService.getFile(user.getAvatar().getId()).getFile().getData()));
                    fileEncode=userDetails.getFileEncode();
                }
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());
                return  new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        refreshToken.getToken(),
                        userDetails.getEmail(),
                        userDetails.getFullName(),
                        userDetails.getName(),
                        userDetails.getLastName(),
                        roles, fileEncode);

            } else {
                return new JwtResponse("Usuario inactivo",
                        userDetails.getId(),
                        userDetails.getUsername(),
                        null,
                        userDetails.getEmail(),
                        userDetails.getFullName(),
                        user.getName(),
                        user.getLastName(),
                        null,
                        null);
            }

    }
    
    @Override
    public JwtResponse register(SignupRequest signupRequest){

        // Create new user's account
        User user = new User(signupRequest.getName(),
                signupRequest.getLastName(),
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);

        userRepository.save(user);
        try {
            sendVerificationEmail(user, signupRequest.getSiteURL());
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new JwtResponse(null,
                user.getId(),
                user.getUsername(),
                " ",
                user.getEmail(),
                user.getName().concat(" "+user.getLastName()),
                user.getName(),
                user.getLastName(),
                null,
                user.getFileEncode());
    }



    @Override
    public boolean verify(String verificationCode) {
        Optional<User> user = userRepository.findByVerificationCode(verificationCode);
            if (user.isPresent() && !user.get().isEnabled()) {
                user.get().setVerificationCode(null);
                user.get().setEnabled(true);
                userRepository.save(user.get());
                return true;
            }
            return false;
    }

    @Override
    public TokenRefreshResponse refreshToken (String requestRefreshToken){

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));

    }
    public boolean verifyPassword(String verificationCode) {
        Optional<User> user = userRepository.findByVerificationCode(verificationCode);
        if (user.isPresent() && user.get().isEnabled()) {
            // user.get().setVerificationCode(null);
            String codigo= Operations.cadenaAleatoria();
            user.get().setPassword(encoder.encode(codigo));
            userRepository.save(user.get());
            try {
                sendRecoveryPasswordEmail(user.get(), codigo);
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }

    public void recoveryPassword(PaswordRequest email)  {
//        Optional<User> user2 = userRepository.findByEmail(email);
//        User user=user2.get();

        User user = userRepository.findByEmail(email.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(User.class, email.getEmail()));

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        try {
            sendCodePasswordEmail(user, email.getSiteURL());
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
        //return userRepository.save(user);
    }
    private void sendCodePasswordEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException, JsonProcessingException {

        Notification notification = notificationRepository.findByType("MAIL-CODE-PASSWORD").orElse(null);
        String verifyURL = siteURL + "?code=" + user.getVerificationCode();
        String subject = "Verifique su correo electrónico";
        java.util.Date fecha = new Date();
        DateFormat formatfecha= new SimpleDateFormat("dd/MM/yyyy");
        String aux= formatfecha.format(fecha);

        notification.setMessage(notification.getMessage().replace("[USUARIO]", user.getUsername()));
        notification.setMessage(notification.getMessage().replace("[FECHA]", aux));
        notification.setMessage(notification.getMessage().replace("[CODIGO]", verifyURL));
        notification.setMessage(notification.getMessage().replace("[APLICACION]", nameapp));
        notification.setMessage(notification.getMessage().replace("[URL_APLICACION]", url));
        emailService.sendSMail(new Email(user.getEmail(), notification.getMessage(), subject, null));
    }
    private void sendRecoveryPasswordEmail(User user, String codigo)
            throws MessagingException, UnsupportedEncodingException, JsonProcessingException {
        // System.out.println(user);
        Notification notification = notificationRepository.findByType("MAIL-CHANGE-PASSWORD").orElse(null);

        String subject = "Cambio de contraseña";
        Date fecha = new Date();
        DateFormat formatfecha= new SimpleDateFormat("dd/MM/yyyy");
        String aux= formatfecha.format(fecha);
        notification.setMessage(notification.getMessage().replace("[USUARIO]", user.getUsername()));
        notification.setMessage(notification.getMessage().replace("[FECHA]", aux));
        notification.setMessage(notification.getMessage().replace("[APLICACION]", nameapp));
        notification.setMessage(notification.getMessage().replace("[CODIGO]", codigo));
        notification.setMessage(notification.getMessage().replace("[URL_APLICACION]", url));
        emailService.sendSMail(new Email(user.getEmail(), notification.getMessage(), subject, null));
    }
    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException, JsonProcessingException {

        Notification notification = notificationRepository.findByType("MAIL-CONFIRMATION").orElse(null);
        String verifyURL = siteURL + "?code=" + user.getVerificationCode();
        String subject = "Verifique su correo electrónico";
        java.util.Date fecha = new Date();
        DateFormat formatfecha= new SimpleDateFormat("dd/MM/yyyy");
        String aux= formatfecha.format(fecha);
        notification.setMessage(notification.getMessage().replace("[USUARIO]", user.getUsername()));
        notification.setMessage(notification.getMessage().replace("[FECHA]", aux));
        notification.setMessage(notification.getMessage().replace("[CODIGO]", verifyURL));
        emailService.sendSMail(new Email(user.getEmail(), notification.getMessage(), subject, null));
    }
}
