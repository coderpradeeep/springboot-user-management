package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.UserInfo;
import org.example.model.UserInfoDto;
import org.example.repository.UserRepo;
import org.example.validationUtil.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final ValidationUtil validationUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepo.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("Unable to found user..");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto) {
        return userRepo.findByUsername(userInfoDto.getUsername());
    }

    public boolean signupUser(UserInfoDto userInfoDto) {
        //validationUtil.validateAllFields(userInfoDto);
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if(Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))) {
            return false;
        }

        String userId = UUID.randomUUID().toString();
        userRepo.save(new UserInfo(
                userId,
                userInfoDto.getUsername(),
                userInfoDto.getPassword(),
                new HashSet<>()
        ));

        return true;
    }
}
