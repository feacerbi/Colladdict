package br.com.felipeacerbi.colladdict.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.felipeacerbi.colladdict.Constants;
import br.com.felipeacerbi.colladdict.R;
import br.com.felipeacerbi.colladdict.app.CollectionsApplication;
import br.com.felipeacerbi.colladdict.fragments.CategoriesFragment;
import br.com.felipeacerbi.colladdict.fragments.CollectionStorageFragment;
import br.com.felipeacerbi.colladdict.models.CollectionStorage;

public class Collections extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_NEW_COLLECTION_STORAGE && resultCode == Activity.RESULT_OK) {
            final CollectionStorageFragment fragment = ((CollectionStorageFragment) getSupportFragmentManager().findFragmentByTag("collections_fragment"));
            final CollectionStorage storage = (CollectionStorage) data.getExtras().getSerializable("collection_storage");
            fragment.getCollectionStoragesAdapter().notifyNewItemInserted(storage);
            Snackbar.make(
                    fragment.getView().findViewById(R.id.coordinator),
                    storage.getTitle() + " collection added",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_collections) {
            fragmentTransaction.replace(R.id.container, CollectionStorageFragment.newInstance(), "collections_fragment");
            setToolbarTitle("Collections");
        } else if (id == R.id.nav_categories) {
            fragmentTransaction.replace(R.id.container, CategoriesFragment.newInstance(), "categories_fragment");
            setToolbarTitle("Categories");
        } else if (id == R.id.nav_sett) {

        } else if (id == R.id.nav_search) {

        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
