package com.sourceallies.android.zonebeacon.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.adapter.CommandSpinnerAdapter;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.CommandType;

import java.util.List;

import lombok.Getter;

public class CommandSetupFragment extends AbstractSetupFragment {

    public CommandSetupFragment() { }

    @Getter
    private TextInputLayout name;

    @Getter
    private TextInputLayout loadNumber;

    @Getter
    private TextInputLayout controllerNumber;

    @Getter
    private Spinner commandTypeSpinner;

    @Getter
    private int currentSpinnerSelection = 0;

    @Getter
    private  CommandSpinnerAdapter commandSpinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_command, container, false);

        name = (TextInputLayout) root.findViewById(R.id.name);
        loadNumber = (TextInputLayout)root.findViewById(R.id.load_number);
        commandTypeSpinner = (Spinner) root.findViewById(R.id.command_type);
        controllerNumber = (TextInputLayout)root.findViewById(R.id.controller_number);

        SetCommandSpinnerAdapter();

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
        int loadNumber = Integer.parseInt((getText(this.loadNumber)));
        CommandType currentCommandType = commandSpinnerAdapter.getItem(getCurrentSpinnerSelection());
        Integer controllerNum = Integer.parseInt((getText(this.controllerNumber)));

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        source.insertNewCommand(name, getCurrentGateway().getId(), loadNumber, currentCommandType.getId(), controllerNum);
        source.close();
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }

    private void SetCommandSpinnerAdapter()
    {
        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        List<CommandType> commandTypes = source.findCommandTypesShownInUI(getCurrentGateway());
        source.close();

        CommandSpinnerAdapter commandSpinnerAdapter = new CommandSpinnerAdapter(getActivity(),commandTypes);
        commandTypeSpinner.setAdapter(commandSpinnerAdapter);

        commandTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == commandTypeSpinner.getCount() - 1 && commandTypeSpinner.getCount() > 1) {
                    commandTypeSpinner.setSelection(currentSpinnerSelection);
                } else {
                    currentSpinnerSelection = position;
                }
            }
        });
    }
}
