package danilf.performers.activity;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Collections;

import danilf.performers.model.Performer;

/**
 * Created by DanilF on 06.04.2016.
 */
public class LoadPermormersAsyncTask extends AsyncTask<JsonArray,Integer,ArrayList<Performer>>  {
    LoadPerformersCallbackListener listener;

    public LoadPermormersAsyncTask(LoadPerformersCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Performer> doInBackground(JsonArray... params) {
        JsonArray jsonArray = params[0];
        ArrayList<Performer> performersList = new ArrayList<>();
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
        return performersList;
    }

    @Override
    protected void onPostExecute(ArrayList<Performer> performers) {
        super.onPostExecute(performers);
        listener.LoadPerformersCallback(performers);
    }
}
