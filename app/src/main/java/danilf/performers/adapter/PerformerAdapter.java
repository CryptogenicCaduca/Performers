package danilf.performers.adapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import danilf.performers.R;
import danilf.performers.model.Performer;

/**
 * Created by DanilF on 02.04.2016.
 */
public class PerformerAdapter extends BaseAdapter {
    private List<Performer> performersList;
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean connectionIsDown = false;

    public PerformerAdapter(Context context, List<Performer> performersList) {
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            view = layoutInflater.inflate(R.layout.performers_list_item, parent, false);
        }
        Performer currentPerformer = getPerformerModel(position);

        SetText(currentPerformer, view);
        SetCover(currentPerformer, view);
        return view;
    }

    private void SetText(Performer currentPerformer, View view) {
        TextView textView = (TextView)view.findViewById(R.id.permormerName);
        textView.setText(currentPerformer.getName());
    }

    private void SetCover(Performer currentPerformer, View view) {
        if (!connectionIsDown)
            try {
                DownloadCoverAsyncTask task = new DownloadCoverAsyncTask();
                URL url = currentPerformer.getCover().get("small");
                Bitmap cover = task.execute(url).get(2, TimeUnit.SECONDS);
                Bitmap sc = Bitmap.createScaledBitmap(cover, 60, 60, false);
                ImageView imageView = (ImageView) view.findViewById(R.id.coverView);
                imageView.setImageBitmap(sc);
            } catch (Exception e) {
                e.printStackTrace();
                connectionIsDown = true;
                Toast.makeText(context, "Sorry, I cannot load covers," +
                        " maybe because Internet is down.", Toast.LENGTH_SHORT).show();
            }
    }

    private Performer getPerformerModel(int position){
        return (Performer) getItem(position);
    }
}
