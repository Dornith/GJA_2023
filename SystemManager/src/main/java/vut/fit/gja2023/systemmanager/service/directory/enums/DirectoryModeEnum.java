package vut.fit.gja2023.systemmanager.service.directory.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DirectoryModeEnum {
    PUBLIC("777"),
    PROJECT("770"),
    PRIVATE("700");

    private final String mode;
}
