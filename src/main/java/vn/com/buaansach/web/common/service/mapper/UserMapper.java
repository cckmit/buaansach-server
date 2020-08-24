package vn.com.buaansach.web.common.service.mapper;

import org.springframework.stereotype.Service;
import vn.com.buaansach.entity.user.UserEntity;
import vn.com.buaansach.web.common.service.dto.read.UserDTO;

import java.util.List;
import java.util.Objects;
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
}
