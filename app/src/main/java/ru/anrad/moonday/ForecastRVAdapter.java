package ru.anrad.moonday;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import ru.anrad.moonday.dao.MoonDay;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MoonDay} and makes a call to the
 * specified {@link ForecastRVAdapter.OnListInteractionListener}.
 *
 * */
public class ForecastRVAdapter extends RecyclerView.Adapter<ForecastRVAdapter.ViewHolder> {

    static final String DATE_FORMAT_STRING = "EEEE d MMMM y";
    private static SimpleDateFormat DF = new SimpleDateFormat(DATE_FORMAT_STRING);

    private final List<MoonDay> mValues;
    private final ForecastRVAdapter.OnListInteractionListener mListener;

    public ForecastRVAdapter(List<MoonDay> items, ForecastRVAdapter.OnListInteractionListener listener) {
        mValues = items;
        //mValues = new ArrayList<>(items);
        //Collections.sort(mValues, new ItemComparator());
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_forecast_list_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_forecast_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(Long.toString(mValues.get(position).getId()));

        //holder.mContentView.setText(mValues.get(position).toFormatString());
        holder.mBeginView.setText(DF.format(mValues.get(position).getBegin()));
        holder.mEndView.setText(DF.format(mValues.get(position).getEnd()));
        //holder.mDaysView.setText(Long.toString(mValues.get(position).getDays()) + " days");

        holder.mView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (null != mListener) {
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
        public final TextView mBeginView;
        public final TextView mEndView;
        //public final TextView mDaysView;
        public MoonDay mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mBeginView = (TextView) view.findViewById(R.id.activity_forecast_cardview_begin);
            mEndView = (TextView) view.findViewById(R.id.activity_forecast_cardview_end);
            //mDaysView = (TextView) view.findViewById(R.id.content_days);
        }

        @Override
        public String toString() {
            return super.toString();// + " '" + mContentView.getText() + "'";
        }
    }

    public class ItemComparator implements Comparator<MoonDay> {
        @Override
        public int compare(MoonDay d1, MoonDay d2) {
            if (d2.getBegin().getTime() > d1.getBegin().getTime()) return 1;
            if (d2.getBegin().getTime() < d1.getBegin().getTime()) return -1;
            return 0;
        }
    }

    public interface OnListInteractionListener {
        // TODO: Update argument type and name
        void onListInteraction(MoonDay item);
    }

}
