package be.howest.nmct.android.kookhet;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import org.w3c.dom.Text;

import be.howest.nmct.android.kookhet.database.ReceptLoader;


public class ReceptActivity extends TabActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, LoaderManager.LoaderCallbacks<Cursor>{

    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_NavigatieId = "NavigatieId";
    private static final String KEY_NavigatieId = "NavigatieId";
    private int mNavigatieId;

    public static final String ARG_CategorieNaam = "CategorieNaam";
    private static final String KEY_CategorieNaam = "CategorieNaam";
    private String mCategorieNaam;

    public static final String ARG_ReceptNaam = "ReceptNaam";
    private static final String KEY_ReceptNaam = "ReceptNaam";
    private String mReceptNaam;

    private int mAantalPersonen = 1;

    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
        setContentView(R.layout.activity_recept);
        createTabs();
        getInfo(savedInstanceState);
    }

    private void getInfo(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mNavigatieId = savedInstanceState.getInt(KEY_NavigatieId);
            mCategorieNaam = savedInstanceState.getString(KEY_CategorieNaam);
            mReceptNaam = savedInstanceState.getString(KEY_ReceptNaam);
        }
        else {
                mNavigatieId = getIntent().getIntExtra(ARG_NavigatieId, 0);
                mCategorieNaam = getIntent().getStringExtra(ARG_CategorieNaam);
                mReceptNaam = getIntent().getStringExtra(ARG_ReceptNaam);

        }


    }

    private void createMAdapter() {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        TextView lblBereidingstijd = (TextView) findViewById(R.id.lblBereidingstijd);
        final TextView lblAantalPersonen = (TextView) findViewById(R.id.lblAantalPersonen);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnSub = (Button) findViewById(R.id.btnSub);

        //Cursor cursor = mAdapter.getCursor();

        lblBereidingstijd.setText("Bereidingstijd: " + cursor.getString(cursor.getColumnIndex(Contract.ReceptenColumns.Bereidingstijd)));
        lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Add", Toast.LENGTH_LONG).show();
                mAantalPersonen = mAantalPersonen + 1;
                lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);
                EnableDisableControls();
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "Sub", Toast.LENGTH_LONG).show();
                if (mAantalPersonen >=2){
                    mAantalPersonen = mAantalPersonen - 1;
                    lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);
                    EnableDisableControls();
                }

            }
        });
        btnSub.setEnabled(false);
        EnableDisableControls();





    }

    private void createTabs() {
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec tabIngrediënten = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tabBereiding = tabHost.newTabSpec("Second Tab");

        tabIngrediënten.setIndicator("Ingrediënten");
        tabBereiding.setIndicator("Bereiding");

        tabIngrediënten.setContent(new Intent(this, IngrediëntActivity.class));
        tabBereiding.setContent(new Intent(this, BereidingActivity.class));

        tabHost.addTab(tabIngrediënten);
        tabHost.addTab(tabBereiding);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NavigatieId, mNavigatieId);
        outState.putString(KEY_CategorieNaam, mCategorieNaam);
        outState.putString(KEY_ReceptNaam, mReceptNaam);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ReceptLoader(this, mReceptNaam);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //mAdapter.swapCursor(cursor);
        this.cursor = cursor;
        createMAdapter();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    class ReceptAdapter extends SimpleCursorAdapter {

        class ViewHolder {

            public ImageView image;
            public TextView lblBereidingstijd;
            public TextView lblAantalPersonen;
            public Button btnAdd;
            public Button btnSub;

            public ViewHolder(View v) {
                image = (ImageView) v.findViewById(R.id.imageView);
                lblBereidingstijd = (TextView) v.findViewById(R.id.lblBereidingstijd);
                lblAantalPersonen = (TextView) v.findViewById(R.id.lblAantalPersonen);
                btnAdd = (Button) v.findViewById(R.id.btnAdd);
                btnSub = (Button) v.findViewById(R.id.btnSub);
            }
        }

        private Context context;
        private int layout;

        public ReceptAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.context = context;
            this.layout = layout;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //final LayoutInflater inflater = LayoutInflater.from(context);
            //View view = inflater.inflate(layout, parent, false);

            View view = super.newView(context, cursor, parent);
            view.setTag(new ViewHolder(view));

            return view;
        }

        @Override
        public void bindView(View view, final Context context, Cursor cursor) {
            //String Bereidingstijd = cursor.getString(cursor.getColumnIndex(Contract.ReceptenColumns.Bereidingstijd));
            //TextView lblBereidingstijd = (TextView) view.findViewById(R.id.lblBereidingstijd);
            //if(Bereidingstijd != null) {lblBereidingstijd.setText("Bereidingstijd: " + Bereidingstijd);}

            final ViewHolder viewholder = (ViewHolder) view.getTag();
            viewholder.lblBereidingstijd.setText("Bereidingstijd: " + cursor.getString(cursor.getColumnIndex(Contract.ReceptenColumns.Bereidingstijd)));
            viewholder.lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);

            viewholder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "Add", Toast.LENGTH_LONG).show();
                    mAantalPersonen = mAantalPersonen + 1;
                    viewholder.lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);
                    EnableDisableControls();
                }
            });

            viewholder.btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "Sub", Toast.LENGTH_LONG).show();
                    if (mAantalPersonen >=2){
                        mAantalPersonen = mAantalPersonen - 1;
                        viewholder.lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);
                        EnableDisableControls();
                    }

                }
            });
            viewholder.btnSub.setEnabled(false);
            EnableDisableControls();

        }
    }

    private void EnableDisableControls() {
        //Toast.makeText(getActivity(), "EDC", Toast.LENGTH_LONG).show();
        if (mAantalPersonen <= 1) {
            if (findViewById(R.id.btnSub) != null)
                findViewById(R.id.btnSub).setEnabled(false);
        } else {
            if (findViewById(R.id.btnSub) != null)
                findViewById(R.id.btnSub).setEnabled(true);
        }
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
}
