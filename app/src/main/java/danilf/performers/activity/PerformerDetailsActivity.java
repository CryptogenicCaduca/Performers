package danilf.performers.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import danilf.performers.DownloadCoverAsyncTask;
import danilf.performers.DownloadCoverListener;
import danilf.performers.R;
import danilf.performers.model.Performer;
import uk.co.senab.bitmapcache.CacheableImageView;

/**
 * Created by DanilF on 10.04.2016.
 */
public class PerformerDetailsActivity extends Activity implements DownloadCoverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
        setContentView(R.layout.activity_performer_details);
        Intent intent = getIntent();
        Performer performer = intent.getParcelableExtra("Performer");
        ShowData(performer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
    }

    private void ShowData(Performer performer) {
        //Start loading big cover. show placeholder till done
        new DownloadCoverAsyncTask(this).execute(performer.getCover().get("big").toString());
        ((CacheableImageView)this.findViewById(R.id.coverView)).setImageDrawable(this.getResources().getDrawable(R.drawable.placeholder));
        //show rest of the data begin
        ((TextView)this.findViewById(R.id.textName)).setText(performer.getName());
        ((TextView) this.findViewById(R.id.textDescription)).setText(performer.getDescription());
        String genresString = makeStringFromListArray(performer.getGenres());
        ((TextView)this.findViewById(R.id.textGenres)).setText(genresString);
        ((TextView)this.findViewById(R.id.textNAlbums)).setText(String.valueOf(performer.getAlbums()));
        ((TextView)this.findViewById(R.id.textNTracks)).setText(String.valueOf(performer.getTracks()));
        //there may not be link, so check for null
        if(performer.getLink()!=null) {
            ((TextView) this.findViewById(R.id.textWebSite)).setText(String.valueOf(performer.getLink()));
        }
        else{
            ((TextView) this.findViewById(R.id.textWebSite)).setText("Нет");
        }
        //show rest of the data end
    }

    private String makeStringFromListArray(ArrayList<String> genres) {
        String genresString ="";
        for (int i = 0; i<genres.size();i++) {
            genresString += genres.get(i);
            if (i != genres.size() - 1)
                genresString += ", ";
        }
        return genresString;
    }

    @Override
    public void onTaskCompleted(Bitmap result) {
        if(result != null)
        ((CacheableImageView)this.findViewById(R.id.coverView)).setImageBitmap(result);
    }

}
