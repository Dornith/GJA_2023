package vut.fit.gja2023.systemmanager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Constants {

    public static final String API_PREFIX = "/api";
    public static final int USERDEL_NOTFOUND_EXIT_CODE = 6;
    public static final int GROUPDEL_NOTFOUND_EXIT_CODE = 6;
    public static final int GPASSWD_NOTFOUND_EXIT_CODE = 3;
    public static final int GETENT_NOTFOUND_EXIT_CODE = 2;
    public static final int USERADD_CONFLICT_EXIT_CODE = 9;
    public static final int GROUPADD_CONFLICT_EXIT_CODE = 9;
    public static final int SUCCESS_EXIT_CODE = 0;
}
