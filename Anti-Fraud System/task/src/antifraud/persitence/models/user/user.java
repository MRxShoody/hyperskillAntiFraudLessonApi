package antifraud.persitence.models.user;


import antifraud.persitence.models.transaction.transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;


@Entity
@Table(name = "users")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name="user_role")
    private role role;

    @JsonIgnore
    private boolean locked = false;

    public user() {
    }

    public user(long id, String name, String username, String password, role role, boolean locked) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locked = locked;
    }

    public user(String name, String username, String password, role role, boolean locked) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locked = locked;
    }

    public user(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return role.getRoleName();
    }

    public long getId() {
        return this.id;
    }

    public boolean getLocked() {
        return this.locked;
    }

    public void setRole(role supportRole) {
        this.role = supportRole;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}