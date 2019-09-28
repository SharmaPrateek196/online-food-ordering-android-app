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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dto.Customer;
import com.dto.FoodItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowFoodItemFragment extends Fragment {

    View v;
//    TextView tvFoodName,tvFoodCat,tvFoodPrice;
//    Button btnFoodUpdate,btnFoodDelete;
//    ImageView ivFoodItem;
    RecyclerView rcv;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager mgr;
    ArrayList<FoodItem> flist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    v = inflater.inflate(R.layout.show_food_item_fragment,container,false);
    return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData()
    {
//        tvFoodName = v.findViewById(R.id.txtFoodName);
//        tvFoodCat = v.findViewById(R.id.txtFoodCat);
//        tvFoodPrice = v.findViewById(R.id.txtFoodPrice);
//        btnFoodDelete = v.findViewById(R.id.btnFoodDelete);
//        btnFoodUpdate = v.findViewById(R.id.btnFoodUpdate);
//        ivFoodItem = v.findViewById(R.id.imgFoodItem);
        rcv = v.findViewById(R.id.show_food_recycle);

        flist = new ArrayList<>();

        mgr = new LinearLayoutManager(getActivity());

        rcv.setLayoutManager(mgr);

        final ProgressDialog pd =
                new ProgressDialog(getActivity());

        pd.setTitle("CONNECTING TO SERVER");
        pd.setMessage("Downloading Food Items");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        StringRequest srq = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/GetAllFoodItems.jsp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("no"))
                        {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "No food Items to display", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            try {

                                pd.dismiss();
                                JSONArray arr = new JSONArray(response.trim());

                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);

                                    FoodItem f = new FoodItem();
                                    f.setId(obj.getInt("id"));
                                    f.setItemName(obj.getString("itemName"));
                                    f.setItemPrice(obj.getInt("itemPrice"));
                                    f.setImg_path(obj.getString("img_path"));
                                    f.setCategoryID(obj.getInt("categoryID"));

                                    flist.add(f);
                                }

                                adapter = new ShowFoodItemAdapter(flist, getActivity());

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

                        Toast.makeText(getActivity(), "Volley Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        srq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(srq,"Get_Food_Items");
    }
}
