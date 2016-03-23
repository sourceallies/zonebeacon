package com.sourceallies.android.zonebeacon.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.activity.MainActivity;
import com.sourceallies.android.zonebeacon.adapter.MainAdapter;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.CommandType;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class CommandSetupFragment extends AbstractSetupFragment {

    public CommandSetupFragment() { }

    @Getter
    private TextInputLayout name;

    @Getter
    private TextInputLayout loadNumber;

    @Getter
    private Spinner commandType;

    @Getter
    private Spinner systemType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_command, container, false);

        name = (TextInputLayout) root.findViewById(R.id.name);
        loadNumber = (TextInputLayout)root.findViewById(R.id.load_number);
        systemType = (Spinner) root.findViewById(R.id.system_type);
        commandType = (Spinner) root.findViewById(R.id.command_type);

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        List<CommandType> commandTypeArray = source.findCommandTypesShownInUI(getCurrentGateway());
        source.close();

        //HERE IS MY PROBLEM
        ArrayAdapter<CommandType> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, commandTypeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        commandType.setAdapter(adapter);

        return root;
    }

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
            input.setError("");
        }

        return false;
    }

    @Override
    public boolean isComplete() {
        boolean complete = true;

        if (isEmpty(name))             complete = false;
        if (isEmpty(loadNumber))       complete = false;

        return complete;
    }

    @Override
    public void save() {
        String name = getText(this.name);
        //int gatewayId = Integer.parseInt(getText(this.gatewayID));
        int loadNum = Integer.parseInt((getText(this.loadNumber)));

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        //source.insertNewCommand(name, getCurrentGateway(), loadNumber , command, controllerNum);
        source.close();
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }
}
