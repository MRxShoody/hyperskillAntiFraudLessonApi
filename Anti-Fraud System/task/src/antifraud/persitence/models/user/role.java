package antifraud.persitence.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;


@Entity
public class role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<user> users;

    public role() {
    }

    public role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<user> getUsers() {
        return users;
    }

}
