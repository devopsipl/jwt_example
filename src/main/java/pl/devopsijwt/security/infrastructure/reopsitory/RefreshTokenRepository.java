package pl.devopsijwt.security.infrastructure.reopsitory;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.devopsijwt.security.domain.model.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {

    Optional<RefreshToken> findByToken(String token);
}
