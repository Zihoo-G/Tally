package com.gazikel.tally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gazikel.tally.utils.FloatUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责管理数据库的类
 * 主要对于表当中的内容进行操作
 */
public class DBManager {

    private static SQLiteDatabase db;

    public static void initDB(Context context) {
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 读取数据库当中的数据，写入内存集合里
     * kind:
     *  0 支出
     *  1 收入
     */

    public static List<TypeBean> getTypeList(int kind) {
        ArrayList<TypeBean> list = new ArrayList<>();
        String sql = "select * from tb_type where kind="+kind;
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    /**
     * 向记账表当中插入一条元素
     * */

    public static void addAccount(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put("typename", bean.getTypename());
        values.put("sImageId", bean.getsImageId());
        values.put("mark", bean.getMark());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("day", bean.getDay());
        values.put("month", bean.getMonth());
        values.put("year", bean.getYear());
        values.put("kind", bean.getKind());

        db.insert("tb_account", null, values);
    }

    /*获取记账表中某一天的所有支出或者记账情况*/
    public static List<AccountBean> getAccountListOneDay(int year, int month, int day){
        ArrayList<AccountBean> list = new ArrayList<>();

        String sql = "select * from tb_account where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});

        /*遍历符合要求的每一行数据*/
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String mark = cursor.getString(cursor.getColumnIndex("mark"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));

            AccountBean accountBean = new AccountBean(id, typename, sImageId, mark, money, time, year, month, day, kind);
            list.add(accountBean);

        }

        return list;
    }

    /**
     * 获取某一个月的收入与支出情况
     * @param year
     * @param month
     * @return
     */
    public static List<AccountBean> getAccountListOneMonthFromAccount(int year, int month){
        List<AccountBean> list = new ArrayList<>();

        String sql = "select * from tb_account where year=? and month=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month)});

        /*遍历符合要求的每一行数据*/
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String mark = cursor.getString(cursor.getColumnIndex("mark"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));

            AccountBean accountBean = new AccountBean(id, typename, sImageId, mark, money, time, year, month, day, kind);
            list.add(accountBean);

        }

        return list;
    }

    /*获取某一天支出或者收入的总金额*/
    public static float getSumMoneyOneDay(int year, int month, int day, int kind) {
        float total = 0;

        String sql = "select sum(money) from tb_account where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(kind)});

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        return total;
    }

    /*获取某一月支出或者收入的总金额*/
    public static float getSumMoneyOneMonth(int year, int month, int kind) {
        float total = 0;

        String sql = "select sum(money) from tb_account where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(kind)});

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        return total;
    }

    /*根据id删除一条数据*/
    public static int deleteAccount(int id) {
        int i = db.delete("tb_account", "id=?", new String[]{String.valueOf(id)});
        return i;
    }

    /**
     * 统计某月份支出或者收入情况有多少条
     */
    public static int getCountItenOneMonth(int year, int month, int kind) {
        int total = 0;
        String sql = "select count(money) from tb_account where year=? and month = ? and kind = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex("count(money)"));
        }
        return total;
    }

    /**
     * 查询指定年份和月份的收入和支出每一种类型的总钱数
     */
    public static List<ChartItemBean> getChartListFromAccount(int year, int month , int kind) {
        List<ChartItemBean> list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);

        String sql = "select typename, sImageId, sum(money) as total from tb_account where year=? and month = ? and kind = ? group by typename order by total desc";

        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while(cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            float ratio = FloatUtil.div(total, sumMoneyOneMonth);

            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }

        return list;
    }

    /**
     * 获取这个月当中某一天收入支出的最大金额
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static float getMaxMoneyOneDayInMonth(int year, int month, int kind) {
        String sql = "select sum(money) from tb_account where year=? and month=? and kind=? group by day order by sum(money) desc";

        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});

        if (cursor.moveToFirst()) {
            return cursor.getFloat(cursor.getColumnIndex("sum(money)"));
        }
        return 0;
    }

    public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year, int month, int kind) {
        String sql = "select day, sum(money) from tb_account where year=? and month =? and kind = ? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});

        List<BarChartItemBean> list= new ArrayList<>();

        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean barChartItemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(barChartItemBean);
        }
        return list;
    }


    public static List<AccountBean> getAccountListByRemarkFromAccount(String mark) {
        List<AccountBean> list=  new ArrayList<>();

        String sql = "select * from tb_account where mark like '%" + mark + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            String realMark = cursor.getString(cursor.getColumnIndex("mark"));


            AccountBean accountBean = new AccountBean(id, typename, sImageId, realMark, money, time, year, month, day, kind);
            System.out.println(accountBean.getMark());
            list.add(accountBean);
        }

        return list;
    }

    public static List<Integer> getYearListFromAccount() {
        List<Integer> list = new ArrayList<>();

        String sql = "select distinct(year) from tb_account order by year";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            System.out.println(cursor.getInt(cursor.getColumnIndex("year")));
            list.add(cursor.getInt(cursor.getColumnIndex("year")));
        }

        return list;
    }

}


