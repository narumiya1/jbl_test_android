package com.example.jbl.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jbl.HomeActivity;
import com.example.jbl.R;
import com.google.android.material.tabs.TabLayout;

public class SystemAdminActivite extends AppCompatActivity {

    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_system_admin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // get the reference of FrameLayout and TabLayout
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        // Create a new Tab named "First"
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("General"); // set the Text for the first Tab
        firstTab.setIcon(R.drawable.baseline); // set an icon for the
        // first tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
//         Create a new Tab named "Third"
        TabLayout.Tab fifthMan = tabLayout.newTab();
        fifthMan.setText("MANDIRI"); // set the Text for the first Tab
        fifthMan.setIcon(R.drawable.baseline); // set an icon for the first tab
        tabLayout.addTab(fifthMan); // add  the tab at in the TabLayout

        // Create a new Tab named "Second"
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("BNI"); // set the Text for the second Tab
        secondTab.setIcon(R.drawable.baseline); // set an icon for the second tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout

        // Create a new Tab named "Third"
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("BRI"); // set the Text for the first Tab
        thirdTab.setIcon(R.drawable.baseline); // set an icon for the first tab
        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout

        // Create a new Tab named "BRI"
        TabLayout.Tab birTab = tabLayout.newTab();
        birTab.setText("BCA"); // set the Text for the first Tab
        birTab.setIcon(R.drawable.baseline); // set an icon for the first tab
        tabLayout.addTab(birTab); // add  the tab at in the TabLayout


        // perform setOnTabSelectedListener event on TabLayout

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            // get the current selected tab's position and replace the fragment accordingly
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                Fragment fragment = null;
                /**switch (tab.getPosition()) {
                    case 0:
                        fragment = new FirstFragment();
                        break;
                    case 1:
                        fragment = new SecondFragment();
                        break;
                    case 2:
                        fragment = new ThirdFragment();
                        break;
                }
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
                **/
                switch (tab.getPosition()) {
                    case 0:
                        fragment = fm.findFragmentByTag("FIRST");
                        if (fragment == null) {
                            fragment = new GeneralFragment();
                            ft.add(R.id.simpleFrameLayout, fragment, "FIRST");
                        }
                        ft.show(fragment);
                        break;
                    case 1:

                        fragment = fm.findFragmentByTag("MANDIRI");
                        if (fragment == null) {
                            fragment = new MandiriFragment();
                            ft.add(R.id.simpleFrameLayout, fragment, "MANDIRI");
                        }
                        ft.show(fragment);
                        break;
                    case 2:
                        fragment = fm.findFragmentByTag("BNI");
                        if (fragment == null) {
                            fragment = new BNIFragment();
                            ft.add(R.id.simpleFrameLayout, fragment, "BNI");
                        }
                        ft.show(fragment);
                        break;
                    case 3:
                        fragment = fm.findFragmentByTag("BRI");
                        if (fragment == null) {
                            fragment = new BriFragment();
                            ft.add(R.id.simpleFrameLayout, fragment, "BRI");
                        }
                        ft.show(fragment);
                        break;
                    case 4:
                        fragment = fm.findFragmentByTag("BCA");
                        if (fragment == null) {
                            fragment = new BCAFragment();
                            ft.add(R.id.simpleFrameLayout, fragment, "BCA");
                        }
                        ft.show(fragment);
                        break;


                }

                /**ft.replace(R.id.simpleFrameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                **/
                for (Fragment frag : fm.getFragments()) {
                    if (frag != fragment) {
                        ft.hide(frag);
                    }
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // Set tab pertama sebagai default saat aplikasi dijalankan
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            /**ft.replace(R.id.simpleFrameLayout, new FirstFragment());
            ft.commit();**/
            ft.add(R.id.simpleFrameLayout, new GeneralFragment(), "FIRST").commit();

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}