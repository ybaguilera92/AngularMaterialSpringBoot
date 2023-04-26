package cu.yoel.projectFULL.payload.request;

import cu.yoel.projectFULL.models.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDTO extends UserDTO{


    private String fileEncode;
    private File avatar;
    private File cover;

}
