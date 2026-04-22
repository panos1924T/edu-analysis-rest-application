package gr.pants.pro.edu_analysis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Table(name = "personal_info")
public class PersonalInfo extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identity_number", unique = true, nullable = false)
    private String identityNumber;

    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Column(name = "municipality_of_registration", nullable = false)
    private String municipalityOfRegistration;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "identity_file_id", unique = true)
    private Attachment identityFile;

    public void addIdentityFile(Attachment attachment) {
        this.identityFile = attachment;
    }

    public void removeIdentityFile() {
        this.identityFile = null;
    }
}
