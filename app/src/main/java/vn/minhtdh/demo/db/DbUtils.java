package vn.minhtdh.demo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vn.minhtdh.demo.model.Conference;
import vn.minhtdh.demo.model.Participant;
import vn.minhtdh.demo.model.Topic;
import vn.minhtdh.demo.model.User;
import vn.minhtdh.demo.utils.Contanst;

/**
 * Created by minhtdh on 8/23/15.
 */
public class DbUtils {


    static class SqlBuilder {
        StringBuilder bld = new StringBuilder();

        SqlBuilder insertSql(String table, ContentValues cv) {
            bld.append("INSERT OR IGNORE INTO ").append(table);
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

        SqlBuilder whereQuery(String... colsName) {
            boolean notFirst = false;
            for (String col : colsName) {
                if (notFirst) {
                    bld.append(" and ");
                }
                bld.append(col).append("=?");
                notFirst = true;
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

    public List<User> queryUsers(SQLiteDatabase db) {
        List<User> ret = null;
        Cursor c = db.query(DbHelper.TABLE_USER, null, null, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                User user = null;
                ret = new ArrayList<User>();
                ArrayHolder holder = new ArrayHolder();
                do {
                    user = readUserFromCursor(holder, c);
                    ret.add(user);
                } while (c.moveToNext());
            }
            c.close();
        }
        return ret;
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

    public long insertConference(SQLiteDatabase db, Conference conference) {
        ContentValues cv= new ContentValues();
        cv.put(DbHelper.CONFERENCE_TITLE, conference.title);
        cv.put(DbHelper.LOCATION, conference.location);
        cv.put(DbHelper.TIME_START, conference.timeStart);
        cv.put(DbHelper.TIME_END, conference.timeEnd);
        String sql = new SqlBuilder().insertSql(DbHelper.TABLE_CONFERENCE, cv).build();
        SQLiteStatement statement =  db.compileStatement(sql);
        return statement.executeInsert();
    }

    public List<Conference> getConferences(SQLiteDatabase db, long id) {
        List<Conference> ret = null;
        Cursor c = db.query(DbHelper.TABLE_CONFERENCE, null, null, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                Conference item = null;
                ret = new ArrayList<Conference>();
                ArrayHolder holder = new ArrayHolder();
                do {
                    item = readConference(holder, c);
                    ret.add(item);
                } while (c.moveToNext());
            }
            c.close();
        }
        return ret;
    }

    public Conference readConference(ArrayHolder holder, Cursor c) {
        Conference ret = null;
        int[] colIds = holder.colIds;
        if (colIds == null) {
            colIds = new int[5];// number of col in user table
            colIds[0] = c.getColumnIndex(DbHelper.CONFERENCE_ID);
            colIds[1] = c.getColumnIndex(DbHelper.CONFERENCE_TITLE);
            colIds[2] = c.getColumnIndex(DbHelper.LOCATION);
            colIds[3] = c.getColumnIndex(DbHelper.TIME_START);
            colIds[4] = c.getColumnIndex(DbHelper.TIME_END);
            holder.colIds = colIds;
        }
        ret = new Conference();
        final int UNKNOW_COL = -1;
        if (colIds[0] != UNKNOW_COL) {
            ret.setConferenceId(c.getLong(colIds[0]));
        }
        if (colIds[1] != UNKNOW_COL) {
            ret.title = c.getString(colIds[1]);
        }
        if (colIds[2] != UNKNOW_COL) {
            ret.location = c.getString(colIds[2]);
        }
        if (colIds[3] != UNKNOW_COL) {
            ret.timeStart = c.getLong(colIds[3]);
        }
        if (colIds[4] != UNKNOW_COL) {
            ret.timeEnd = c.getLong(colIds[4]);
        }
        return ret;
    }

    public boolean insertParticipants(SQLiteDatabase db, Conference conference, List<User> users) {
        ContentValues cv= new ContentValues();
        db.beginTransaction();
        for (User user : users) {
            cv.clear();
            cv.put(DbHelper.CONFERENCE_ID, conference.conferenceId);
            cv.put(DbHelper.USER_ID, user.userId);
            cv.put(DbHelper.PARTICIPANT_STATUS, Contanst.ParticipantState.PENDING);
            String sql = new SqlBuilder().insertSql(DbHelper.TABLE_CONFERENCE, cv).build();
            SQLiteStatement statement = db.compileStatement(sql);
            statement.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return true;
    }

    public long insertTopic(SQLiteDatabase db, Topic topic) {
        ContentValues cv= new ContentValues();
        cv.put(DbHelper.TOPIC_TITLE, topic.title);
        cv.put(DbHelper.TOPIC_CONTENT, topic.content);
        cv.put(DbHelper.CONFERENCE_ID, topic.conferenceId);
        cv.put(DbHelper.TOPIC_STATUS, topic.status);
        cv.put(DbHelper.LOCATION, topic.location);
        cv.put(DbHelper.TIME_START, topic.timeStart);
        cv.put(DbHelper.TIME_END, topic.timeEnd);
        String sql = new SqlBuilder().insertSql(DbHelper.TABLE_CONFERENCE, cv).build();
        SQLiteStatement statement =  db.compileStatement(sql);
        return statement.executeInsert();
    }

    public List<Topic> getTopics(SQLiteDatabase db, long confernceId, int status) {
        List<Topic> ret = null;
        Cursor c = db.query(DbHelper.TABLE_TOPIC, null, new SqlBuilder().whereQuery(DbHelper.CONFERENCE_ID, DbHelper.TOPIC_STATUS).build(), new String[]{String.valueOf(confernceId), String.valueOf(status)}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                Topic item = null;
                ret = new ArrayList<Topic>();
                ArrayHolder holder = new ArrayHolder();
                do {
                    item = readTopic(holder, c);
                    ret.add(item);
                } while (c.moveToNext());
            }
            c.close();
        }
        return ret;
    }

    public Topic readTopic(ArrayHolder holder, Cursor c) {
        Topic ret = null;
        int[] colIds = holder.colIds;
        if (colIds == null) {
            colIds = new int[7];// number of col in user table
            colIds[0] = c.getColumnIndex(DbHelper.TOPIC_ID);
            colIds[1] = c.getColumnIndex(DbHelper.TOPIC_TITLE);
            colIds[2] = c.getColumnIndex(DbHelper.LOCATION);
            colIds[3] = c.getColumnIndex(DbHelper.TIME_START);
            colIds[4] = c.getColumnIndex(DbHelper.TIME_END);
            colIds[5] = c.getColumnIndex(DbHelper.CONFERENCE_ID);
            colIds[6] = c.getColumnIndex(DbHelper.TOPIC_CONTENT);
            holder.colIds = colIds;
        }
        ret = new Topic();
        final int UNKNOW_COL = -1;
        if (colIds[0] != UNKNOW_COL) {
            ret.setTopicId(c.getLong(colIds[0]));
        }
        if (colIds[1] != UNKNOW_COL) {
            ret.title = c.getString(colIds[1]);
        }
        if (colIds[2] != UNKNOW_COL) {
            ret.location = c.getString(colIds[2]);
        }
        if (colIds[3] != UNKNOW_COL) {
            ret.timeStart = c.getLong(colIds[3]);
        }
        if (colIds[4] != UNKNOW_COL) {
            ret.timeEnd = c.getLong(colIds[4]);
        }
        if (colIds[5] != UNKNOW_COL) {
            ret.conferenceId = c.getLong(colIds[5]);
        }
        if (colIds[6] != UNKNOW_COL) {
            ret.content = c.getString(colIds[6]);
        }
        return ret;
    }

}
