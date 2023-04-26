package cu.yoel.projectFULL.controllers;

import cu.yoel.projectFULL.payload.request.UserChangePasswordDTO;
import cu.yoel.projectFULL.payload.request.UserCreateDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateImageDTO;
import cu.yoel.projectFULL.payload.response.MessageResponse;
import cu.yoel.projectFULL.payload.response.ResponseHandler;
import cu.yoel.projectFULL.repositories.UserRepository;
import cu.yoel.projectFULL.service.RoleService;
import cu.yoel.projectFULL.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }


}

