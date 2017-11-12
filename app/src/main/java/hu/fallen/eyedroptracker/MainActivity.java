package hu.fallen.eyedroptracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import hu.fallen.eyedroptracker.data.DroptimeContract;
import hu.fallen.eyedroptracker.data.DroptimeDbHelper;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    private DropListAdapter mAdapter;
    private Chronometer mChLastdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = new DroptimeDbHelper(this).getWritableDatabase();

        mAdapter = new DropListAdapter(this, getDropData());
        RecyclerView droplistRecyclerView;
        droplistRecyclerView = (RecyclerView) findViewById(R.id.rv_drop_history);
        droplistRecyclerView.setAdapter(mAdapter);
        droplistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeDrop(id);
                refreshChLastdropBase();
                mAdapter.setCursor(getDropData());
            }
        }).attachToRecyclerView(droplistRecyclerView);

        mChLastdrop = (Chronometer) findViewById(R.id.ch_lastdrop);
        refreshChLastdropBase();
    }

    private void refreshChLastdropBase() {
        long currentRelative = android.os.SystemClock.elapsedRealtime();
        long currentAbsolute = System.currentTimeMillis();
        long baseAbsolute = getLastdrop();
        long difference = currentAbsolute - baseAbsolute;
        long baseRelative = currentRelative - difference;
        if (baseAbsolute != 0) {
            mChLastdrop.setBase(baseRelative);
            mChLastdrop.start(); // TODO figure out when to stop
        }
        Intent intent = new Intent(MainWidget.LAST_DROPTIME_UPDATE);
        intent.putExtra("last", baseAbsolute == 0? 0 : baseRelative);
        getApplicationContext().sendBroadcast(intent);
        Log.d("MainActivity", "broadcasted: "+baseRelative);
    }

    private long getLastdrop() {
        Cursor cursor = getDropData();
        for (int i = 0; i < cursor.getColumnCount(); ++i) {
            if (!(cursor.moveToPosition(i))) continue;
            if (cursor.getString(cursor.getColumnIndex(DroptimeContract.DroptimeEntry.COLUMN_DROPTYPE)).equals("Flucon")) {
                long time = cursor.getInt(cursor.getColumnIndex(DroptimeContract.DroptimeEntry.COLUMN_DATETIME)) * 1000L;
                Log.d("MainActivity", "getLastDrop returns " + time + ", format: " + mChLastdrop.getFormat());
                return time;
            }
        }
        return 0L;
    }

    public void addDrop(View view) {
        Log.d("MainActivity", String.valueOf(view.getId()));
        if (view.getId() == R.id.bt_systane) {
            addDrop("Systane");
        }
        if (view.getId() == R.id.bt_flucon) {
            addDrop("Flucon");
        }
        mAdapter.setCursor(getDropData());
        refreshChLastdropBase();
    }

    private void addDrop(String type) {
        ContentValues values = new ContentValues();
        values.put(DroptimeContract.DroptimeEntry.COLUMN_DROPTYPE, type);
        values.put(DroptimeContract.DroptimeEntry.COLUMN_DATETIME, System.currentTimeMillis() / 1000L);
        mDb.insert(DroptimeContract.DroptimeEntry.TABLE_NAME, null, values);
    }

    private boolean removeDrop(long id) {
        return mDb.delete(DroptimeContract.DroptimeEntry.TABLE_NAME, DroptimeContract.DroptimeEntry._ID + " = " + id, null) > 0;
    }

    private Cursor getDropData() {
        return mDb.query(DroptimeContract.DroptimeEntry.TABLE_NAME, null, null, null, null, null, DroptimeContract.DroptimeEntry.COLUMN_DATETIME+" DESC");
    }
}
