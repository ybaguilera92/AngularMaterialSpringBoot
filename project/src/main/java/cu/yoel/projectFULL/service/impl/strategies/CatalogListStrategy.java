package cu.yoel.projectFULL.service.impl.strategies;

import cu.yoel.projectFULL.models.Catalog;
import cu.yoel.projectFULL.payload.request.BaseDTO;
import cu.yoel.projectFULL.repositories.CatalogRepository;
import cu.yoel.projectFULL.service.strategies.CatalogGetStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CatalogListStrategy implements CatalogGetStrategy {

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public Map<String, Object> getList(BaseDTO baseDTO) {
        Pageable pageable = PageRequest.of(baseDTO.getPageIndex(), baseDTO.getPageSize());
        List<Catalog> catalogs =  catalogRepository.findAll();
        Set<String> seen = new HashSet<>();

        catalogs.removeIf(c->!seen.add(c.getIdTabla()));

        Page page = new PageImpl<>(catalogs, pageable, catalogs.size());

        Map<String, Object> response = new HashMap<>();
        response.put("catalogs", page.getContent());
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return response;
    }
}
