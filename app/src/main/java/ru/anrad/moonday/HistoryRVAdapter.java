package ru.anrad.moonday;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.anrad.moonday.dao.Interval;
import ru.anrad.moonday.dao.MoonDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MoonDay} and makes a call to the
 * specified {@link HistoryRVAdapter.OnListInteractionListener}.
 *
 * */
public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.ViewHolder> {

    static final String DATE_FORMAT_STRING = "EEE d MMMM y";
    private static SimpleDateFormat DF = new SimpleDateFormat(DATE_FORMAT_STRING);

    //private final List<MoonDay> mValues;
    private final List<Interval> mValues;
    private final HistoryRVAdapter.OnListInteractionListener mListener;

    public HistoryRVAdapter(List<Interval> items, HistoryRVAdapter.OnListInteractionListener listener) {
        mValues = new ArrayList<>(items);
        Collections.sort(mValues, new ItemComparator());
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(Long.toString(mValues.get(position).getId()));

        //holder.mContentView.setText(mValues.get(position).toFormatString());
        //holder.mBeginView.setText(DF.format(mValues.get(position).getBegin()));
        //holder.mEndView.setText(DF.format(mValues.get(position).getEnd()));
        holder.mDaysView.setText(Long.toString(mValues.get(position).getDays()) + " days");
        holder.mBeginView.setText(DF.format(mValues.get(position).getBegin()) + " - " + DF.format(mValues.get(position).getEnd()));
        //holder.mEndView.setText(Long.toString(mValues.get(position).getDays()) + " days");

        holder.mView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        //public final TextView mContentView;
        public final TextView mBeginView;
        public final TextView mEndView;
        public final TextView mDaysView;
        public Interval mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mBeginView = (TextView) view.findViewById(R.id.activity_history_cardview_begin);
            mEndView = (TextView) view.findViewById(R.id.activity_history_cardview_end);
            mDaysView = (TextView) view.findViewById(R.id.activity_history_cardview_days);
        }

        @Override
        public String toString() {
            return super.toString();// + " '" + mContentView.getText() + "'";
        }
    }

    public class ItemComparator implements Comparator<Interval> {
        @Override
        public int compare(Interval d1, Interval d2) {
            if (d2.getBegin().getTime() > d1.getBegin().getTime()) return 1;
            if (d2.getBegin().getTime() < d1.getBegin().getTime()) return -1;
            return 0;
        }
    }

    public interface OnListInteractionListener {
        // TODO: Update argument type and name
        void onListInteraction(Interval item);
    }

}
