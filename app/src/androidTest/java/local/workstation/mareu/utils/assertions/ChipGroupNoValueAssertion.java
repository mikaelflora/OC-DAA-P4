package local.workstation.mareu.utils.assertions;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import com.google.android.material.chip.ChipGroup;

import static org.junit.Assert.assertEquals;

public class ChipGroupNoValueAssertion implements ViewAssertion {
    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        int count = ((ChipGroup) view).getChildCount();

        assertEquals(0, count);
    }

    public static ChipGroupNoValueAssertion matchesChipGroupHasNoChip() {
        return new ChipGroupNoValueAssertion();
    }
}
