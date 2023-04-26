package cu.yoel.projectFULL.service.impl;

import cu.yoel.projectFULL.factory.CatalogListStrategyFactory;
import cu.yoel.projectFULL.models.Catalog;
import cu.yoel.projectFULL.models.types.CatalogListTypes;
import cu.yoel.projectFULL.payload.request.BaseDTO;
import cu.yoel.projectFULL.repositories.CatalogRepository;
import cu.yoel.projectFULL.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    CatalogListStrategyFactory catalogListStrategyFactory;

    @Override
    public Catalog save(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    public Catalog update(Catalog catalog) {
        if (catalogRepository.findById(catalog.getId()).isPresent()) {
            Catalog existingCatalog = catalogRepository.findById(catalog.getId()).get();
            existingCatalog.setTablaArgumento(catalog.getTablaArgumento());
            existingCatalog.setTablaDescription(catalog.getTablaDescription());
            existingCatalog.setTablaReferencia(catalog.getTablaReferencia());
            existingCatalog.setStatus(catalog.getStatus());

            return catalogRepository.save(existingCatalog);
        } else {
            return null;
        }
    }

    @Override
    public Optional<Catalog> getById(String id) {
        return catalogRepository.findById(id);
    }


    public Map<String, Object> getCatalogs(BaseDTO baseDTO, CatalogListTypes type){
        return catalogListStrategyFactory.getStrategy(type).getList(baseDTO);
    }
}
