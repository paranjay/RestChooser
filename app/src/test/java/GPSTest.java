import android.test.ActivityInstrumentationTestCase2;

import com.google.android.gms.maps.GoogleMap;

import junit.framework.TestResult;

import org.junit.Test;

import edu.osu.restchooser.CreateFiltersFragment;
import edu.osu.restchooser.MapsActivity;

/**
 * Created by alcoa on 11/30/2015.
 */
public class GPSTest extends ActivityInstrumentationTestCase2 <CreateFiltersFragment> {

    private CreateFiltersFragment filtersFragmentActivity;

    public GPSTest(){
        super("edu.osu.restchooser.CreateFiltersFragment", CreateFiltersFragment.class);
    }

    @Test
    public void network_tester(){
        assertTrue(filtersFragmentActivity.checkInternet());
    }
}
