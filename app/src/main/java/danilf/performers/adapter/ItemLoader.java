package danilf.performers.adapter;

import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.Adapter;

import danilf.performers.DownloadCoverAsyncTask;
import danilf.performers.DownloadCoverListener;
import danilf.performers.R;
import danilf.performers.model.PerformerViewHolder;
import danilf.performers.model.Performer;
import uk.co.senab.bitmapcache.BitmapLruCache;
import uk.co.senab.bitmapcache.CacheableBitmapDrawable;

import org.lucasr.smoothie.SimpleItemLoader;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by DanilF on 07.04.2016.
 */
public class ItemLoader extends SimpleItemLoader<Performer, CacheableBitmapDrawable> implements DownloadCoverListener {
    final BitmapLruCache mCache;
    final PerformerAdapter adapter;
    ErrorListener errorListener;
    private boolean exceptionisShown = false;

    public ItemLoader(BitmapLruCache mCache ,PerformerAdapter adapter,ErrorListener errorListener ) {
        this.mCache = mCache;
        this.adapter = adapter;
        this.errorListener = errorListener;
    }

    @Override
    public CacheableBitmapDrawable loadItem(Performer performer) {
        String url = performer.getCover().get("small").toString();
        CacheableBitmapDrawable wrapper = mCache.get(performer.getCover().get("small").toString());
        if (wrapper == null) {
            try {
                Bitmap bitmap = new DownloadCoverAsyncTask(this).execute(url).get(5, TimeUnit.SECONDS);
                if(bitmap != null)
                wrapper = mCache.put(url, bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                if (!exceptionisShown) {
                    errorListener.throwError(e);
                    exceptionisShown = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return wrapper;
    }

    @Override
    public CacheableBitmapDrawable loadItemFromMemory(Performer performer) {
        return mCache.getFromMemoryCache(performer.getCover().get("small").toString());
    }

    @Override
    public void displayItem(View itemView, CacheableBitmapDrawable result, boolean fromMemory) {
        PerformerViewHolder holder = (PerformerViewHolder) itemView.getTag();

        if (result == null) {
            holder.getCover().setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.placeholder));
            return;
        }

        result.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        if (fromMemory) {
            holder.getCover().setImageDrawable(result);
        } else {
            BitmapDrawable emptyDrawable = new BitmapDrawable(itemView.getResources());

            TransitionDrawable fadeInDrawable =
                    new TransitionDrawable(new Drawable[] { emptyDrawable, result });

            holder.getCover().setImageDrawable(fadeInDrawable);
            fadeInDrawable.startTransition(200);
        }
    }

    @Override
    public Performer getItemParams(Adapter adapter, int position) {
        return (Performer)adapter.getItem(position);
    }

    @Override
    public void onTaskCompleted(Bitmap result) {

    }
}
