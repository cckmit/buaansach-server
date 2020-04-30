package vn.com.buaansach.web.user.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.user.AuthorityEntity;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.web.user.service.dto.UserDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public List<UserDTO> usersToUserDTOs(List<UserEntity> userEntities) {
        return userEntities.stream()
                .filter(Objects::nonNull)
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity);
    }

    public List<UserEntity> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::userDTOToUser)
                .collect(Collectors.toList());
    }

    public UserEntity userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.setGuid(userDTO.getGuid());
            userEntity.setLogin(userDTO.getLogin());
            userEntity.setFirstName(userDTO.getFirstName());
            userEntity.setLastName(userDTO.getLastName());
            userEntity.setEmail(userDTO.getEmail());
            userEntity.setImageUrl(userDTO.getImageUrl());
            userEntity.setActivated(userDTO.isActivated());
            userEntity.setLangKey(userDTO.getLangKey());
            Set<AuthorityEntity> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            userEntity.setAuthorities(authorities);
            return userEntity;
        }
    }


    private Set<AuthorityEntity> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<AuthorityEntity> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                AuthorityEntity auth = new AuthorityEntity();
                auth.setName(string);
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }

    public UserEntity userFromId(Long id) {
        if (id == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        return userEntity;
    }
}
