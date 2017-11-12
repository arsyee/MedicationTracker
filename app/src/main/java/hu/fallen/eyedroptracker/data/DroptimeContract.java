package hu.fallen.eyedroptracker.data;

import android.provider.BaseColumns;

/**
 * Created by tibi on 2017-08-26.
 */

public class DroptimeContract {
    public static final class DroptimeEntry implements BaseColumns {
        public static final String TABLE_NAME = "droptime";
        public static final String COLUMN_DROPTYPE = "droptype";
        public static final String COLUMN_DATETIME = "datetime";
    }
}
