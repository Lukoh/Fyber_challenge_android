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

package com.goforer.fyber_challenge_android.ui.view.drawer.model.drawer.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.action.CommentLikeAction;

import org.greenrobot.eventbus.EventBus;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    protected View view;
    protected ImageView picture;
    protected TextView commenter;
    protected TextView comment;
    protected TextView date;
    protected LinearLayout like;
    protected TextView like_count;

    protected long comment_id;
    protected long commenter_id;

    public CommentViewHolder(View view, final long commentId, final long commenterId) {
        super(view);

        this.view = view;

        this.comment_id = commentId;
        this.commenter_id = commenterId;

        this.picture = (ImageView) view.findViewById(R.id.iv_picture);
        this.commenter = (TextView) view.findViewById(R.id.tv_commenter);
        this.comment = (TextView) view.findViewById(R.id.tv_comment);
        this.date = (TextView) view.findViewById(R.id.tv_date);
        this.like = (LinearLayout) view.findViewById(R.id.container_like_bar);
        this.like_count = (TextView) view.findViewById(R.id.tv_like_count);

        this.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.valueOf(like_count.getText().toString().trim());
                like_count.setText(String.valueOf(++count));

                CommentLikeAction action = new CommentLikeAction();
                action.setCommenter(commenter.getText().toString().trim());
                action.setCommentId(comment_id);
                action.setCommenterId(commenter_id);
                EventBus.getDefault().post(action);
            }
        });
    }

    public View getView() {
        return view;
    }

    public ImageView getPicture() {
        return picture;
    }

    public TextView getCommenter() {
        return commenter;
    }

    public TextView getComment() {
        return comment;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getLikeCount() {
        return like_count;
    }
}
