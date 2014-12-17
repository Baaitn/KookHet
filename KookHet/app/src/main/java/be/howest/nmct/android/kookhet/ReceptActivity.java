package be.howest.nmct.android.kookhet;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class ReceptActivity extends TabActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ReceptFragment.OnFragmentInteractionListener {

    private CharSequence mTitle;

    public static final String ARG_ReceptBereiding = "ReceptBereiding";
    private static final String KEY_ReceptBereiding = "ReceptBereiding";
    private String mReceptBereiding;

    public static final String ARG_NavigatieId = "NavigatieId";
    private static final String KEY_NavigatieId = "NavigatieId";
    private int mNavigatieId;

    public static final String ARG_CategorieNaam = "CategorieNaam";
    private static final String KEY_CategorieNaam = "CategorieNaam";
    private String mCategorieNaam;

    public static final String ARG_ReceptNaam = "ReceptNaam";
    private static final String KEY_ReceptNaam = "ReceptNaam";
    private String mReceptNaam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recept);
        getInfo(savedInstanceState);
        createTabs();
        setReceptFragment();
    }

    private void getInfo(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mNavigatieId = savedInstanceState.getInt(KEY_NavigatieId);
            mCategorieNaam = savedInstanceState.getString(KEY_CategorieNaam);
            mReceptNaam = savedInstanceState.getString(KEY_ReceptNaam);
            mReceptBereiding = savedInstanceState.getString(KEY_ReceptBereiding);
        }
        else {
            mNavigatieId = getIntent().getIntExtra(ARG_NavigatieId, 0);
            mCategorieNaam = getIntent().getStringExtra(ARG_CategorieNaam);
            mReceptNaam = getIntent().getStringExtra(ARG_ReceptNaam);
            mReceptBereiding = getIntent().getStringExtra(ARG_ReceptBereiding);
        }
    }

    private void createTabs() {
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tabIngrediënten = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tabBereiding = tabHost.newTabSpec("Second Tab");

        tabIngrediënten.setIndicator("Ingrediënten");
        tabBereiding.setIndicator("Bereiding");

        Intent IngredientIntent = new Intent(this, IngredientActivity.class);
        tabIngrediënten.setContent(IngredientIntent);

        Intent BereidingsIntent = new Intent(this, BereidingsActivity.class);
        BereidingsIntent.putExtra("BereidingsWijze","test");
        tabBereiding.setContent(BereidingsIntent);

        tabHost.addTab(tabIngrediënten);
        tabHost.addTab(tabBereiding);
    }

    private void setReceptFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.receptcontainer, ReceptFragment.newInstance(
                mNavigatieId,
                mCategorieNaam,
                    /*parent.getItemAtPosition(position).toString()*/
                mReceptNaam
        )).addToBackStack(null).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recept, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                fragmentManager.beginTransaction().replace(R.id.container, CategorieenFragment.newInstance(position + 1)).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, ReceptenFragment.newInstance(position + 1, null)).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, ReceptenFragment.newInstance(position + 1, null)).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.container, InstellingenFragment.newInstance(position + 1)).commit();
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void onSectionAttached(int number, String text) {
        if (text != null){
            mTitle = text;
        } else {
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
                case 4:
                    mTitle = getString(R.string.title_section4);
                    break;
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
