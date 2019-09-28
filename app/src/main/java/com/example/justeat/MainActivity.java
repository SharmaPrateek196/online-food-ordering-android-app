package com.example.justeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView btnSignup,frgtpass;
    EditText usr,pswd;
    RadioGroup rgp;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rgp = findViewById(R.id.rgp);
        usr = findViewById(R.id.edtName);
        pswd = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        frgtpass = findViewById(R.id.forgot_password);
        frgtpass.setPaintFlags(frgtpass.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        frgtpass.setText("Forgot Password");

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch(checkedId)
                {
                    case R.id.radAdmin : choice = "admin"; break;

                    case R.id.radCustomer : choice = "customer"; break;
                }
            }
        });

        frgtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this,PasswordActivity.class);
                startActivity(registerIntent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pd =
                        new ProgressDialog(MainActivity.this);

                pd.setTitle("CONNECTING TO SERVER");
                pd.setMessage("Verifying User Details");
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.show();


                String myurl ="";

                if(choice.equals("admin"))
                        myurl = ServerAddress.MYSERVER+"/AdminLogin.jsp";
                else
                    myurl = ServerAddress.MYSERVER+"/CustomerLogin.jsp";

                String tag_string_req = "myrequest_1";

                Gson g = new Gson();

                HashMap<String,String> hmap =
                        new HashMap<>();

                hmap.put("ID",usr.getText().toString());
                hmap.put("PASS",pswd.getText().toString());


                try{
                    JSONObject job =
                            new JSONObject(g.toJson(hmap).trim());

                    JsonObjectRequest request =
                            new JsonObjectRequest(Request.Method.POST,
                                    myurl, job, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try{
                                        pd.dismiss();
                                        String str =
                                                response.getString("cid");

                                            if (str.equals("SUCCESS") && choice.equals("admin")) {
                                                Toast.makeText(MainActivity.this,
                                                        "LOGIN SUCCESSFUL !!!", Toast.LENGTH_SHORT).show();
                                                Intent in = new Intent(MainActivity.this,
                                                        AdminHomeActivity.class);
                                                startActivity(in);
                                                finish();
                                            }
                                            else if(str.equals("failure"))
                                            {
                                                Toast.makeText(MainActivity.this,
                                                        "LOGIN UNSUCCESSFUL !!!", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(!str.equals("SUCCESS") && choice.equals("customer")){

                                                Toast.makeText(MainActivity.this,
                                                        "LOGIN SUCCESSFUL !!!", Toast.LENGTH_SHORT).show();
                                                Intent in = new Intent(MainActivity.this,
                                                        CustomerHomeActivity.class);
                                                startActivity(in);
                                                finish();
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

                                            pd.dismiss();
                                            Toast.makeText(MainActivity.this,
                                                    "Volley Error "+error.getMessage(),
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    });

                    AppController.getInstance().
                            addToRequestQueue(request,tag_string_req);

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });


    }

}
