package vut.fit.gja2023.systemmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vut.fit.gja2023.systemmanager.service.dto.NameWrapperDto;
import vut.fit.gja2023.systemmanager.service.group.GroupService;
import vut.fit.gja2023.systemmanager.service.group.dto.AddUserToGroupDto;
import vut.fit.gja2023.systemmanager.service.group.dto.GroupInfoDto;
import vut.fit.gja2023.systemmanager.service.group.dto.RemoveUserFromGroupDto;

import static vut.fit.gja2023.systemmanager.Constants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX + "/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "Get group info")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Group info successfully retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public ResponseEntity<GroupInfoDto> getGroup(@RequestParam(value = "group") String groupName) {
        return ResponseEntity.ok(groupService.getGroup(groupName));
    }

    @Operation(summary = "Add group to the system")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Group created")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "409", description = "Group already exists")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus createGroup(@RequestBody @NonNull NameWrapperDto dto) { //TODO: maybe return group system ID?
        return groupService.createGroup(dto);
    }

    @Operation(summary = "Delete group from the system")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User deleted")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus deleteGroup(@RequestBody @NonNull NameWrapperDto dto) {
        return groupService.deleteGroup(dto);
    }

    @Operation(summary = "Add user to group")
    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User has been added to the group")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User or group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus addUserToGroup(@RequestBody @NonNull AddUserToGroupDto dto) {
        return groupService.addUserToGroup(dto);
    }

    @Operation(summary = "Remove user from group")
    @DeleteMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "User has been removed from the group")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "User or group not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus removeUserFromGroup(@RequestBody @NonNull RemoveUserFromGroupDto dto) {
        return groupService.removeUserFromGroup(dto);
    }
}
