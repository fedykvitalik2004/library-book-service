package org.vitalii.fedyk.librarybookservice.search;

import org.springframework.data.jpa.domain.Specification;

public abstract class GenericSpecification <T> implements Specification<T> {
    protected final SearchCriteria searchCriteria;

    public GenericSpecification(final SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
