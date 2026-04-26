package gr.pants.pro.edu_analysis.dto;


public record AnalystReadOnlyDTO(

        String uuid,

        String firstname,

        String lastname,

        String email,

        String firm
) {
}
