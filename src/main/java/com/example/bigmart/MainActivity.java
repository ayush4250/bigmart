package com.example.bigmart;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.MenuItem;


import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bigmart.RegisterActivity;;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FrameLayout frameLayout;

    private static final int HOME_FRAGMENT=0;
    private static final int CART_FRAGMENT=1;
    private static final int ORDER_FRAGMENT=2;
    private static final int OFFER_FRAGMNT=3;
    private static final int ACCOUNT_FRAGMENT=4;
    private static final int FAVOURITE_FRAGMENT=5;

    private static int currentfragment=-1;
    private NavigationView navigationView;
    private Dialog signinDialog;
    public static DrawerLayout drawer;
    private Window window;
    private Toolbar toolbar;
    private TextView badgecount;
    public static Activity mainActivity;
    public static boolean resetMainactivity = false;

    private FirebaseUser currentUser;
    private AppBarLayout.LayoutParams params;
    private int scrollFlags;
    public static boolean showCart;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params  = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

//         appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
//                        .setDrawerLayout(drawer)
//                        .build();
//         navController = Navigation.findNavController(this, R.id.nav_view);
//         NavigationView navView = findViewById(R.id.nav_view);
//         NavigationUI.setupWithNavController(navView, navController);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);





        frameLayout=findViewById(R.id.main_framelayout);
        if (showCart){
            mainActivity = this;
            //drawer.setDrawerLockMode(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart",new CartFragment(),-2);
        }else{
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setFragment(new HomeFragment(),HOME_FRAGMENT);
        }


        signinDialog = new Dialog(MainActivity.this);
        signinDialog.setContentView(R.layout.signin_dialog);
        signinDialog.setCancelable(true);
        signinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signindialogbtn = signinDialog.findViewById(R.id.signinbutton);
        Button signupdialogbtn= signinDialog.findViewById(R.id.signupbutton);
        final Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
        signindialogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupFragment.disableclosebtn=true;
                SigninFragment.disableclosebtn=true;
               // setSignupFragment=false;
                startActivity(registerIntent);
            }
        });

        signupdialogbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninFragment.disableclosebtn=true;
                SignupFragment.disableclosebtn=true;
                signinDialog.dismiss();
                //setSignupFragment=true;
                startActivity(registerIntent);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
        if(currentUser==null){
            currentUser= FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser==null){
                navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
            }else{
                navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);

            }
            if (resetMainactivity){
                resetMainactivity = false;
                setFragment(new HomeFragment(), HOME_FRAGMENT);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
            invalidateOptionsMenu();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else{
            if(currentfragment==HOME_FRAGMENT) {
                currentfragment=-1;
                super.onBackPressed();
            }else{
                if (showCart){
                    mainActivity = null;
                    showCart = false;
                    finish();
                }else {
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.main_search_icon) {
//            Intent searchIntent =new Intent(this,SearchActivity.class);
//            startActivity(searchIntent);
            return true;
        } else if (id == R.id.main_notification_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if(currentUser==null) {
                signinDialog.show();
            }else {
                gotoFragment("My Cart", new CartFragment(), CART_FRAGMENT);
            }

            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(currentfragment==HOME_FRAGMENT){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main,menu);
            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeicon=cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeicon.setImageResource(R.drawable.cart_white);
                badgecount = cartItem.getActionView().findViewById(R.id.badge_count);
                if(currentUser!=null){
                    if (DbQueries.cartList.size() == 0) {
                        DbQueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this),false,badgecount,new TextView(MainActivity.this));

                    }else{
                        badgecount.setVisibility(View.VISIBLE);
                        if(DbQueries.cartList.size()<50) {
                            badgecount.setText(String.valueOf(DbQueries.cartList.size()));
                        }else{
                            badgecount.setText("50+");
                        }
                    }
                }


                cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentUser==null) {
                            signinDialog.show();
                        }else {
                            gotoFragment("My Cart", new CartFragment(), CART_FRAGMENT);
                        }
                    }
                });




        }
        return true;
    }


    private void gotoFragment(String title, Fragment fragment, int fragmentNo){
         getSupportActionBar().setDisplayShowTitleEnabled(true);
         getSupportActionBar().setTitle(title);
         invalidateOptionsMenu();
         setFragment(fragment,fragmentNo);
        //navigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if(currentUser!=null) {
            int id = menuItem.getItemId();

            if (id == R.id.nav_home) {
                //gotoFragment("foodZone", new HomeFragment(), HOME_FRAGMENT);
                invalidateOptionsMenu();
                setFragment(new HomeFragment(), HOME_FRAGMENT);
            } else if (id == R.id.nav_cart) {
                gotoFragment("Cart", new CartFragment(), CART_FRAGMENT);
            } else if (id == R.id.nav_orders) {
                gotoFragment("Orders", new OrderFragment(), ORDER_FRAGMENT);

            } else if (id == R.id.nav_rewards) {
                gotoFragment("Offers", new RewardFragment(), OFFER_FRAGMNT);

            } else if (id == R.id.nav_account) {
                gotoFragment("My Account", new AccountFragment(), ACCOUNT_FRAGMENT);

            } else if(id==R.id.nav_favourite) {
                gotoFragment("My Favourites",new WishlistFragment(),FAVOURITE_FRAGMENT);

            } else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                DbQueries.clearData();
                Intent registerIntent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                finish();

            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else{
            drawer.closeDrawer(GravityCompat.START);
            signinDialog.show();
            return false;
        }


    }


    private void setFragment(Fragment fragment,int fragmentNo) {
        if (fragmentNo != currentfragment) {

           if(fragmentNo==OFFER_FRAGMNT){
                window.setStatusBarColor(Color.parseColor("#000000"));
                toolbar.setBackgroundColor(Color.parseColor("#000000"));
            }else{
               window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
               toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            currentfragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.main_framelayout);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}



 /*

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       // NavigationUI.setupWithNavController(navigationView, navController);
 */

