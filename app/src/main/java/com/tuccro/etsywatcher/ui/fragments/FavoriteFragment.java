package com.tuccro.etsywatcher.ui.fragments;

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
import com.tuccro.etsywatcher.db.DBObject;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.adapters.FavoriteItemsListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Item> itemList;
    FavoriteItemsListAdapter favoriteItemsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listFavorite);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        initList();
        return rootView;
    }

    public void initList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DBObject dbObject = new DBObject(getActivity());
                List<Item> items = dbObject.getItemsListFromDB();
                Collections.reverse(items);
                updateUI(items);
            }
        }).start();
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        initList();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateUI(final List<Item> items) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemList = items;
                favoriteItemsListAdapter = new FavoriteItemsListAdapter(getActivity(), itemList);
                recyclerView.setAdapter(favoriteItemsListAdapter);
            }
        });
    }
}
