package cu.yoel.projectFULL.repositories;

import cu.yoel.projectFULL.models.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatusRepository extends MongoRepository<Status, String> {}
