package be.gsnauw.respawnmine.utils;

public class TimeConvert {

    public static int convertTime(String blockValue) {

        int newValue = 1;

        if (blockValue.contains("s")) {
            blockValue = blockValue.replace("s", "");
            newValue = Integer.parseInt(blockValue);
        }
        if (blockValue.contains("m")) {
            blockValue = blockValue.replace("m", "");
            newValue = Integer.parseInt(blockValue) * 60;
        }
        if (blockValue.contains("h")) {
            blockValue = blockValue.replace("h", "");
            newValue = Integer.parseInt(blockValue) * 3600;
        }

        return newValue;
    }

    public static String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (secs > 0 || (secs == 0 && minutes == 0 && hours == 0)) {
            sb.append(secs).append("s");
        }

        return sb.toString().trim();
    }
}
