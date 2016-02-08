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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;

import de.psawicki.wired.R;
import de.psawicki.wired.news.domain.NewsEntry;
import de.psawicki.wired.news.tasks.GetNewsEntryTask;

public class NewsDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "news_entry";

    private TextView articleTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final NewsEntry newsEntry = intent.getParcelableExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(newsEntry.getTitle());

        final TextView descriptionTextView = (TextView) findViewById(R.id.textView_description);
        descriptionTextView.setText(Jsoup.parse(newsEntry.getDescription()).text());

        loadBackdrop(newsEntry);

        articleTextView = (TextView) findViewById(R.id.textView_article);

        final FloatingActionButton gotoFab = (FloatingActionButton) findViewById(R.id.fab_goto);
        gotoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoBrowserIntent = new Intent(Intent.ACTION_VIEW);
                gotoBrowserIntent.setData(Uri.parse(newsEntry.getLink()));
                startActivity(gotoBrowserIntent);
            }
        });

        loadArticle(newsEntry);
    }

    private void loadBackdrop(final NewsEntry newsEntry) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(newsEntry.getImageUri()).centerCrop().into(imageView);
    }

    private void loadArticle(final NewsEntry newsEntry) {
        GetNewsEntryTask getNewsEntryTask = new GetNewsEntryTask(new GetNewsEntryTask.IOnNewsEntryContentLoaded() {
            @Override
            public void onNewsEntryContentLoaded(String newsEntryContent) {
                articleTextView.setText(Html.fromHtml(newsEntryContent));
            }
        });

        getNewsEntryTask.execute(newsEntry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
}
