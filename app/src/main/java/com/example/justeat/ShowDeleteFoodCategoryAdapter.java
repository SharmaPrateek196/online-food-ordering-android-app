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
import com.dto.FoodItemCategory;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShowDeleteFoodCategoryAdapter extends RecyclerView.Adapter<ShowDeleteFoodCategoryAdapter.MyViewHolder>
{
    MyViewHolder holder;
    ArrayList<FoodItemCategory> catlist;
    Context context;


    public ShowDeleteFoodCategoryAdapter(ArrayList<FoodItemCategory> catlist, Context context) {
        this.catlist = catlist;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.food_category_card,viewGroup,false);

        holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final int position = i;

        myViewHolder.txtFoodCat.setText(catlist.get(i).getCategoryName());

        myViewHolder.btnFoodCatDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int catid = catlist.get(position).getId();

                final ProgressDialog pd = new ProgressDialog(context);

                pd.setTitle("CONNECTING TO SERVER");
                pd.setMessage("Deleting Food Category");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                StringRequest req1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/DeleteFoodCategory.jsp?catid=" + catid,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    pd.dismiss();

                                    JSONObject obj = new JSONObject(response.trim());

                                    if(obj.getString("msg").equals("success")) {
                                        Toast.makeText(context, "The Food Category been deleted successfully !!!", Toast.LENGTH_SHORT).show();
                                        catlist.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else
                                        Toast.makeText(context, "The Food Category has not been deleted successfully !!!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Volley errror while deleting Food Category "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                req1.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(req1,"delete_food_category_request");
            }
        });
    }

    @Override
    public int getItemCount() {
        return catlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
            TextView txtFoodCat;
            Button btnFoodCatDelete;

        public MyViewHolder(View v)
        {
            super(v);
            txtFoodCat = v.findViewById(R.id.txtFoodCat);
            btnFoodCatDelete = v.findViewById(R.id.btnFoodCatDelete);
        }
    }

}
