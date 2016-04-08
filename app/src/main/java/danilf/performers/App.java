package danilf.performers;

import java.io.File;

import uk.co.senab.bitmapcache.BitmapLruCache;
import android.app.Application;
import android.content.Context;

/**
 * Created by DanilF on 07.04.2016.
 */
public class App extends Application {
    private BitmapLruCache mCache;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            File cacheDir = new File(getCacheDir(), "smoothie");
            cacheDir.mkdirs();

            BitmapLruCache.Builder builder = new BitmapLruCache.Builder(this);
            builder.setMemoryCacheEnabled(true).setMemoryCacheMaxSizeUsingHeapSize();
            builder.setDiskCacheEnabled(true).setDiskCacheLocation(cacheDir);
            mCache = builder.build();

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public BitmapLruCache getBitmapCache() {
        return mCache;
    }

    public static App getInstance(Context context) {
        return (App) context.getApplicationContext();
    }
}
