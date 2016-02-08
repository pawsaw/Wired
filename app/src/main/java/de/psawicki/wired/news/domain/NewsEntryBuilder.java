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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pawel Sawicki on 07.02.16.
 */
public class NewsEntryBuilder {

    private static final String DEFAULT_IMAGE_URI = "http://www.wired.com/wp-content/uploads/2016/01/AI_Bulb-200x200.jpg";

    private static final Pattern IMAGE_PATTERN = Pattern.compile(".*<img[^>]*src=\"([^\"]*)",Pattern.CASE_INSENSITIVE);

    private NewsCategory category = null;
    private String title = "";
    private String description = "";
    private String imageUrl = "";
    private String link = "";
    
    public NewsEntryBuilder reset() {
        NewsCategory category = null;
        title = "";
        description = "";
        imageUrl = "";
        link = "";
        return this;
    }

    public NewsEntryBuilder withCategory(NewsCategory category) {
        this.category = category;
        return this;
    }

    public NewsEntryBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public NewsEntryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public NewsEntryBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public NewsEntryBuilder withLink(String link) {
        this.link = link;
        return this;
    }

    public NewsEntry build() {
        // for now we use a simple default string strategy

        title = defaultString(title);
        description = defaultString(description);

        if (imageUrl == null) {
            // in case of no thumbnail, we'd try to find an image in the description text
            imageUrl = findImageUri(description);
        }

        imageUrl = defaultString(imageUrl, DEFAULT_IMAGE_URI);
        link = defaultString(link);

        return new AutoParcel_NewsEntry(category, defaultString(title), defaultString(description), defaultString(imageUrl), defaultString(link));
    }

    private String defaultString(String str) {
        return str != null ? str : "";
    }

    private String defaultString(String str, String defaultStr) {
        return str != null ? str : defaultStr;
    }

    private String findImageUri(String text) {
        Matcher matcher = IMAGE_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

}
