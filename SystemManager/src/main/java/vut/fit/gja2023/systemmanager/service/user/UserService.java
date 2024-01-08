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

@Service
public class UserService {


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

    public HttpStatus createUser(CreateUserDto dto) {
        if (StringUtils.isEmpty(dto.name())) {
            throw new BaseBadRequestException("User name cannot be empty");
        }
        ExitCodeEnum exitCode = CommandUtils.createNewUser(dto.name(), dto.password());
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.CONFLICT) {
            throw new UserAlreadyExistsException(dto.name());
        } else {
            throw new BaseServerErrorException();

        }
    }

    public HttpStatus deleteUser(NameWrapperDto dto) {
        if (StringUtils.isEmpty(dto.name())) {
            throw new BaseBadRequestException("User name cannot be empty");
        }
        ExitCodeEnum exitCode = CommandUtils.deleteUser(dto.name());
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new UserNotFoundException(dto.name());
        } else {
            throw new BaseServerErrorException();
        }
    }
}
