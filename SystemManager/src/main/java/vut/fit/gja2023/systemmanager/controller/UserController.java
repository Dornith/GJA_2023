package vut.fit.gja2023.systemmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vut.fit.gja2023.systemmanager.service.UserService;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Add user to system", description = "If not found return 404")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User added")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "409", description = "User already exists")
    @ApiResponse(responseCode = "500", description = "Unexpected error occured")
    public ResponseEntity<Void> createUser(@RequestBody String name) {
        return new ResponseEntity<>(userService.createUser(name));
    }

    @Operation(summary = "Delete user from system", description = "If not found return 404")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Unexpected error occured")
    public ResponseEntity<Void> deleteUser(@RequestBody String name) {
        return new ResponseEntity<>(userService.deleteUser(name));
    }

}
