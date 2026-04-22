package gr.pants.pro.edu_analysis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Table(name = "roles")
/**
 * JPA entity representing a security role and its related users and capabilities.
 */
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "roles_capabilities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "capability_id")
    )
    private Set<Capability> capabilities = new HashSet<>();

    /**
     * Returns a read-only view of role capabilities.
     * @return immutable capability set.
     */
    public Set<Capability> getAllCapabilities() {
        return Set.copyOf(capabilities);
    }

    /**
     * Returns a read-only view of assigned users.
     * @return immutable user set.
     */
    public Set<User> getAllUsers() {
        return Set.copyOf(users);
    }

    /**
     * Assigns a capability to this role.
     * @param capability capability to add.
     */
    public void addCapability(Capability capability) {
        capabilities.add(capability);
        capability.getRoles().add(this);
    }

    /**
     * Removes a capability from this role.
     * @param capability capability to remove.
     */
    public void removeCapability(Capability capability) {
        capabilities.remove(capability);
        capability.getRoles().remove(this);
    }

    /**
     * Assigns a user to this role.
     * @param user user to add.
     */
    public void addUser(User user) {
        users.add(user);
        user.setRole(this);
    }

    /**
     * Removes a user from this role.
     * @param user user to remove.
     */
    public void removeUser(User user) {
        users.remove(user);
        user.setRole(null);
    }

    /**
     * Assigns multiple users to this role.
     * @param users users to add.
     */
    public void addUsers(Collection<User> users) {
        users.forEach(this::addUser);
    }

    /**
     * Compares roles by id.
     * @param o other object.
     * @return true when ids are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    /**
     * Returns hash code based on id.
     * @return id-based hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
