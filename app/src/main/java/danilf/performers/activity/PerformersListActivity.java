package danilf.performers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
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
import danilf.performers.adapter.ErrorListener;
import danilf.performers.adapter.ItemLoader;
import danilf.performers.adapter.PerformerAdapter;
import danilf.performers.model.Performer;
import uk.co.senab.bitmapcache.BitmapLruCache;

public class PerformersListActivity extends AppCompatActivity implements LoadPerformersCallbackListener, ErrorListener {

    List<Performer> performersList = new ArrayList<Performer>();
    List<Performer> visiblePerformersList = new ArrayList<Performer>();
    private PerformerAdapter adapter;
    private AsyncListView performersListView;
    private RelativeLayout loadingPanel;
    Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performers_list);

        performersListView = (AsyncListView) findViewById(R.id.performersList);
        //start animation of loading
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
        //Downloading JSON with POP-performers from the URL that given there
        //https://academy.yandex.ru/events/mobdev/msk-2016/register/
        Ion.with(this)
                .load("http://cache-ektmts02.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json")
                .asJsonArray()
                .setCallback(jsonArrayFutureCallback);
    }

    FutureCallback<JsonArray> jsonArrayFutureCallback = new FutureCallback<JsonArray>(){
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            //check exception
            if(e == null) {
                new LoadPermormersAsyncTask(PerformersListActivity.this).execute(result);
            } else {
                //stop animation
                loadingPanel.setVisibility(View.GONE);
                Toast.makeText(PerformersListActivity.this, "The Internet is down, please, check connection", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void loadMoreListItems() {
        int currentSize = visiblePerformersList.size();
        for (int i = currentSize; i < currentSize + 2 && i < performersList.size(); i++) {
            //fill the item
            visiblePerformersList.add(performersList.get(i));
        }
        // this cause the list to refresh
        adapter.notifyDataSetChanged();
    }
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //what is the bottom item that is visible
            int lastInScreen = firstVisibleItem + visibleItemCount;
            //load more
            if ((lastInScreen == totalItemCount) &&
                    adapter.getCount()< performersList.size()) {
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
                Toast.makeText(PerformersListActivity.this,
                        "Загрузка " + performersList.get(position).getName(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PerformersListActivity.this,PerformerDetailsActivity.class);
                intent.putExtra("Performer", performersList.get(position));
                startActivity(intent);
            }
            catch (Exception ex){
                Toast.makeText(PerformersListActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        //save listview state
        state = performersListView.onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(state != null) {
            //restore listview state with its position
            performersListView.onRestoreInstanceState(state);
        }
    }

    @Override
    public void LoadPerformersCallback(ArrayList<Performer> result) {
        //set Adapter begin
        performersList = result;
        adapter = new PerformerAdapter(this, visiblePerformersList);
        performersListView.setAdapter(adapter);
        performersListView.setOnScrollListener(onScrollListener);
        performersListView.setOnItemClickListener(onItemClickListener);
        //set Adapter end
        //Set cache for images begin
        BitmapLruCache cache = App.getInstance(this).getBitmapCache();
        ItemLoader loader = new ItemLoader(cache,adapter, this);
        ItemManager.Builder builder = new ItemManager.Builder(loader);
        builder.setPreloadItemsEnabled(true).setPreloadItemsCount(5);
        builder.setThreadPoolSize(1);
        performersListView.setItemManager(builder.build());
        //Set cache for images end
        //stop animation
        loadingPanel.setVisibility(View.GONE);
    }

    @Override
    public void throwError(Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PerformersListActivity.this, "Соединение с интернетом отсутствует", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
