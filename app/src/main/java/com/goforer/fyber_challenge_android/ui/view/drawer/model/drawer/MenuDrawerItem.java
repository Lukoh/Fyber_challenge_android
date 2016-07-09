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
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.Pair;
import android.view.View;

import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer.holder.MenuViewHolder;
import com.goforer.fyber_challenge_android.ui.view.drawer.model.interfaces.drawer.Menuable;
import com.goforer.fyber_challenge_android.ui.view.drawer.model.utils.SlidingDrawerImageLoader;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public class MenuDrawerItem extends AbstractDrawerItem<MenuDrawerItem, MenuViewHolder>
        implements Menuable<MenuDrawerItem>, Iconable<MenuDrawerItem>, Tagable<MenuDrawerItem>,
        Typefaceable<MenuDrawerItem> {
    protected ImageHolder background;
    protected ImageHolder icon;
    protected StringHolder menu;
    protected StringHolder menuSub1;
    protected StringHolder menuSub2;
    protected StringHolder description;

    protected boolean iconTinted = false;

    protected ColorHolder selectedColor;
    protected ColorHolder textColor;
    protected ColorHolder selectedTextColor;
    protected ColorHolder disabledTextColor;

    protected ColorHolder iconColor;
    protected ColorHolder selectedIconColor;
    protected ColorHolder disabledIconColor;

    protected Typeface typeface = null;

    protected Pair<Integer, ColorStateList> colorStateList;

    protected int level = 1;

    /**
     * set the background for the slider as color
     *
     * @param menuBackground
     * @return
     */
    public MenuDrawerItem withBackground(Drawable menuBackground) {
        this.background = new ImageHolder(menuBackground);
        return this;
    }

    /**
     * set the background for the header as resource
     *
     * @param menuBackground
     * @return
     */
    public MenuDrawerItem withHeaderBackground(@DrawableRes int menuBackground) {
        this.background = new ImageHolder(menuBackground);
        return this;
    }

    /**
     * set the background for the header via the ImageHolder class
     *
     * @param menuBackground
     * @return
     */
    public MenuDrawerItem withHeaderBackground(ImageHolder menuBackground) {
        this.background = menuBackground;
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(ImageHolder icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(Drawable icon) {
        this.icon = new ImageHolder(icon);
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(Uri uri) {
        this.icon = new ImageHolder(uri);
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(Bitmap bitmap) {
        this.icon = new ImageHolder(bitmap);
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(@DrawableRes int iconRes) {
        this.icon = new ImageHolder(iconRes);
        return this;
    }

    @Override
    public MenuDrawerItem withIcon(IIcon iicon) {
        this.icon = new ImageHolder(iicon);
        return this;
    }

    @Override
    public MenuDrawerItem withMenu(StringHolder menu) {
        this.menu = menu;
        return this;
    }

    @Override
    public MenuDrawerItem withMenu(String menu) {
        this.menu = new StringHolder(menu);
        return this;
    }

    @Override
    public MenuDrawerItem withMenu(@StringRes int menuRes) {
        this.menu = new StringHolder(menuRes);
        return this;
    }

    @Override
    public MenuDrawerItem withMenuSub1(StringHolder menuSub1) {
        this.menuSub1 = menuSub1;
        return this;
    }

    @Override
    public MenuDrawerItem withMenuSub1(String menuSub1) {
        this.menuSub1 = new StringHolder(menuSub1);
        return this;
    }

    @Override
    public MenuDrawerItem withMenuSub1(@StringRes int menuSub1Res) {
        this.menuSub1 = new StringHolder(menuSub1Res);
        return this;
    }

    @Override
    public MenuDrawerItem withMenuSub2(StringHolder menuSub2) {
        this.menuSub2 = menuSub2;
        return this;
    }

    @Override
    public MenuDrawerItem withMenuSub2(String menuSub2) {
        this.menuSub2 = new StringHolder(menuSub2);
        return this;
    }

    @Override
    public MenuDrawerItem withMenuSub2(@StringRes int menuSub2Res) {
        this.menuSub2 = new StringHolder(menuSub2Res);
        return this;
    }

    @Override
    public MenuDrawerItem withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    @Override
    public MenuDrawerItem withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    public MenuDrawerItem withSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public MenuDrawerItem withSelectedColorRes(@ColorRes int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public MenuDrawerItem withTextColor(@ColorInt int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public MenuDrawerItem withTextColorRes(@ColorRes int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public MenuDrawerItem withSelectedTextColor(@ColorInt int selectedTextColor) {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor);
        return this;
    }

    public MenuDrawerItem withSelectedTextColorRes(@ColorRes int selectedColorRes) {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public MenuDrawerItem withDisabledTextColor(@ColorInt int disabledTextColor) {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor);
        return this;
    }

    public MenuDrawerItem withDisabledTextColorRes(@ColorRes int disabledTextColorRes) {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes);
        return this;
    }

    public MenuDrawerItem withIconTintingEnabled(boolean iconTintingEnabled) {
        this.iconTinted = iconTintingEnabled;
        return this;
    }

    @Deprecated
    public MenuDrawerItem withIconTinted(boolean iconTinted) {
        this.iconTinted = iconTinted;
        return this;
    }

    public MenuDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public MenuDrawerItem withLevel(int level) {
        this.level = level;
        return this;
    }

    public ColorHolder getSelectedColor() {
        return selectedColor;
    }

    public ColorHolder getTextColor() {
        return textColor;
    }

    public ColorHolder getSelectedTextColor() {
        return selectedTextColor;
    }

    public ColorHolder getDisabledTextColor() {
        return disabledTextColor;
    }

    public ImageHolder getBackground() {
        return background;
    }

    @Override
    public ImageHolder getIcon() {
        return icon;
    }

    @Override
    public StringHolder getMenu() {
        return menu;
    }

    @Override
    public StringHolder getMenuSub1() {
        return menuSub1;
    }

    @Override
    public StringHolder getMenuSub2() {
        return menuSub2;
    }

    @Override
    public StringHolder getDescription() {
        return description;
    }

    public ColorHolder getDisabledIconColor() {
        return disabledIconColor;
    }

    public ColorHolder getSelectedIconColor() {
        return selectedIconColor;
    }

    public ColorHolder getIconColor() {
        return iconColor;
    }

    @Override
    public Typeface getTypeface() {
        return typeface;
    }

    protected int getSelectedColor(Context context) {
        return ColorHolder.color(getSelectedColor(), context, R.attr.material_drawer_selected,
                R.color.material_drawer_selected);
    }

    protected int getColor(Context context) {
        int color;
        if (this.isEnabled()) {
            color = ColorHolder.color(getTextColor(), context, R.attr.material_drawer_primary_text,
                    R.color.material_drawer_primary_text);
        } else {
            color = ColorHolder.color(getDisabledTextColor(), context, R.attr.material_drawer_hint_text,
                    R.color.material_drawer_hint_text);
        }
        return color;
    }

    protected int getSelectedTextColor(Context context) {
        return ColorHolder.color(getSelectedTextColor(), context, R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    public int getIconColor(Context context) {
        int iconColor;
        if (this.isEnabled()) {
            iconColor = ColorHolder.color(getIconColor(), context,
                    R.attr.material_drawer_primary_icon, R.color.material_drawer_primary_icon);
        } else {
            iconColor = ColorHolder.color(getDisabledIconColor(), context,
                    R.attr.material_drawer_hint_icon, R.color.material_drawer_hint_icon);
        }
        return iconColor;
    }

    protected int getSelectedIconColor(Context context) {
        return ColorHolder.color(getSelectedIconColor(), context,
                R.attr.material_drawer_selected_text, R.color.material_drawer_selected_text);
    }

    protected ColorStateList getTextColorStateList(@ColorInt int color,
                                                   @ColorInt int selectedTextColor) {
        if (colorStateList == null || color + selectedTextColor != colorStateList.first) {
            colorStateList = new Pair<>(color + selectedTextColor,
                    DrawerUIUtils.getTextColorStateList(color, selectedTextColor));
        }

        return colorStateList.second;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_menu;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.drawer_item_menu;
    }

    @Override
    public void bindView(MenuViewHolder viewHolder) {
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

        //set the background for the item
        if (background != null) {
            UIUtils.setBackground(viewHolder.getView(), background.getIconRes());
        } else {
            UIUtils.setBackground(viewHolder.getView(), UIUtils.getSelectableBackground(
                    ctx, selectedColor, true));
        }

        //set the text for the menu
        StringHolder.applyTo(this.getMenu(), viewHolder.getMenu());
        //set the text for the menu sub1
        StringHolder.applyTo(this.getMenuSub1(), viewHolder.getMenuSub1());
        //set the text for the menu sub2
        StringHolder.applyTo(this.getMenuSub2(), viewHolder.getMenuSub2());
        //set the text for the description
        StringHolder.applyTo(this.getDescription(), viewHolder.getDescription());

        //set the colors for menu textViews
        viewHolder.getMenu().setTextColor(selectedTextColor);
        //set the colors for menu sub1 textViews
        viewHolder.getMenuSub1().setTextColor(selectedTextColor);
        //set the colors for menu sub2 textViews
        viewHolder.getMenuSub1().setTextColor(selectedTextColor);
        //set the colors for menu sub2 textViews
        viewHolder.getDescription().setTextColor(selectedTextColor);

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.getMenu().setTypeface(getTypeface());
            viewHolder.getMenuSub1().setTypeface(getTypeface());
            viewHolder.getMenuSub2().setTypeface(getTypeface());
            viewHolder.getDescription().setTypeface(getTypeface());
        }


        SlidingDrawerImageLoader.getInstance().cancelImage(viewHolder.getIcon());
        //set the placeholder
        viewHolder.getIcon().setImageDrawable(SlidingDrawerImageLoader.getInstance()
                .getImageLoader().placeholder(viewHolder.getIcon().getContext(),
                        SlidingDrawerImageLoader.Tags.MENU_PICTURE.name()));
        //set the icon
        ImageHolder.applyTo(getIcon(), viewHolder.getIcon(),
                SlidingDrawerImageLoader.Tags.MENU_PICTURE.name());

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.getView());

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<MenuViewHolder> getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<MenuViewHolder> {
        public MenuViewHolder create(View v) {
            return new MenuViewHolder(v);
        }
    }
}
