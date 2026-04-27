package gr.pants.pro.edu_analysis.specification;

import gr.pants.pro.edu_analysis.core.filters.AnalystFilters;
import gr.pants.pro.edu_analysis.model.Analyst;
import org.springframework.data.jpa.domain.Specification;

public class AnalystSpecification {

    public static Specification<Analyst> build(AnalystFilters filters) {
        return Specification.allOf(
                hasLastName(filters.getLastname()),
                hasFirm(filters.getFirm()),
                isDeleted(filters.isDeleted())
        );
    }

    private static Specification<Analyst> hasLastName(String lastname) {
        return (root, query, cb) ->                 // cb = criteria builder
                lastname == null ? cb.conjunction() : cb.like(cb.lower(root.get("lastname")), "%" + lastname.toLowerCase() + "%");
    }

    private static Specification<Analyst> hasFirm(String firm) {
        return (root, query, cb) ->
                firm == null ? cb.conjunction() : cb.equal(cb.lower(root.get(firm).get("name")),firm.toLowerCase());
    }

    private static Specification<Analyst> isDeleted(boolean deleted) {
        return (root, query, cb) ->
                cb.equal(root.get("deleted"), deleted);
    }
}
