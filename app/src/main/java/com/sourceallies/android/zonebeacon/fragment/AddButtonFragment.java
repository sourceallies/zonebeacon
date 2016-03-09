package com.sourceallies.android.zonebeacon.fragment;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.List;

/**
 * Fragment for creating a button and adding it to the current gateway that is selected.
 */
public class AddButtonFragment extends AbstractAddFragment<Command> {

    /**
     * Default constructor for the fragment.
     */
    public AddButtonFragment() {

    }

    @Override
    public String getPageTitle() {
        return getString(R.string.add_button_title);
    }

    @Override
    public String getListTitle() {
        return getString(R.string.include_commands_title);
    }

    @Override
    public String getNameHint() {
        return getString(R.string.button_name_hint);
    }

    @Override
    public void insertItems(DataSource dataSource, String name, List<Command> items) {
        dataSource.insertNewButton(name, items);
    }

    @Override
    public List<Command> findItems(DataSource dataSource, Gateway currentGateway) {
        return dataSource.findCommands(currentGateway);
    }

}
