package vut.fit.gja2023.app;

public enum View {
    MAIN("views/index"),
    ERROR("views/error");

    public final  String value;

    private View(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
