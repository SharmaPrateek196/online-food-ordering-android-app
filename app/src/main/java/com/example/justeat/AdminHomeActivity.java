package com.example.justeat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment[] fragments;
    FragmentManager mgr;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        fragments = new Fragment[8];

        fragments[0] = new ApproveCustFragment();
        fragments[1] = new AddFoodItemFragment();
        fragments[2] = new ShowFoodItemFragment();
        fragments[3] = new ShowDeleteCustomerFragment();
        fragments[4] = new ShowDeleteFoodCategoryFragment();

        mgr = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.approve_customer) {

            ft = mgr.beginTransaction();

            ft.replace(R.id.approve_cust_linear,fragments[0]);

            ft.detach(fragments[0]);
            ft.attach(fragments[0]);

            ft.commit();
        } else if (id == R.id.add_food_category) {

            final Dialog d = new Dialog(AdminHomeActivity.this);
            d.setContentView(R.layout.add_food_category);

            final EditText edt = d.findViewById(R.id.edtFoodCat);
            Button btn = d.findViewById(R.id.btnAddCatg);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog pd = new ProgressDialog(AdminHomeActivity.this);
                    pd.setTitle("CONNECTING TO SERVER");
                    pd.setMessage("Uploading Data");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();

                    StringRequest req1 = new StringRequest(Request.Method.GET, ServerAddress.MYSERVER + "/AddFoodItemCategory.jsp?foodcat="+edt.getText().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    pd.dismiss();

                                    try
                                    {
                                        JSONObject obj = new JSONObject(response.trim());

                                        if(obj.getString("msg").equals("success"))
                                        {
                                            d.dismiss();
                                            Toast.makeText(AdminHomeActivity.this,
                                                    "Food Category Added Successfully !!!", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            d.dismiss();
                                            Toast.makeText(AdminHomeActivity.this,
                                                    "Food Category Not Added !!!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(AdminHomeActivity.this, "Volley Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    req1.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppController.getInstance().addToRequestQueue(req1,"Add_Food_Category_Request");
                }
            });

            d.show();

        } else if (id == R.id.add_food_item) {

            ft = mgr.beginTransaction();

            ft.replace(R.id.approve_cust_linear,fragments[1]);

            ft.detach(fragments[1]);
            ft.attach(fragments[1]);

            ft.commit();
        } else if (id == R.id.view_food_item) {

            ft = mgr.beginTransaction();

            ft.replace(R.id.approve_cust_linear,fragments[2]);

            ft.detach(fragments[2]);
            ft.attach(fragments[2]);

            ft.commit();
        } else if (id == R.id.show_customer) {

            ft = mgr.beginTransaction();

            ft.replace(R.id.approve_cust_linear,fragments[3]);

            ft.detach(fragments[3]);
            ft.attach(fragments[3]);

            ft.commit();
        }
        else if(id == R.id.view_food_category)
        {
            ft = mgr.beginTransaction();

            ft.replace(R.id.approve_cust_linear,fragments[4]);

            ft.detach(fragments[4]);
            ft.attach(fragments[4]);

            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
