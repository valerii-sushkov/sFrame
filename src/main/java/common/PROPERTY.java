package common;


import helpers.Config;

public enum PROPERTY {
    BROWSER("browser"),
    HOST_URL("host"),
    TIMEOUT("timeout"),
    RESOURCE_FOLDER("res.folder"),
    TMP_FOLDER("tmp.folder"),
    SUITE_TO_RUN("test.suite"),
    TEST_TO_RUN("test.name"),
    TEST_ID("test.id");

    private String valueName;

    PROPERTY(final String param) {
        valueName = param;
    }

    public String getValueName() {
        return valueName;
    }

    public String getValue() {
        return Config.getProperty(this);
    }
}
