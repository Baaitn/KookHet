package be.howest.nmct.android.kookhet;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import be.howest.nmct.android.kookhet.database.ReceptenLoader;

// A fragment representing a list of Items.
// Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
// Activities containing this fragment MUST implement the {@link Callbacks} interface.
public class ReceptenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnItemClickListener {

    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NavigatieId = "NavigatieId";
    private static final String KEY_NavigatieId = "NavigatieId";
    private int mNavigatieId;

    private static final String ARG_CategorieNaam = "CategorieNaam";
    private static final String KEY_CategorieNaam = "CategorieNaam";
    private String mCategorieNaam;

    private OnFragmentInteractionListener mListener;

    // The Adapter which will be used to populate the ListView/GridView with Views.
    //private ListAdapter mAdapter;
    private CursorAdapter mAdapter;

    // The fragment's ListView/GridView.
    private AbsListView mListView;

    // Use this factory method to create a new instance of this fragment using the provided parameters.
    public static ReceptenFragment newInstance(int NavigatieId, String CategorieNaam) {
        ReceptenFragment fragment = new ReceptenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NavigatieId, NavigatieId);
        args.putString(ARG_CategorieNaam, CategorieNaam);
        fragment.setArguments(args);
        return fragment;
    }

    // Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public ReceptenFragment() {}

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
        }
        else {
            if (getArguments() != null) {
                mNavigatieId = getArguments().getInt(ARG_NavigatieId);
                mCategorieNaam = getArguments().getString(ARG_CategorieNaam);
            }
        }

        String[] columns = new String[] { Contract.ReceptenColumns.Naam, Contract.ReceptenColumns.Bereidingstijd };
        int[] viewIds = new int[] { R.id.lblReceptnaam, R.id.lblBereidingstijd };

        //mAdapter = new ArrayAdapter<DummyContent.Recept>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.RECEPTEN);
        //mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, columns, viewIds, 0);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.row_recept, null, columns, viewIds , 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recepten, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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
        // Receptenfragment kan op 2 manieren gestart worden:
        // - Vanuit de navigationdrawer, zonder een categorienaam. De titel is hetzelfde als het aangeklikte item in de naviagtiondrawer.
        // - Vanuit categorieenfragment, met een categorienaam. De titel is een custom waarde, nl. de naam van een categorie.
        if (getArguments().getString(ARG_CategorieNaam) == null){
            ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(ARG_NavigatieId), null);
            ((MainActivity) getActivity()).restoreActionBar();
        } else {
            ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(ARG_NavigatieId), getArguments().getString(ARG_CategorieNaam));
            ((MainActivity) getActivity()).restoreActionBar();
        }
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
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ReceptenLoader(getActivity(), mCategorieNaam);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.RECEPTEN.get(position).id);

            Cursor cursor = (Cursor)mAdapter.getItem(position);

            // Bij klikken op recept, details van het recept ophalen
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, ReceptFragment.newInstance(
                    getArguments().getInt(ARG_NavigatieId),
                    getArguments().getString(ARG_CategorieNaam),
                    /*parent.getItemAtPosition(position).toString()*/
                    cursor.getString(cursor.getColumnIndex(Contract.ReceptenColumns.Naam))
            )).addToBackStack(null).commit();
        }
    }

    // This interface must be implemented by activities that contain this fragment to allow an interaction in this fragment to be communicated to the activity and potentially other fragments contained in that activity.
    // See the Android Training lesson <a href="http://developer.android.com/training/basics/fragments/communicating.html">Communicating with Other Fragments</a> for more information.
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    // The default content for this Fragment has a TextView that is shown when the list is empty. If you would like to change the text, call this method to supply the text it should use.
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();
        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }
}
