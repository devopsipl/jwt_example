package pl.devopsijwt.security.domain.mapper;


import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import pl.devopsijwt.security.domain.dto.CreateUserResponse;
import pl.devopsijwt.security.domain.model.User;
import pl.devopsijwt.security.infrastructure.reopsitory.UserRepo;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ObjectIdMapper.class})
public abstract class UserViewMapper {

    @Autowired
    private UserRepo userRepo;

    public abstract CreateUserResponse toUserView(User user);

    public abstract List<CreateUserResponse> toUserView(List<User> users);

    public CreateUserResponse toUserViewById(ObjectId id) {
        if (id == null) {
            return null;
        }
        return toUserView(userRepo.getById(id));
    }

}
