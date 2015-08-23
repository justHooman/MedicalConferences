package vn.minhtdh.demo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.Map;
import java.util.Set;

import vn.minhtdh.demo.model.User;

/**
 * Created by minhtdh on 8/23/15.
 */
public class DbUtils {


    static class SqlBuilder {
        StringBuilder bld = new StringBuilder();

        SqlBuilder insertSql(String table, ContentValues cv) {
            bld.append("INSERT INTO ").append(table);
            if (cv.size() > 0) {
                Set<Map.Entry<String, Object>> sets = cv.valueSet();
                StringBuilder colBld = new StringBuilder();
                StringBuilder valBld = new StringBuilder(" values(");
                bld.append(" (");
                boolean notFirst = false;
                for (Map.Entry<String, Object> entry : sets) {
                    if (notFirst) {
                        colBld.append(", ");
                        valBld.append(", ");
                    }
                    colBld.append(entry.getKey());
                    if (entry.getValue() instanceof String) {
                        valBld.append("'").append(entry.getValue()).append("'");
                    } else {
                        valBld.append(entry.getValue());
                    }
                    notFirst = true;
                }
                bld.append(colBld).append(") ").append(valBld)
                        .append(")");
            }
            return this;
        }

        String build() {
            return bld.toString();
        }
    }

    public long insertUser(SQLiteDatabase db, User user) {
        ContentValues cv= new ContentValues();
        cv.put(DbHelper.USER_MAIL, user.userMail);
        cv.put(DbHelper.USER_ROLES, user.roles);
        String sql = new SqlBuilder().insertSql(DbHelper.TABLE_USER, cv).build();
        SQLiteStatement statement =  db.compileStatement(sql);
        return statement.executeInsert();
    }

    static class ArrayHolder {
        int[] colIds;
    }
    public User queryUser(SQLiteDatabase db, String mail) {
        User ret = null;
        Cursor c = db.query(DbHelper.TABLE_USER, null, DbHelper.USER_MAIL + "=?", new
                String[]{mail}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                ret = readUserFromCursor(new ArrayHolder(), c);
            }
            c.close();
        }
        return  ret;
    }

    private User readUserFromCursor(ArrayHolder holder, Cursor c) {
        User ret = null;
        int[] colIds = holder.colIds;
        if (colIds == null) {
            colIds = new int[3];// number of col in user table
            colIds[0] = c.getColumnIndex(DbHelper.USER_ID);
            colIds[1] = c.getColumnIndex(DbHelper.USER_MAIL);
            colIds[2] = c.getColumnIndex(DbHelper.USER_ROLES);
            holder.colIds = colIds;
        }
        ret = new User();
        final int UNKNOW_COL = -1;
        if (colIds[0] != UNKNOW_COL) {
            ret.userId = c.getLong(colIds[0]);
        }
        if (colIds[1] != UNKNOW_COL) {
            ret.userMail = c.getString(colIds[1]);
        }
        if (colIds[2] != UNKNOW_COL) {
            ret.roles = c.getString(colIds[2]);
        }
        return ret;
    }
}
