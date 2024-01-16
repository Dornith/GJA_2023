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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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


    public static final String ETC_HOSTS_ALLOW = "/etc/hosts.allow";

    /**
     * Get information about a group.
     *
     * @param groupName The name of the group to be found.
     * @return A Pair where the left value is {@link ExitCodeEnum} representing the group information finding process result
     * and the right value is the command output if the command was successful or null if it was not.
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
     * Create user in OS.
     *
     * @param name The name of the user to be created.
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
     * Deletes a user from the system.
     *
     * @param name The name of the user to be deleted.
     * @return {@link ExitCodeEnum} representing the deletion process result.
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
     * Creates a group on the system.
     *
     * @param name The name of the group to be created.
     * @return {@link ExitCodeEnum} representing the group creation process result.
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
     * Deletes a group from the system.
     *
     * @param name The name of the group to be deleted.
     * @return {@link ExitCodeEnum} representing the deletion process result.
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
     * Adds a specified user to a given group.
     *
     * @param dto {@link AddUserToGroupDto} containing the user and the group names.
     * @return {@link ExitCodeEnum} representing the addition process result.
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
     * Removes a specified user from a given group.
     *
     * @param dto {@link RemoveUserFromGroupDto} containing the user and the group names.
     * @return {@link ExitCodeEnum} representing the removal process result.
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
     * Checks whether a specified user exists or not.
     *
     * @param name The name of the user to be checked.
     * @return true if user exists, false otherwise.
     */
    public static boolean existsUser(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("id", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    /**
     * Checks whether a specified group exists or not.
     *
     * @param name The name of the group to be checked.
     * @return true if group exists, false otherwise.
     */
    public static boolean existsGroup(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("getent", "group", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    /**
     * Creates a new directory with a given path.
     *
     * @param path The path to the directory to be created.
     * @param mode A mode representing rights of access of owner/group/others. -optional
     * @param ownerGroupName The name of the group to be set as the owner. -optional
     * @return {@link ExitCodeEnum} representing the directory creation process result.
     */
    public static ExitCodeEnum createNewDirectory(@NonNull String path, DirectoryModeEnum mode, String ownerGroupName) {
        if (checkIfFileExistsOnPath(path)) {
            return ExitCodeEnum.CONFLICT;
        }
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("mkdir");
        builder.command().add("-p");
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
     * Deletes a specified directory.
     *
     * @param path The path to the directory to be deleted.
     * @return {@link ExitCodeEnum} representing the removal process result.
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
     * Changes which group a specified directory belongs to.
     *
     * @param path The path to the directory to be changed.
     * @param groupName The name of the group to be set as the new owner.
     * @return {@link ExitCodeEnum} representing the removal process result.
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

    /**
     * Creates a new hosts file rule.
     * 
     * @param IPAddress An IP address to be included in the hosts file.
     * @return The result of adding a new rule.
     */
    public static ExitCodeEnum createHostsFileRule(@NonNull String IPAddress) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ETC_HOSTS_ALLOW, true))) {
            writer.println("sshd: " + IPAddress + "# " + "Created by system manager");
            return ExitCodeEnum.SUCCESS;
        } catch (IOException e) {
            log.error("Error adding rule to " + ETC_HOSTS_ALLOW, e);
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Removes a specified hosts file rule.
     * 
     * @param IPAddress An IP address to be removed from the hosts file.
     * @return The result of removing a hosts file rule.
     */
    public static ExitCodeEnum removeHostsFileRule(@NonNull String IPAddress) {
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(ETC_HOSTS_ALLOW));
             PrintWriter writer = new PrintWriter(new FileWriter(ETC_HOSTS_ALLOW + ".tmp"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("sshd: " + IPAddress + "# " + "Created by system manager")) {
                    found = true;
                    continue;
                }
                writer.println(line);
            }
            writer.flush();

        } catch (IOException e) {
            return ExitCodeEnum.ERROR;
        }
        if (!found) {
            return ExitCodeEnum.NOT_FOUND;
        }

        // Replace the original file with the hosts.allow file with the specified rule removed
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mv", ETC_HOSTS_ALLOW + ".tmp", ETC_HOSTS_ALLOW);
            Process process = processBuilder.start();
            process.waitFor();

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                return ExitCodeEnum.SUCCESS;
            } else {
                return ExitCodeEnum.ERROR;
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExitCodeEnum.ERROR;
        }
    }

    /**
     * Checks whether a file exists on a given path.
     * 
     * @param path The path to be checked.
     * @return true if a file exists on the specified path, false otherwise.
     */
    private static boolean checkIfFileExistsOnPath(String path) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("test", "-e", path);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    /**
     * Checks whether a directory exists on a given path.
     * 
     * @param path The path to be checked.
     * @return true if a directory exists on the specified path, false otherwise.
     */
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
