package be.howest.nmct.android.kookhet;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import be.howest.nmct.android.kookhet.database.ReceptLoader;
import be.howest.nmct.android.kookhet.dummy.DummyContent;

// A simple {@link Fragment} subclass.
// Activities that contain this fragment must implement the {@link ReceptFragment.OnFragmentInteractionListener} interface to handle interaction events.
// Use the {@link ReceptFragment#newInstance} factory method to create an instance of this fragment.
public class ReceptFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NavigatieId = "NavigatieId";
    private static final String KEY_NavigatieId = "NavigatieId";
    private int mNavigatieId;

    private static final String ARG_CategorieNaam = "CategorieNaam";
    private static final String KEY_CategorieNaam = "CategorieNaam";
    private String mCategorieNaam;

    private static final String ARG_ReceptNaam = "ReceptNaam";
    private static final String KEY_ReceptNaam = "ReceptNaam";
    private String mReceptNaam;

    private int mAantalPersonen = 1;

    private OnFragmentInteractionListener mListener;

    private CursorAdapter mAdapter;

    private FragmentTabHost mTabHost;

    // Use this factory method to create a new instance of this fragment using the provided parameters.
    public static ReceptFragment newInstance(int NavigatieId, String CategorieNaam, String ReceptNaam) {
        ReceptFragment fragment = new ReceptFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NavigatieId, NavigatieId);
        args.putString(ARG_CategorieNaam, CategorieNaam);
        args.putString(ARG_ReceptNaam, ReceptNaam);
        fragment.setArguments(args);
        return fragment;
    }

    // Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public ReceptFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNavigatieId = savedInstanceState.getInt(KEY_NavigatieId);
            mCategorieNaam = savedInstanceState.getString(KEY_CategorieNaam);
            mReceptNaam = savedInstanceState.getString(KEY_ReceptNaam);
        }
        else {
            if (getArguments() != null) {
                mNavigatieId = getArguments().getInt(ARG_NavigatieId);
                mCategorieNaam = getArguments().getString(ARG_CategorieNaam);
                mReceptNaam = getArguments().getString(ARG_ReceptNaam);
            }
        }

        //String[] columns = new String[] { Contract.Recepten.Bereidingstijd };
        //int[] viewIds = new int[] { R.id.lblBereidingstijd };

        String[] columns = new String[] {  };
        int[] viewIds = new int[] {  };

        mAdapter = new ReceptAdapter(getActivity(), R.layout.recept, null, columns, viewIds, 0); //werkt
        //mAdapter = new ReceptAdapter(getActivity(), R.layout.recept, null, null , null, 0); //crash
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recept, container, false);

        // Set the adapter
        GridView grdRecept = (GridView)view.findViewById(R.id.grdRecept);
        grdRecept.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
//        grdRecept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Cursor cursor = (Cursor)mAdapter.getItem(position);
//                //...
//            }
//        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Receptfragment kan maar op 1 manier gestart worden:
        // - Vanuit receptenfragment, met een receptnaam. De titel is een custom waarde, nl. de naam van een recept.
        ((ReceptActivity) getActivity()).onSectionAttached(getArguments().getInt(ARG_NavigatieId), getArguments().getString(ARG_ReceptNaam));
        ((ReceptActivity) getActivity()).restoreActionBar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        return new ReceptLoader(getActivity(), mReceptNaam);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    // This interface must be implemented by activities that contain this fragment to allow an interaction in this fragment to be communicated to the activity and potentially other fragments contained in that activity.
    // See the Android Training lesson <a href="http://developer.android.com/training/basics/fragments/communicating.html">Communicating with Other Fragments</a> for more information.
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
                    mAantalPersonen = mAantalPersonen + 1;
                    viewholder.lblAantalPersonen.setText("Aantal personen: " + mAantalPersonen);
                    EnableDisableControls();
                }
            });

            viewholder.btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
            if (getActivity().findViewById(R.id.btnSub) != null)
            getActivity().findViewById(R.id.btnSub).setEnabled(false);
        } else {
            if (getActivity().findViewById(R.id.btnSub) != null)
            getActivity().findViewById(R.id.btnSub).setEnabled(true);
        }
    }
}
