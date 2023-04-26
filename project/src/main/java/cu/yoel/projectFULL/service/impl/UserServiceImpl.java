package cu.yoel.projectFULL.service.impl;

import cu.yoel.projectFULL.exception.EntityNotFoundException;
import cu.yoel.projectFULL.models.ERole;
import cu.yoel.projectFULL.models.File;
import cu.yoel.projectFULL.payload.request.UserChangePasswordDTO;
import cu.yoel.projectFULL.payload.request.UserDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateImageDTO;
import cu.yoel.projectFULL.repositories.*;
import cu.yoel.projectFULL.service.FileService;
import cu.yoel.projectFULL.service.UserService;
import cu.yoel.projectFULL.models.Role;
import cu.yoel.projectFULL.models.User;
import org.assertj.core.util.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;



    @Autowired
    PasswordEncoder encoder;



    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileService fileService;

    /*@Override
    public Tag save(String tagTitle, String tagColor) {
        Tag tag = new Tag();
        tag.setTitle(tagTitle);
        tag.setColor(tagColor);
        // return tagRepository.save(tag);
    }*/

    @Override
    public Map<String, Object> getUsers() {

        List<User> page = userRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("users", page);
      //  response.put("page", page);

        return response;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(UserDTO userDTO){

        User user = new User();
        this.modelMapper.map(userDTO,user);
        user.setPassword(encoder.encode(user.getPassword()));
      //  user.setUsername(user.getEmail().substring(0, user.getEmail().indexOf('@')));
        //user.setProfile(profileRepository.save(new Profile(user.getName(), user.getName())));
        user.setRoles(this.getRoles(userDTO));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow( () -> new EntityNotFoundException(User.class, userDTO.getId()));
        modelMapper.map(userDTO, user);
        user.setRoles(this.getRoles(userDTO));

        return userRepository.save(user);
    }

    @Override
    public User updatePassword(UserChangePasswordDTO userDTO) {
//        System.out.println("Hola");
//        System.out.println(userDTO);
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow( () -> new EntityNotFoundException(User.class, userDTO.getId()));
        modelMapper.map(userDTO, user);
        user.setPassword(encoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User updateImage(UserUpdateImageDTO userDTO) throws IOException {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow( () -> new EntityNotFoundException(User.class, userDTO.getId()));
        modelMapper.map(userDTO, user);
        if (userDTO.getMultipartFilea() != null) {
            System.out.println(userDTO.getMultipartFilea());
            File file = fileService.addFile(user.getMultipartFilea());
            user.setAvatar(file);
            user.setMultipartFilea(null);
            user.setFileEncode(Base64.getEncoder().encodeToString(fileService.getFile(user.getAvatar().getId()).getFile().getData()));
        }
        return userRepository.save(user);
    }

    @Override
    public Map<String, Object> countALl() {
        Map<String, Object> response = new HashMap<>();
        response.put("total", userRepository.countAllByEnabledIsTrue());

        return response;
    }



    private Set<Role> getRoles(UserDTO userDTO){
        Set<Role> roles = new HashSet<>();
        Arrays.stream(userDTO.getRoles()).forEach( role -> {
            roles.add(roleRepository.findByName(Arrays.stream(ERole.values()).filter(erol -> erol.name().equals(role)).findFirst()
                            .orElseThrow(() -> new TypeNotPresentException(role, null)))
                    .orElseThrow(() -> new EntityNotFoundException(Role.class, role)));
        });

        return roles;
    }
}
