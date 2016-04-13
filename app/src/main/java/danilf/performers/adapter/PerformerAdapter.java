package danilf.performers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import danilf.performers.R;
import danilf.performers.model.PerformerViewHolder;
import danilf.performers.model.Performer;
import uk.co.senab.bitmapcache.CacheableImageView;

/**
 * Created by DanilF on 02.04.2016.
 */
public class PerformerAdapter extends BaseAdapter{
    private List<Performer> performersList;
    private Context context;

    public PerformerAdapter(Context context, List<Performer> performersList) {
        this.context = context;
        this.performersList = performersList;
    }

    @Override
    public int getCount() {
        return performersList.size();
    }

    @Override
    public Object getItem(int position) {
        return performersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View view, ViewGroup parent) {
        PerformerViewHolder holder = null;
        //set up viewholder pattern
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.performers_list_item, parent, false);
            holder = new PerformerViewHolder((CacheableImageView) view.findViewById(R.id.coverView),
                    (TextView) view.findViewById(R.id.performerName));
            view.setTag(holder);
        } else {
            holder = (PerformerViewHolder) view.getTag();
        }
        holder.getCover().setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.placeholder));
        holder.getTextView().setText(performersList.get(position).getName());

        return view;
    }

}
