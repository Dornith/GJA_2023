package vut.fit.gja2023.app.dto;

import vut.fit.gja2023.app.enums.DirectoryModeEnum;

public record CreateDirectoryDto(String path, DirectoryModeEnum mode, String ownerGroupName) {
}
