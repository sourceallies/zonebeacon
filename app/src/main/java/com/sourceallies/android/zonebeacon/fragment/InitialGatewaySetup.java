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
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
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

    @Getter
    private Link videoLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_gateway, container, false);

        name = (TextInputLayout) root.findViewById(R.id.name);
        ipAddress = (TextInputLayout) root.findViewById(R.id.ip_address);
        port = (TextInputLayout) root.findViewById(R.id.port);

        buildLinkToSetupVideo(root);

        return root;
    }

    private void buildLinkToSetupVideo(View root) {
        videoLink = new Link("this YouTube channel")
                .setUnderlined(false)
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.youtube.com/playlist?list=PL4A84439A46A149C0"));
                        getActivity().startActivity(i);
                    }
                });

        LinkBuilder
                .on((TextView) root.findViewById(R.id.description))
                .addLink(videoLink)
                .build();
    }

    public boolean isComplete() {
        boolean complete = true;

        if (isEmpty(name))      complete = false;
        if (isEmpty(ipAddress)) complete = false;
        if (isEmpty(port))      complete = false;

        return complete;
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
}
