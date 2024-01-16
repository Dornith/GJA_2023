package vut.fit.gja2023.systemmanager.service.directory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseBadRequestException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseServerErrorException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.DirectoryNotFoundException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.FileAlreadyExistsException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.PathNotUnderBasePathException;
import vut.fit.gja2023.systemmanager.service.directory.dto.CreateDirectoryDto;
import vut.fit.gja2023.systemmanager.service.directory.dto.DeleteDirectoryDto;
import vut.fit.gja2023.systemmanager.service.directory.dto.ModifyDirectoryGroupDto;
import vut.fit.gja2023.systemmanager.util.CommandUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A service for manipulating the directory structure of an OS.
 */
@Slf4j
@Service
public class DirectoryService {

    @Value("${base.path:/tmp}")
    private String BASE_PATH;

    /**
     * Create directory on specified path
     * Can also specify privileges for the directory
     * If the path is empty, the directory will be created in the base path
     *
     * @param dto Contains path, mode and ownerGroupName to be used in creating new directory.
     * @return HTTP status indicating success or containig a problem that ocurred.
     */
    public HttpStatus createDirectory(CreateDirectoryDto dto) {
        String processedPath = processPath(dto.path());
        ExitCodeEnum exitCode = CommandUtils.createNewDirectory(processedPath, dto.mode(), dto.ownerGroupName());
        if (null == exitCode) {
            throw new BaseServerErrorException();
        } else {
            switch (exitCode) {
                case SUCCESS:
                    return HttpStatus.OK;
                case NOT_FOUND:
                    throw new BaseBadRequestException("Path not found");
                case CONFLICT:
                    throw new FileAlreadyExistsException(dto.path());
                default:
                    throw new BaseServerErrorException();
            }
        }
    }

    /**
     * Deletes a specified directory.
     * 
     * @param dto Contains the path of a directory.
     * @return HTTP status indicating success or containig a problem that ocurred.
     */
    public HttpStatus deleteDirectory(DeleteDirectoryDto dto) {
        String processedPath = processPath(dto.path());
        ExitCodeEnum exitCode = CommandUtils.deleteDirectory(processedPath);
        if (null == exitCode) {
            throw new BaseServerErrorException();
        } else {
            switch (exitCode) {
                case SUCCESS:
                    return HttpStatus.OK;
                case NOT_FOUND:
                    throw new DirectoryNotFoundException(dto.path());
                default:
                    throw new BaseServerErrorException();
            }
        }
    }

    /**
     * Changes which group does a specified directory belong to.
     * 
     * @param dto Contains the path of a directory and the name of a new owner group.
     * @return HTTP status indicating success or containig a problem that ocurred.
     */
    public HttpStatus modifyDirectoryGroup(ModifyDirectoryGroupDto dto) {
        String processedPath = processPath(dto.path());
        ExitCodeEnum exitCode = CommandUtils.changeDirGroup(processedPath, dto.groupName());
        if (null == exitCode) {
            throw new BaseServerErrorException();
        }
        else {
            switch (exitCode) {
                case SUCCESS:
                    return HttpStatus.OK;
                case NOT_FOUND:
                    throw new DirectoryNotFoundException(processedPath);
                default:
                    throw new BaseServerErrorException();
            }
        }
    }

    /**
     * Converts a given director path to a normalized directory path.
     * 
     * @param path A directory path.
     * @return A normalized directory path.
     */
    private String processPath(String path) {
        if (!StringUtils.isEmpty(path)) {
            Path normalizedPath = Paths.get(BASE_PATH, path).normalize();
            if (!normalizedPath.startsWith(BASE_PATH)) {
                log.error("Path {} is not under base path {}", path, BASE_PATH);
                throw new PathNotUnderBasePathException(BASE_PATH);
            }
            return normalizedPath.toString();
        } else {
            throw new BaseBadRequestException("Path cannot be empty");
        }
    }
}
