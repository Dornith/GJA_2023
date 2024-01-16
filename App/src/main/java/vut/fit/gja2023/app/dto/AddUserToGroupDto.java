package vut.fit.gja2023.app.dto;

/**
 * Data transfer object carrying data required for adding a user to a group.
 */
public record AddUserToGroupDto(String userName, String groupName) {
}
