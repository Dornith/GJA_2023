package vut.fit.gja2023.systemmanager.service.directory.dto;

import vut.fit.gja2023.systemmanager.service.directory.enums.DirectoryModeEnum;

public record CreateDirectoryDto(String path, DirectoryModeEnum mode, String ownerGroupName) {
}
