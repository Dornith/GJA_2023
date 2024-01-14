package vut.fit.gja2023.app;

public enum Layout {
    DEFAULT("layouts/layout"),
    EMPTY("layouts/empty");

    public final String value;

    private Layout(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
