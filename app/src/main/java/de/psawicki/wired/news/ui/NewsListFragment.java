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

package de.psawicki.wired.news.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.psawicki.wired.R;
import de.psawicki.wired.news.domain.NewsCategory;
import de.psawicki.wired.news.domain.NewsEntry;
import de.psawicki.wired.news.tasks.GetNewsListTask;

public class NewsListFragment extends Fragment {

    private static final String ARGUMENT_NEWS_CATEGORY = "newsCategory";

    private NewsEntryRecyclerViewAdapter newsEntryRecyclerViewAdapter = null;


    public NewsCategory getNewsCategory() {
        return NewsCategory.byId(getArguments().getString(ARGUMENT_NEWS_CATEGORY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_news_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadNewsEntries(getActivity());
    }

    public void reloadNewsEntries(final Context context) {
        GetNewsListTask getNewsListTask = new GetNewsListTask(new GetNewsListTask.IOnNewsListLoaded() {
            @Override
            public void onNewsListLoaded(ArrayList<NewsEntry> newsEntries) {
                getNewsEntryRecyclerViewAdapter(context).notifyNewsEntriesChanged(newsEntries);
            }
        });

        getNewsListTask.execute(getNewsCategory());
    }

    private NewsEntryRecyclerViewAdapter getNewsEntryRecyclerViewAdapter(Context context) {

        if (newsEntryRecyclerViewAdapter == null) {
            newsEntryRecyclerViewAdapter = new NewsEntryRecyclerViewAdapter(context, new ArrayList<NewsEntry>());
        }

        return newsEntryRecyclerViewAdapter;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(getNewsEntryRecyclerViewAdapter(getActivity()));
    }

    public static NewsListFragment createNewsListFragment(NewsCategory newsCategory) {
        NewsListFragment newsListFragment = new NewsListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_NEWS_CATEGORY, newsCategory.getId());
        newsListFragment.setArguments(arguments);
        return newsListFragment;
    }

}
