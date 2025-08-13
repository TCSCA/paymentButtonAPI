package api.loginext.security;

import api.loginext.entity.HistoryPasswordEntity;
import api.loginext.entity.UserEntity;
import api.loginext.repository.HistoryPasswordRepository;
import api.loginext.service.LoginService;
import api.loginext.service.UserService;
import api.loginext.util.SecurityUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailService  implements UserDetailsService {

    private final UserService userService;

    private final HistoryPasswordRepository historyPasswordRepository;


    public CustomUserDetailService(UserService userService, HistoryPasswordRepository historyPasswordRepository) {
        this.userService = userService;
        this.historyPasswordRepository = historyPasswordRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("Usuario no encontrado"));

        List<HistoryPasswordEntity> historyPasswordEntities;
        historyPasswordEntities = historyPasswordRepository.getLastPasswordFromIdUser(PageRequest.of(0,1), userEntity.getIdUser());
        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(userEntity.getProfileEntity().getProfileDescription()));

        return UserPrincipal.builder()
                .user(userEntity)
                .id(userEntity.getIdUser())
                .username(username)
                .password(historyPasswordEntities.get(0).getPassword())
                .authorities(authorities)
                .build();
    }
}
