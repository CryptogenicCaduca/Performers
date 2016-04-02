package danilf.performers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import danilf.performers.R;
import danilf.performers.model.PerformerModel;

/**
 * Created by DanilF on 02.04.2016.
 */
public class PerformerModelAdapter extends BaseAdapter {
    private List<PerformerModel> performersList;
    private LayoutInflater layoutInflater;

    public PerformerModelAdapter(Context context, List<PerformerModel> performersList) {
        this.performersList = performersList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = layoutInflater.inflate(R.layout.performers_list_item, parent, false);
        }
        TextView textView = (TextView)view.findViewById(R.id.textView);
        textView.setText(getPerformerModel(position).getName());

        return view;
    }

    private PerformerModel getPerformerModel(int position){
        return (PerformerModel) getItem(position);
    }
}
