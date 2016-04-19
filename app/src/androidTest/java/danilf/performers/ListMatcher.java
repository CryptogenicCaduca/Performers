package danilf.performers;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import uk.co.senab.bitmapcache.CacheableImageView;

/**
 * Created by DanilF on 18.04.2016.
 */
class ListMatcher {
    public static Matcher<View> withListSize (final int size) {
        return new TypeSafeMatcher<View>() {
            int length;
            @Override public boolean matchesSafely (final View view) {
                length = ((AdapterView) view).getAdapter().getCount();
                return length >= size;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have " + size + " items, the actual size is " + length);
            }
        };
    }
    public static TypeSafeMatcher<View> compareImages() {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("Compare with placeholder. If images are different then true.");
            }

            @Override
            protected boolean matchesSafely(View actualImageView) {
                Drawable expectedImage = actualImageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                return !((CacheableImageView)actualImageView).getDrawable().equals(expectedImage);
            }
        };
    }
}
