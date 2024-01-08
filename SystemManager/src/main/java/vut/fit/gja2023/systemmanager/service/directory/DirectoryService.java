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
     * @param dto contains path, mode and ownerGroupName to be used in creating new directory
     * @return
     * @return
     */
    public HttpStatus createDirectory(CreateDirectoryDto dto) {
        String processedPath = processPath(dto.path());
        ExitCodeEnum exitCode = CommandUtils.createNewDirectory(processedPath, dto.mode(), dto.ownerGroupName());
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new BaseBadRequestException("Path not found");
        } else if (exitCode == ExitCodeEnum.CONFLICT) {
            throw new FileAlreadyExistsException(dto.path());
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus deleteDirectory(DeleteDirectoryDto dto) {
        String processedPath = processPath(dto.path());
        ExitCodeEnum exitCode = CommandUtils.deleteDirectory(processedPath);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new DirectoryNotFoundException(dto.path());
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus modifyDirectoryGroup(ModifyDirectoryGroupDto dto) {
        String processedPath = processPath(dto.path());
        ExitCodeEnum exitCode = CommandUtils.changeDirGroup(processedPath, dto.groupName());
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new DirectoryNotFoundException(processedPath);
        } else {
            throw new BaseServerErrorException();
        }
    }

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
