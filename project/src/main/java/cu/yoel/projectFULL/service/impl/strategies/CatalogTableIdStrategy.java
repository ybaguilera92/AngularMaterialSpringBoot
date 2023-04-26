package cu.yoel.projectFULL.service.impl.strategies;

import cu.yoel.projectFULL.models.Catalog;
import cu.yoel.projectFULL.payload.request.BaseDTO;
import cu.yoel.projectFULL.payload.request.CatalogListDTO;
import cu.yoel.projectFULL.repositories.CatalogRepository;
import cu.yoel.projectFULL.service.strategies.CatalogGetStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CatalogTableIdStrategy implements CatalogGetStrategy {

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public Map<String, Object> getList(BaseDTO baseDTO) {
        CatalogListDTO catalogListDTO = (CatalogListDTO) baseDTO;
        Pageable pageable = PageRequest.of(catalogListDTO.getPageIndex(), catalogListDTO.getPageSize());
        Page<Catalog> catalogs =  catalogRepository.findByIdTabla(catalogListDTO.getArg(), pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("catalogs", catalogs.getContent());
        response.put("currentPage", catalogs.getNumber());
        response.put("totalItems", catalogs.getTotalElements());
        response.put("totalPages", catalogs.getTotalPages());

        return response;
    }
}
