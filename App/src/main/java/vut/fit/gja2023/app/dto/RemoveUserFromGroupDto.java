package vut.fit.gja2023.app.dto;

/**
 * Data transfer object carrying data required for removing a user from a group.
 */
public record RemoveUserFromGroupDto(String userName, String groupName) {
}
