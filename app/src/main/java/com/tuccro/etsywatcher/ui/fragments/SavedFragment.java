package com.tuccro.etsywatcher.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.db.DBObject;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.adapters.SavedListAdapter;

import java.util.List;

/**
 * Created by tuccro on 12/19/15.
 */
public class SavedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<Item> itemList;
    SavedListAdapter savedListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listSaved);

//        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        initList();
        return rootView;
    }

    public void initList() {
        DBObject dbObject = new DBObject(getActivity());
        itemList = dbObject.getItemsListFromDB();
        savedListAdapter = new SavedListAdapter(getActivity(), itemList);
        recyclerView.setAdapter(savedListAdapter);
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        initList();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
