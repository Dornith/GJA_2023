package vut.fit.gja2023.app.dto;

import vut.fit.gja2023.app.enums.DirectoryModeEnum;

/**
 * Data transfer object carrying data required for creating a directory.
 */
public record CreateDirectoryDto(String path, DirectoryModeEnum mode, String ownerGroupName) {
}
