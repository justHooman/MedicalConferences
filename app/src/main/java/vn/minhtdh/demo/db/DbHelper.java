package vn.minhtdh.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import vn.minhtdh.demo.model.User;
import vn.minhtdh.demo.utils.Contanst;

/**
 * Created by minhtdh on 8/23/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Conferences.db";
    public static final int DB_VERSION = 1;

    private static volatile DbHelper mInstance;

    private static final Object sLock = new Object();

    public static void initialIns(Context pContext) {
        if (mInstance == null) {
            synchronized (sLock) {
                if (mInstance == null) {
                    mInstance = new DbHelper(pContext);
                }
            }
        }
    }

    public static DbHelper getIns() {
        return mInstance;
    }

    private DbHelper(final Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.d("tag", TABLE_USER_CREATE_SQL);
        Log.d("tag", TABLE_CONFERENCE_CREATE_SQL);
        Log.d("tag", TABLE_PARTICIPANTS_CREATE_SQL);
        Log.d("tag", TABLE_TOPIC_CREATE_SQL);
        Log.d("tag", TABLE_HOSTS_CREATE_SQL);

        db.execSQL(TABLE_USER_CREATE_SQL);
        db.execSQL(TABLE_CONFERENCE_CREATE_SQL);
        db.execSQL(TABLE_PARTICIPANTS_CREATE_SQL);
        db.execSQL(TABLE_TOPIC_CREATE_SQL);
        db.execSQL(TABLE_HOSTS_CREATE_SQL);
        User root = new User();
        root.userMail = "minhtdh89@gmail.com";
        root.roles = Contanst.UserRole.ADMIN;
        new DbUtils().insertUser(db, root);
        User user = new User();
        user.userMail = "minhtdhk52@gmail.com";
        new DbUtils().insertUser(db, user);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        SqlTableBuilder.dropTable(TABLE_USER);
        SqlTableBuilder.dropTable(TABLE_CONFERENCE);
        SqlTableBuilder.dropTable(TABLE_PARTICIPANTS);
        SqlTableBuilder.dropTable(TABLE_TOPIC);
        SqlTableBuilder.dropTable(TABLE_HOSTS);
        onCreate(db);
    }

    public static final String TABLE_USER = "users";
    public static final String USER_ID = "userId";
    public static final String USER_MAIL = "userMail";
    public static final String USER_ROLES = "userRoles";
    public static final String TABLE_USER_CREATE_SQL;
    static  {
        SqlTableBuilder bld = new SqlTableBuilder().createTable(TABLE_USER)
                .addNumKeyAutoIncreament(USER_ID)
                .addTextColUnique(USER_MAIL)
                .addTextCol(USER_ROLES)
                .setKey(USER_ID)
                .end();
        TABLE_USER_CREATE_SQL = bld.build();
    }

    public static final String TABLE_CONFERENCE = "conferences";
    public static final String CONFERENCE_ID = "conferenceId";
    public static final String CONFERENCE_TITLE = "title";
    public static final String LOCATION = "location";
    public static final String TIME_START = "timeStart";
    public static final String TIME_END = "timeEnd";
    public static final String TABLE_CONFERENCE_CREATE_SQL;
    static {
        SqlTableBuilder bld = new SqlTableBuilder().createTable(TABLE_CONFERENCE)
                .addNumKeyAutoIncreament(CONFERENCE_ID)
                .addTextCol(CONFERENCE_TITLE)
                .addTextCol(LOCATION)
                .addNumCol(TIME_START)
                .addNumCol(TIME_END)
                .setKey(CONFERENCE_ID)
                .end();
        TABLE_CONFERENCE_CREATE_SQL = bld.build();
    }

    public static final String TABLE_PARTICIPANTS = "participants";
    public static final String PARTICIPANT_STATUS = "participantStatus";
    public static final String TABLE_PARTICIPANTS_CREATE_SQL;
    static {
        SqlTableBuilder bld = new SqlTableBuilder().createTable(TABLE_PARTICIPANTS)
                .addNumCol(CONFERENCE_ID)
                .addNumCol(USER_ID)
                .addNumCol(PARTICIPANT_STATUS)
                .setKey(CONFERENCE_ID, USER_ID)
                .end();
        TABLE_PARTICIPANTS_CREATE_SQL = bld.build();
    }

    public static final String TABLE_TOPIC = "topics";
    public static final String TOPIC_ID = "topicId";
    public static final String TOPIC_TITLE = "title";
    public static final String TOPIC_CONTENT = "content";
    public static final String TABLE_TOPIC_CREATE_SQL;
    static {
        SqlTableBuilder bld = new SqlTableBuilder().createTable(TABLE_TOPIC)
                .addNumKeyAutoIncreament(TOPIC_ID)
                .addTextCol(TOPIC_TITLE)
                .addTextCol(TOPIC_CONTENT)
                .addTextCol(LOCATION)
                .addNumCol(TIME_START)
                .addNumCol(TIME_END)
                .setKey(TOPIC_ID)
                .end();
        TABLE_TOPIC_CREATE_SQL = bld.build();
    }

    public static final String TABLE_HOSTS = "hosts";
    public static final String TABLE_HOSTS_CREATE_SQL;
    static {
        SqlTableBuilder bld = new SqlTableBuilder().createTable(TABLE_HOSTS)
                .addNumCol(TOPIC_ID)
                .addNumCol(USER_ID)
                .setKey(TOPIC_ID, USER_ID)
                .end();
        TABLE_HOSTS_CREATE_SQL = bld.build();
    }


    static class SqlTableBuilder {

        static String dropTable(String tableName) {
            return new StringBuilder("Drop table if exists ").append(tableName).toString();
        }

        StringBuilder bld;

        SqlTableBuilder() {
            bld = new StringBuilder();
        }

        SqlTableBuilder createTable(String tableName) {
            bld.append("Create table if not exists ").append(tableName).append(" (");
            return this;
        }

        SqlTableBuilder addNumKeyAutoIncreament(String keyName) {
            bld.append(keyName).append(" INTEGER , ");//AUTOINCREMENT
            return this;
        }

        SqlTableBuilder addTextColUnique(String colName) {
            bld.append(colName).append(" TEXT UNIQUE, ");
            return this;
        }

        SqlTableBuilder addTextCol(String colName) {
            bld.append(colName).append(" TEXT, ");
            return this;
        }

        SqlTableBuilder addNumCol(String colName) {
            bld.append(colName).append(" INTEGER, ");
            return this;
        }

        SqlTableBuilder setKey(String... colNames) {
            bld.append("PRIMARY KEY (");
            //column1, column2))
            for (int i=0; i< colNames.length; i++) {
                if (i > 0) {
                    bld.append(", ");
                }
                bld.append(colNames[i]);
            }
            bld.append(")");
            return this;
        }

        SqlTableBuilder end() {
            bld.append(");");
            return this;
        }

        String build() {
            return bld.toString();
        }
    }
}
