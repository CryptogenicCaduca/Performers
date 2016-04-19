package danilf.performers;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;



import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import danilf.performers.activity.PerformersListActivity;
import danilf.performers.model.Performer;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by DanilF on 14.04.2016.
 */
@RunWith(AndroidJUnit4.class)
public class ListActivityTest {
    @Rule public final ActivityTestRule<PerformersListActivity> main = new ActivityTestRule<PerformersListActivity>(PerformersListActivity.class);
    @Test public void thisPerformerShouldBeShownFirst() {
        //List should be sorted by name of performer
        onData(instanceOf(Performer.class)).
                inAdapterView(withId(R.id.performersList)).
                atPosition(0).onChildView(withText("5 Seconds Of Summer")).
                check(matches(isDisplayed()));
    }
    @Test public void thisTestCaseShouldFailed() {
        //Show that previous test is correct
        onData(instanceOf(Performer.class)).
                inAdapterView(withId(R.id.performersList)).
                atPosition(0).onChildView(withText("6 Minutes Of Winter")).
                check(matches(isDisplayed()));
    }
    @Test public void checkCover() {
        //cover should be shown
        onData(instanceOf(Performer.class)).
                inAdapterView(withId(R.id.performersList)).
                atPosition(0).
                onChildView(withId(R.id.itemCoverView)).
                check(matches(ListMatcher.compareImages()));
    }
    //Should be 136 items
    final int listsize = 136;
    //Should be less than 6, because scrolling adds items to list
    final int step = 5;
    @Test public void checkEveryIntentToDetails() throws InterruptedException {
        //initializing
        DataInteraction pList = onData(instanceOf(Performer.class)).
                inAdapterView(withId(R.id.performersList));
        ViewInteraction view = onView(withId(R.id.performersList));
        int i = 0;

        //Open every 5 performer details activity until end of the list
        while (i < listsize) {
            //test can be failed this line
            view.check(matches(ListMatcher.withListSize(i)));
            //load details activity
            pList.atPosition(i).perform(click());
            //go back to performers list activity
            Espresso.pressBack();
            i+=step;
            //scroll down
            onView(withId(R.id.performersList)).perform(swipeUp());
        }
    }

}
