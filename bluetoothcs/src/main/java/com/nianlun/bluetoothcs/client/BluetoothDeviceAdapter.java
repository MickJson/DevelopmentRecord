package com.nianlun.bluetoothcs.client;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nianlun.bluetoothcs.R;

import java.util.ArrayList;

public class BluetoothDeviceAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    private int selectedPosition = -1;

    public BluetoothDeviceAdapter() {
        super(R.layout.recyclerview_bluetooth_item, new ArrayList<BluetoothDevice>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.tv_bluetooth_item, item.getName() + "--" + item.getAddress());
        helper.itemView.setBackgroundColor(selectedPosition == helper.getAdapterPosition() ?
                Color.GRAY : Color.TRANSPARENT);
    }

    public void setSelectPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

}

