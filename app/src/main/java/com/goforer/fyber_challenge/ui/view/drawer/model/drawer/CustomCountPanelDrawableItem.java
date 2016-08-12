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

package com.goforer.fyber_challenge.ui.view.drawer.model.drawer;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.ui.view.drawer.model.drawer.holder.CountPanelViewHolder;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.icons.MaterialDrawerFont;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class CustomCountPanelDrawableItem extends CountPanelDrawerItem<CustomCountPanelDrawableItem,
                CustomCountPanelDrawableItem.ViewHolder> {

    private static final int ICONIC_DRAWABLE_SIZE = 16;
    private static final int ICONIC_DRAWABLE_PADDING = 2;

    private Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener;

    protected boolean arrowVisible = true;

    protected ColorHolder arrowColor;

    public CustomCountPanelDrawableItem withArrowVisible(boolean visible) {
        this.arrowVisible = visible;
        return this;
    }

    public CustomCountPanelDrawableItem withArrowColor(@ColorInt int arrowColor) {
        this.arrowColor = ColorHolder.fromColor(arrowColor);
        return this;
    }

    public CustomCountPanelDrawableItem withArrowColorRes(@ColorRes int arrowColorRes) {
        this.arrowColor = ColorHolder.fromColorRes(arrowColorRes);
        return this;
    }

    public boolean getArrowVisible() {
        return arrowVisible;
    }

    @Override
    public int getType() {
        return com.mikepenz.materialdrawer.R.id.material_drawer_item_expandable;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.drawer_item_count_panel;
    }


    @Override
    public CustomCountPanelDrawableItem withOnDrawerItemClickListener(
            Drawer.OnDrawerItemClickListener onDrawerItemClickListener) {
        mOnDrawerItemClickListener = onDrawerItemClickListener;
        return this;
    }

    @Override
    public Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mOnArrowDrawerItemClickListener;
    }

    /**
     * our internal onDrawerItemClickListener which will handle the arrow animation
     */
    private Drawer.OnDrawerItemClickListener mOnArrowDrawerItemClickListener =
            new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem instanceof AbstractDrawerItem && drawerItem.isEnabled()) {
                if (((AbstractDrawerItem) drawerItem).getSubItems() != null) {
                    if (((AbstractDrawerItem) drawerItem).isExpanded()) {
                        ViewCompat.animate(view.findViewById(
                                com.mikepenz.materialdrawer.R.id.material_drawer_arrow))
                                .rotation(180).start();
                    } else {
                        ViewCompat.animate(view.findViewById(
                                com.mikepenz.materialdrawer.R.id.material_drawer_arrow))
                                .rotation(0).start();
                    }
                }
            }

            return mOnDrawerItemClickListener != null && mOnDrawerItemClickListener.onItemClick(
                    view, position, drawerItem);
        }
    };

    @Override
    public void bindView(ViewHolder viewHolder) {
        Context context = viewHolder.itemView.getContext();
        //bind the basic view parts
        bindViewHelper(viewHolder);

        //make sure all animations are stopped
        if (this.arrowVisible) {
            viewHolder.getArrowContainer().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getArrowContainer().setVisibility(View.GONE);
        }

        if (this.arrowColor != null) {
            viewHolder.arrow.setColor(this.arrowColor.color(context));
        }
        else {
            viewHolder.arrow.setColor(getIconColor(context));
        }
        viewHolder.arrow.clearAnimation();
        if (!isExpanded()) {
            ViewCompat.setRotation(viewHolder.arrow, 0);
        } else {
            ViewCompat.setRotation(viewHolder.arrow, 180);
        }

        //call the onPostBindView method to trigger post bind view actions
        // (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<ViewHolder> getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    public static class ViewHolder extends CountPanelViewHolder {
        public IconicsImageView arrow;

        public ViewHolder(View view) {
            super(view);
            arrow = (IconicsImageView) view.findViewById(
                    com.mikepenz.materialdrawer.R.id.material_drawer_arrow);
            arrow.setIcon(new IconicsDrawable(view.getContext(),
                    MaterialDrawerFont.Icon.mdf_expand_more)
                    .sizeDp(ICONIC_DRAWABLE_SIZE)
                    .paddingDp(ICONIC_DRAWABLE_PADDING)
                    .color(Color.BLACK));
        }
    }
}
