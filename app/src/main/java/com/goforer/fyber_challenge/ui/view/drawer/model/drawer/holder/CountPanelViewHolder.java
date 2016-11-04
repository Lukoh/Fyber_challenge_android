/*
 * Copyright (C) 2015-2016 Lukoh Nam, goForer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goforer.fyber_challenge.ui.view.drawer.model.drawer.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goforer.fyber_challenge.R;
import com.mikepenz.materialdrawer.model.BaseViewHolder;

public class CountPanelViewHolder extends BaseViewHolder {
    protected TextView mCount;
    protected LinearLayout arrowContainer;

    public CountPanelViewHolder(View view) {
        super(view);

        this.mCount = (TextView) view.findViewById(R.id.material_drawer_count);
        this.arrowContainer = (LinearLayout) view.findViewById(R.id.material_drawer_arrow_container);
    }

    public View getView() {
        return view;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getName() {
        return name;
    }

    public TextView getDescription() {
        return description;
    }

    public TextView getCount() {
        return mCount;
    }

    public LinearLayout getArrowContainer() {
        return arrowContainer;
    }
}
