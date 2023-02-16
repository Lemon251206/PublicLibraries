package nade.empty.developers;

import java.util.Objects;

public class DeveloperMode {
    
    private static boolean enable;

    public static void setEnable(boolean enable) {
        DeveloperMode.enable = enable;
    }

    public static boolean isEnable() {
        return enable;
    }

    public static void log(String message) {
        DeveloperMode.log("DEVELOPER", message);
    }

    public static void log(String prefix, String message) {
        if (Objects.isNull(prefix)) prefix = "DEVELOPER";
        if (enable) {
            System.out.println("[" + prefix + "] " + message);
        }
    }

    public static void notEmpty(String str, String message) {
        DeveloperMode.notEmpty(str, null, message);
    }

    public static void notEmpty(String str, String prefix, String message) {
        if (str == null || str.isEmpty()) DeveloperMode.log(prefix, message);
    }
}
