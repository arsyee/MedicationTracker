package hu.fallen.eyedroptracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.RemoteViews;

/**
 * Created by tibi on 2017-08-28.
 */

public class MainWidget extends AppWidgetProvider {
    public static final String LAST_DROPTIME_UPDATE = "hu.fallen.eyedroptracker.LAST_DROPTIME_UPDATE";

    private Chronometer mChLastdrop;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; ++i) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            views.setOnClickPendingIntent(R.id.main_widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
        Log.d("MainWidget", "onUpdate called");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(LAST_DROPTIME_UPDATE)) {
            long base = intent.getLongExtra("last", 0);
            Log.d("MainWidget", "onReceive: "+base);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            if (base != 0) views.setChronometer(R.id.ch_lastdrop_widget, base, null, true);
            AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, MainWidget.class), views);
        }
        super.onReceive(context, intent);
    }
}
