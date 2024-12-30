package org.vitalii.fedyk.librarybookservice.search;

import lombok.Getter;

import java.util.Arrays;

import static org.vitalii.fedyk.librarybookservice.constant.ExceptionConstants.SEARCH_OPERATION_NOT_FOUND;

@Getter
public enum SearchOperation {
    EQUALS(":"),
    NOT_EQUALS("!:"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUALS(">="),
    LESS_THAN_EQUALS("<="),
    CONTAINS("contains");

    private final String operation;

    SearchOperation(String operation) {
        this.operation = operation;
    }

    public static SearchOperation findByValue(final String operation) {
        return Arrays.stream(SearchOperation.values())
                .filter(o -> o.operation.equals(operation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(SEARCH_OPERATION_NOT_FOUND));
    }
}
