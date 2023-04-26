package cu.yoel.projectFULL.factory;

import cu.yoel.projectFULL.models.types.CatalogListTypes;
import cu.yoel.projectFULL.service.impl.strategies.CatalogListStrategy;
import cu.yoel.projectFULL.service.impl.strategies.CatalogMultipleStrategy;
import cu.yoel.projectFULL.service.impl.strategies.CatalogTableIdStrategy;
import cu.yoel.projectFULL.service.strategies.CatalogGetStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class CatalogListStrategyFactory {

    CatalogListStrategy catalogListStrategy;
    CatalogTableIdStrategy catalogTableIdStrategy;
    CatalogMultipleStrategy catalogMultipleStrategy;

    private Map<CatalogListTypes, CatalogGetStrategy> strategies = new EnumMap<>(CatalogListTypes.class);

    public CatalogListStrategyFactory(CatalogListStrategy catalogListStrategy,
                                      CatalogTableIdStrategy catalogTableIdStrategy,
                                      CatalogMultipleStrategy catalogMultipleStrategy) {

        this.catalogListStrategy = catalogListStrategy;
        this.catalogTableIdStrategy = catalogTableIdStrategy;
        this.catalogMultipleStrategy = catalogMultipleStrategy;
        initStrategies();
    }

    public CatalogGetStrategy getStrategy(CatalogListTypes type) {
        if (type == null || !strategies.containsKey(type)) {
            throw new IllegalArgumentException("Invalid " + type);
        }
        return strategies.get(type);
    }

    private void initStrategies() {
        strategies.put(CatalogListTypes.ALL, catalogListStrategy);
        strategies.put(CatalogListTypes.BYIDTABLA, catalogTableIdStrategy);
        strategies.put(CatalogListTypes.MULTIPLE, catalogMultipleStrategy);
    }
}
