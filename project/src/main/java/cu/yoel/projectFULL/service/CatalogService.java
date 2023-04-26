package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.models.Catalog;
import cu.yoel.projectFULL.models.types.CatalogListTypes;
import cu.yoel.projectFULL.payload.request.BaseDTO;

import java.util.Map;
import java.util.Optional;

public interface CatalogService {
    Catalog save(Catalog catalog);
    Catalog update(Catalog catalog);
    Optional<Catalog> getById (String id);
    Map<String, Object> getCatalogs(BaseDTO baseDTO, CatalogListTypes type);
    /*Map<String, Object> list(Integer pageNo, Integer pageSize);
    Map<String, Object> getCatalogsMultiple(String [] idTabla );*/
}
