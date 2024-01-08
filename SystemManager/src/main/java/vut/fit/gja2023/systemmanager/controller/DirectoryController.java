package vut.fit.gja2023.systemmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vut.fit.gja2023.systemmanager.service.directory.DirectoryService;
import vut.fit.gja2023.systemmanager.service.directory.dto.CreateDirectoryDto;
import vut.fit.gja2023.systemmanager.service.directory.dto.DeleteDirectoryDto;
import vut.fit.gja2023.systemmanager.service.directory.dto.ModifyDirectoryGroupDto;

import static vut.fit.gja2023.systemmanager.Constants.API_PREFIX;

@RestController
@RequestMapping(value = API_PREFIX + "/dir")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @Operation(summary = "Create directory on specified path", description = "Can also specify privileges for the directory")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Directory created")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Path not found")
    @ApiResponse(responseCode = "409", description = "Directory already exists on specified path")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus createDirectory(@RequestBody @NonNull CreateDirectoryDto dto) {
        return directoryService.createDirectory(dto);
    }

    @Operation(summary = "Delete directory on specified path")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Directory deleted")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Directory not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus deleteDirectory(@RequestBody @NonNull DeleteDirectoryDto dto) {
        return directoryService.deleteDirectory(dto);
    }

    @Operation(summary = "Change directory group")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Directory group changed")
    @ApiResponse(responseCode = "401", description = "Unauthorized/Invalid Api Key")
    @ApiResponse(responseCode = "404", description = "Directory not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error occurred")
    public HttpStatus changeDirectoryGroup(@RequestBody @NonNull ModifyDirectoryGroupDto dto) {
        return directoryService.modifyDirectoryGroup(dto);
    }
}
