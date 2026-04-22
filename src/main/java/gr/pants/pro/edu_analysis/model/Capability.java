package gr.pants.pro.edu_analysis.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Table(name = "capabilities")
/**
 * JPA entity representing a fine-grained permission assignable to roles.
 */
public class Capability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true ,nullable = false)
    private String name;

    private String description;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.PROTECTED)
    @ManyToMany(mappedBy = "capabilities", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    /**
     * Returns a read-only view of roles linked to this capability.
     * @return immutable role set.
     */
    public Set<Role> getAllRoles() {
        return Set.copyOf(roles);
    }

    /**
     * Links this capability to a role.
     * @param role role to add.
     */
    public void addRole(Role role) {
        roles.add(role);
        role.getCapabilities().remove(this);
    }

    /**
     * Unlinks this capability from a role.
     * @param role role to remove.
     */
    public void removeRole(Role role) {
        roles.remove(role);
        role.getCapabilities().remove(this);
    }

    /**
     * Compares capabilities by id.
     * @param o other object.
     * @return true when ids are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Capability that = (Capability) o;
        return Objects.equals(id, that.id);
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
