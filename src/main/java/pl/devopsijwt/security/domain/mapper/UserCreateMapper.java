package pl.devopsijwt.security.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.devopsijwt.security.domain.dto.CreateUserRequest;
import pl.devopsijwt.security.domain.model.Role;
import pl.devopsijwt.security.domain.model.User;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public abstract class UserCreateMapper {

    @Mapping(target = "authorities", ignore = true)
    public abstract User create(CreateUserRequest request);

    @AfterMapping
    protected void afterCreate(CreateUserRequest request, @MappingTarget User user) {
        if (request.getAuthorities() != null) {
            user.setAuthorities(request.getAuthorities().stream().map(Role::new).collect(toSet()));
        }
    }
}
