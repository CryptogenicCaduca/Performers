package danilf.performers.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import danilf.performers.R;
import danilf.performers.adapter.PerformerAdapter;
import danilf.performers.model.Performer;

public class PerformersListActivity extends AppCompatActivity {

    List<Performer> performersList = new ArrayList<>();
    List<Performer> visiblePerformersList = new ArrayList<>();
    private PerformerAdapter adapter;
    private ListView performers;
    private boolean loadingMore = false;


    @Override
    protected void onStart() {
        super.onStart();
        DownloadPerformers();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performers_list);
        performers = (ListView) findViewById(R.id.performersList);
    }

    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            //Set flag so we cant load new items 2 at the same time
            loadingMore = true;
            //Get 15 new listitems
            int currentSize = visiblePerformersList.size();
            for (int i = currentSize; i < currentSize + 15 && i < performersList.size(); i++) {
                //Fill the item with some bogus information
                visiblePerformersList.add(performersList.get(i));
            }
            //Done! now continue on the UI thread
            runOnUiThread(returnRes);
        }
    };
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            //Tell to the adapter that changes have been made, this will cause the list to refresh
            //adapter.notifyDataSetChanged();
            setTitle("Neverending List with " + String.valueOf(adapter.getCount()) + " items");
            adapter.notifyDataSetInvalidated();
            //Done loading more.
            loadingMore = false;
        }
    };
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //nothing to do
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //what is the bottom item that is visible
            int lastInScreen = firstVisibleItem + visibleItemCount;
            //is the bottom item visible & not loading more already ? Load more !
            if ((lastInScreen == totalItemCount) && !(loadingMore)) {
                Thread thread = new Thread(null, loadMoreListItems);
                thread.start();
            }
        }
    };

    FutureCallback<JsonArray> jsonArrayFutureCallback = new FutureCallback<JsonArray>(){
        @Override
        public void onCompleted(Exception e, JsonArray result) {
            //stop showing animation of downloading
            //TODO:make animation
        }
    };
    //Downloading JSON with POP performers from the URL that given there
    //https://academy.yandex.ru/events/mobdev/msk-2016/register/
    private void DownloadPerformers() {
        try {
            JsonArray jsonArray = Ion.with(PerformersListActivity.this)
                    .load("http://cache-ektmts02.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json")
                    .asJsonArray()
                    .setCallback(jsonArrayFutureCallback).get();
            Gson gson = new Gson();
            // filter the collection to pass pop
            for (int i = 0; i < jsonArray.size(); i++) {
                Performer performer = gson.fromJson(jsonArray.get(i), Performer.class);
                if (performer.getGenres().contains("pop")) {
                    performersList.add(performer);
                } else {
                    //nothing to do
                }
            }
            // sort collection by name of performer
            Collections.sort(performersList);
            adapter = new PerformerAdapter(PerformersListActivity.this, visiblePerformersList);
            performers.setAdapter(adapter);
            performers.setOnScrollListener(onScrollListener);
            performers.setOnItemClickListener(onItemClickListener);
        }
        catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
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
}
