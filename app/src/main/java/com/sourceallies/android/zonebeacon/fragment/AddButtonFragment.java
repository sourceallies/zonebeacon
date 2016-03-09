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
public class AddButtonFragment extends AbstractSetupFragment{
    /**
     * Default constructor for the fragment
     */
    public AddButtonFragment() { }

    @Getter
    private TextInputLayout name;
    @Getter
    private ListView commandList;
    @Getter
    private List<Command> allCommands;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_button, container, false);

        // setup the UI elements
        name = (TextInputLayout) root.findViewById(R.id.name);
        commandList = (ListView) root.findViewById(R.id.commandList);
        populateCommandList();

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
        List<Command> commandList = getCheckedCommands();

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        source.insertNewButton(name, commandList);
        source.close();
    }

    private List<Command> getCheckedCommands() {
        SparseBooleanArray checked = commandList.getCheckedItemPositions();
        List<Command> checkedCommands = new ArrayList<Command>();
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            if (checked.valueAt(i)) {
                checkedCommands.add(allCommands.get(position));
            }
        }
        return checkedCommands;
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }

    private void populateCommandList(){
        Gateway currentGateway = getCurrentGateway();

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        allCommands = source.findCommands(currentGateway);
        source.close();

        ListView commandList = getCommandList();
        //create list of buttons
        String[] commandNames = new String[allCommands.size()];

        for (int i = 0; i < allCommands.size(); i++) {
            commandNames[i] = allCommands.get(i).getName();
        }

        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, commandNames);
        //Configure the list
        commandList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        commandList.setAdapter(adapter);
    }
}
