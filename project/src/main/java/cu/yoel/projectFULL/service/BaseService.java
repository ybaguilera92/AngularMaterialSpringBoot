package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.models.types.CatalogListTypes;
import cu.yoel.projectFULL.payload.request.BaseDTO;

import java.util.Map;
import java.util.Optional;

public interface BaseService<T> {
    T save(T catalog);
    T update(T t);
    Optional<T> getById (String id);
    Map<String, Object> getList(BaseDTO baseDTO, CatalogListTypes type);
}
