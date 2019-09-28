package com.example.justeat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dto.Customer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApproveCustFragment extends Fragment {

    RecyclerView rcv;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<Customer> clist;
    String url =
            ServerAddress.MYSERVER+"/GetAllUnApprovedCustomers.jsp";

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.approve_cust_fragment,container,false);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData()
    {
        rcv = v.findViewById(R.id.recycle_approve_cust);

        clist = new ArrayList<>();

        mgr = new LinearLayoutManager(getActivity());

        rcv.setLayoutManager(mgr);

        final ProgressDialog pd =
                new ProgressDialog(getActivity());

        pd.setTitle("CONNECTING TO SERVER");
        pd.setMessage("Downloading Customers");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        StringRequest srq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("no"))
                        {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "No records to display", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {

                                pd.dismiss();
                                JSONArray arr = new JSONArray(response.trim());

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);

                                    Customer cust = new Customer();
                                    cust.setId(obj.getString("id"));
                                    cust.setFirstName(obj.getString("firstName"));
                                    cust.setLastName(obj.getString("lastName"));
                                    cust.setMobileNumber(obj.getString("mobileNumber"));

                                    clist.add(cust);
                                }

                                adapter = new ApproveCustomerAdapter(clist, getActivity());

                                rcv.setAdapter(adapter);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),
                                "Connection error : "+error.getMessage(),
                                Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }
                });

        srq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(srq,"approve_reject_request");
    }
}
