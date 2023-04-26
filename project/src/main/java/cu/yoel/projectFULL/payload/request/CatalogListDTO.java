package cu.yoel.projectFULL.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CatalogListDTO extends BaseDTO{
    String arg;
    String [] tablas;
}
