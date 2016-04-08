package danilf.performers.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import danilf.performers.R;

/**
 * Created by DanilF on 04.04.2016.
 */
class DownloadCoverAsyncTask extends AsyncTask<String,Void,Bitmap> {
    DownloadCoverListener listener;

    public DownloadCoverAsyncTask(DownloadCoverListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input), 60, 60, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        listener.onTaskCompleted();
    }
}
