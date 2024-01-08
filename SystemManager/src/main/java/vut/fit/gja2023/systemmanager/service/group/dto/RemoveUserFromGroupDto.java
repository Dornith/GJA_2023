package vut.fit.gja2023.systemmanager.service.group.dto;

public record RemoveUserFromGroupDto(String userName, String groupName) implements UserGroupRecordDto {
}
