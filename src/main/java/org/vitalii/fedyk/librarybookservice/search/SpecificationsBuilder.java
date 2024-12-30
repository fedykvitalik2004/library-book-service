package org.vitalii.fedyk.librarybookservice.search;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SpecificationsBuilder<T> {
    private List<SearchCriteria> criteria = new ArrayList<>();

    public SpecificationsBuilder<T> with(final String key, final String operation, final Object value) {
        SearchCriteria searchCriteria = new SearchCriteria(key, SearchOperation.findByValue(operation), value);
        criteria.add(searchCriteria);
        return this;
    }

    public Specification<T> build(final Function<SearchCriteria, Specification<T>> mapper) {
        if (criteria.isEmpty()) {
            return null;
        }
        final List<Specification<T>> specifications = criteria.stream().map(mapper).toList();
        return combineSpecification(specifications);
    }

    public Specification<T> combineSpecification(final List<Specification<T>> specifications) {
        Specification<T> combinedSpecification = Specification.where(specifications.get(0));
        for (int i = 1; i < specifications.size(); i++) {
            combinedSpecification = combinedSpecification.and(specifications.get(i));
        }
        return combinedSpecification;
    }
}
