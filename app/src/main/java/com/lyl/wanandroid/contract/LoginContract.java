package com.lyl.wanandroid.contract;

/**
 * Created by lym on 2020/3/29
 * Describe :
 *      定义一个 Contract契约接口，把Model/View/Presenter的接口都放入Contract的内部
 *      这里的一个 Contract 就对应一个页面（一个 Activity 或者一个 Fragment）
 *      放在 Contract 内部是为了让同一个页面的逻辑方法都放在一起，方便查看和修改
 *
 *      MVP(Model-View-Presenter)模式:
 *      Model: 数据层,提供数据,负责与网络层和数据库层的逻辑交互.
 *      View: UI层,负责显示数据, 并向Presenter报告用户行为.
 *      Presenter: 逻辑处理层,从Model拿数据, 应用到UI层, 管理UI的状态, 决定要显示什么, 响应用户的行为.
 *      https://blog.csdn.net/lyl0530/article/details/87557817
 *
 *      问题点：
 *      1. 要不要使用Contract？
 *      2. mvp中接口和实现的关系，接口文件何时可以不要，规则是？
 *          m-数据层，负责从网络或数据库中获取数据。
 *          若既要从网络或获取数据，也要从数据库中获取数据，
 *          则要使用接口文件，提供统一接口，两个modelImpl文件分别实现该接口，
 *          便于p层拿数据，此时p层就不用关心这个数据是从网络中来的，还是从数据库中来的；
 *          若只从网络或数据库中获取数据，则m层没必要写接口文件，直接写实现文件即可！
 *      3. 可以没有m层的情形？
 *
 *
 */
public interface LoginContract {

    interface View{
        void showProgressDialog();
        void hideProgressDialog();
        void loginSuccess();
        void loginFailed(String msg);
    }
}
