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

package com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer.holder.CountPanelViewHolder;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public abstract class CountPanelDrawerItem<T, VH extends CountPanelViewHolder>
        extends BaseDrawerItem<T, VH> {
    private StringHolder count;

    private ColorHolder countTextColor;

    public T withCount(String count) {
        this.count = new StringHolder(count);
        return (T) this;
    }

    public T withCount(@StringRes int countRes) {
        this.count = new StringHolder(countRes);
        return (T) this;
    }

    public T withCountTextColor(@ColorInt int color) {
        this.countTextColor = ColorHolder.fromColor(color);
        return (T) this;
    }

    public T withCountTextColorRes(@ColorRes int colorRes) {
        this.countTextColor = ColorHolder.fromColorRes(colorRes);
        return (T) this;
    }


    public StringHolder getCount() {
        return count;
    }

    public ColorHolder getCountTextColor() {
        return countTextColor;
    }

    /**
     * a helper method to have the logic for all secondaryDrawerItems only once
     *
     * @param viewHolder
     */
    protected void bindViewHelper(CountPanelViewHolder viewHolder) {
        Context ctx = viewHolder.itemView.getContext();

        //set the identifier from the drawerItem here. It can be used to run tests
        viewHolder.itemView.setId(hashCode());

        //set the item selected if it is
        viewHolder.itemView.setSelected(isSelected());

        //set the item enabled if it is
        viewHolder.itemView.setEnabled(isEnabled());

        //
        viewHolder.itemView.setTag(this);

        //get the correct color for the background
        int selectedColor = getSelectedColor(ctx);
        //get the correct color for the text
        int color = getColor(ctx);
        ColorStateList selectedTextColor = getTextColorStateList(color, getSelectedTextColor(ctx));
        //get the correct color for the icon
        int iconColor = getIconColor(ctx);
        int selectedIconColor = getSelectedIconColor(ctx);

        //set the background for the item
        UIUtils.setBackground(viewHolder.getView(), UIUtils.getSelectableBackground(
                ctx, selectedColor, true));
        //set the text for the name
        StringHolder.applyTo(this.getName(), viewHolder.getName());
        //set the text for the count or hide
        StringHolder.applyToOrHide(this.getCount(), viewHolder.getCount());

        //set the colors for textViews
        viewHolder.getName().setTextColor(selectedTextColor);
        //set the count text color
        ColorHolder.applyToOr(getCountTextColor(), viewHolder.getCount(), selectedTextColor);

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.getName().setTypeface(getTypeface());
            viewHolder.getDescription().setTypeface(getTypeface());
            viewHolder.getCount().setTypeface(getTypeface());
        }

        //get the drawables for our icon and set it
        Drawable icon = ImageHolder.decideIcon(getIcon(), ctx, iconColor, isIconTinted(), 1);
        Drawable selectedIcon = ImageHolder.decideIcon(getSelectedIcon(), ctx, selectedIconColor,
                isIconTinted(), 1);
        ImageHolder.applyMultiIconTo(icon, iconColor, selectedIcon, selectedIconColor,
                isIconTinted(), viewHolder.getIcon());

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.getView(), level);
    }
}
