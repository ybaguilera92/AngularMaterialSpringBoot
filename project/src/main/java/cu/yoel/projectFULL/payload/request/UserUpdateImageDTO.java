package cu.yoel.projectFULL.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateImageDTO extends UserUpdateDTO {

//    @DBRef
//    private File avatar;
    private MultipartFile multipartFilea;
}
