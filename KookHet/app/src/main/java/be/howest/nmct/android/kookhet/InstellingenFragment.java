package be.howest.nmct.android.kookhet;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// A simple {@link Fragment} subclass.
// Activities that contain this fragment must implement the {@link InstellingenFragment.OnFragmentInteractionListener} interface to handle interaction events.
// Use the {@link InstellingenFragment#newInstance} factory method to create an instance of this fragment.
public class InstellingenFragment extends Fragment {

    // The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NavigatieId = "NavigatieId";

    private static final String KEY_NavigatieId = "NavigatieId";

    private int mNavigatieId;

    private OnFragmentInteractionListener mListener;

    // Use this factory method to create a new instance of this fragment using the provided parameters.
    public static InstellingenFragment newInstance(int NavigatieId) {
        InstellingenFragment fragment = new InstellingenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NavigatieId, NavigatieId);
        fragment.setArguments(args);
        return fragment;
    }

    // Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
    public InstellingenFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

//            // Instellingenfragment kan maar op 1 manier gestart worden:
//            // - Vanuit de navigationdrawer, met een waarde als id. De titel is hetzelfde als het aangeklikte item in de naviagtiondrawer.
//            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_NavigatieId), null);

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mNavigatieId = savedInstanceState.getInt(ARG_NavigatieId);
        }
        else {
            if (getArguments() != null) {
                mNavigatieId = getArguments().getInt(ARG_NavigatieId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instellingen, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Instellingenfragment kan maar op 1 manier gestart worden:
        // - Vanuit de navigationdrawer, met een waarde als id. De titel is hetzelfde als het aangeklikte item in de naviagtiondrawer.
        ((MainActivity) getActivity()).onSectionAttached(getArguments().getInt(ARG_NavigatieId), null);
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
