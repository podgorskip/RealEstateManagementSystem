package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "role")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "client"),
        @JsonSubTypes.Type(value = Owner.class, name = "owner"),
        @JsonSubTypes.Type(value = Accountant.class, name = "accountant"),
        @JsonSubTypes.Type(value = Administrator.class, name = "admin"),
        @JsonSubTypes.Type(value = Broker.class, name = "broker")
})
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@MappedSuperclass
public abstract class User {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    @Setter(AccessLevel.NONE)
    private Date created;

    @Transient
    @Setter(AccessLevel.NONE)
    private Role role;

    protected void setRole(Role role) {
        this.role = role;
    }
}
