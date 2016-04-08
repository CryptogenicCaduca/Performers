package danilf.performers.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import danilf.performers.R;
import danilf.performers.model.Performer;
import uk.co.senab.bitmapcache.CacheableImageView;

/**
 * Created by DanilF on 02.04.2016.
 */
public class PerformerAdapter extends BaseAdapter{
    private List<Performer> performersList;
    private Context context;
    private boolean connectionIsDown = false;

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
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.performers_list_item, parent, false);

            holder = new ViewHolder();
            holder.cover = (CacheableImageView) view.findViewById(R.id.coverView);
            holder.text = (TextView) view.findViewById(R.id.permormerName);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.cover.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.placeholder));
        holder.text.setText(performersList.get(position).getName());

        return view;
    }

    public class ViewHolder {
        CacheableImageView cover;
        TextView text;
        int position;
        public URL url;
    }
}
