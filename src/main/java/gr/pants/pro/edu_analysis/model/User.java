package gr.pants.pro.edu_analysis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
@Table(name = "users")
/**
 * JPA entity for authenticated application users, including Spring Security user details.
 */
public class User extends AbstractEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Creates a user with raw username and password.
     * @param username login username.
     * @param password raw password.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Indicates whether this user can authenticate.
     * @return true when the user is not soft deleted.
     */
    @Override
    public boolean isEnabled() {
        return !isDeleted();
    }

    /**
     * Indicates whether credentials are still valid.
     * @return always true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the account is unlocked.
     * @return always true.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the account is still active.
     * @return always true.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Builds granted authorities from role and capabilities.
     * @return authority collection used by Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        role.getCapabilities()
                .forEach(capability -> grantedAuthorities.add(new SimpleGrantedAuthority(capability.getName())));
        return grantedAuthorities;
    }

    /**
     * Compares users by username.
     * @param o other object.
     * @return true when usernames are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        return Objects.equals(getUsername(), user.getUsername());
    }

    /**
     * Returns hash code based on username.
     * @return username-based hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername());
    }
}
