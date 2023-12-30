package vut.fit.gja2023.systemmanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.util.CommandUtils;

@Service
public class UserService {

    public HttpStatus createUser(String name) {
        ExitCodeEnum exitCode = CommandUtils.createNewUser(name);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.CONFLICT) {
            return HttpStatus.CONFLICT;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public HttpStatus deleteUser(String name) {
        ExitCodeEnum exitCode = CommandUtils.deleteUser(name);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
