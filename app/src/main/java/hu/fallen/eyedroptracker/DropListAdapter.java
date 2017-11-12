package hu.fallen.eyedroptracker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import hu.fallen.eyedroptracker.data.DroptimeContract;
import hu.fallen.eyedroptracker.helper.TimeHelper;

/**
 * Created by tibi on 2017-08-26.
 */

public class DropListAdapter extends RecyclerView.Adapter<DropListAdapter.DropViewHolder> {

    private Context mContext;
    private Cursor mDropData;

    public DropListAdapter(Context context, Cursor dropData) {
        mContext = context;
        mDropData = dropData;
    }

    @Override
    public DropListAdapter.DropViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.drop_list_item, parent, false);
        return new DropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DropListAdapter.DropViewHolder holder, int position) {
        if (!(mDropData.moveToPosition(position))) return;
        holder.mDropType.setText(mDropData.getString(mDropData.getColumnIndex(DroptimeContract.DroptimeEntry.COLUMN_DROPTYPE))); // TODO replace with icons
        long time = mDropData.getInt(mDropData.getColumnIndex(DroptimeContract.DroptimeEntry.COLUMN_DATETIME)) * 1000L;
        long today = System.currentTimeMillis() / (1000L*60L*60L*24L) * (1000L*60L*60L*24L);
        Log.d("DropListAdapted", Long.toString(time));
        holder.mDropTime.setText(TimeHelper.getFormattedTime(time));
        holder.itemView.setTag(mDropData.getLong(mDropData.getColumnIndex(DroptimeContract.DroptimeEntry._ID)));
        if (mDropData.getString(mDropData.getColumnIndex(DroptimeContract.DroptimeEntry.COLUMN_DROPTYPE)).equals("Flucon") && time > today) {
            holder.mLRow.setBackgroundColor(Color.rgb(255, 204, 153));
        } else {
            holder.mLRow.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mDropData.getCount();
    }

    public void setCursor(Cursor dropData) {
        if (mDropData != null) mDropData.close();
        mDropData = dropData;
        if (mDropData != null) {
            this.notifyDataSetChanged();
        }
    }

    public class DropViewHolder extends RecyclerView.ViewHolder {
        TextView mDropType;
        TextView mDropTime;
        LinearLayout mLRow;

        public DropViewHolder(View itemView) {
            super(itemView);
            mDropType = (TextView) itemView.findViewById(R.id.tv_droptype);
            mDropTime = (TextView) itemView.findViewById(R.id.tv_droptime);
            mLRow = (LinearLayout) itemView.findViewById(R.id.ll_row);
        }
    }
}
