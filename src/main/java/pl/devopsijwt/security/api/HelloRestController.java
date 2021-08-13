package pl.devopsijwt.security.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.devopsijwt.security.domain.model.Role;

@RestController
@RequestMapping("/hello")
public class HelloRestController {

    @Secured(Role.ROLE_ADMIN)
    @GetMapping("/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }

    @Secured(Role.ROLE_USER)
    @GetMapping("user")
    public String helloUser() {
        return "Hello User";
    }
}
