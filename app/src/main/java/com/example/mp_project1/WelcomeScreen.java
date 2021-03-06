package com.example.mp_project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeScreen extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    public DrawerLayout drawer_layout;
    public NavController navController;
    public NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    //private androidx.appcompat.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        setupNavigationView();
    }

    @SuppressLint("WrongConstant")
    public void setupNavigationView(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();

        drawer_layout= findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView2);
        navController = Navigation.findNavController(this,R.id.host_fragment);
// setup drawer
        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout);
        NavigationUI.setupWithNavController(navigationView,navController);

        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START))
        {
            drawer_layout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar.
     *
     * <p>If a parent was specified in the manifest for this activity or an activity-alias to it,
     * default Up navigation will be handled automatically. See
     * {@link #getSupportParentActivityIntent()} for how to specify the parent. If any activity
     * along the parent chain requires extra Intent arguments, the Activity subclass
     * should override the method {@link #onPrepareSupportNavigateUpTaskStack(TaskStackBuilder)}
     * to supply those arguments.</p>
     *
     * <p>See <a href="{@docRoot}guide/topics/fundamentals/tasks-and-back-stack.html">Tasks and
     * Back Stack</a> from the developer guide and
     * <a href="{@docRoot}design/patterns/navigation.html">Navigation</a> from the design guide
     * for more information about navigating within your app.</p>
     *
     * <p>See the {@link TaskStackBuilder} class and the Activity methods
     * {@link #getSupportParentActivityIntent()}, {@link #supportShouldUpRecreateTask(Intent)}, and
     * {@link #supportNavigateUpTo(Intent)} for help implementing custom Up navigation.</p>
     *
     * @return true if Up navigation completed successfully and this Activity was finished,
     * false otherwise.
     */
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.host_fragment),drawer_layout);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setCheckable(true);
        drawer_layout.closeDrawers();
        int id = menuItem.getItemId();

        Fragment fragment = null;
        switch (id)
        {
            case R.id.profileFragment:
                Toast.makeText(getApplicationContext(),"User Profile",Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.profileFrag);
                ProfileFrag profileFrag = new ProfileFrag();
                break;

            case R.id.logout:
                firebaseAuth.signOut();
                Toast.makeText(getApplicationContext(),"User Logout",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WelcomeScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                fragment = new DashboardFragment();
                break;
        }

        return true;
    }
}
