package com.example.samsung.p0531_simplecursortreeadapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by samsung on 03.03.2017.
 */

public class DB {
    //Константы для формирования команд к БД
    private static final String
        CREATE_TABLE = "create table ", INTEGER = " integer ", TEXT = " text ",
        PRIMARY_KEY = " primary key ", AUTOINCREMENT = " autoincrement ";
    //Версия и имя БД
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mydb";
    /**Таблица компаний: её имя, поля и команда создания */
    public static final String COMPANY_COLUMN_ID = "_id",
                                COMPANY_COLUMN_NAME = "name";
    private static final String COMPANY_TABLE = "copany",
            COMPANY_TABLE_CREATE = CREATE_TABLE + COMPANY_TABLE + "("
                    + COMPANY_COLUMN_ID + INTEGER + PRIMARY_KEY + ", "
                    + COMPANY_COLUMN_NAME + TEXT + ");";
    /**Таблица телефонов: её имя, поля и команда создания*/
    public static final String PHONE_COLUMN_ID = "_id",
                               PHONE_COLUMN_NAME = "name",
                               PHONE_COLUMN_COMPANY = "company";
    private static final String PHONE_TABLE = "phone",
            PHONE_TABLE_CREATE = CREATE_TABLE + PHONE_TABLE + "("
                    + PHONE_COLUMN_ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + ", "
                    + PHONE_COLUMN_NAME + TEXT + ", "
                    + PHONE_COLUMN_COMPANY + INTEGER + ");";
    private final Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;

    public DB (Context context) {
        mContext = context;
    }

    //Метод открытия покдключения
    public void openDB() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDb = mDBHelper.getWritableDatabase();
    }

    //Метод закрытия подключения
    public void closeDB() {
        if (mDBHelper != null) mDBHelper.close();
    }

    //Метод получения данных из таблицы компаний
    public Cursor getCompanyData() {
        return mDb.query(COMPANY_TABLE, null, null, null, null, null, null);
    }

    //Метод получения данных из таблицы телефонов по конкретной компании
    public Cursor getPhoneData(long copanyID) {
        return mDb.query(PHONE_TABLE, null, PHONE_COLUMN_COMPANY + " = " + copanyID, null, null, null, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /**Данные рнаходятся в массивах companys, phonesHTC, phonesSams и phonesLG
             * в файле strings.xml. Создаём БД и заполняем её этими данными
             */
            ContentValues cv = new ContentValues();
            db.beginTransaction();
            try {
                db.execSQL(COMPANY_TABLE_CREATE);
                String[] companys = mContext.getResources().getStringArray(R.array.companys).clone();
                int index = 1;
                for (String company :
                        companys) {
                    cv.put(COMPANY_COLUMN_ID, index);
                    cv.put(COMPANY_COLUMN_NAME, company);
                    db.insert(COMPANY_TABLE, null, cv);
                    index++;
                }

                cv.clear();
                db.execSQL(PHONE_TABLE_CREATE);
                String[] phones = mContext.getResources().getStringArray(R.array.phonesHTC).clone();
                for (String phoneHTC :
                        phones) {
                    cv.put(PHONE_COLUMN_COMPANY, 1);
                    cv.put(COMPANY_COLUMN_NAME, phoneHTC);
                    db.insert(PHONE_TABLE, null, cv);
                }
                phones = mContext.getResources().getStringArray(R.array.phonesSams).clone();
                for (String phoneSams :
                        phones) {
                    cv.put(PHONE_COLUMN_COMPANY, 2);
                    cv.put(COMPANY_COLUMN_NAME, phoneSams);
                    db.insert(PHONE_TABLE, null, cv);
                }
                phones = mContext.getResources().getStringArray(R.array.phonesLG).clone();
                for (String phoneLG :
                        phones) {
                    cv.put(PHONE_COLUMN_COMPANY, 3);
                    cv.put(COMPANY_COLUMN_NAME, phoneLG);
                    db.insert(PHONE_TABLE, null, cv);
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
