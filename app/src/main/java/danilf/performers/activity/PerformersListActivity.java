package danilf.performers.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.lucasr.smoothie.AsyncListView;
import org.lucasr.smoothie.ItemManager;

import java.util.ArrayList;
import java.util.List;

import danilf.performers.App;
import danilf.performers.R;
import danilf.performers.adapter.DownloadCoverListener;
import danilf.performers.adapter.ItemLoader;
import danilf.performers.adapter.PerformerAdapter;
import danilf.performers.model.Performer;
import uk.co.senab.bitmapcache.BitmapLruCache;

public class PerformersListActivity extends AppCompatActivity implements LoadPerformersCallbackListener{

    List<Performer> performersList = new ArrayList<>();
    List<Performer> visiblePerformersList = new ArrayList<>();
    private PerformerAdapter adapter;
    private AsyncListView performersListView;
    private RelativeLayout loadingPanel;
    private boolean loadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performers_list);
        performersListView = (AsyncListView) findViewById(R.id.performersList);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        Toast.makeText(this, "The Internet is down, please, check connection", Toast.LENGTH_SHORT);
        //Downloading JSON with POP-performers from the URL that given there
        //https://academy.yandex.ru/events/mobdev/msk-2016/register/
        Ion.with(PerformersListActivity.this)
                .load("http://cache-ektmts02.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json")
                .asJsonArray()
                .setCallback(jsonArrayFutureCallback);
    }
    FutureCallback<JsonArray> jsonArrayFutureCallback = new FutureCallback<JsonArray>(){
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            if(e == null) {
                LoadPermormersAsyncTask loader = new LoadPermormersAsyncTask(PerformersListActivity.this);
                loader.execute(result);
            } else {
                loadingPanel.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "The Internet is down, please, check connection", Toast.LENGTH_SHORT);
            }
        }
    };
    private void loadMoreListItems() {
        int currentSize = visiblePerformersList.size();
        for (int i = currentSize; i < currentSize + 5 && i < performersList.size(); i++) {
            //Fill the item with some bogus information
            visiblePerformersList.add(performersList.get(i));
        }
        //Done! now continue on the UI thread
        //Tell to the adapter that changes have been made, this will cause the list to refresh
        adapter.notifyDataSetChanged();
        loadingPanel.setVisibility(View.GONE);
    }
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //what is the bottom item that is visible
            int lastInScreen = firstVisibleItem + visibleItemCount;
            //is the bottom item visible & not loading more already ? Load more !
            if ((lastInScreen == totalItemCount) &&
                    !(loadingMore) &&
                    adapter.getCount()< performersList.size()) {
                if(loadingPanel.getVisibility() != View.VISIBLE)
                loadingPanel.setVisibility(View.VISIBLE);
                loadMoreListItems();
            }
        }
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //nothing to do
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                //TODO: Make intent to another activity
                Toast.makeText(PerformersListActivity.this, "Clicked " + ((TextView) view.findViewById(R.id.permormerName)).getText().toString(), Toast.LENGTH_SHORT).show();
            }
            catch (Exception ex){
                Toast.makeText(PerformersListActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void LoadPerformersCallback(ArrayList<Performer> result) {
        //set Adapter
        performersList = result;
        adapter = new PerformerAdapter(this, visiblePerformersList);
        performersListView.setAdapter(adapter);
        performersListView.setOnScrollListener(onScrollListener);
        performersListView.setOnItemClickListener(onItemClickListener);
        //Set cache for images
        BitmapLruCache cache = App.getInstance(this).getBitmapCache();
        ItemLoader loader = new ItemLoader(cache,this,adapter);

        ItemManager.Builder builder = new ItemManager.Builder(loader);
        builder.setPreloadItemsEnabled(true).setPreloadItemsCount(5);
        builder.setThreadPoolSize(1);
        performersListView.setItemManager(builder.build());
    }
}
