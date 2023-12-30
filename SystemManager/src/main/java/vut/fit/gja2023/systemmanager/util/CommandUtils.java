package vut.fit.gja2023.systemmanager.util;


import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import vut.fit.gja2023.systemmanager.enums.ExitCodeEnum;

import java.io.IOException;

import static vut.fit.gja2023.systemmanager.Constants.SUCCESS_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.USERADD_CONFLICT_EXIT_CODE;
import static vut.fit.gja2023.systemmanager.Constants.USERDEL_NOTFOUND_EXIT_CODE;

@Slf4j
@UtilityClass
public class CommandUtils {

    /**
     * Create user on system
     *
     * @param name name of user to be created
     * @return {@link ExitCodeEnum} representing the user creation process result
     */
    public static ExitCodeEnum createNewUser(@NonNull String name) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("useradd", name);
        log.info(String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case USERADD_CONFLICT_EXIT_CODE -> ExitCodeEnum.CONFLICT;
                default -> {
                    log.error("Unhandled error occured while creating user, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Error occured while creating user", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Error occured while creating user", e);
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
        log.info(String.join(" ", builder.command()));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            return switch (exitCode) {
                case SUCCESS_EXIT_CODE -> ExitCodeEnum.SUCCESS;
                case USERDEL_NOTFOUND_EXIT_CODE -> ExitCodeEnum.NOT_FOUND;
                default -> {
                    log.error("Unhandled error occured while deleting user, exit code: " + exitCode);
                    yield ExitCodeEnum.ERROR;
                }
            };
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Error occured while deleting user", e);
            return ExitCodeEnum.ERROR;
        } catch (IOException e) {
            log.error("Error occured while deleting user", e);
            return ExitCodeEnum.ERROR;
        }
    }
}
