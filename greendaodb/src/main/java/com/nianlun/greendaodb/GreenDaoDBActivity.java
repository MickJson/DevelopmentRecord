package com.nianlun.greendaodb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nianlun.greendaodb.adapter.UserAdapter;
import com.nianlun.greendaodb.database.DaoManager;
import com.nianlun.greendaodb.database.DaoUtilsStore;
import com.nianlun.greendaodb.entity.User;
import com.nianlun.greendaodb.utils.NameUtils;
import com.nianlun.greendaodb.utils.SnowflakeIdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GreenDaoDBActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAdd;
    private Button btnDelete;
    private Button btnUpdate;
    private Button btnQuery;
    private RecyclerView rvUser;

    private List<User> mUserList;
    private UserAdapter mUserAdapter;
    private SnowflakeIdGenerator mIdWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao_db);
        DaoManager.getInstance().init(getApplication());
        initView();
        initUser();
    }

    private void initView() {
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnQuery = (Button) findViewById(R.id.btn_query);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        rvUser = (RecyclerView) findViewById(R.id.rv_user);
        rvUser.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            User user = new User();
            user.setId((long) mUserList.size());
            user.setUserId(String.valueOf(mIdWorker.nextId()));
            user.setUserName(NameUtils.createRandomZHName(new Random().nextInt(4) + 1));
            user.setAge(18 + new Random().nextInt(10));

            // 插入新用户
            DaoUtilsStore.getInstance().getUserDaoUtils().insert(user);

            queryAllUser();

            Toast.makeText(this, "增加用户", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_delete) {
            User user = mUserList.get(mUserList.size() - 1);

            //删除最末用户
            DaoUtilsStore.getInstance().getUserDaoUtils().delete(user);

            queryAllUser();

            Toast.makeText(this, "删除用户", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_update) {
            User user = mUserList.get(mUserList.size() - 1);
            user.setUserName(NameUtils.createRandomZHName(new Random().nextInt(4) + 1));

            //更新最末用户
            DaoUtilsStore.getInstance().getUserDaoUtils().update(user);

            queryAllUser();

            Toast.makeText(this, "更新用户", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.btn_query) {
            mUserAdapter.setNewData(new ArrayList<User>());

            btnQuery.postDelayed(new Runnable() {
                @Override
                public void run() {
                    queryAllUser();
                }
            }, 1000);

            Toast.makeText(this, "查询用户", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryAllUser() {
        mUserList = DaoUtilsStore.getInstance().getUserDaoUtils().queryAll();
        mUserAdapter.setNewData(mUserList);
        rvUser.smoothScrollToPosition(mUserList.size() - 1);
    }

    private void initUser() {

        //用户ID生成器
        mIdWorker = new SnowflakeIdGenerator(0, 0);

        DaoUtilsStore.getInstance().getUserDaoUtils().deleteAll();

        mUserList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId((long) i);
            user.setUserId(String.valueOf(mIdWorker.nextId()));
            // 随机生成汉语名称
            user.setUserName(NameUtils.createRandomZHName(random.nextInt(4) + 1));
            user.setAge(18 + random.nextInt(10));
            mUserList.add(user);
        }

        mUserAdapter = new UserAdapter(mUserList);
        rvUser.setAdapter(mUserAdapter);

        DaoUtilsStore.getInstance().getUserDaoUtils().insertMultiple(mUserList);
    }

}
