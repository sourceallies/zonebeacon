package com.sourceallies.android.zonebeacon.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_button, container, false);

        // setup the UI elements
        name = (TextInputLayout) root.findViewById(R.id.name);
        commandList = (ListView) root.findViewById(R.id.buttonList);
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

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        //source.insertNewZone(name, );
        source.close();
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }

    public void populateButtonList(){
        ListView buttonList = getCommandList();
        //create list of buttons
        String[] commandNames = {"Command1", "Back", "Patio", "Lamp"};
        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, buttonNames);
        //Configure the list
        buttonList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        buttonList.setAdapter(adapter);




    }
}
