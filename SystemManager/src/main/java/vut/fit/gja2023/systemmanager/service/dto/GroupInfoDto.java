package vut.fit.gja2023.systemmanager.service.dto;

import java.util.List;

public record GroupInfoDto(int systemId, String name, List<String> users) {
}
