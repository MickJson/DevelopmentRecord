package com.nianlun.greendaodb.database;


import com.nianlun.greendao.gen.UserDao;
import com.nianlun.greendaodb.entity.User;

/**
 * 存放DaoUtils
 */
public class DaoUtilsStore {
    private volatile static DaoUtilsStore instance = new DaoUtilsStore();
    private CommonDaoUtils<User> mUserDaoUtils;

    public static DaoUtilsStore getInstance() {
        return instance;
    }

    private DaoUtilsStore() {
        DaoManager mManager = DaoManager.getInstance();
        UserDao _UserDao = mManager.getDaoSession().getUserDao();
        mUserDaoUtils = new CommonDaoUtils<>(User.class, _UserDao);
    }

    public CommonDaoUtils<User> getUserDaoUtils() {
        return mUserDaoUtils;
    }

}