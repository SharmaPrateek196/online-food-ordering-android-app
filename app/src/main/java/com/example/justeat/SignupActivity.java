package com.example.justeat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.app.ProgressDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.HashMap;


public class SignupActivity extends AppCompatActivity {
    EditText fname,lname,passwordd,mobphone,mail,usraddr,cnfpasswordd;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fname =(EditText) findViewById(R.id.edFname);
        lname =(EditText) findViewById(R.id.edLname);
        usraddr = (EditText) findViewById(R.id.usraddr);
        passwordd = (EditText)findViewById(R.id.passwrd);
        cnfpasswordd = (EditText)findViewById(R.id.confirm_passwrd);
        mail = (EditText) findViewById(R.id.mail);
        mobphone = (EditText) findViewById(R.id.mobphone);
        signup = (Button) findViewById(R.id.sup);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/cantarell_regular.ttf");
        signup.setTypeface(custom_font);
        fname.setTypeface(custom_font);
        lname.setTypeface(custom_font);
        mail.setTypeface(custom_font);
        mobphone.setTypeface(custom_font);
        passwordd.setTypeface(custom_font);
        cnfpasswordd.setTypeface(custom_font);
        usraddr.setTypeface(custom_font);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(SignupActivity.this);
                pd.setTitle("CONNECTING TO SERVER");
                pd.setMessage("Registering User Details");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();

                    String myurl =ServerAddress.MYSERVER+"/RegisterCustomer.jsp";

                    String tag_string_req = "myrequest_2";
                    Gson g = new Gson();
                    HashMap<String,String>hmap=
                            new HashMap<>();
                    hmap.put("firstName",fname.getText().toString());
                    hmap.put("lastName",lname.getText().toString());
                    hmap.put("mobileNumber",mobphone.getText().toString());
                    hmap.put("address",usraddr.getText().toString());
                    hmap.put("PASS",passwordd.getText().toString());
                    hmap.put("ID",mail.getText().toString());

                    try {
                        JSONObject job = new JSONObject(g.toJson(hmap).trim());
//                        Toast.makeText(SignupActivity.this,
//                                "json is"+job.toString(),Toast.LENGTH_LONG).show();
                        JsonObjectRequest request =
                                new JsonObjectRequest(Request.Method.POST,
                                        myurl, job, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(final JSONObject response) {
                                        try {
                                            pd.dismiss();
                                            String str =
                                                    response.getString("cid");
                                            if (str.equals("failure")) {

                                                Toast.makeText(SignupActivity.this, "Customer Not Registered !!!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(SignupActivity.this,
                                                        "Please enter OTP sent on your email-id !!!", Toast.LENGTH_SHORT).show();

                                                final Dialog d = new Dialog(SignupActivity.this);

                                                d.setContentView(R.layout.dialogue_confirm);

                                                Button btn = d.findViewById(R.id.btnSubmit);

                                                final EditText edtOTP = d.findViewById(R.id.editTextOtp);

                                                btn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        try {
                                                            String custid = response.getString("cid");

                                                            String otp = edtOTP.getText().toString();

                                                            StringRequest srq = new StringRequest(Request.Method.GET,
ServerAddress.MYSERVER + "/VerifyCustomer.jsp?ID="+custid+"&genpass="+otp,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {

                                                                            try {
                                                                                JSONObject obj = new JSONObject(response.trim());

                                                                                String msg = obj.getString("msg");

                                                                                if (msg.equals("verified"))
                                                                                {
                                                                                    Toast.makeText(SignupActivity.this,
                                                                                            "Verification Successful", Toast.LENGTH_SHORT).show();
                                                                                    d.dismiss();
                                                                                    Intent in = new Intent(SignupActivity.this, MainActivity.class);
                                                                                    startActivity(in);
                                                                                    finish();
                                                                                }
                                                                                else
                                                                                {
                                                                                    Toast.makeText(SignupActivity.this,
"Invalid OTP entered !!!", Toast.LENGTH_SHORT).show();
                                                                                }
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

                                                                            d.dismiss();
                                                                            Toast.makeText(SignupActivity.this
                                                                                    , "Volley Error "+error.getMessage(),
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                            srq.setRetryPolicy(new DefaultRetryPolicy(
                                                                    0,
                                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                            AppController.getInstance().addToRequestQueue(srq,"OTP request");

                                                        }
                                                        catch (Exception ex)
                                                        {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                });

                                                d.show();
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                },

                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                pd.dismiss();
                                                Toast.makeText(SignupActivity.this
                                                , "Volley Error "+error.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });

                        request.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                        AppController.getInstance().addToRequestQueue(request,tag_string_req);

                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
            }
        });

    }
}