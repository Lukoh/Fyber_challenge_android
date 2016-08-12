/*
 * Copyright (C) 2016 Lukoh Nam, goForer
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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.goforer.fyber_challenge.R;

public class MenuViewHolder extends RecyclerView.ViewHolder {
    protected View view;
    protected ImageView icon;
    protected TextView menu;
    protected TextView menu_sub1;
    protected TextView menu_sub2;
    protected TextView description;

    public MenuViewHolder(View view) {
        super(view);

        this.view = view;
        this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
        this.menu = (TextView) view.findViewById(R.id.material_drawer_menu);
        this.menu_sub1 = (TextView) view.findViewById(R.id.material_drawer_menu_sub1);
        this.menu_sub2 = (TextView) view.findViewById(R.id.material_drawer_menu_sub2);
        this.description = (TextView) view.findViewById(R.id.material_drawer_menu_description);
    }

    public View getView() {
        return view;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getMenu() {
        return menu;
    }

    public TextView getMenuSub1() {
        return menu_sub1;
    }

    public TextView getMenuSub2() {
        return menu_sub2;
    }

    public TextView getDescription() {
        return description;
    }
}
