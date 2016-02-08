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
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.psawicki.wired.R;
import de.psawicki.wired.news.domain.NewsEntry;

/**
 * Created by Pawel Sawicki on 07.02.16.
 */
public class NewsEntryRecyclerViewAdapter extends RecyclerView.Adapter<NewsEntryRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ArrayList<NewsEntry> newsEntries;



    public NewsEntry getValueAt(int position) {
        return newsEntries.get(position);
    }

    public NewsEntryRecyclerViewAdapter(Context context, ArrayList<NewsEntry> newsEntries) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.newsEntries = newsEntries;
    }

    public void notifyNewsEntriesChanged(ArrayList<NewsEntry> newsEntries) {
        this.newsEntries = newsEntries;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.newsEntry = newsEntries.get(position);
        holder.mTextView.setText(holder.newsEntry.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.EXTRA_NAME, holder.newsEntry);

                context.startActivity(intent);
            }
        });

        Glide.with(holder.mImageView.getContext())
                .load(holder.newsEntry.getImageUri())
                .fitCenter()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return newsEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NewsEntry newsEntry;

        public final View mView;
        public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }
}