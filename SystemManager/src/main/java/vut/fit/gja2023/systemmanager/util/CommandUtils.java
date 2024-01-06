package vut.fit.gja2023.systemmanager.util;


import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;
import vut.fit.gja2023.systemmanager.service.dto.RemoveUserFromGroupDto;

import java.io.IOException;

import static vut.fit.gja2023.systemmanager.Constants.GETENT_NOTFOUND_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.GPASSWD_NOTFOUND_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.GROUPADD_CONFLICT_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.GROUPDEL_NOTFOUND_EXIT_CODE;
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
            return new ImmutablePair<>(ExitCodeEnum.ERROR, null);
        } catch (IOException e) {
            return new ImmutablePair<>(ExitCodeEnum.ERROR, null);
        }
    }



    /**
     * Create user on system
     *
     * @param name name of user to be created
     * @return {@link ExitCodeEnum} representing the user creation process result
     */
    public static ExitCodeEnum createNewUser(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("useradd", name);
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
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
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
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            return ExitCodeEnum.ERROR;
        }
    }

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
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            return ExitCodeEnum.ERROR;
        }
    }

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
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            return ExitCodeEnum.ERROR;
        }
    }

    public static ExitCodeEnum addUserToGroup(@NonNull RemoveUserFromGroupDto dto) {
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
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            return ExitCodeEnum.ERROR;
        }
    }

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
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            return ExitCodeEnum.ERROR;
        }
    }

    public static boolean existsUser(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("id", name);
        log.info("Executing command: " + String.join(" ", builder.command()));
        return checkSuccess(builder);
    }

    public static boolean existsGroup(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("getent", "group", name);
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
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
