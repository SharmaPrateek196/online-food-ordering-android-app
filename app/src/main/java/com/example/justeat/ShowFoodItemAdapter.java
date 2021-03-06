package com.example.justeat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.dto.Customer;
import com.dto.FoodItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShowFoodItemAdapter extends RecyclerView.Adapter<ShowFoodItemAdapter.MyViewHolder>
{
    MyViewHolder holder;
    ArrayList<FoodItem> flist;
    Context context;


    public ShowFoodItemAdapter(ArrayList<FoodItem> flist, Context context) {
        this.flist = flist;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.food_item_card,viewGroup,false);

        holder = new MyViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final int position = i;

        myViewHolder.tvFoodName.setText(flist.get(i).getItemName());

        myViewHolder.tvFoodCat.setText(flist.get(i).getCategoryID()+"");

        myViewHolder.tvFoodPrice.setText(flist.get(i).getItemPrice()+"");

        Glide.with(context)
                .load(flist.get(i).getImg_path())
                .into(myViewHolder.ivFoodItem);

        myViewHolder.btnFoodUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int foodid = flist.get(position).getId();

                Intent in = new Intent(context,UpdateFoodItem.class);
                in.putExtra("foodid",foodid);
                context.startActivity(in);
            }
        });

        myViewHolder.btnFoodDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int foodid = flist.get(position).getId();

                final ProgressDialog pd = new ProgressDialog(context);
                pd.setTitle("CONNECTING TO SERVER");
                pd.setMessage("Performing requested operation !!!");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                StringRequest req1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/DeleteFoodItem.jsp?foodid=" + foodid,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try
                                {
                                    pd.dismiss();

                                    JSONObject obj = new JSONObject(response.trim());

                                    if(obj.getString("msg").equals("success")) {
                                        Toast.makeText(context, "Food Item Deleted successfully !!!", Toast.LENGTH_SHORT).show();
                                        flist.remove(position);
                                        notifyDataSetChanged();
                                    }
                                    else
                                        Toast.makeText(context, "Food Item has not been deleted successfully !!!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Volley errror while deleting food item "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                req1.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(req1,"delete_food_item");
            }
        });
    }

    @Override
    public int getItemCount() {
        return flist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvFoodName,tvFoodCat,tvFoodPrice;
        Button btnFoodUpdate,btnFoodDelete;
        ImageView ivFoodItem;

        public MyViewHolder(View v)
        {
            super(v);
            tvFoodName = v.findViewById(R.id.txtFoodName);
            tvFoodCat = v.findViewById(R.id.txtFoodCat);
            tvFoodPrice = v.findViewById(R.id.txtFoodPrice);
            btnFoodDelete = v.findViewById(R.id.btnFoodDelete);
            btnFoodUpdate = v.findViewById(R.id.btnFoodUpdate);
            ivFoodItem = v.findViewById(R.id.imgFoodItem);
        }
    }

}
