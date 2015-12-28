package com.tuccro.etsywatcher.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.adapters.SearchItemsListAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ITEM_LIST = "item list";

    RecyclerView recyclerView;
    List<Item> itemList;
    SearchItemsListAdapter searchItemsListAdapter;
    GridLayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;

    OnListEvents onListEvents;

    private boolean mIsLoading = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onListEvents = (OnListEvents) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listSearch);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setOnScrollListener(mRecyclerViewOnScrollListener);

        initList();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            itemList = (List<Item>) savedInstanceState.getSerializable(ITEM_LIST);
        }
        initList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ITEM_LIST, (Serializable) itemList);
    }

    public void initList() {

        if (itemList != null && !itemList.isEmpty()) {

            if (searchItemsListAdapter != null) {
                searchItemsListAdapter.notifyDataSetChanged();
            } else {
                searchItemsListAdapter = new SearchItemsListAdapter(getActivity(), itemList);
                recyclerView.setAdapter(searchItemsListAdapter);
            }
        }
    }

    public void fillList(List<Item> itemsList) {

        if (this.itemList == null) {
            this.itemList = itemsList;
        } else this.itemList.addAll(itemsList);
        initList();
    }

    public void clearList() {
        if (this.itemList != null) {
            this.itemList.clear();
            searchItemsListAdapter.notifyDataSetChanged();
        }
    }

    public void setRefreshLayoutRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
        mIsLoading = refreshing;
    }

    @Override
    public void onRefresh() {

        setRefreshLayoutRefreshing(true);
        onListEvents.onListUpdate();
    }

    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView,
                                                 int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                    if (!mIsLoading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0) {
                            onListEvents.loadMoreItems();
                        }
                    }
                }
            };

    public interface OnListEvents {
        void onListUpdate();

        void loadMoreItems();
    }
}
