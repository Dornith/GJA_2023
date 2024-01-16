package vut.fit.gja2023.app.dto;

/**
 * Data transfer object carrying data required for creating a user.
 */
public record CreateUserDto(String name, String password) {
}
