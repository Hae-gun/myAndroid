package org.techtown.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String DEVICE_ID = "Android01";
    private static final String DEVICE_TYPE = "USER";

    RoomReservationFrag roomList;
    SensorListFrag sensorList;
    UserInfoFrag userInfo;

    ViewPager pager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomList = new RoomReservationFrag();
        sensorList = new SensorListFrag();
        userInfo = new UserInfoFrag();

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        adapter.addItem(roomList);
        adapter.addItem(sensorList);
        adapter.addItem(userInfo);

       // getSupportFragmentManager().beginTransaction().replace(R.id.container, roomList).commit();
        pager.setAdapter(adapter);



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {



                        switch (menuItem.getItemId()) {
                            case R.id.tab1:
                                Toast.makeText(getApplicationContext(), "1번째", Toast.LENGTH_SHORT).show();
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, roomList).commit();
                                pager.setCurrentItem(0);
                                return true;
                            case R.id.tab2:
                                Toast.makeText(getApplicationContext(), "2번째", Toast.LENGTH_SHORT).show();
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, sensorList).commit();
                                pager.setCurrentItem(1);
                                return true;
                            case R.id.tab3:
                                Toast.makeText(getApplicationContext(), "3번째", Toast.LENGTH_SHORT).show();
                                pager.setCurrentItem(2);
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, userInfo).commit();
                                return true;
                        }


                        return false;
                    }
                }
        );






    }





    public static String getDeviceId() {
        return DEVICE_ID;
    }

    public static String getDeviceType() {
        return DEVICE_TYPE;
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter{




        ArrayList<Fragment> items = new ArrayList<Fragment>();
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

    }
}
