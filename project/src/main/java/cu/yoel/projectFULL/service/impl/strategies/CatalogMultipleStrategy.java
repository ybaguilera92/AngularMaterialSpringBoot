package cu.yoel.projectFULL.service.impl.strategies;


import cu.yoel.projectFULL.payload.request.BaseDTO;
import cu.yoel.projectFULL.payload.request.CatalogListDTO;
import cu.yoel.projectFULL.repositories.CatalogRepository;
import cu.yoel.projectFULL.service.strategies.CatalogGetStrategy;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CatalogMultipleStrategy implements CatalogGetStrategy {

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public Map<String, Object> getList(BaseDTO baseDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        CatalogListDTO catalogListDTO = (CatalogListDTO) baseDTO;
        for (String t : catalogListDTO.getTablas()) {
            response.put(t, Lists.newArrayList(authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_USER")) ? catalogRepository.findByIdTablaAndStatus(t, "A") :
                    catalogRepository.findByIdTabla(t)));
        }
        return response;
    }

}
