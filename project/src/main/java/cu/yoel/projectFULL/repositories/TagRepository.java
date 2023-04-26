package cu.yoel.projectFULL.repositories;

import cu.yoel.projectFULL.models.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {}
