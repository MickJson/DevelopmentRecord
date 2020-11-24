package com.nianlun.objectboxdb.database;

import android.content.Context;

import com.nianlun.objectboxdb.entity.MyObjectBox;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore mBoxStore;

    public static void init(Context context) {
        mBoxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get() { return mBoxStore; }
}