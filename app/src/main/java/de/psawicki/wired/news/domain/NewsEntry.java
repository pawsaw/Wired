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

import android.os.Parcelable;

import java.net.URI;
import java.net.URISyntaxException;

import auto.parcel.AutoParcel;

/**
 * Created by Pawel Sawicki on 07.02.16.
 */
@AutoParcel
public abstract class NewsEntry implements Parcelable {

    public abstract NewsCategory getCategory();

    public abstract String getTitle();
    public abstract String getDescription();
    public abstract String getImageUri();
    public abstract String getLink();

    private String article;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public boolean isValid() {

        if (!isValidString(getTitle()) || !isValidString(getDescription())) {
            return false;
        }

        try {
            new URI(getImageUri());
            new URI(getLink());
        } catch (URISyntaxException e) {
            return false;
        }
        return true;
    }

    private boolean isValidString(String str) {
        return str != null && str.length() > 0;
    }
}
