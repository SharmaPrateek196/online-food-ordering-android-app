package com.example.justeat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dto.Customer;

import org.json.JSONObject;

public class ApproveCustomerAdapter extends RecyclerView.Adapter<ApproveCustomerAdapter.MyViewHolder>
{
    MyViewHolder holder;
    ArrayList<Customer> clist;
    Context context;


    public ApproveCustomerAdapter(ArrayList<Customer> clist, Context context) {
        this.clist = clist;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.cust_approve_reject,viewGroup,false);

        holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final int position = i;

        myViewHolder.txtID.setText(clist.get(i).getId());

        myViewHolder.txtFname.setText(clist.get(i).getFirstName());

        myViewHolder.txtLname.setText(clist.get(i).getLastName());

        myViewHolder.txtMobile.setText(clist.get(i).getMobileNumber());

        myViewHolder.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String custid = clist.get(position).getId();

                StringRequest req1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/ApproveCustomer.jsp?custid=" + custid,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    JSONObject obj = new JSONObject(response.trim());


                                    if(obj.getString("msg").equals("success")) {
                                        Toast.makeText(context, "The Customer has been verified successfully !!!", Toast.LENGTH_SHORT).show();
                                        clist.remove(position);
                                        notifyDataSetChanged();
                                    }
                                        else
                                        Toast.makeText(context, "The Customer has not been verified successfully !!!", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(context, "Volley errror while approving customer "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                req1.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(req1,"approve_customer_request");
            }
        });

        myViewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String custid = clist.get(position).getId();

                StringRequest req1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/RejectCustomer.jsp?custid=" + custid,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    JSONObject obj = new JSONObject(response.trim());

                                    if(obj.getString("msg").equals("success")) {
                                        Toast.makeText(context, "The Customer has been rejected successfully !!!", Toast.LENGTH_SHORT).show();
                                        clist.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else
                                        Toast.makeText(context, "The Customer has not been rejected successfully !!!", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(context, "Volley errror while rejecting customer "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                req1.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(req1,"approve_customer_request");
            }
        });
    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtID,txtMobile,txtFname,txtLname;
        Button btnApprove,btnReject;

        public MyViewHolder(View v)
        {
            super(v);
            txtID = v.findViewById(R.id.txtCustID);
            txtMobile = v.findViewById(R.id.txtCustMobile);
            txtFname = v.findViewById(R.id.txtCustFname);
            txtLname = v.findViewById(R.id.txtCustLname);
            btnApprove = v.findViewById(R.id.btnApprove);
            btnReject = v.findViewById(R.id.btnReject);
        }
    }

}
