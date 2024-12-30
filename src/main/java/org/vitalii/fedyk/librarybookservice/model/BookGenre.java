package org.vitalii.fedyk.librarybookservice.model;

public enum BookGenre {
    FICTION("FICTION"),

    NON_FICTION("NON_FICTION"),

    SCIENCE_FICTION("SCIENCE_FICTION"),

    FANTASY("FANTASY"),

    MYSTERY("MYSTERY"),

    BIOGRAPHY("BIOGRAPHY"),

    HISTORICAL("HISTORICAL"),

    ROMANCE("ROMANCE"),

    THRILLER("THRILLER"),

    HORROR("HORROR"),

    SELF_HELP("SELF_HELP"),

    HEALTH("HEALTH"),

    COOKBOOK("COOKBOOK"),

    CHILDREN("CHILDREN");

    private String value;

    BookGenre(String value) {
        this.value = value;
    }
}
