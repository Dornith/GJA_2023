package vut.fit.gja2023.systemmanager.util;


import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.service.directory.enums.DirectoryModeEnum;
import vut.fit.gja2023.systemmanager.service.group.dto.AddUserToGroupDto;
import vut.fit.gja2023.systemmanager.service.group.dto.RemoveUserFromGroupDto;

import java.io.IOException;

import static vut.fit.gja2023.systemmanager.Constants.GETENT_NOTFOUND_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.GPASSWD_NOTFOUND_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.GROUPADD_CONFLICT_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.GROUPDEL_NOTFOUND_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.MKDIR_ERROR_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.SUCCESS_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.USERADD_CONFLICT_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.USERDEL_NOTFOUND_EXIT_CODE;

@Slf4j
@UtilityClass
public class CommandUtils {


    /**
     * Get information about group
     *
     * @param groupName name of the group to be found
     * @return pair where left value is {@link ExitCodeEnum} representing the group information finding process result
     * and right value is the command output if the command was successful or null if not
     */
    public static Pair<ExitCodeEnum, String> getGroup(@NonNull String groupName) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("getent", "group", groupName);
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> {
                    String output = new String(process.getInputStream().readAllBytes());
                    yield new ImmutablePair<>(ExitCodeEnum.SUCCESS, output);
                }
                case GETENT_NOTFOUND_EXIT_CODE -> new ImmutablePair<>(ExitCodeEnum.NOT_FOUND, null);
                default -> {
                    log.error("Unhandled error occurred while getting groups, exit code: " + exitCode);
                    yield new ImmutablePair<>(ExitCodeEnum.ERROR, null);
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while getting groups", e);
            return new ImmutablePair<>(ExitCodeEnum.ERROR, null);
        } catch (IOException e) {
            log.error("Unhandled error occurred while getting groups", e);
            return new ImmutablePair<>(ExitCodeEnum.ERROR, null);
        }
    }


    /**
     * Create user on system
     *
     * @param name name of user to be created
     * @return {@link ExitCodeEnum} representing the user creation process result
     */
    public static ExitCodeEnum createNewUser(@NonNull String name, String password) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("useradd", name);
        if (!StringUtils.isEmpty(password)) {
            builder.command().add("-p");
            builder.command().add(password);
        }
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case USERADD_CONFLICT_EXIT_CODE -> ExitCodeEnum.CONFLICT;
                default -> {
                    log.error("Unhandled error occurred while creating user, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while creating user", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while creating user", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Deletes user from system
     *
     * @param name name of user to be deleted
     * @return {@link ExitCodeEnum} representing the deletion process result
     */
    public static ExitCodeEnum deleteUser(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("userdel", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case USERDEL_NOTFOUND_EXIT_CODE -> ExitCodeEnum.NOT_FOUND;
                default -> {
                    log.error("Unhandled error occurred while deleting user, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while deleting user", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while deleting user", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Creates group on system
     *
     * @param name name of group to be created
     * @return {@link ExitCodeEnum} representing the group creation process result
     */
    public static ExitCodeEnum createNewGroup(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("groupadd", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case GROUPADD_CONFLICT_EXIT_CODE -> ExitCodeEnum.CONFLICT;
                default -> {
                    log.error("Unhandled error occurred while creating group, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while creating group", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while creating group", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Deletes group from system
     *
     * @param name name of group to be deleted
     * @return {@link ExitCodeEnum} representing the deletion process result
     */
    public static ExitCodeEnum deleteGroup(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("groupdel", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case GROUPDEL_NOTFOUND_EXIT_CODE -> ExitCodeEnum.NOT_FOUND;
                default -> {
                    log.error("Unhandled error occurred while deleting group, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while deleting group", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while deleting group", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Adds user to group
     *
     * @param dto {@link AddUserToGroupDto} containing user and group names
     * @return {@link ExitCodeEnum} representing the addition process result
     */
    public static ExitCodeEnum addUserToGroup(@NonNull AddUserToGroupDto dto) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("gpasswd", "-a", dto.userName(), dto.groupName());
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case GPASSWD_NOTFOUND_EXIT_CODE -> ExitCodeEnum.NOT_FOUND;
                default -> {
                    log.error("Unhandled error occurred while adding user to group, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while adding user to group", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while adding user to group", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Removes user from group
     *
     * @param dto {@link RemoveUserFromGroupDto} containing user and group names
     * @return {@link ExitCodeEnum} representing the removal process result
     */
    public static ExitCodeEnum removeUserFromGroup(@NonNull RemoveUserFromGroupDto dto) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("gpasswd", "-d", dto.userName(), dto.groupName());
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case GPASSWD_NOTFOUND_EXIT_CODE -> ExitCodeEnum.NOT_FOUND;
                default -> {
                    log.error("Unhandled error occurred while removing user from group, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while removing user from group", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while removing user from group", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Checks if user exists
     *
     * @param name name of user to be checked
     * @return true if user exists, false otherwise
     */
    public static boolean existsUser(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("id", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    /**
     * Checks if group exists
     *
     * @param name name of group to be checked
     * @return true if group exists, false otherwise
     */
    public static boolean existsGroup(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("getent", "group", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    /**
     * Checks if directory exists
     *
     * @param path           path to directory to be checked
     * @param mode           mode representing rights of access to owner/group/others -optional
     * @param ownerGroupName name of group to be set as owner -optional
     * @return {@link ExitCodeEnum} representing the directory creation process result
     */
    public static ExitCodeEnum createNewDirectory(@NonNull String path, DirectoryModeEnum mode, String ownerGroupName) {
        if (checkIfFileExistsOnPath(path)) {
            return ExitCodeEnum.CONFLICT;
        }
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("mkdir");
        builder.command().add("-m");
        if (mode != null) {
            builder.command().add(mode.getMode());
        } else {
            builder.command().add(DirectoryModeEnum.PUBLIC.getMode());
        }
        builder.command().add(path);
        log.info("Executing command: " + String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> {
                    if (ownerGroupName != null) {
                        if (changeDirGroup(path, ownerGroupName) == ExitCodeEnum.SUCCESS) {
                            yield ExitCodeEnum.SUCCESS;
                        } else {
                            deleteDirectory(path);
                            yield ExitCodeEnum.ERROR;
                        }
                    }
                    yield ExitCodeEnum.SUCCESS;
                }
                case MKDIR_ERROR_EXIT_CODE -> ExitCodeEnum.NOT_FOUND;
                default -> {
                    log.error("Unhandled error occurred while creating directory, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred while creating directory", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Unhandled error occurred while creating directory", e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Deletes directory
     *
     * @param path path to directory to be deleted
     * @return {@link ExitCodeEnum} representing the removal process result
     */
    public static ExitCodeEnum deleteDirectory(@NonNull String path) {
        if (!checkIfDirExistsOnPath(path)) {
            return ExitCodeEnum.NOT_FOUND;
        }
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("rm", "-r", path);
        log.info("Executing command: " + String.join(" ", builder.command()));
        if (checkSuccess(builder)) {
            return ExitCodeEnum.SUCCESS;
        } else {
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Changes directory group
     *
     * @param path      path to directory to be changed
     * @param groupName name of group to be set
     * @return {@link ExitCodeEnum} representing the removal process result
     */
    public static ExitCodeEnum changeDirGroup(@NonNull String path, @NonNull String groupName) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("chgrp", groupName, path);
        log.info("Executing command: " + String.join(" ", builder.command()));
        if (checkSuccess(builder)) {
            return ExitCodeEnum.SUCCESS;
        } else {
            return ExitCodeEnum.ERROR;
        }
    }

    private static boolean checkIfFileExistsOnPath(String path) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("test", "-e", path);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    private static boolean checkIfDirExistsOnPath(String path) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("test", "-d", path);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    private static boolean checkSuccess(ProcessBuilder builder) {
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return exitCode == SUCCESS_EXIT_CODE;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Unhandled error occurred", e);
            return false;
        } catch (IOException e) {
            log.error("Unhandled error occurred", e);
            return false;
        }
    }
}
