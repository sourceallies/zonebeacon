package com.sourceallies.android.zonebeacon.fragment;

import android.content.Intent;
import android.net.Uri;
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

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 *
 */
public class AddZoneFragment extends AbstractSetupFragment{
    /**
     * Default constructor for the fragment
     */
    public AddZoneFragment() { }

    @Getter
    private TextInputLayout name;
    @Getter
    private ListView buttonList;
    @Getter
    private List<Button> allButtons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_zone, container, false);

        // setup the UI elements
        name = (TextInputLayout) root.findViewById(R.id.name);
        buttonList = (ListView) root.findViewById(R.id.buttonList);
        populateButtonList();

        return root;
    }

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
        boolean complete = true;

        if (isEmpty(name))      complete = false;

        return complete;
    }


    @Override
    public void save() {
        String name = getText(this.name);
        List<Button> buttonList = getCheckedButtons();

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        source.insertNewZone(name, buttonList);
        source.close();
    }

    private List<Button> getCheckedButtons() {
        SparseBooleanArray checked = buttonList.getCheckedItemPositions();
        List<Button> checkedButtons = new ArrayList<Button>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            if (checked.valueAt(i)) {
                checkedButtons.add(allButtons.get(position));
            }
        }

        return checkedButtons;
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }

    public void populateButtonList(){
        Gateway currentGateway = getCurrentGateway();

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        allButtons = source.findButtons(currentGateway);
        source.close();

        ListView buttonList = getButtonList();
        //create list of buttons
        String[] buttonNames = new String[allButtons.size()];

        for (int i = 0; i < allButtons.size(); i++) {
            buttonNames[i] = allButtons.get(i).getName();
        }

        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, buttonNames);
        //Configure the list
        buttonList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        buttonList.setAdapter(adapter);




    }
}
