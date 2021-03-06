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

package com.goforer.fyber_challenge.model.action;

public class CommentLikeAction {
    private String mCommenter;
    private long mCommentId;
    private long mCommenterId;

    public String getCommenter() {
        return mCommenter;
    }

    public long getCommentId() {
        return mCommentId;
    }

    public long getCommenterId() {
        return mCommenterId;
    }

    public void setCommenter(String commenter) {
        mCommenter = commenter;
    }

    public void setCommentId(long id) {
        mCommentId = id;
    }

    public void setCommenterId(long id) {
        mCommenterId = id;
    }
}
