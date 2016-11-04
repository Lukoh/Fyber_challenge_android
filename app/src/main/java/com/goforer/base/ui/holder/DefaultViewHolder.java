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

package com.goforer.base.ui.holder;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * The default ViewHolder
 */
public class DefaultViewHolder extends BaseViewHolder<Object> {
    public DefaultViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindItemHolder(@NonNull Object obj, int position) {
    }

    @Override
    public void onItemSelected() {
    }

    @Override
    public void onItemClear() {
    }
}
