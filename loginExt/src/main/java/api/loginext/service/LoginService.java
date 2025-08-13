package api.loginext.service;

import api.loginext.entity.HistoryPasswordEntity;
import api.loginext.entity.StatusUserEntity;

import api.loginext.entity.UserEntity;
import api.loginext.repository.HistoryPasswordRepository;
import api.loginext.repository.UserRepository;
import api.loginext.security.UserPrincipal;
import api.loginext.security.jwt.JwtUtils;
import api.loginext.to.UserTo;
import org.springframework.data.domain.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.util.List;

import java.util.Optional;

@Service
public class LoginService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final HistoryPasswordRepository historyPasswordRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    public LoginService(UserRepository userRepository, AuthenticationManager authenticationManager, HistoryPasswordRepository historyPasswordRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.historyPasswordRepository = historyPasswordRepository;
        this.jwtUtils = jwtUtils;
    }

    public String validateCredentials(UserTo user) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(user.getUserName());
        if(userEntity.isEmpty()) {
            return "invalid";
        }

        if(userEntity.get().getTemporalPassword() != null) {
            if(validateTemporalPassword(userEntity.get(), user.getPassword())) {
                return "tempValid";
            }
        }

        if(userEntity.isPresent()) {
            String validatePassword = validatePasswordFromHistory(userEntity.get(), user.getPassword());
            if(validatePassword.equals("valid")) {
                return "valid";
            }
            if(validatePassword.equals("suspended")) {
                return "suspended";
            }
            if(validatePassword.equals("blocked")) {
                return "blocked";
            }
            if(validatePassword.equals("invalid")) {
                if(userEntity.get().getProfileEntity().getIdProfile() != 2L) {
                    setFailedAttempt(userEntity.get());
                }
                return "invalid";
            }
            if(validatePassword.equals("expired")) {
                return "expired";
            }
        } else {
            return "invalid";
        }
        return "invalid";
    }
    @Transactional
    public void setFailedAttempt(UserEntity userEntity) {
        if(userEntity.getFailedAttempts() == null) {
            userEntity.setFailedAttempts(1);
        } else {
            userEntity.setFailedAttempts(userEntity.getFailedAttempts() + 1);
            if(userEntity.getFailedAttempts() == 3) {
                userEntity.setStatusUserEntity(new StatusUserEntity(4L));
                userEntity.setUpdateDate(OffsetDateTime.now(ZoneId.of("America/Caracas")));
            }
        }
        userRepository.save(userEntity);
    }

    public void resetFailedAttempts(UserEntity userEntity) {
        userEntity.setFailedAttempts(0);
        userRepository.save(userEntity);
    }

    private boolean validateTemporalPassword(UserEntity userEntity, String tempPassword) {
        if(userEntity.getTemporalPassword().equals(tempPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public UserEntity loginUser(UserTo userEntity) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEntity.getUserName(), userEntity.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userPrincipal.getUser());

        UserEntity userAuthenticated = userPrincipal.getUser();
        userAuthenticated.setToken(jwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return userAuthenticated;

    }
    public HistoryPasswordEntity getUserPassword(Long idUser) {
        List<HistoryPasswordEntity> historyPasswordEntities;
        historyPasswordEntities = historyPasswordRepository.getLastPasswordFromIdUser(PageRequest.of(0,1), idUser);
        return historyPasswordEntities.get(0);
    }

    public String validatePasswordFromHistory(UserEntity user, String password) {
       HistoryPasswordEntity historyPasswordEntity = getUserPassword(user.getIdUser());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(user.getStatusUserEntity().getIdStatusUser() == 3L) {
            return "blocked";
        }
        if(user.getStatusUserEntity().getIdStatusUser() == 4L) {
           if(!validateExpiredBlock(user)) {
               return "suspended";
           } else {
               user.setStatusUserEntity(new StatusUserEntity(1L));
               userRepository.save(user);
           }

        }
        if(!passwordEncoder.matches(password, historyPasswordEntity.getPassword())) {
            return "invalid";
        } else {
            if(user.getProfileEntity().getIdProfile() == 2) {
                return "valid";
            } else {
                if(validatePasswordExpired(historyPasswordEntity).equals("valid")) {
                    return "valid";
                } else {
                    return "expired";
                }
            }
        }
    }

    private Boolean validateExpiredBlock(UserEntity user) {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        OffsetDateTime expireDate = user.getUpdateDate().withOffsetSameInstant(ZoneOffset.of("-04:00")).plusHours(1L);
        if(currentDate.isAfter(expireDate)) {
            return true;
        } else {
            return false;
        }
    }

    public String validatePasswordExpired(HistoryPasswordEntity historyPasswordEntity) {
        OffsetDateTime currentDate = OffsetDateTime.now(ZoneId.of("America/Caracas"));
        OffsetDateTime expireDate = historyPasswordEntity.getExipartionDate().withOffsetSameInstant(ZoneOffset.of("-04:00"));

        if(currentDate.isBefore(expireDate)) {
            return "valid";
        } else {
            return "expired";
        }
    }


}
