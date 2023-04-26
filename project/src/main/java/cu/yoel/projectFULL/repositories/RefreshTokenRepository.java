package cu.yoel.projectFULL.repositories;

import cu.yoel.projectFULL.models.RefreshToken;
import cu.yoel.projectFULL.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);

}
