package com.example.justeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dto.FoodItem;
import com.google.gson.Gson;

import org.json.JSONObject;

public class UpdateFoodItem extends AppCompatActivity {

    Intent in;
    EditText edtItemName,edtItemDesc,edtfoodItemPrice,edtPerOff,edtItemStatus,edtprsnCnt;
    Button btnUpdateItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food_item);

        in = getIntent();

        int foodid = in.getIntExtra("foodid",0);

        edtItemName = findViewById(R.id.edtItemName);
        edtItemDesc = findViewById(R.id.edtItemDesc);
        edtfoodItemPrice = findViewById(R.id.edtfoodItemPrice);
        edtPerOff = findViewById(R.id.edtPerOff);
        edtItemStatus = findViewById(R.id.edtItemStatus);
        edtprsnCnt = findViewById(R.id.edtprsnCnt);

        btnUpdateItem = findViewById(R.id.btnUpdateItem);

        final ProgressDialog pd = new ProgressDialog(UpdateFoodItem.this);
        pd.setTitle("CONNECTING TO SERVER");
        pd.setMessage("Getting Food Item");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        StringRequest srq1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/ShowSingleFoodItem.jsp?foodid=" + foodid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            pd.dismiss();

                            JSONObject obj = new JSONObject(response.trim());

                            edtItemName.setText(obj.getString("itemName"));
                            edtItemDesc.setText(obj.getString("itemDesc"));
                            edtfoodItemPrice.setText(obj.getInt("itemPrice")+"");
                            edtPerOff.setText(obj.getInt("percentoff")+"");
                            edtItemStatus.setText(obj.getBoolean("itemStatus")+"");
                            edtprsnCnt.setText(obj.getInt("personCount")+"");

                            Toast.makeText(UpdateFoodItem.this, "Food Item Displayed !!!", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(UpdateFoodItem.this, "Volley Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        srq1.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(srq1,"SHOW_SINGLE_FOOD_ITEM");


        btnUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd = new ProgressDialog(UpdateFoodItem.this);
                pd.setTitle("CONNECTING TO SERVER");
                pd.setMessage("Saving Changes !!!");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                FoodItem fd = new FoodItem();

                fd.setId( in.getIntExtra("foodid",0));
                fd.setItemName(edtItemName.getText().toString());
                fd.setItemDesc(edtItemDesc.getText().toString());
                fd.setItemPrice(Integer.parseInt(edtfoodItemPrice.getText().toString()));
                fd.setItemStatus(Boolean.parseBoolean(edtItemStatus.getText().toString()));
                fd.setPercentoff(Integer.parseInt(edtPerOff.getText().toString()));
                fd.setPersonCount(Integer.parseInt(edtprsnCnt.getText().toString()));

                Gson g = new Gson();

                //Toast.makeText(UpdateFoodItem.this, ""+g.toJson(fd), Toast.LENGTH_SHORT).show();
                try
                {
                    JSONObject obj = new JSONObject(g.toJson(fd).trim());

                    //Toast.makeText(UpdateFoodItem.this, "generated json is "+obj.toString(), Toast.LENGTH_LONG).show();

                    JsonObjectRequest jrq = new JsonObjectRequest(
                            Request.Method.POST, ServerAddress.MYSERVER+"/UpdateFoodItem.jsp",obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try
                            {
                                pd.dismiss();

                                String msg = response.getString("msg");

                                if(msg.equals("success"))
                                    Toast.makeText(UpdateFoodItem.this,
"Food Item Updated Successfully !!!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(UpdateFoodItem.this,
                                            "Food Item Updation Failed !!!", Toast.LENGTH_SHORT).show();
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

                                    Toast.makeText(UpdateFoodItem.this,
                                            "Volley error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    jrq.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    AppController.getInstance().addToRequestQueue(jrq,"UPDATE_FOOD_TEM_REQUEST");
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }
}













