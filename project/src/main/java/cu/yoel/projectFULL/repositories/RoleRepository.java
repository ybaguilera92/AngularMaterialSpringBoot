package cu.yoel.projectFULL.repositories;

import cu.yoel.projectFULL.models.ERole;
import cu.yoel.projectFULL.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
