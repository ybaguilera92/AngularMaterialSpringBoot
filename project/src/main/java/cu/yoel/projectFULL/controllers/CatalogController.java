package cu.yoel.projectFULL.controllers;

import cu.yoel.projectFULL.models.Catalog;
import cu.yoel.projectFULL.models.types.CatalogListTypes;
import cu.yoel.projectFULL.payload.request.CatalogDTO;
import cu.yoel.projectFULL.payload.request.CatalogListDTO;
import cu.yoel.projectFULL.payload.response.ResponseHandler;
import cu.yoel.projectFULL.service.CatalogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/catalogs")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CatalogDTO catalogDTO){
        return ResponseHandler.generateResponse("Registro realizado satisfactoriamente", HttpStatus.OK, catalogService.save(modelMapper.map(catalogDTO, Catalog.class)));
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody CatalogDTO catalogDTO){
        return ResponseHandler.generateResponse("Registro actualizado satisfactoriamente", HttpStatus.OK, catalogService.update(modelMapper.map(catalogDTO, Catalog.class)));
    }

    @GetMapping
    public ResponseEntity<?> getList(@Valid CatalogListDTO catalogListDTO) {
        CatalogListTypes catType = Arrays.stream(CatalogListTypes.values()).filter(catalogListType -> catalogListType.name().equals(catalogListDTO.getType().toUpperCase())).findFirst().get();
        return  ResponseEntity.ok(catalogService.getCatalogs(catalogListDTO, catType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        return ResponseEntity.ok(catalogService.getById(id));
    }
}

