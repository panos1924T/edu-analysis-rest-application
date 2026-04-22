package gr.pants.pro.edu_analysis.model.static_data;

import gr.pants.pro.edu_analysis.model.Analyst;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "firms")
/**
 * JPA entity for brokerage or research firms associated with analysts.
 */
public class Firm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "firm", fetch = FetchType.LAZY)
    private Set<Analyst> analysts = new HashSet<>();

    /**
     * Returns a read-only view of analysts in this firm.
     * @return unmodifiable analyst set.
     */
    public Set<Analyst> getAllAnalysts() {
        return Collections.unmodifiableSet(analysts);
    }

    /**
     * Adds an analyst to this firm.
     * @param analyst analyst to add.
     */
    public void addAnalyst(Analyst analyst) {
        if (analysts == null) analysts = new HashSet<>();
        analysts.add(analyst);
        analyst.setFirm(this);
    }

    /**
     * Removes an analyst from this firm.
     * @param analyst analyst to remove.
     */
    public void removeAnalyst(Analyst analyst) {
        if (analysts == null) return;
        analysts.remove(analyst);
        analyst.setFirm(null);
    }
}
