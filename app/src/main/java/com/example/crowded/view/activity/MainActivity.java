package com.example.crowded.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.crowded.R;
import com.example.crowded.helper.PrefManager;
import com.example.crowded.util.constants.ConstantMenu;
import com.example.crowded.util.constants.ConstantMenuFrag;
import com.example.crowded.view.fragment.DashboardFragment;
import com.example.crowded.view.fragment.HandleFragment;
import com.example.crowded.view.fragment.ProfileFragment;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import it.sephiroth.android.library.bottomnavigation.BadgeProvider;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity implements BottomNavigation.OnMenuItemSelectionListener{

    private String msjNotiVal;
    private BadgeProvider provider;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private PrefManager pref;
    private HandleFragment handleFrag;

    private String currentFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        pref = new PrefManager(this);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_coordinator_container);
        FrameLayout frameLayout = findViewById(R.id.main_frame_container);

        BottomNavigation bottomNavigation = findViewById(R.id.main_BottomNavigation);

        fragmentManager = this.getSupportFragmentManager();
        handleFrag = new HandleFragment(fragmentManager);
        handleFrag.loadInitialFragments();

        provider = bottomNavigation.getBadgeProvider();

        bottomNavigation.setDefaultTypeface(ResourcesCompat.getFont(this, R.font.roboto_light));

        bottomNavigation.setMenuItemSelectionListener(this);

        bottomNavigation.setMenuChangedListener(new BottomNavigation.OnMenuChangedListener() {
            @Override
            public void onMenuChanged(@NotNull BottomNavigation bottomNavigation) {
                menuSelected(bottomNavigation.getSelectedIndex());
            }
        });

        //Seteo por defecto el mapa
        bottomNavigation.setDefaultSelectedIndex(1);
    }

    @Override
    public void onMenuItemReselect(int i, int indexSelected, boolean b) {
        menuSelected(indexSelected);
    }

    @Override
    public void onMenuItemSelect(int i, int indexSelected, boolean b) {
        menuSelected(indexSelected);
    }

    public void menuSelected(int selectedIndex){
        switch (selectedIndex){
            case ConstantMenu.DASHBOARD:
                fragment = new DashboardFragment();
                handleFrag.setFragment(fragment, ConstantMenuFrag.MAIN_FRAG_DASH);
                break;
            case ConstantMenu.PERFIL:
                fragment = new ProfileFragment();
                handleFrag.setFragment(fragment, ConstantMenuFrag.MAIN_FRAG_PROF);
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}