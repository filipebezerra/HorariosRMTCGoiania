package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.app.ApplicationSingleton;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.EventBusProvider;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.FavoritesListFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.WapFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.NetworkUtils;
import mx.x10.filipebezerra.horariosrmtcgoiania.util.SnackBarHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.util.LogUtils.LOGE;

/**
 * Activity base containing based implementation of Navigation Drawer and all application base behavior.
 *
 * @author Filipe Bezerra
 * @version 2.0, 08/03/2015
 * @since #
 */
public abstract class BaseActivity extends MaterialNavigationDrawer {

    private static final String LOG_TAG = BaseActivity.class.getSimpleName();
    /**
     * Search handled over all application.
     */
    protected SearchView mSearchView;

    /**
     * Search menu item reference.
     *
     * @see #onCreateOptionsMenu
     */
    protected MenuItem mSearchMenuItem;

    /**
     * Broadcast Receiver to detect and notify internet connection issues.
     */
    protected BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isConnectingToInternet(context)) {
                SnackBarHelper.show(BaseActivity.this, getString(R.string.no_internet_connectivity));
            }
        }
    };

    /**
     * The delegation method that initializes the activity. Don't use activity's onCreate method.
     */
    @Override
    public void init(final Bundle savedInstanceState) {
        setUpHeader();
        addPrimarySections();
        addDivisor();
        addSecondarySections();
        addBottomSections();
    }

    /**
     *  Setup the drawer header such as the image if drawerType is DRAWERHEADER_IMAGE or
     *  {@link it.neokree.materialnavigationdrawer.elements.MaterialAccount} if drawerType is
     *  DRAWERHEADER_ACCOUNTS.
     */
    private void setUpHeader() {
        setDrawerHeaderImage(R.drawable.drawer_header);
        addAccountSections();
    }

    /**
     * Setup the user account
     */
    private void addAccountSections() {
        // TODO : reserved for future implementation
    }

    /**
     * Dynamical sections, according with user preferences
     */
    @SuppressWarnings("unchecked")
    private void addPrimarySections() {
        addSection(newSection(getString(R.string.navdrawer_menu_item_favorite_bus_stops),
                R.drawable.ic_drawer_pontos_favoritos, new FavoritesListFragment())
                .setNotifications(getFavoriteCount())
                .setSectionColor(
                        getColor(R.color.navdrawer_favorites_section_color),
                        getColor(R.color.navdrawer_favorites_section_dark_color)));
    }

    /**
     * Convenient method for return a color integer from the application's package's
     * default color table.
     *
     * @param resId The desired resource identifier, must be a color identifier.
     * @return Resource id for the color
     */
    public final int getColor(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    /**
     * Statical sections, containing predefined sections
     */
    @SuppressWarnings("unchecked")
    private void addSecondarySections() {
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_wap),
                R.drawable.ic_drawer_wap, new WapFragment())
                .setSectionColor(
                        getColor(R.color.navdrawer_wap_section_color),
                        getColor(R.color.navdrawer_wap_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_horarios_viagem),
                R.drawable.ic_drawer_horario_viagem, new HorarioViagemFragment())
                .setSectionColor(
                        getColor(R.color.navdrawer_horario_viagem_section_color),
                        getColor(R.color.navdrawer_horario_viagem_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_planeje_viagem),
                R.drawable.ic_drawer_planeje_sua_viagem, new PlanejeViagemFragment())
                .setSectionColor(
                        getColor(R.color.navdrawer_planeje_sua_viagem_section_color),
                        getColor(R.color.navdrawer_planeje_sua_viagem_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_ponto_a_ponto),
                R.drawable.ic_drawer_ponto_a_ponto, new PontoToPontoFragment())
                .setSectionColor(
                        getColor(R.color.navdrawer_ponto_a_ponto_section_color),
                        getColor(R.color.navdrawer_ponto_a_ponto_section_dark_color)));
        addSection(newSection(getString(R.string.navdrawer_menu_item_rmtc_sac),
                R.drawable.ic_drawer_sac, new SacFragment())
                .setSectionColor(
                        getColor(R.color.navdrawer_sac_section_color),
                        getColor(R.color.navdrawer_sac_section_dark_color)));
    }

    /**
     * Statical sections, containing specific app definitions and help content
     */
    @SuppressWarnings("unchecked")
    private void addBottomSections() {
        // TODO : Reserved for future implementation - issue #66
        /*
        addBottomSection(newSection(getString(R.string.navdrawer_fixed_menu_item_help),
                R.drawable.ic_drawer_help, new SacFragment())
                .setSectionColor(
                        getColor(R.color.navdrawer_help_section_color),
                        getColor(R.color.navdrawer_help_section_dark_color)));
                */

        addBottomSection(newSection(getString(R.string.navdrawer_fixed_menu_item_configurations),
                R.drawable.ic_drawer_settings, new Intent(BaseActivity.this, SettingsActivity.class))
                .setSectionColor(
                        getColor(R.color.navdrawer_settings_section_color),
                        getColor(R.color.navdrawer_settings_section_dark_color)));
    }

    private int getFavoriteCount() {
        return (int) ApplicationSingleton.getInstance().getDaoSession()
                .getFavoriteBusStopDao().count();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusProvider.getInstance().getEventBus().register(BaseActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mConnectionReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));

        // TODO : this is the callback handle search (comes from GlobalSearch configuration)
        handleSearchQuery(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionReceiver);
        EventBusProvider.getInstance().getEventBus().unregister(BaseActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearchQuery(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchMenuItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                startActivity(Intent.createChooser(createShareIntent(),
                        getString(R.string.share_dialog_title)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Creates the share intent.
     *
     * @return share intent
     */
    @SuppressWarnings("deprecation")
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
        return shareIntent;
    }

    /**
     * Inject custom font into {@link Context}.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Handles search hardware button. Compatibility for old Android devices.
     */
    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            if (MenuItemCompat.expandActionView(mSearchMenuItem)) {
                mSearchView.requestFocus();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * Handles the menu hardware button to opening the Navigation Drawer.
     */
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (isDrawerOpen()) {
                closeDrawer();
            } else {
                openDrawer();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Search helper method.
     *
     * @param intent search intent
     */
    public void handleSearchQuery(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            onSearch(intent);
        }
    }

    /**
     * Handles and dispatch the search query intent received from the {@link SearchView}.
     *
     * @param intent search intent
     */
    private void onSearch(Intent intent) {
        final String query = intent.getStringExtra(SearchManager.QUERY);

        if (TextUtils.isDigitsOnly(query)) {
            mSearchView.clearFocus();
            mSearchView.setQuery(query, false);

            String url = Uri.parse(getString(R.string.url_validate_rmtc_horarios_viagem))
                    .buildUpon()
                    .appendQueryParameter(
                            getString(R.string.query_param_validate_rmtc_horarios_viagem), query)
                    .build().toString();

            // TODO : Network request here, Show ProgressDialog while requesting...
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString(getString(
                                        R.string.json_attr_status_validate_rmtc_horarios_viagem));

                                if (getString(R.string.json_attr_success_validate_rmtc_horarios_viagem)
                                        .equals(status)) {

                                    searchStopCode(query);

                                } else {

                                    SnackBarHelper.show(BaseActivity.this, response.getString(
                                            getString(R.string
                                                    .json_attr_message_validate_rmtc_horarios_viagem)));

                                }
                            } catch (JSONException e) {
                                LOGE(LOG_TAG, String.format(
                                                getString(R.string.log_error_network_request),
                                                e.getClass().toString(), "onResponse", "JSONObject",
                                                query),
                                        e);

                                LOGE(LOG_TAG, String.format("Error parsing %s",
                                        response.toString()), e);
                                SnackBarHelper.show(BaseActivity.this,
                                        getString(R.string.error_in_network_search_request));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            LOGE(LOG_TAG, String.format(
                                    getString(R.string.log_error_network_request),
                                    error.getClass().toString(), "onErrorResponse", "JSONObject",
                                    query), error);
                            SnackBarHelper.show(BaseActivity.this,
                                    getString(R.string.error_in_network_search_request));
                        }
                    }
            );

            RequestQueueManager.getInstance(BaseActivity.this).addToRequestQueue(request, LOG_TAG);
        } else {
            SnackBarHelper.show(BaseActivity.this, getString(R.string.non_digit_voice_search));
        }
    }

    @SuppressWarnings("unchecked")
    public void searchStopCode(final String stopCode) {
        MaterialSection horarioViagemSection = getSectionByTitle(getString(
                R.string.navdrawer_menu_item_rmtc_horarios_viagem));

        // TODO : This method is being invoked twice. Prevent this
        getCurrentSection().unSelect();
        setFragment(HorarioViagemFragment.newInstance(stopCode),
                horarioViagemSection.getTitle());

        // TODO : After programatically selecting, when section Pontos Favoritos selected, this remains selected
        horarioViagemSection.select();
        changeToolbarColor(horarioViagemSection);
    }
}
