package vn.com.buaansach.model.entity.enumeration;

public enum Language {
    VIETNAMESE("vi"),
    ENGLISH("en");

    private String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
