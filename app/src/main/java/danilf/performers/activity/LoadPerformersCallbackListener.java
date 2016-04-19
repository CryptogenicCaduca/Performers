package danilf.performers.activity;

import java.util.ArrayList;

import danilf.performers.model.Performer;

/**
 * Created by DanilF on 06.04.2016.
 */
public interface LoadPerformersCallbackListener {
    void LoadPerformersCallback(ArrayList<Performer> result);
}
