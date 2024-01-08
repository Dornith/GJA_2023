package vut.fit.gja2023.systemmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vut.fit.gja2023.systemmanager.service.dto.NameWrapperDto;
import vut.fit.gja2023.systemmanager.service.user.UserService;
import vut.fit.gja2023.systemmanager.service.user.dto.CreateUserDto;

import static vut.fit.gja2023.systemmanager.Constants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX + "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Check if user is already in the system")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User exists")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
    public HttpStatus getUser(@RequestParam(value = "user") String userName) {
        return userService.existsUser(userName);
    }

    @Operation(summary = "Add user to the system")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User added")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
    public HttpStatus createUser(@RequestBody @NonNull CreateUserDto dto) {
        return userService.createUser(dto);
    }

    @Operation(summary = "Delete user from the system")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error occurred")
    public HttpStatus deleteUser(@RequestBody @NonNull NameWrapperDto dto) {
        return userService.deleteUser(dto);
    }

}
