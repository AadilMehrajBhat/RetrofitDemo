package com.example.aadilmehraj.ideamanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aadilmehraj.ideamanager.models.Idea;
import com.example.aadilmehraj.ideamanager.services.IdeaService;
import com.example.aadilmehraj.ideamanager.services.ServiceBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdeaListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_list);
        final Context context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IdeaCreateActivity.class);
                context.startActivity(intent);
            }
        });

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.idea_list);
        assert recyclerView != null;

        if (findViewById(R.id.idea_detail_container) != null) {
            mTwoPane = true;
        }

        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put("owner", "Jim");
        filterMap.put("count", "3");
        filterMap.put("status", "Working");

        IdeaService ideaService = ServiceBuilder.buildService(IdeaService.class);
        final Call<List<Idea>> ideasRequest;
//        ideasRequest = ideaService.getIdeas(filterMap);
        ideasRequest = ideaService.getIdeas(null, 20);

        findViewById(R.id.loading_bar).setVisibility(View.VISIBLE);
        ideasRequest.enqueue(new Callback<List<Idea>>() {
            @Override
            public void onResponse(Call<List<Idea>> call, Response<List<Idea>> response) {
                if (response.isSuccessful()) {
                    findViewById(R.id.loading_bar).setVisibility(View.GONE);
                    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(response.body()));
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Your session has expired!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to retrieve items", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Idea>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "A Connection error occurred!", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    Toast.makeText(context, "Failed to retrieve ideas", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //region Adapter Region
    public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Idea> mValues;

        public SimpleItemRecyclerViewAdapter(List<Idea> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.idea_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(Integer.toString(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(IdeaDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        IdeaDetailFragment fragment = new IdeaDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.idea_detail_container, fragment)
                            .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, IdeaDetailActivity.class);
                        intent.putExtra(IdeaDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Idea mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
//endregion
}
