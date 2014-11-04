package mx.x10.filipebezerra.horariosrmtcgoiania.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.johnpersano.supertoasts.SuperCardToast;

import java.util.ArrayList;
import java.util.List;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.adapter.DrawerAdapter;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerHeader;
import mx.x10.filipebezerra.horariosrmtcgoiania.model.widget.DrawerItem;
import mx.x10.filipebezerra.horariosrmtcgoiania.provider.SuggestionsProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.ToastHelper;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.BaseWebViewFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.view.fragment.SacFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends BaseActivity {

    public static final int DEFAULT_MENU_ITEM = 0;
    private SearchView searchView;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private ListView mDrawerList;

    private DrawerAdapter mDrawerAdapter;

    private List<DrawerItem> mDrawerItems = new ArrayList<>();

    private String[] urls;
    private String[] drawerMenuTitles;
    private TypedArray drawerMenuIcons;

    private int mActiveMenuItem = DEFAULT_MENU_ITEM;

    private String query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_menu_white_24dp);
        loadResoures();

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        SuperCardToast.onRestoreState(savedInstanceState, this);

        handleIntent(getIntent());

        setupDrawer();
        if (savedInstanceState == null) {
            displayView(DEFAULT_MENU_ITEM);
            mDrawerLayout.openDrawer(mDrawerList);
            mDrawerToggle.onDrawerOpened(mDrawerList);
        }
    }

    private void loadResoures() {
        drawerMenuTitles = getResources().getStringArray(R.array.drawer_menu_row_title);
        drawerMenuIcons = getResources().obtainTypedArray(R.array.drawer_menu_row_icon);
        urls = getResources().getStringArray(R.array.drawer_menu_row_url);
    }

    private void setupDrawer() {
        mDrawerList = (ListView) findViewById(R.id.list_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerAdapter = new DrawerAdapter(this);

        mDrawerAdapter.addSectionHeaderItem(new DrawerHeader(getString(R.string.drawer_header_personalizacao)));
        mDrawerAdapter.addItem(new DrawerItem(getString(R.string.drawer_item_personalizacao_pontos_favoritados),
                R.drawable.ic_favorite_24px));

        mDrawerAdapter.addSectionHeaderItem(new DrawerHeader(getString(R.string.drawer_header_servicos_rmtc)));

        for (int i = 0; i < drawerMenuTitles.length; i++) {
            mDrawerAdapter.addItem(new DrawerItem(drawerMenuTitles[i],
                    drawerMenuIcons.getResourceId(i, -1)));
        }
        drawerMenuIcons.recycle();

        mDrawerAdapter.addSectionHeaderItem(new DrawerHeader(getString(R.string.drawer_header_outros)));

        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_title_opened, R.string.drawer_title_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setSubtitle(drawerMenuTitles[mActiveMenuItem]);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void displayView(int position) {
        Fragment fragment = null;

        Bundle args = new Bundle();
        if (query == null) {
            args.putString(BaseWebViewFragment.ARG_PARAM_URL_TO_LOAD, urls[position]);
        } else {
            args.putInt(BaseWebViewFragment.ARG_PARAM_POINT_TO_LOAD, Integer.parseInt(query));
        }

        switch (position) {
            case 0:
                fragment = new HorarioViagemFragment(args);
                break;
            case 1:
                fragment = new PlanejeViagemFragment(args);
                break;
            case 2:
                fragment = new PontoToPontoFragment(args);
                break;
            case 3:
                fragment = new SacFragment(args);
                break;
            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
            mActiveMenuItem = position;
        } else {
            // TODO samethins is wrong
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            displayView(position);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH == intent.getAction()) {
            doSearch(intent);
        }
    }

    private void doSearch(Intent intent) {
        query = intent.getStringExtra(SearchManager.QUERY);

        if (TextUtils.isDigitsOnly(query)) {
            searchView.clearFocus();
            searchView.setQuery(query, false);

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            displayView(0);
        } else {
            new ToastHelper(this).showGeneralAlert(getResources()
                    .getString(R.string.non_digit_voice_search));
        }

        query = null;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectionReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionReceiver);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START|Gravity.LEFT)){
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.openDrawer(mDrawerList);
            } else {
                mDrawerLayout.closeDrawer(mDrawerList);
            }

            return true;
        }
        return super.onKeyDown(keyCode, e);
    }

}