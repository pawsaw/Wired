/*
 * Copyright (C) 2016 Pawel Sawicki
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

package de.psawicki.wired.news.domain;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import de.psawicki.wired.R;

/**
 * Created by Pawel Sawicki on 07.02.16.
 */
public enum NewsCategory {

    BUSINESS("Business", R.string.news_category_business, "http://www.wired.com/category/business/feed/"),
    DESIGN("Design", R.string.news_category_design, "http://www.wired.com/category/design/feed/"),
    TECH("Tech", R.string.news_category_tech, "http://www.wired.com/category/gear/feed/"),
    SCIENCE("Science", R.string.news_category_science, "http://www.wired.com/category/science/feed/")
    ;

    private String id;
    @StringRes
    private int labelRId;
    private String url;

    NewsCategory(String id, int labelRId, String url) {
        this.id = id;
        this.labelRId = labelRId;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public int getLabelRId() {
        return labelRId;
    }

    public String getUrl() {
        return url;
    }

    @Nullable
    public static NewsCategory byId(String id) {
        for (NewsCategory newsCategory : NewsCategory.values()) {
            if (newsCategory.getId().equals(id)) {
                return newsCategory;
            }
        }
        return null;
    }
}
