package com.example.student.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author somelou
 * @description
 * @date 2019-04-22
 */
public abstract class UpgradeDB {
    public abstract void update(SQLiteDatabase db);
}
