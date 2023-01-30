package com.sarafan.security;

import com.sarafan.domain.User;
import com.sarafan.repo.UserDetailRepo;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {


    private final UserDetailRepo userDetailRepo;

    public CustomOidcUserService(UserDetailRepo userDetailRepo) {
        this.userDetailRepo = userDetailRepo;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            return processOidcUser(userRequest, oidcUser);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OidcUser processOidcUser(OidcUserRequest userRequest, OidcUser oidcUser) {

        Map<String, Object> map = oidcUser.getAttributes();

        String id = (String) map.get("sub");

        User user = userDetailRepo.findById(id).orElseGet(() -> {
            User newUser = new User();

            newUser.setId(id);
            newUser.setName((String) map.get("name"));
            newUser.setEmail((String) map.get("email"));
            newUser.setGender((String) map.get("gender"));
            newUser.setLocale((String) map.get("locale"));
            newUser.setUserpic((String) map.get("picture"));

            return newUser;
        });

        user.setLastVisit(LocalDateTime.now());

        userDetailRepo.save(user);

        return oidcUser;
    }
}