package vut.fit.gja2023.systemmanager.service.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseBadRequestException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseServerErrorException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.UserAlreadyExistsException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.UserNotFoundException;
import vut.fit.gja2023.systemmanager.service.dto.NameWrapperDto;
import vut.fit.gja2023.systemmanager.service.user.dto.CreateUserDto;
import vut.fit.gja2023.systemmanager.util.CommandUtils;

/**
 * A service for manipulating users in the system.
 */
@Service
public class UserService {

    /**
     * Checks whether a user already exists in the system.
     * 
     * @param name A user's name.
     * @return HTTP response containing true, if a user with given name already exists, false otherwise
     */
    public HttpStatus existsUser(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new UserNotFoundException(name);
        }
        if (CommandUtils.existsUser(name)) {
            return HttpStatus.OK;
        } else {
            throw new UserNotFoundException(name);
        }
    }

    /**
     * Creates a user in the system.
     * 
     * @param dto Contains the user's login and password.
     * @return HTTP status indicating success or containig a problem that ocurred.
     */
    public HttpStatus createUser(CreateUserDto dto) {
        if (StringUtils.isEmpty(dto.name())) {
            throw new BaseBadRequestException("User name cannot be empty");
        }
        ExitCodeEnum exitCode = CommandUtils.createNewUser(dto.name(), dto.password());
        if (null == exitCode) {
            throw new BaseServerErrorException();

        } else {
            switch (exitCode) {
                case SUCCESS:
                    return HttpStatus.OK;
                case CONFLICT:
                    throw new UserAlreadyExistsException(dto.name());
                default:
                    throw new BaseServerErrorException();
            }
        }
    }

    /**
     * Deletes the user with a specified login from the system.
     * 
     * @param dto Contains the login of a user.
     * @return HTTP status indicating success or containig a problem that ocurred.
     */
    public HttpStatus deleteUser(NameWrapperDto dto) {
        if (StringUtils.isEmpty(dto.name())) {
            throw new BaseBadRequestException("User name cannot be empty");
        }
        ExitCodeEnum exitCode = CommandUtils.deleteUser(dto.name());
        if (null == exitCode) {
            throw new BaseServerErrorException();
        } else {
            switch (exitCode) {
                case SUCCESS:
                    return HttpStatus.OK;
                case NOT_FOUND:
                    throw new UserNotFoundException(dto.name());
                default:
                    throw new BaseServerErrorException();
            }
        }
    }
}
