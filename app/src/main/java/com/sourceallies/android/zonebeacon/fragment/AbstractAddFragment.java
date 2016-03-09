package com.sourceallies.android.zonebeacon.fragment;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Fragment for easily implementing adding a zone or button to the database.
 * @param <T> the type of object that is to be added and invoked when this object is clicked on.
 *           For example, if we are adding a zone then T should be a button.
 */
public abstract class AbstractAddFragment<T> extends AbstractSetupFragment {

    @Getter
    private TextInputLayout name;
    @Getter
    private ListView list;
    @Getter
    private List<T> allItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        // setup the UI elements
        name = (TextInputLayout) root.findViewById(R.id.name);
        list = (ListView) root.findViewById(R.id.list);
        populateCommandList();

        TextView listTitle = (TextView) root.findViewById(R.id.list_title);
        TextView pageTitle = (TextView) root.findViewById(R.id.page_title);

        listTitle.setText(getListTitle());
        pageTitle.setText(getPageTitle());
        name.setHint(getNameHint());

        return root;
    }

    /**
     * Gets the title that should be applied to the page.
     *
     * @return the page title.
     */
    public abstract String getPageTitle();

    /**
     * Gets the title that should be applied to the top of the list.
     *
     * @return the list title.
     */
    public abstract String getListTitle();

    /**
     * Gets the hint that should be displayed for the name field.
     *
     * @return the name field hint.
     */
    public abstract String getNameHint();

    /**
     * Check whether or not the text input layout is empty or not. If it is, set an error and
     * set up a TextWatcher to clear that error.
     *
     * @param input TextInputLayout that is either empty or filled
     * @return true if the layout is empty, false otherwise
     */
    public boolean isEmpty(final TextInputLayout input) {
        if (TextUtils.isEmpty(input.getEditText().getText())) {
            // display an error message on the edit text
            input.setError(getString(R.string.fill_field));

            // used to clear the error message on the edit text
            input.getEditText().addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                @Override public void afterTextChanged(Editable editable) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    input.setError("");
                }
            });
            return true;
        } else {
            // clear the error
            input.setError("");
        }

        return false;
    }

    @Override
    public boolean isComplete() {
        return !isEmpty(name);
    }


    @Override
    public void save() {
        String name = getText(this.name);
        List<T> list = getCheckedItems();

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        insertItems(source, name, list);
        source.close();
    }

    public abstract void insertItems(DataSource dataSource, String name, List<T> items);

    private List<T> getCheckedItems() {
        SparseBooleanArray checked = list.getCheckedItemPositions();
        List<T> checkedItems = new ArrayList<T>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            if (checked.valueAt(i)) {
                checkedItems.add(allItems.get(position));
            }
        }
        return checkedItems;
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }

    private void populateCommandList() {
        Gateway currentGateway = getCurrentGateway();

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        allItems = findItems(source, currentGateway);
        source.close();

        ListView commandList = getList();
        //create list of buttons
        String[] commandNames = new String[allItems.size()];

        for (int i = 0; i < allItems.size(); i++) {
            commandNames[i] = allItems.get(i).toString();
        }

        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, commandNames);
        //Configure the list
        commandList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        commandList.setAdapter(adapter);
    }

    /**
     * Grabs a list of something from the data source. Normally, this would be either commands for
     * the button fragment, or buttons for the zone fragment. Do not open or close the data source
     * in the implementation, this is done already for us.
     *
     * @param dataSource the data source to get data from.
     * @param currentGateway the current gateway to get data for.
     * @return a list of all items of type T in the database associated with the gateway.
     */
    public abstract List<T> findItems(DataSource dataSource, Gateway currentGateway);

}
