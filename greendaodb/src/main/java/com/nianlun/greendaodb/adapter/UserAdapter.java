package com.nianlun.greendaodb.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nianlun.greendaodb.R;
import com.nianlun.greendaodb.entity.User;

import java.util.List;

public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public UserAdapter(List<User> list) {
        super(R.layout.recyclerview_item_user,list);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_id_item, item.getUserId());
        helper.setText(R.id.tv_name_item, item.getUserName());
        helper.setText(R.id.tv_age_item, String.valueOf(item.getAge()));
    }

}
