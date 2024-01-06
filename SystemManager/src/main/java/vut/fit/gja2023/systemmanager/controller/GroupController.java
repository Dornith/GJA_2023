package vut.fit.gja2023.systemmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vut.fit.gja2023.systemmanager.service.GroupService;
import vut.fit.gja2023.systemmanager.service.dto.GroupInfoDto;
import vut.fit.gja2023.systemmanager.service.dto.RemoveUserFromGroupDto;

import static vut.fit.gja2023.systemmanager.Constants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX + "/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "Get group info")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Group and its users returned")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public ResponseEntity<GroupInfoDto> getGroup(@RequestParam(value = "group") String groupName) {
        return ResponseEntity.ok(groupService.getGroup(groupName));
    }

    @Operation(summary = "Add group to the system")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Group created")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "409", description = "Group already exists")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public ResponseEntity<Void> createGroup(@RequestBody String name) {
        return new ResponseEntity<>(groupService.createGroup(name));
    }

    @Operation(summary = "Delete group from the system")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User deleted")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public ResponseEntity<Void> deleteGroup(@RequestBody String name) {
        return new ResponseEntity<>(groupService.deleteGroup(name));
    }

    @Operation(summary = "Add user to group")
    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User has been added to the group")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User or group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public ResponseEntity<Void> addUserToGroup(@RequestBody RemoveUserFromGroupDto dto) {
        return new ResponseEntity<>(groupService.addUserToGroup(dto));
    }

    @Operation(summary = "Remove user from group")
    @DeleteMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User has been removed from the group")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User or group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public ResponseEntity<Void> removeUserFromGroup(@RequestBody RemoveUserFromGroupDto dto) {
        return new ResponseEntity<>(groupService.removeUserFromGroup(dto));
    }
}
