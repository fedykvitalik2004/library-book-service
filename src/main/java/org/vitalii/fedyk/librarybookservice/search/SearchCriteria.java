package org.vitalii.fedyk.librarybookservice.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
}
