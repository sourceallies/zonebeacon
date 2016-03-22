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

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.List;

/**
 * Fragment for creating a zone and adding it to the current gateway that is selected.
 */
public class AddZoneFragment extends AbstractAddFragment<Button> {

    /**
     * Default constructor for the fragment.
     */
    public AddZoneFragment() {

    }

    @Override
    public String getPageTitle() {
        return getString(R.string.add_zone_title);
    }

    @Override
    public String getListTitle() {
        return getString(R.string.include_buttons_title);
    }

    @Override
    public String getNameHint() {
        return getString(R.string.zone_name_hint);
    }

    @Override
    public void insertItems(DataSource dataSource, String name, List<Button> items) {
        dataSource.insertNewZone(name, items);
    }

    @Override
    public List<Button> findItems(DataSource dataSource, Gateway currentGateway) {
        return dataSource.findButtons(currentGateway);
    }

}
