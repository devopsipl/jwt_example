package pl.devopsijwt.security.domain.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refreshTokens")
@Data
@Builder
public class RefreshToken {

    @Id
    private ObjectId id;

    private ObjectId userId;

    private String token;

    private Instant expiryDate;

}
