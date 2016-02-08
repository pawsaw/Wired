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

package de.psawicki.wired.news.tasks;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import de.psawicki.wired.news.domain.NewsEntry;

/**
 * Created by Pawel Sawicki on 07.02.16.
 */
public final class GetNewsEntryTask extends AsyncTask<NewsEntry, Void, String> {

    public interface IOnNewsEntryContentLoaded {
        void onNewsEntryContentLoaded(String newsEntryContent);
    }

    private final IOnNewsEntryContentLoaded onNewsEntryContentLoaded;

    public GetNewsEntryTask(IOnNewsEntryContentLoaded onNewsEntryContentLoaded) {
        this.onNewsEntryContentLoaded = onNewsEntryContentLoaded;
    }

    @Override
    protected String doInBackground(NewsEntry... newsEntries) {

        NewsEntry newsEntry = newsEntries[0];
        String article = newsEntry.getArticle();

        if (article == null) {
            try {
                Document doc = Jsoup.connect(newsEntries[0].getLink()).get();
                article = doc.select("article").html();
            } catch (IOException e) {
                e.printStackTrace();
                article = "Couldn't load article."; //TODO: notify the user in a nicer way
            }
            newsEntry.setArticle(article);
        }

        return article;

    }


    @Override
    protected void onPostExecute(String article) {
        super.onPostExecute(article);
        onNewsEntryContentLoaded.onNewsEntryContentLoaded(article);
    }
}
