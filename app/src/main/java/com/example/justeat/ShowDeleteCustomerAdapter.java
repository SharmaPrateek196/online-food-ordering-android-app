package com.example.justeat;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dto.Customer;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShowDeleteCustomerAdapter extends RecyclerView.Adapter<ShowDeleteCustomerAdapter.MyViewHolder>
{
    MyViewHolder holder;
    ArrayList<Customer> clist;
    Context context;


    public ShowDeleteCustomerAdapter(ArrayList<Customer> clist, Context context) {
        this.clist = clist;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.show_delete_customer,viewGroup,false);

        holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final int position = i;

        myViewHolder.txtCustID.setText(clist.get(i).getId());

        myViewHolder.txtCustFname.setText(clist.get(i).getFirstName());

        myViewHolder.txtCustLname.setText(clist.get(i).getLastName());

        myViewHolder.txtCustMobile.setText(clist.get(i).getMobileNumber());

        myViewHolder.btnDeleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String custid = clist.get(position).getId();

                final ProgressDialog pd = new ProgressDialog(context);

                pd.setTitle("CONNECTING TO SERVER");
                pd.setMessage("Deleting Customers");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                StringRequest req1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/DeleteCustomer.jsp?custid=" + custid,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    pd.dismiss();

                                    JSONObject obj = new JSONObject(response.trim());

                                    if(obj.getString("msg").equals("success")) {
                                        Toast.makeText(context, "The Customer has been deleted successfully !!!", Toast.LENGTH_SHORT).show();
                                        clist.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else
                                        Toast.makeText(context, "The Customer has not been deleted successfully !!!", Toast.LENGTH_SHORT).show();
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

                                pd.dismiss();
                                Toast.makeText(context, "Volley errror while deleting customer "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                req1.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(req1,"delete_customer_request");
            }
        });
    }

    @Override
    public int getItemCount() {
        return clist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
            TextView txtCustID,txtCustMobile,txtCustFname,txtCustLname;
            Button btnDeleteCustomer;

        public MyViewHolder(View v)
        {
            super(v);

            txtCustID = v.findViewById(R.id.txtCustID);
            txtCustMobile = v.findViewById(R.id.txtCustMobile);
            txtCustFname = v.findViewById(R.id.txtCustFname);
            txtCustLname = v.findViewById(R.id.txtCustLname);
            btnDeleteCustomer = v.findViewById(R.id.btnDeleteCustomer);
        }
    }

}
