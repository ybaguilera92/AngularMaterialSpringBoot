package cu.yoel.projectFULL.service.strategies;

import cu.yoel.projectFULL.models.generic.BaseEntity;
import cu.yoel.projectFULL.payload.request.BaseDTO;

import java.util.Map;

public interface BaseGetStrategy <T extends BaseEntity> {
    public Map<String, Object> getList(BaseDTO baseDTO);
}
