package com.sourceallies.android.zonebeacon.fragment;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.List;

/**
 * Fragment for creating a zone and adding it to the current gateway that is selected.
 */
public class AddZoneFragment extends AbstractAddFragment<Button> {

    /**
     * Default constructor for the fragment.
     */
    public AddZoneFragment() {

    }

    @Override
    public String getPageTitle() {
        return getString(R.string.add_zone_title);
    }

    @Override
    public String getListTitle() {
        return getString(R.string.include_buttons_title);
    }

    @Override
    public String getNameHint() {
        return getString(R.string.zone_name_hint);
    }

    @Override
    public void insertItems(DataSource dataSource, String name, List<Button> items) {
        dataSource.insertNewZone(name, items);
    }

    @Override
    public List<Button> findItems(DataSource dataSource, Gateway currentGateway) {
        return dataSource.findButtons(currentGateway);
    }

}
