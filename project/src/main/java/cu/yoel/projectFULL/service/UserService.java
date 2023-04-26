package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.models.User;
import cu.yoel.projectFULL.payload.request.UserChangePasswordDTO;
import cu.yoel.projectFULL.payload.request.UserDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateImageDTO;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;


public interface UserService {
    // Tag save(String tagName, String tagColor);
    Map<String, Object> getUsers();
    Optional<User> getUserById(String id);
    User save(UserDTO user);
    User update(UserDTO userDTO);
    User updatePassword(UserChangePasswordDTO userDTO);
    User updateImage(UserUpdateImageDTO userDTO) throws IOException;
    Map<String, Object> countALl();
}
