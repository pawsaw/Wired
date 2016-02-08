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

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSItem;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

import java.util.ArrayList;
import java.util.List;

import de.psawicki.wired.news.domain.NewsCategory;
import de.psawicki.wired.news.domain.NewsEntry;
import de.psawicki.wired.news.domain.NewsEntryBuilder;

/**
 * Created by Pawel Sawicki on 07.02.16.
 */
public final class GetNewsListTask extends AsyncTask<NewsCategory, Void, ArrayList<NewsEntry>> {

    public interface IOnNewsListLoaded {
        void onNewsListLoaded(ArrayList<NewsEntry> newsEntries);
    }

    private final IOnNewsListLoaded onNewsListLoaded;

    public GetNewsListTask(IOnNewsListLoaded onNewsListLoaded) {
        this.onNewsListLoaded = onNewsListLoaded;
    }

    @Override
    protected ArrayList<NewsEntry> doInBackground(NewsCategory... newsCategories) {

        ArrayList<NewsEntry> newsEntries = new ArrayList<>();

        RSSReader rssReader = new RSSReader();

        for (NewsCategory newsCategory : newsCategories) {
            loadNewsInCategory(rssReader, newsCategory, newsEntries);
        }

        return newsEntries;
    }

    private void loadNewsInCategory(RSSReader rssReader, NewsCategory category, ArrayList<NewsEntry> newsEntries) {
        try {
            RSSFeed rssFeed = rssReader.load(category.getUrl());
            List<RSSItem> rssItems = rssFeed.getItems();
            NewsEntryBuilder newsEntryBuilder = new NewsEntryBuilder();
            for (RSSItem rssItem : rssItems) {
                NewsEntry newsEntry = newsEntryBuilder.reset()
                        .withCategory(category)
                        .withTitle(rssItem.getTitle())
                        .withDescription(rssItem.getDescription())
                        .withLink(rssItem.getLink().toString())
                        .withImageUrl(rssItem.getThumbnails().size() > 0 ? rssItem.getThumbnails().get(0).toString() : null)
                        .build();
                if (newsEntry.isValid()) {
                    newsEntries.add(newsEntry);
                }
            }

        } catch (RSSReaderException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<NewsEntry> newsEntries) {
        super.onPostExecute(newsEntries);
        onNewsListLoaded.onNewsListLoaded(newsEntries);
    }
}
