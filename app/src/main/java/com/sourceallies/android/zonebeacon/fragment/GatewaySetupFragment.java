/*
 * Copyright (C) 2016 Source Allies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;

import lombok.Getter;

/**
 * Fragment to insert a name, ip address, and port for a gateway that you want to create.
 */
public class GatewaySetupFragment extends AbstractSetupFragment {

    /**
     * Default constructor for the fragment
     */
    public GatewaySetupFragment() {
    }

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

        // setup the UI elements
        name = (TextInputLayout) root.findViewById(R.id.name);
        ipAddress = (TextInputLayout) root.findViewById(R.id.ip_address);
        port = (TextInputLayout) root.findViewById(R.id.port);

        // create a clickable link to the YouTube channel for help setting up ZoneBeacon
        buildLinkToSetupVideo(root);

        return root;
    }

    /**
     * Create a clickable link to the YouTube app for help setting up your hardware.
     *
     * @param root main view on the fragment
     */
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

        // make the text view clickable with the LinkBuilder library
        LinkBuilder
                .on((TextView) root.findViewById(R.id.description))
                .addLink(videoLink)
                .build();
    }

    @Override
    public boolean isComplete() {
        boolean complete = true;

        if (isEmpty(name)) complete = false;
        if (isEmpty(ipAddress)) complete = false;
        if (isEmpty(port)) complete = false;

        return complete;
    }

    @Override
    public void save() {
        String name = getText(this.name);
        String ipAddress = getText(this.ipAddress);
        int port = Integer.parseInt(getText(this.port));

        DataSource source = DataSource.getInstance(getActivity());
        source.open();
        source.insertNewGateway(name, ipAddress, port);
        source.close();
    }

    private String getText(TextInputLayout input) {
        return input.getEditText().getText().toString();
    }
}
