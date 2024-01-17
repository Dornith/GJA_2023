package vut.fit.gja2023.app.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Layout {
    DEFAULT("layouts/layout"),
    EMPTY("layouts/empty");

    public final String value;
}
