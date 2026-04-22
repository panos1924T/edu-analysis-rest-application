package gr.pants.pro.edu_analysis.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
/**
 * Base audited entity that provides creation/update timestamps and soft-delete state.
 */
public abstract class AbstractEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    private Instant deletedAt;

    @Column(nullable = false)
    private boolean deleted;

    /**
     * Marks the entity as soft deleted and sets deletion timestamp.
     */
    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }

}
