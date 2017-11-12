package hu.fallen.eyedroptracker.helper;

import java.sql.Timestamp;

/**
 * Created by tibi on 2017-08-27.
 */

public class TimeHelper {

    public static String getFormattedTime(long time) {
        // TODO write API level dependent code, use SimpleDateFormat when possible
        Timestamp timestamp = new Timestamp(time);
        String[] datetimeTokens = timestamp.toString().split(" ");
        String dateTokens[] = datetimeTokens[0].split("-");
        String timeTokens[] = datetimeTokens[1].split(":");
        return dateTokens[0]+"-"+dateTokens[1]+"-"+dateTokens[2]+" "+timeTokens[0]+":"+timeTokens[1];
    }
}
