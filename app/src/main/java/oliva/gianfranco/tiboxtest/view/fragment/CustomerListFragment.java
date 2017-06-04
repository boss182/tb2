package oliva.gianfranco.tiboxtest.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import oliva.gianfranco.tiboxtest.R;
import oliva.gianfranco.tiboxtest.domain.model.Customer;
import oliva.gianfranco.tiboxtest.domain.usecase.CustomerUseCase;
import oliva.gianfranco.tiboxtest.view.adapter.CustomerListAdapter;


public class CustomerListFragment extends Fragment {

    private LinearLayoutManager mServicioLayoutManager;
    private RecyclerView mServicioRecyclerView;

    private CustomerListAdapter mAdapter;

    private OnFragmentCustomerListListener mListener;

    public CustomerListFragment() {
        // Required empty public constructor
    }

    public static CustomerListFragment newInstance() {
        CustomerListFragment fragment = new CustomerListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        iniView(view);
        return view;
    }

    private void iniView(View view) {

        mServicioLayoutManager = new LinearLayoutManager(getActivity());

        mServicioRecyclerView = (RecyclerView) view.findViewById(R.id.rv_servicios);
        mServicioRecyclerView.setHasFixedSize(true);
        mServicioRecyclerView.setLayoutManager(mServicioLayoutManager);

        List<Customer> customers = CustomerUseCase.listAllCustomer();
        mAdapter = new CustomerListAdapter(customers,getActivity());
        mServicioRecyclerView.setAdapter(mAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCustomerListUpdate();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCustomerListListener) {
            mListener = (OnFragmentCustomerListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentCustomerListListener {
        // TODO: Update argument type and name
        void onCustomerListUpdate();
    }

    public void refreshGrid(){

        mAdapter = new CustomerListAdapter(CustomerUseCase.listAllCustomer(), getActivity());
        mAdapter.notifyDataSetChanged();
    }
}
