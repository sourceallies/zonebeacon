package com.sourceallies.android.zonebeacon.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sourceallies.android.zonebeacon.R;

import lombok.Getter;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class InitialGatewaySetup extends Fragment {

    public InitialGatewaySetup() { }

    @Getter
    private TextInputLayout name;
    @Getter
    private TextInputLayout ipAddress;
    @Getter
    private TextInputLayout port;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_gateway, container, false);

        name = (TextInputLayout) root.findViewById(R.id.name);
        ipAddress = (TextInputLayout) root.findViewById(R.id.ip_address);
        port = (TextInputLayout) root.findViewById(R.id.port);

        return root;
    }

    public boolean isComplete() {
        boolean complete = true;

        if (isEmpty(name))      complete = false;
        if (isEmpty(ipAddress)) complete = false;
        if (isEmpty(port))      complete = false;

        return complete;
    }

    public boolean isEmpty(TextInputLayout input) {
        if (TextUtils.isEmpty(input.getEditText().getText())) {
            name.setError(getString(R.string.fill_field));
            return true;
        }

        return false;
    }
}
