package mx.x10.filipebezerra.horariosrmtcgoiania.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.widgets.SnackBar;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import mx.x10.filipebezerra.horariosrmtcgoiania.R;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionEvent;
import mx.x10.filipebezerra.horariosrmtcgoiania.event.DrawerItemSelectionMessage;
import mx.x10.filipebezerra.horariosrmtcgoiania.network.RequestQueueManager;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.HorarioViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PlanejeViagemFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.PontoToPontoFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.fragment.SacFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.AbstractNavDrawerActivity;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment;
import mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.NavDrawerActivityConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_ITEM_PONTO_A_PONTO;
import static mx.x10.filipebezerra.horariosrmtcgoiania.ui.navdrawer.LeftDrawerFragment.ID_DRAWER_MENU_MENU_ITEM_SAC;

/**
 * @author Filipe Bezerra
 * @since 2.0
 */
public class HomeActivity extends AbstractNavDrawerActivity {

    public static final int SEARCH_RESULT_VIEW = ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM;
    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO : read Android doc for this method
        //setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(true);

        return true;
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
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        NavDrawerActivityConfiguration navDrawerConf = new NavDrawerActivityConfiguration();
        navDrawerConf.setDrawerLayoutId(R.id.drawer_container);
        navDrawerConf.setDrawerShadow(0);
        navDrawerConf.setDrawerOpenDesc(R.string.drawer_title_opened);
        navDrawerConf.setDrawerCloseDesc(R.string.drawer_title_closed);

        return navDrawerConf;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Subscribe
    public void onDrawerItemSelectionEvent(DrawerItemSelectionEvent event) {
        final int position = event.getMessage().getNavDrawerItem().getId();
        Fragment fragment = null;

        switch (position) {
            case ID_DRAWER_MENU_ITEM_FAVORITE_BUS_STOPS:
                openDrawer(Gravity.RIGHT);
                break;
            case ID_DRAWER_MENU_ITEM_HORARIOS_VIAGEM:
                fragment = HorarioViagemFragment.newInstance(event.getMessage().getParams());
                break;
            case ID_DRAWER_MENU_ITEM_PLANEJE_VIAGEM:
                fragment = new PlanejeViagemFragment();
                break;
            case ID_DRAWER_MENU_ITEM_PONTO_A_PONTO:
                fragment = new PontoToPontoFragment();
                break;
            case ID_DRAWER_MENU_MENU_ITEM_SAC:
                fragment = new SacFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            super.onDrawerItemSelectionEvent(event);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        } else {
            Log.d(LOG_TAG, String.format("No fragment found for NavItem %d selected!", position));
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            doSearch(intent);
        }
    }

    private void doSearch(Intent intent) {
        final String query = intent.getStringExtra(SearchManager.QUERY);

        if (TextUtils.isDigitsOnly(query)) {
            mSearchView.clearFocus();
            mSearchView.setQuery(query, false);

            String url = Uri.parse("http://m.rmtcgoiania.com.br/horariodeviagem/validar")
                    .buildUpon().appendQueryParameter("txtNumeroPonto", query)
                    .build().toString();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");

                                if ("sucesso".equals(status)) {
                                    Bundle arguments = new Bundle();
                                    arguments.putString(
                                            HorarioViagemFragment.ARG_PARAM_BUS_STOP_NUMBER, query);

                                    onDrawerItemSelectionEvent(new DrawerItemSelectionEvent(new DrawerItemSelectionMessage(
                                            LeftDrawerFragment.getDrawerItemHorariosViagem(), 
                                            arguments)));
                                } else {
                                    SnackBar snackbar = new SnackBar(HomeActivity.this,
                                            response.getString("mensagem"), null, null);
                                    snackbar.show();
                                }
                            } catch (JSONException e) {
                                Log.e(LOG_TAG, String.format("Erro no parsing de %s",
                                        response.toString()), e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(LOG_TAG, String.format("Erro na requisição de %s", query),
                                    error);
                            SnackBar snackbar = new SnackBar(HomeActivity.this,
                                    "Não foi possível fazer a busca. Por favor, Tente novamente!",
                                    null, null);
                            snackbar.show();
                        }
                    }
            );

            RequestQueueManager.getInstance(HomeActivity.this).addToRequestQueue(request, LOG_TAG);
        } else {
            SnackBar snackbar = new SnackBar(HomeActivity.this,
                    getString(R.string.non_digit_voice_search), null, null);
            snackbar.show();
        }
    }

}