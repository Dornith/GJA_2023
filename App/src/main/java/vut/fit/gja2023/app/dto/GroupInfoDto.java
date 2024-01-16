package vut.fit.gja2023.app.dto;

import java.util.List;

/**
 * Data transfer object carrying data about a group.
 */
public record GroupInfoDto(int systemId, String name, List<String> users) {
}
