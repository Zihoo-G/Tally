package com.gazikel.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gazikel.tally.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }

    /*创建数据库的方法
    * 只有项目第一次运行时会被调用*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_type(id integer primary key autoincrement, " +
                "typename varchar(10)," +
                "imageId integer," +
                "sImageId integer," +
                "kind integer)";
        db.execSQL(sql);

        insertType(db);

        /*创建记账表*/
        String sql2 = "create table tb_account(id integer primary key autoincrement, " +
                "typename varchar(10), " +
                "sImageId integer, " +
                "mark varchar(80), " +
                "money float, " +
                "time varchar(60), " +
                "year integer, " +
                "month integer, " +
                "day integer, " +
                "kind integer) ";
        db.execSQL(sql2);
    }

    /**
     * 数据库版本在更新时发生改变，会调用此方法*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertType(SQLiteDatabase db) {
        //向tb_type表中插入元素
        String sql = "insert into tb_type(typename, imageId, sImageId, kind) values(?,?,?,?)";
        db.execSQL(sql, new Object[]{"其它", R.mipmap.ic_other, R.mipmap.ic_other, 0});
        db.execSQL(sql, new Object[]{"餐饮", R.mipmap.ic_catering, R.mipmap.ic_catering_selected, 0});
        db.execSQL(sql, new Object[]{"日用", R.mipmap.ic_daily, R.mipmap.ic_daily_selected, 0});
        db.execSQL(sql, new Object[]{"水果", R.mipmap.ic_fruit, R.mipmap.ic_fruit_selected, 0});
        db.execSQL(sql, new Object[]{"娱乐", R.mipmap.ic_recreation, R.mipmap.ic_recreation_selected, 0});
        db.execSQL(sql, new Object[]{"宠物", R.mipmap.ic_pet, R.mipmap.ic_pet_selected, 0});
        db.execSQL(sql, new Object[]{"学习", R.mipmap.ic_study, R.mipmap.ic_study_selected, 0});
        db.execSQL(sql, new Object[]{"交通", R.mipmap.ic_traffic, R.mipmap.ic_traffic_selected, 0});
        db.execSQL(sql, new Object[]{"医疗", R.mipmap.ic_medical, R.mipmap.ic_medical_selected, 0});

        db.execSQL(sql, new Object[]{"其它", R.mipmap.ic_other, R.mipmap.ic_other, 1});
        db.execSQL(sql, new Object[]{"工资", R.mipmap.ic_salary, R.mipmap.ic_salary_selected, 1});
        db.execSQL(sql, new Object[]{"兼职", R.mipmap.ic_part_time, R.mipmap.ic_part_time_selected, 1});
        db.execSQL(sql, new Object[]{"理财", R.mipmap.ic_transaction, R.mipmap.ic_transaction_selected, 1});
        db.execSQL(sql, new Object[]{"礼金", R.mipmap.ic_gift, R.mipmap.ic_gift_selected, 1});

    }
}
