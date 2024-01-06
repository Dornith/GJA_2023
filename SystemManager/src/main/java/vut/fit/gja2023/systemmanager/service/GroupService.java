package vut.fit.gja2023.systemmanager.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseBadRequestException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.BaseServerErrorException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.GroupAlreadyExistsException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.GroupNotFoundException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.GroupUserNotFoundException;
import vut.fit.gja2023.systemmanager.errorhandling.exception.UserNotFoundException;
import vut.fit.gja2023.systemmanager.service.dto.GroupInfoDto;
import vut.fit.gja2023.systemmanager.service.dto.RemoveUserFromGroupDto;
import vut.fit.gja2023.systemmanager.util.CommandUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class GroupService {

    public GroupInfoDto getGroup(String groupName) {
        if (StringUtils.isEmpty(groupName)) {
            throw new BaseBadRequestException();
        }
        Pair<ExitCodeEnum, String> exitCode = CommandUtils.getGroup(groupName);
        ExitCodeEnum exitCodeEnum = exitCode.getLeft();
        if (exitCodeEnum == ExitCodeEnum.SUCCESS) {
            return parseGetGroupOutput(exitCode.getRight());
        } else if (exitCodeEnum == ExitCodeEnum.NOT_FOUND) {
            throw new GroupNotFoundException(groupName);
        } else {
            throw new BaseServerErrorException();
        }
    }

    private GroupInfoDto parseGetGroupOutput(@NonNull String commandOutput) {
        Pattern pattern = Pattern.compile("^(.*?):.*?:(\\d+):(.*)$");
        Matcher matcher = pattern.matcher(commandOutput.trim());

        if (matcher.matches()) {
            int groupId = Integer.parseInt(matcher.group(2));
            String groupName = matcher.group(1);
            String membersString = matcher.group(3);
            List<String> members = Arrays.asList(membersString.split(","));

            return new GroupInfoDto(groupId, groupName, members);
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus createGroup(String groupName) {
        if (StringUtils.isEmpty(groupName)) {
            throw new BaseBadRequestException();
        }
        ExitCodeEnum exitCode = CommandUtils.createNewGroup(groupName);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.CONFLICT) {
            throw new GroupAlreadyExistsException(groupName);
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus deleteGroup(String groupName) {
        if (StringUtils.isEmpty(groupName)) {
            throw new BaseBadRequestException();
        }
        ExitCodeEnum exitCode = CommandUtils.deleteGroup(groupName);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new GroupNotFoundException(groupName);
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus addUserToGroup(RemoveUserFromGroupDto dto) {
        validateUserAndGroup(dto);
        ExitCodeEnum exitCode = CommandUtils.addUserToGroup(dto);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new GroupUserNotFoundException(dto.userName(), dto.groupName());
        } else {
            throw new BaseServerErrorException();
        }
    }

    public HttpStatus removeUserFromGroup(RemoveUserFromGroupDto dto) {
        validateUserAndGroup(dto);
        ExitCodeEnum exitCode = CommandUtils.removeUserFromGroup(dto);
        if (exitCode == ExitCodeEnum.SUCCESS) {
            return HttpStatus.OK;
        } else if (exitCode == ExitCodeEnum.NOT_FOUND) {
            throw new GroupUserNotFoundException(dto.userName(), dto.groupName());
        } else {
            throw new BaseServerErrorException();
        }
    }

    private void validateUserAndGroup(RemoveUserFromGroupDto dto) {
        if (dto == null || StringUtils.isEmpty(dto.userName()) || StringUtils.isEmpty(dto.groupName())) {
            throw new BaseBadRequestException();
        }
        if (!CommandUtils.existsGroup(dto.groupName())) {
            throw new GroupNotFoundException(dto.groupName());
        }
        if (!CommandUtils.existsUser(dto.userName())) {
            throw new UserNotFoundException(dto.userName());
        }
    }

}
