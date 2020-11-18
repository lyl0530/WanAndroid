package com.lyl.wanandroid.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.wanandroid.R;
import com.lyl.wanandroid.app.BaseApplication;
import com.lyl.wanandroid.base.BaseActivity;
import com.lyl.wanandroid.listener.DialogListener;
import com.lyl.wanandroid.service.entity.LogoutResult;
import com.lyl.wanandroid.service.present.LogoutPresenter;
import com.lyl.wanandroid.service.view.LogoutView;
import com.lyl.wanandroid.ui.fragment.FragmentHome;
import com.lyl.wanandroid.utils.ConstUtil;
import com.lyl.wanandroid.utils.ErrorUtil;
import com.lyl.wanandroid.utils.PreferenceUtil;
import com.lyl.wanandroid.ui.fragment.FrgmHierarchy;
import com.lyl.wanandroid.ui.fragment.FrgmNavigation;
import com.lyl.wanandroid.utils.LogUtil;
import com.lyl.wanandroid.ui.view.CircleView;
import com.lyl.wanandroid.widget.ConfirmDialog;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity implements View.OnClickListener, LogoutView {
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    //    private AppBarLayout mTitleBar;
//    private ImageView mAvatar;
    private BottomNavigationView mBottomView;
    private ViewPager mViewPager;

    //activity和fragment交互，即调用fragment的方法时，要先判断空
    // 若设置的是默认缓存，即mViewPager.setOffscreenPageLimit(size);的参数为0或1时
    // 左边一个fragment，右边一个fragment，滑动到第3个fragment时，
    // 第0个fragment已经被remove了，此时tabFragment为null
    private /*List<Fragment>*/ SparseArray<Fragment> mFragmentList = new SparseArray<>();

    private ActionBarDrawerToggle mToggle;

    private NavigationView mNavigationView;
    private LinearLayout mSliderBar;
    //private ImageButton mCloseDrawer;
    private /*ImageView*/ CircleView mSliderBarAvatar;
    private TextView mSliderBarNickName;
    private FragmentPagerAdapter mAdapter;
    private static final int FM_CNT = 3;//fragment的数量

    private Menu menu;
    private static final String DIALOG_TAG = "dialog_tag";
    private LogoutPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null != savedInstanceState && null != getSupportFragmentManager()){
            dialog = (ConfirmDialog)getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
            if (null != dialog) {
                dialog.setOnDialogClickListener(mListener);
            }
        }

        initView();
        initAdapter();
        initData();
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp_main);
        //bottomView
        mBottomView = findViewById(R.id.bottom_bar);
        mNavigationView = findViewById(R.id.nav_view);
        //https://blog.csdn.net/dummyo/article/details/79463711
        View headerView = mNavigationView.getHeaderView(0);
        mSliderBar = headerView.findViewById(R.id.slider_bar_layout);
        mSliderBar.findViewById(R.id.slider_bar_close).setOnClickListener(this);
        mSliderBarAvatar = mSliderBar.findViewById(R.id.slider_bar_avatar);
        mSliderBarAvatar.setOnClickListener(this);
        mSliderBarNickName = mSliderBar.findViewById(R.id.slider_bar_nick_name);
        mSliderBarNickName.setOnClickListener(this);

        //DrawerLayout
        mDrawerLayout = findViewById(R.id.drawer_layout);
    }

    private void initAdapter() {
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                LogUtil.d(TAG, "getItem" + position);
                Fragment fm;
                switch (position) {
                    case 1:
                        fm = new FrgmHierarchy();//FragmentHierarchy();
                        break;
                    case 2:// tab 2
                        fm = new FrgmNavigation();//FragmentProject();
                        break;
                    case 3:// tab 3 temp input here
                        fm = new Fragment();
                        break;
                    default:
                        fm = new FragmentHome();//FragmentMain();
                        break;
                }
                return fm;
                //return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return /*mFragmentList.size()*/FM_CNT;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                LogUtil.d(TAG, "instantiateItem" + position);
                //super方法里会调用getItem()方法！
                Fragment fm = (Fragment) super.instantiateItem(container, position);
                mFragmentList.put(position, fm);
                return fm;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                LogUtil.d(TAG, "destroyItem" + position);
                mFragmentList.remove(position);
                super.destroyItem(container, position, object);
            }
        };
    }

    private void initData() {
        //add fragment的错误写法！
        //旋转屏幕时，onCreate会执行，此时activity中会出现4个新对象。
        // 但此后不会调用getItem方法，因为fragmentManger和FragmentPagerAdapter管理fragment，
        // 屏幕旋转后，fragmentManger和FragmentPagerAdapter可以恢复上次的fragment对象。不需要再次调用getItem方法
//        mFragmentList = new ArrayList<>();
//        mFragmentList.add(new MainFragment());
//        mFragmentList.add(new HierarchyFragment());
        //此时操作fragments，会无法执行成功
        //fragments.get(0);

        //大部分应用使用 android:screenOrientation="portrait"，使屏幕不会旋转，但APP很可能在后台被杀死，
        // 当APP重新返回主界面时，依然会出现只执行onCreate方法，不走getItem方法。
        // 此时调用fragment的方法，不生效

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        //https://blog.csdn.net/xiaolaohuqwer/article/details/75670294
        // viewpager是默认预加载下一页的界面的。
        // viewpager提供了一个设置预加载页面数量的方法，那就是setOffscreenPageLimit()。
        // 默认不设置数量的情况下预加载下一页。设置0和1是同样的效果。设置2表示预加载下2页。
        mViewPager.setOffscreenPageLimit(FM_CNT);

        //http://dditblog.com/itshare_821.html
        mBottomView.setItemTextAppearanceActive(R.style.bottom_text_size);
        mBottomView.setItemTextAppearanceInactive(R.style.bottom_text_size);
        //设置点击事件
        mBottomView.setOnNavigationItemSelectedListener(mNavigationItemSelectedListener);
        //BottomNavigationView 3个以上图标不显示文字 http://www.bloguan.com/?id=507
        mBottomView.setLabelVisibilityMode(1);

        menu = mNavigationView.getMenu();
        menu.getItem(0).setOnMenuItemClickListener(mMenuItemClickListener);
        menu.getItem(1).setOnMenuItemClickListener(mMenuItemClickListener);
        menu.getItem(2).setOnMenuItemClickListener(mMenuItemClickListener);
        menu.getItem(3).setOnMenuItemClickListener(mMenuItemClickListener);
        menu.getItem(3).setVisible(BaseApplication.isLogin());

        initToggle();
        mDrawerLayout.addDrawerListener(mToggle);

        mPresenter = new LogoutPresenter();
        mPresenter.attach(this);
    }

    private void initToggle() {
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.about_us, R.string.app_name) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //LogUtils.d(TAG, "onDrawerSlide: " + slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //super.onDrawerOpened(drawerView);
                LogUtil.d(TAG, "onDrawerOpened");

                //使用sp change的监听！
                //mSliderBarAvatar.setBackgroundResource(R.drawable.ic_default_avatar);
                mSliderBarNickName.setText(BaseApplication.isLogin()
                        ? PreferenceUtil.instance().getAccount()
                        : getString(R.string.pls_login));
                //20201101
                menu.getItem(3).setVisible(BaseApplication.isLogin());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //super.onDrawerClosed(drawerView);
                LogUtil.d(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mToggle.syncState();
    }

    private ConfirmDialog dialog;

    private final MenuItem.OnMenuItemClickListener mMenuItemClickListener
            = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_my_like:
                    if(BaseApplication.isLogin()){
                        startActivity(new Intent(MainActivity.this, CollectListActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    break;
                case R.id.item_reading_history:
                    if(BaseApplication.isLogin()){
                        //阅读历史
                        showToast(R.string.wait);
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    break;
                case R.id.item_system_setting:
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));
//                    showToast(R.string.wait);
                    break;
                case R.id.item_logout:
                    dialog = ConfirmDialog.getInstance(getString(R.string.are_you_sure_to_logout));
                    dialog.setOnDialogClickListener(mListener);
                    dialog.show(getSupportFragmentManager(), DIALOG_TAG);
                    break;
            }
            mDrawerLayout.closeDrawers();
            return true;
        }
    };

    private DialogListener mListener = new DialogListener() {
        @Override
        public void leftClick() {
            //do nothing
        }

        @Override
        public void rightClick() {
            if (null != mPresenter){
                mPresenter.logout();
            }
        }
    };

    @Override
    public void Success(LogoutResult result) {
        LogUtil.d(TAG, "loginSuccess: " + result);
        EventBus.getDefault().post(ConstUtil.REFRESH_MAIN);//刷新首页
        //退出登录成功 - 清除缓存
        PreferenceUtil.instance().setUserId(0);
//        PreferenceUtil.instance().setAccount("");
//        PreferenceUtil.instance().setPwd("");
        PreferenceUtil.instance().setCookieSet(null);//清除本地cookie
        finish();
    }

    @Override
    public void Failed(int code, String msg) {
        String str = ErrorUtil.getErrorInfo(this, code, msg);
        showToast(str);
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
                case R.id.tab_project:
                    setPosition(2);
                    break;
                default:
                    break;
            }
            return false;
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
//            case R.id.avatar:
//                mDrawerLayout.openDrawer(Gravity.START);//设置的方向应该跟xml文件里gravity相同，start和LEFT都为从左边出现
//                break;
            case R.id.slider_bar_close:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.slider_bar_nick_name:
            case R.id.slider_bar_avatar:
                if (BaseApplication.isLogin()) {
                    //edit user info
                    LogUtil.d(TAG, "will edit user info");
//                    showToast("will edit user info");
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    public void clickMainAvatar() {
        mDrawerLayout.openDrawer(Gravity.START);//设置的方向应该跟xml文件里gravity相同，start和LEFT都为从左边出现
    }

}
