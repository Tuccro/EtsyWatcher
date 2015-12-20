package com.tuccro.etsywatcher.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.adapters.SearchListAdapter;

import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<Item> itemList;
    SearchListAdapter searchListAdapter;
    GridLayoutManager layoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;

    OnListEvents onListEvents;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onListEvents = (OnListEvents) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listSearch);
        recyclerView.setLayoutManager(layoutManager);

        initList();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }

    public void initList() {
        if (itemList != null && !itemList.isEmpty()) {

            searchListAdapter = new SearchListAdapter(getActivity(), itemList);
            recyclerView.setAdapter(searchListAdapter);
        }
    }

    public void fillList(List<Item> itemsList) {

        this.itemList = itemsList;
        initList();
    }

    public void setRefreshLayoutRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onRefresh() {

        setRefreshLayoutRefreshing(true);
        onListEvents.onListUpdate();
    }

    public interface OnListEvents {
        void onListUpdate();
    }
}
