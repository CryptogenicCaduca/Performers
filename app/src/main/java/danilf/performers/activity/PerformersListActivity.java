package danilf.performers.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import danilf.performers.R;
import danilf.performers.adapter.PerformerModelAdapter;
import danilf.performers.model.PerformerModel;

public class PerformersListActivity extends AppCompatActivity {
    List<PerformerModel> listItems = new ArrayList<PerformerModel>();
    private PerformerModelAdapter adapter;
    private ListView performers;


    @Override
    protected void onStart() {
        super.onStart();
        try {
            //Downloading JSON with performers from the URL that given there
            //https://academy.yandex.ru/events/mobdev/msk-2016/register/
            Ion.with(PerformersListActivity.this)
                    .load("http://cache-ektmts02.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json")
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            Gson gson = new Gson();
                            for (int i = 0; i < result.size(); i++) {
                                listItems.add(gson.fromJson(result.get(i), PerformerModel.class));
                            }
                            adapter = new PerformerModelAdapter(PerformersListActivity.this, listItems);
                            performers.setAdapter(adapter);
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performers_list);
        performers = (ListView) findViewById(R.id.performersList);


        performers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Toast.makeText(PerformersListActivity.this, "Clicked " + ((TextView) view.findViewById(R.id.textView)).getText().toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex){
                    Toast.makeText(PerformersListActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
