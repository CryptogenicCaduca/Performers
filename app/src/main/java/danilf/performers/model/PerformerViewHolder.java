package danilf.performers.model;

import android.widget.TextView;

import uk.co.senab.bitmapcache.CacheableImageView;

/**
 * Created by DanilF on 10.04.2016.
 */
public class PerformerViewHolder {
    public PerformerViewHolder(CacheableImageView cover, TextView textView) {
        this.cover = cover;
        this.textView = textView;
    }

    CacheableImageView cover;
    TextView textView;

    public CacheableImageView getCover() {
        return cover;
    }

    public void setCover(CacheableImageView cover) {
        this.cover = cover;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView text) {
        this.textView = text;
    }
}
