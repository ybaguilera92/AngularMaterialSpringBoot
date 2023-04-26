package cu.yoel.projectFULL.repositories;

import cu.yoel.projectFULL.models.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends MongoRepository<File, String> {}
