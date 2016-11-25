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

package com.goforer.fyber_challenge.ui.view.drawer.model.drawer;

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

import com.goforer.fyber_challenge.R;
import com.goforer.fyber_challenge.ui.view.drawer.model.drawer.holder.CommentViewHolder;
import com.goforer.fyber_challenge.ui.view.drawer.model.interfaces.drawer.Commentable;
import com.goforer.fyber_challenge.ui.view.drawer.model.interfaces.drawer.Picturable;
import com.goforer.fyber_challenge.ui.view.drawer.model.utils.SlidingDrawerImageLoader;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.holder.ColorHolder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.AbstractDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Tagable;
import com.mikepenz.materialdrawer.model.interfaces.Typefaceable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

public class CommentDrawerItem extends AbstractDrawerItem<CommentDrawerItem, CommentViewHolder>
        implements Commentable<CommentDrawerItem>, Picturable<CommentDrawerItem>, Tagable<CommentDrawerItem>,
        Typefaceable<CommentDrawerItem> {

    protected ImageHolder background;
    protected ImageHolder picture;
    protected StringHolder commenter;
    protected StringHolder comment;
    protected StringHolder date;
    protected StringHolder like_count;

    protected boolean pictureTinted = false;

    protected ColorHolder selectedColor;
    protected ColorHolder textColor;
    protected ColorHolder selectedTextColor;
    protected ColorHolder disabledTextColor;

    protected ColorHolder pictureColor;
    protected ColorHolder selectedIconColor;
    protected ColorHolder disabledIconColor;

    protected Typeface typeface = null;

    protected Pair<Integer, ColorStateList> colorStateList;

    protected int level = 1;

    protected static long comment_id;
    protected static long commenter_id;

    /**
     * set the background for the slider as color
     *
     * @param commentBackground
     * @return
     */
    public CommentDrawerItem withBackground(Drawable commentBackground) {
        this.background = new ImageHolder(commentBackground);
        return this;
    }

    /**
     * set the background for the header as resource
     *
     * @param commentBackground
     * @return
     */
    public CommentDrawerItem withHeaderBackground(@DrawableRes int commentBackground) {
        this.background = new ImageHolder(commentBackground);
        return this;
    }

    /**
     * set the background for the header via the ImageHolder class
     *
     * @param commentBackground
     * @return
     */
    public CommentDrawerItem withHeaderBackground(ImageHolder commentBackground) {
        this.background = commentBackground;
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(ImageHolder picture) {
        this.picture = picture;
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(Drawable picture) {
        this.picture = new ImageHolder(picture);
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(String url) {
        this.picture = new ImageHolder(url);
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(Uri uri) {
        this.picture = new ImageHolder(uri);
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(Bitmap bitmap) {
        this.picture = new ImageHolder(bitmap);
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(@DrawableRes int iconRes) {
        this.picture = new ImageHolder(iconRes);
        return this;
    }

    @Override
    public CommentDrawerItem withPicture(IIcon iicon) {
        this.picture = new ImageHolder(iicon);
        return this;
    }

    @Override
    public CommentDrawerItem withCommenter(StringHolder commenter) {
        this.commenter = commenter;
        return this;
    }

    @Override
    public CommentDrawerItem withCommenter(String commenter) {
        this.commenter = new StringHolder(commenter);
        return this;
    }

    @Override
    public CommentDrawerItem withCommenter(@StringRes int commenterRes) {
        this.commenter = new StringHolder(commenterRes);
        return this;
    }

    @Override
    public CommentDrawerItem withComment(StringHolder comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public CommentDrawerItem withComment(String comment) {
        this.comment = new StringHolder(comment);
        return this;
    }

    @Override
    public CommentDrawerItem withComment(@StringRes int commentRes) {
        this.comment = new StringHolder(commentRes);
        return this;
    }

    @Override
    public CommentDrawerItem withCommentId(long id) {
        comment_id = id;
        return this;
    }

    @Override
    public CommentDrawerItem withCommenterId(long id) {
        commenter_id = id;
        return this;
    }

    @Override
    public CommentDrawerItem withDate(StringHolder date) {
        this.date = date;
        return this;
    }

    @Override
    public CommentDrawerItem withDate(String date) {
        this.date = new StringHolder(date);
        return this;
    }

    @Override
    public CommentDrawerItem withDate(@StringRes int dateRes) {
        this.date = new StringHolder(dateRes);
        return this;
    }

    @Override
    public CommentDrawerItem withLikeCount(StringHolder likeCount) {
        this.like_count = likeCount;
        return this;
    }

    @Override
    public CommentDrawerItem withLikeCount(String likeCount) {
        this.like_count = new StringHolder(likeCount);
        return this;
    }

    @Override
    public CommentDrawerItem withLikeCount(@StringRes int likeCountRes) {
        this.like_count = new StringHolder(likeCountRes);
        return this;
    }

    public CommentDrawerItem withSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = ColorHolder.fromColor(selectedColor);
        return this;
    }

    public CommentDrawerItem withSelectedColorRes(@ColorRes int selectedColorRes) {
        this.selectedColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public CommentDrawerItem withTextColor(@ColorInt int textColor) {
        this.textColor = ColorHolder.fromColor(textColor);
        return this;
    }

    public CommentDrawerItem withTextColorRes(@ColorRes int textColorRes) {
        this.textColor = ColorHolder.fromColorRes(textColorRes);
        return this;
    }

    public CommentDrawerItem withSelectedTextColor(@ColorInt int selectedTextColor) {
        this.selectedTextColor = ColorHolder.fromColor(selectedTextColor);
        return this;
    }

    public CommentDrawerItem withSelectedTextColorRes(@ColorRes int selectedColorRes) {
        this.selectedTextColor = ColorHolder.fromColorRes(selectedColorRes);
        return this;
    }

    public CommentDrawerItem withDisabledTextColor(@ColorInt int disabledTextColor) {
        this.disabledTextColor = ColorHolder.fromColor(disabledTextColor);
        return this;
    }

    public CommentDrawerItem withDisabledTextColorRes(@ColorRes int disabledTextColorRes) {
        this.disabledTextColor = ColorHolder.fromColorRes(disabledTextColorRes);
        return this;
    }

    public CommentDrawerItem withPictureTintingEnabled(boolean pictureTintingEnabled) {
        this.pictureTinted = pictureTintingEnabled;
        return this;
    }

    @Deprecated
    public CommentDrawerItem withPicureTinted(boolean pictureTinted) {
        this.pictureTinted = pictureTinted;
        return this;
    }

    public CommentDrawerItem withTypeface(Typeface typeface) {
        this.typeface = typeface;
        return this;
    }

    public CommentDrawerItem withLevel(int level) {
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
    public ImageHolder getPicture() {
        return picture;
    }

    @Override
    public StringHolder getCommenter() {
        return commenter;
    }

    @Override
    public StringHolder getComment() {
        return comment;
    }

    @Override
    public StringHolder getDate() {
        return date;
    }

    @Override
    public StringHolder getLikeCount() {
        return like_count;
    }

    public ColorHolder getDisabledIconColor() {
        return disabledIconColor;
    }

    public ColorHolder getSelectedIconColor() {
        return selectedIconColor;
    }

    public ColorHolder getPictureColor() {
        return pictureColor;
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
            iconColor = ColorHolder.color(getPictureColor(), context,
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
        return R.id.material_drawer_item_comment;
    }

    @Override
    @LayoutRes
    public int getLayoutRes() {
        return R.layout.drawer_item_comment;
    }

    @Override
    public void bindView(CommentViewHolder viewHolder) {
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

        //set the text for the commenter
        StringHolder.applyTo(this.getCommenter(), viewHolder.getCommenter());
        //set the text for the comment
        StringHolder.applyTo(this.getComment(), viewHolder.getComment());
        //set the text for the date
        StringHolder.applyTo(this.getDate(), viewHolder.getDate());
        //set the text for the Like count
        StringHolder.applyTo(this.getLikeCount(), viewHolder.getLikeCount());

        //define the typeface for our textViews
        if (getTypeface() != null) {
            viewHolder.getCommenter().setTypeface(getTypeface());
            viewHolder.getComment().setTypeface(getTypeface());
            viewHolder.getDate().setTypeface(getTypeface());
            viewHolder.getLikeCount().setTypeface(getTypeface());
        }


        SlidingDrawerImageLoader.getInstance().cancelImage(viewHolder.getPicture());
        //set the placeholder
        viewHolder.getPicture().setImageDrawable(SlidingDrawerImageLoader.getInstance()
                .getImageLoader().placeholder(viewHolder.getPicture().getContext(),
                        SlidingDrawerImageLoader.Tags.COMMENT_PICTURE.name()));
        //set the picture
        ImageHolder.applyTo(getPicture(), viewHolder.getPicture(),
                SlidingDrawerImageLoader.Tags.COMMENT_PICTURE.name());

        //for android API 17 --> Padding not applied via xml
        DrawerUIUtils.setDrawerVerticalPadding(viewHolder.getView());

        //call the onPostBindView method to trigger post bind view actions (like the listener to modify the item if required)
        onPostBindView(this, viewHolder.itemView);
    }

    @Override
    public ViewHolderFactory<CommentViewHolder> getFactory() {
        return new ItemFactory();
    }

    public static class ItemFactory implements ViewHolderFactory<CommentViewHolder> {
        public CommentViewHolder create(View v) {
            return new CommentViewHolder(v, comment_id, commenter_id);
        }
    }
}
