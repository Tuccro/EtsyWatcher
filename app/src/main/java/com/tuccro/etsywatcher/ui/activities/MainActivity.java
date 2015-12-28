package com.tuccro.etsywatcher.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Spinner;

import com.tuccro.etsywatcher.R;
import com.tuccro.etsywatcher.loaders.CategoriesLoader;
import com.tuccro.etsywatcher.loaders.ImagesUrlsLoader;
import com.tuccro.etsywatcher.loaders.ItemsLoader;
import com.tuccro.etsywatcher.model.Category;
import com.tuccro.etsywatcher.model.Item;
import com.tuccro.etsywatcher.ui.adapters.CategoriesAdapter;
import com.tuccro.etsywatcher.ui.fragments.FavoriteFragment;
import com.tuccro.etsywatcher.ui.fragments.SearchFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Object>
        , SearchFragment.OnListEvents {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static final int CATEGORIES_LOADER_ID = 1;
    private static final int ITEMS_LOADER_ID = 2;
    private static final int IMAGES_LOADER_ID = 3;

    Spinner spinnerCategories;
    SearchView searchView;

    Loader itemsLoader;

    List<Item> itemsList;

    SearchFragment searchFragment;
    FavoriteFragment favoriteFragment;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            searchFragment = (SearchFragment) getSupportFragmentManager().getFragment(
                    savedInstanceState, "current_search");
        }

        initFragments();
    }

    private void initFragments() {

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        favoriteFragment = new FavoriteFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try {
            //Save the fragment's instance
            getSupportFragmentManager().putFragment(outState, "current_search", searchFragment);
        } catch (Exception e) {

        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(CATEGORIES_LOADER_ID, null, MainActivity.this);
        Loader categoriesLoader = getSupportLoaderManager().getLoader(CATEGORIES_LOADER_ID);

        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);

        if (categoriesLoader != null) {
            categoriesLoader.forceLoad();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setEnabled(false);
        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Loader loader;

        if (id == CATEGORIES_LOADER_ID) {
            loader = new CategoriesLoader(MainActivity.this);
        } else if (id == ITEMS_LOADER_ID) loader = new ItemsLoader(MainActivity.this, args);
        else loader = new ImagesUrlsLoader(MainActivity.this, itemsList);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        List<Item> items;

        switch (loader.getId()) {

            case CATEGORIES_LOADER_ID:

                List<Category> categories = (List<Category>) data;

                CategoriesAdapter adapter = new CategoriesAdapter(this
                        , android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerCategories.setAdapter(adapter);
                if (spinnerCategories.getCount() > 0 && searchView != null)
                    searchView.setEnabled(true);
                break;

            case ITEMS_LOADER_ID:

                items = (List<Item>) data;
                this.itemsList = items;

                if (!itemsList.isEmpty()) {
                    getSupportLoaderManager().initLoader(IMAGES_LOADER_ID, null, MainActivity.this);
                    Loader imagesUrlsLoader = getSupportLoaderManager().getLoader(IMAGES_LOADER_ID);

                    if (imagesUrlsLoader != null) {
                        imagesUrlsLoader.forceLoad();
                    }
                } else searchFragment.setRefreshLayoutRefreshing(false);

                break;

            case IMAGES_LOADER_ID:

                items = (List<Item>) data;
                this.itemsList = items;
                getSupportLoaderManager().destroyLoader(IMAGES_LOADER_ID);

                searchFragment.fillList(itemsList);
                searchFragment.setRefreshLayoutRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onListUpdate() {
        searchFragment.setRefreshLayoutRefreshing(false);
    }

    @Override
    public void loadMoreItems() {

        if (itemsLoader != null) {
            itemsLoader.forceLoad();
            searchFragment.setRefreshLayoutRefreshing(true);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) return searchFragment;
            else return favoriteFragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.search_for_goods);
                case 1:
                    return getString(R.string.saved_goods);
            }
            return null;
        }
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            Bundle bundle = new Bundle();
            bundle.putString(ItemsLoader.ARG_QUERY, query);

            try {
                bundle.putString(ItemsLoader.ARG_CATEGORY
                        , ((Category) spinnerCategories.getSelectedItem()).getName());
            } catch (NullPointerException e) {
                return true;
            }

            getSupportLoaderManager().destroyLoader(ITEMS_LOADER_ID);
            getSupportLoaderManager().initLoader(ITEMS_LOADER_ID, bundle, MainActivity.this);
            itemsLoader = getSupportLoaderManager().getLoader(ITEMS_LOADER_ID);

            if (itemsLoader != null) {
                itemsLoader.forceLoad();
                searchFragment.setRefreshLayoutRefreshing(true);
                searchFragment.clearList();
            }

            mViewPager.setCurrentItem(0);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}
