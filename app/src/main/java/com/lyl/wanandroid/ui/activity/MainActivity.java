package com.lyl.wanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.ui.fragment.HierarchyFragment;
import com.lyl.wanandroid.ui.fragment.MainFragment;
import com.lyl.wanandroid.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private AppBarLayout mTitleBar;
    private ImageView mAvatar;
    private BottomNavigationView mBottomView;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragmentList;
    private ActionBarDrawerToggle mToggle;

    private NavigationView mNavigationView;
    private LinearLayout mSliderBar;
    //private ImageButton mCloseDrawer;
    private ImageView mSliderBarAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //titleBar
        mTitleBar = findViewById(R.id.title_bar);
        mAvatar = mTitleBar.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);

        //viewPager+fragment
        mViewPager = findViewById(R.id.vp_main);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MainFragment());
        mFragmentList.add(new HierarchyFragment());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);

        //bottomView
        mBottomView = findViewById(R.id.bottom_bar);
        //http://dditblog.com/itshare_821.html
        mBottomView.setItemTextAppearanceActive(R.style.bottom_text_size);
        mBottomView.setItemTextAppearanceInactive(R.style.bottom_text_size);
        //设置点击事件
        mBottomView.setOnNavigationItemSelectedListener(mNavigationItemSelectedListener);
        //BottomNavigationView 3个以上图标不显示文字 http://www.bloguan.com/?id=507
        mBottomView.setLabelVisibilityMode(1);

        mNavigationView = findViewById(R.id.nav_view);
        //https://blog.csdn.net/dummyo/article/details/79463711
        View headerView = mNavigationView.getHeaderView(0);
        mSliderBar = headerView.findViewById(R.id.slider_bar_layout);
        mSliderBar.findViewById(R.id.slider_bar_close).setOnClickListener(this);
        mSliderBarAvatar = mSliderBar.findViewById(R.id.slider_bar_avatar);
        mSliderBarAvatar.setOnClickListener(this);
        mSliderBar.findViewById(R.id.slider_bar_nick_name).setOnClickListener(this);

        //DrawerLayout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.about_us, R.string.app_name) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                LogUtils.d(TAG, "onDrawerSlide: " + slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //super.onDrawerOpened(drawerView);
                LogUtils.d(TAG, "onDrawerOpened");
                //mSliderBarAvatar.setBackgroundResource(R.drawable.ic_default_avatar);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //super.onDrawerClosed(drawerView);
                LogUtils.d(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle/*mDrawerListener*/);
    }

    private final ViewPager.OnPageChangeListener mPageChangeListener =
            new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    setPosition(i);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            };

    private final BottomNavigationView.OnNavigationItemSelectedListener mNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_main_pager:
                    setPosition(0);
                    break;
                case R.id.tab_hierarchy:
                    setPosition(1);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View view, float v) {

        }

        @Override
        public void onDrawerOpened(@NonNull View view) {

        }

        @Override
        public void onDrawerClosed(@NonNull View view) {

        }

        @Override
        public void onDrawerStateChanged(int i) {

        }
    };

    private void setPosition(int pos) {
        //viewPager带着BottomNavigationView切换
        mBottomView.getMenu().getItem(pos).setChecked(true);
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                mDrawerLayout.openDrawer(Gravity.START);//设置的方向应该跟xml文件里gravity相同，start和LEFT都为从左边出现
                break;
            case R.id.slider_bar_close:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.slider_bar_nick_name:
            case R.id.slider_bar_avatar:
                if (!BaseApplication.isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }
    }
//    public void Next(View view) {
//        //throw new RuntimeException("test");//add bugly
//        startActivity(new Intent(this, LoginActivity.class));
//    }
}
