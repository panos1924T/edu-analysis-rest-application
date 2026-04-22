package gr.pants.pro.edu_analysis.model;

import gr.pants.pro.edu_analysis.model.static_data.Firm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "analysts")
/**
 * JPA entity representing an analyst profile managed by the system.
 */
public class Analyst extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Firm firm;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * Assigns a UUID before first persistence when missing.
     */
    @PrePersist
    public void initializeUuid() {
        if (uuid == null) uuid = UUID.randomUUID();
    }

    /**
     * Compares analysts by UUID.
     * @param o other object.
     * @return true when UUID values are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Analyst analyst)) return false;
        return Objects.equals(getUuid(), analyst.getUuid());
    }

    /**
     * Returns hash code based on UUID.
     * @return UUID-based hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
