package danilf.performers.adapter;

import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import danilf.performers.R;
import danilf.performers.activity.PerformersListActivity;
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
    final Context context;
    final PerformerAdapter adapter;

    public ItemLoader(BitmapLruCache mCache, Context context,PerformerAdapter adapter ) {
        this.mCache = mCache;
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public CacheableBitmapDrawable loadItem(Performer performer) {
        String url = performer.getCover().get("small").toString();
        CacheableBitmapDrawable wrapper = mCache.get(performer.getCover().get("small").toString());
        if (wrapper == null) {
            try {
                wrapper = mCache.put(url, new DownloadCoverAsyncTask(this).execute(url).get(5, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }catch (TimeoutException e){
                Toast.makeText(context,"Check connection otherwise I cannot load data for you",Toast.LENGTH_SHORT);
            } catch (Exception e){
                Toast.makeText(context,"",Toast.LENGTH_SHORT);
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
        PerformerAdapter.ViewHolder holder = (PerformerAdapter.ViewHolder) itemView.getTag();

        if (result == null) {
            holder.cover.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.placeholder));
            return;
        }

        result.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        if (fromMemory) {
            holder.cover.setImageDrawable(result);
        } else {
            BitmapDrawable emptyDrawable = new BitmapDrawable(itemView.getResources());

            TransitionDrawable fadeInDrawable =
                    new TransitionDrawable(new Drawable[] { emptyDrawable, result });

            holder.cover.setImageDrawable(fadeInDrawable);
            fadeInDrawable.startTransition(200);
        }
    }

    @Override
    public Performer getItemParams(Adapter adapter, int position) {
        return (Performer)adapter.getItem(position);
    }

    @Override
    public void onTaskCompleted() {

    }
}
