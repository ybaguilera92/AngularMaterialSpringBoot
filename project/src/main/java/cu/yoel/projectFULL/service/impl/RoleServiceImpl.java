package cu.yoel.projectFULL.service.impl;

import cu.yoel.projectFULL.exception.EntityNotFoundException;
import cu.yoel.projectFULL.models.ERole;
import cu.yoel.projectFULL.models.File;
import cu.yoel.projectFULL.models.Role;
import cu.yoel.projectFULL.models.User;
import cu.yoel.projectFULL.payload.request.UserChangePasswordDTO;
import cu.yoel.projectFULL.payload.request.UserDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateImageDTO;
import cu.yoel.projectFULL.repositories.RoleRepository;
import cu.yoel.projectFULL.repositories.UserRepository;
import cu.yoel.projectFULL.service.FileService;
import cu.yoel.projectFULL.service.RoleService;
import cu.yoel.projectFULL.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

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
    public Map<String, Object> getRoles() {

        List<Role> page = roleRepository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("roles", page);

        return response;
    }
}
