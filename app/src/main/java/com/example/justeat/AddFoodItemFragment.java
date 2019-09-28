package com.example.justeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dto.FoodItemCategory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddFoodItemFragment extends Fragment {

    View v;
    ImageView iv;
    EditText edtName,edtDesc,edtPrice,edtPersonCount;
    Spinner spCategory;
    Button btnAddItem;
    ArrayList<FoodItemCategory> catlist;
    ArrayAdapter<FoodItemCategory> cadapter;
    String imageName="";
    File f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    v = inflater.inflate(R.layout.add_food_item,container,false);

    return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData()
    {
        iv = v.findViewById(R.id.imgFood);
        edtName = v.findViewById(R.id.edtItemName);
        edtDesc = v.findViewById(R.id.edtFoodDesc);
        edtPrice = v.findViewById(R.id.edtItemPrice);
        edtPersonCount = v.findViewById(R.id.edtPrsnCnt);
        btnAddItem = v.findViewById(R.id.btnAddItem);
        spCategory = v.findViewById(R.id.spFoodCategory);
        //btnAddItem.setEnabled(false);

        long milis = System.currentTimeMillis();

        imageName="img"+milis+".png";

        catlist = new ArrayList<>();

        cadapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,catlist);

        spCategory.setAdapter(cadapter);

        cadapter.setDropDownViewResource(
                android.R.layout.select_dialog_singlechoice);


        final ProgressDialog pd =
                new ProgressDialog(getActivity());

        pd.setTitle("CONNECTING TO SERVER");
        pd.setMessage("Downloading Data From Server");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        StringRequest srq = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/GetAllFoodCategories.jsp",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        try
                        {
                            JSONArray arr = new JSONArray(response.trim());

                            for(int i = 0 ; i < arr.length() ; i++)
                            {
                                JSONObject obj = arr.getJSONObject(i);

                                FoodItemCategory fcat = new FoodItemCategory();
                                fcat.setId(obj.getInt("id"));
                                fcat.setCategoryName(obj.getString("categoryName"));
                                catlist.add(fcat);
                            }

                            cadapter.notifyDataSetChanged();
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),
                                "Could not load data , Volley error !!!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });

        AppController.getInstance().addToRequestQueue(srq,"Get All Categories");



        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(in,123);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtName.getText().toString().equals("")
                        || edtDesc.getText().toString().equals("") || edtPersonCount.getText().toString().equals("")
                || edtPrice.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(),
                            "Plz fill up all the details !!!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    String filePath = f.getAbsolutePath();
                    MyTask tsk = new MyTask();
                    tsk.execute(filePath);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 123 && data != null &&
                resultCode == RESULT_OK)
        {
            Bitmap bmp=null;
            try {

                bmp = (Bitmap)data.getExtras().get("data");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            iv.setImageBitmap(bmp);

            File appPackageDir= getActivity().getDir("user_img",MODE_PRIVATE);


            f=new File(appPackageDir, imageName);

            try{
                FileOutputStream fout=
                        new FileOutputStream(f);
                //bmp.compress(format, quality, stream);
                boolean result=bmp.compress(Bitmap.CompressFormat.PNG,
                        100, fout);
                if(result==true)
                    Toast.makeText(getActivity(),
                            "image selected", Toast.LENGTH_LONG).show();

                btnAddItem.setEnabled(true);

                fout.close();

            }catch(Exception ex)
            {
                Log.e("error;", ex.toString());
            }
        }

    }


    class MyTask extends AsyncTask<String,Void,String>
    {
        ProgressDialog pd =
                new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            pd.setTitle("CONNECTING TO SERVER");
            pd.setMessage("Uploading Food Item");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            if(s.trim().equals("success")) {
                Toast.makeText(getActivity(),
                        "Food Item added successfully !!!"
                        ,Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity(),
                        "Food Item Upload failed !!!"
                        ,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings)
        {
            String filePath=strings[0];
            String str="";
            try{
                URL u=new URL(ServerAddress.MYSERVER +
                        "/AddFoodItem.jsp");
                MultipartUtility mpu=
                        new MultipartUtility(u);

                mpu.addFilePart("file",
                        new File(filePath));

                mpu.addFormField("fooditemname",
                        edtName.getText().toString());

                mpu.addFormField("fooddesc",
                        edtDesc.getText().toString());

                mpu.addFormField("fooditemprice",
                        edtPrice.getText().toString());

                mpu.addFormField("personcount",
                        edtPersonCount.getText().toString());

                mpu.addFormField("categoryname",
                        spCategory.getSelectedItem().toString());

                byte []result=mpu.finish();

                str=new String(result);

            }catch(Exception ex)
            {
                str=ex.toString();
            }

            return str;
        }
    }
}











