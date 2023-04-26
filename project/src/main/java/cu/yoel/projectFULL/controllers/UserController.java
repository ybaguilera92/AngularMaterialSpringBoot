package cu.yoel.projectFULL.controllers;

import cu.yoel.projectFULL.payload.response.MessageResponse;
import cu.yoel.projectFULL.payload.response.ResponseHandler;
import cu.yoel.projectFULL.repositories.UserRepository;
import cu.yoel.projectFULL.service.UserService;
import cu.yoel.projectFULL.payload.request.UserChangePasswordDTO;
import cu.yoel.projectFULL.payload.request.UserCreateDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateDTO;
import cu.yoel.projectFULL.payload.request.UserUpdateImageDTO;
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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/count")
    public ResponseEntity<?> getTags() {
        return ResponseEntity.ok(userService.countALl());
    }

   /* @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CatalogDTO catalogDTO){
        return ResponseHandler.generateResponse("Registro realizado satisfactoriamente", HttpStatus.OK, catalogService.save(modelMapper.map(catalogDTO, Catalog.class)));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody CatalogDTO catalogDTO){
        return ResponseHandler.generateResponse("Registro actualizado satisfactoriamente", HttpStatus.OK, catalogService.update(modelMapper.map(catalogDTO, Catalog.class)));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getTags(@RequestParam("idTabla") String idTabla) {
        System.out.print(idTabla);
        return ResponseEntity.ok(catalogService.getCatalogs(idTabla));
    }*/

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }



    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDTO userDTO){

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        return ResponseHandler.generateResponse("Registro realizado satisfactoriamente", HttpStatus.OK, userService.save(userDTO));
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateDTO userDTO){
        return ResponseHandler.generateResponse("Registro realizado satisfactoriamente", HttpStatus.OK, userService.update(userDTO));
    }
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordDTO userDTO){
        return ResponseHandler.generateResponse("Cambio realizado satisfactoriamente", HttpStatus.OK, userService.updatePassword(userDTO));
    }
   // @PutMapping(value="/updateImage",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    //@RequestMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    //@PutMapping( "/updateImage")
   @PutMapping("/updateImage")
    public ResponseEntity<?> updateImage(@RequestPart("avatar") MultipartFile file,
                                         @RequestPart("data") @Valid UserUpdateImageDTO userDTO) throws IOException {
       userDTO.setMultipartFilea(file);
       return ResponseHandler.generateResponse("Cambio realizado satisfactoriamente", HttpStatus.OK, userService.updateImage(userDTO));
   }
}

