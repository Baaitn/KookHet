package be.howest.nmct.android.kookhet;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// A simple {@link Fragment} subclass.
// Activities that contain this fragment must implement the {@link ReceptFragment.OnFragmentInteractionListener} interface to handle interaction events.
// Use the {@link ReceptFragment#newInstance} factory method to create an instance of this fragment.
public class ReceptFragment extends Fragment {

    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NavigatieId = "NavigatieId";
    private static final String ARG_CategorieNaam = "CategorieNaam";
    private static final String ARG_ReceptNaam = "ReceptNaam";

    private static final String KEY_NavigatieId = "NavigatieId";
    private static final String KEY_CategorieNaam = "CategorieNaam";
    private static final String KEY_ReceptNaam = "ReceptNaam";

    private int mNavigatieId;
    private String mCategorieNaam;
    private String mReceptNaam;

    private OnFragmentInteractionListener mListener;

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

//            // Receptfragment kan maar op 1 manier gestart worden:
//            // - Vanuit receptenfragment, met een receptnaam. De titel is een custom waarde, nl. de naam van een recept.
//            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_NavigatieId), getArguments().getString(ARG_ReceptNaam));
//            ((MainActivity) activity).restoreActionBar();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recept, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Receptfragment kan maar op 1 manier gestart worden:
        // - Vanuit receptenfragment, met een receptnaam. De titel is een custom waarde, nl. de naam van een recept.
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(ARG_NavigatieId), getArguments().getString(ARG_ReceptNaam));
        ((MainActivity) getActivity()).restoreActionBar();
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

}
