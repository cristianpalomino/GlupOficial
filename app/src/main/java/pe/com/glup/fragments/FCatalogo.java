package pe.com.glup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pe.com.glup.R;
import pe.com.glup.adapters.PrendaAdapter;
import pe.com.glup.beans.Prenda;
import pe.com.glup.datasource.DSCatalogo;
import pe.com.glup.decorations.MarginDecoration;
import pe.com.glup.interfaces.OnSuccessCatalogo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FCatalogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCatalogo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static FCatalogo newInstance(String param1, String param2) {
        FCatalogo fragment = new FCatalogo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FCatalogo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fcatalogo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        DSCatalogo dsCatalogo = new DSCatalogo(getActivity());
        dsCatalogo.sendRequest("todos", "1", "10");
        dsCatalogo.setOnSuccessCatalogo(new OnSuccessCatalogo() {
            @Override
            public void onSuccess(String success_msg, ArrayList<Prenda> prendas) {
                PrendaAdapter adapter = new PrendaAdapter(prendas);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailed(String error_msg) {

            }
        });
    }
}
