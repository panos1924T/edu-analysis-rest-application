package gr.pants.pro.edu_analysis.core.filters;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString
public class AnalystFilters {
    private UUID uuid;
    private String email;
    private String identityNumber;
    private String lastname;
    private boolean deleted;
    private String firm;
}
