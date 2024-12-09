package com.example.discussfirst;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class dbConnect extends SQLiteOpenHelper {

    private static final String dbName = "UniConnectDB.db";
    private static final int dbVersion = 1;
    private static final String test ="C:\\Users\\ACER\\Desktop\\UniConnectDB.db";
    private static final String USERS_TABLE = "User";
    private static final String UNIVERSITY_TABLE = "University";
    private static final String DEPARTAMENT_TABLE = "Departament";
    private static final String ARTICLES_TABLE = "Article";
    private static final String ARTICLE_REPOST_TABLE = "Article_Repost_Table";
    private static final String ARTICLE_IMAGES_TABLE = "Article_Image";
    private static final String ARTICLE_TAGS_TABLE = "Article_Tag";
    private static final String ARTICLE_COMMENTS_TABLE = "Article_Comment";
    private static final String ARTICLE_LIKES_TABLE = "Article_Like";

    // User table column names
    private static final String ID = "id";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "userName";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String GENDER = "gender";
    private static final String PROFILE_IMAGE = "image";
    private static final String ISBLOCKED = "isBlocked";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";

    // University table column names
    private static final String UID = "universityId";
    private static final String UNAME = "universityname";

    // Department table column names
    private static final String DEPARTAMENTID = "departmentId";
    private static final String DEPARTAMENTNAME = "departmentname";

    // Article table column names
    private static final String ARTICLE_USER_ID = "userId";
    private static final String ARTICLE_TITLE = "title";
    private static final String ARTICLE_CONTENT = "content";
    private static final String ARTICLE_CREATED_AT = "createdAt";
    private static final String ARTICLE_CATEGORY = "category";

    private static final String IMAGE_URL = "image_url";
    private static final String ARTICLE_ID = "articleId";

    private static final String TAG = "tag";
    private static final String USER_ID_FK = "userId";
    private static final String CREATED_AT = "createdAt";
    private static final String PARENT_ID = "parentId";

    private static final String CREATED_AT_LIKE = "createdAt";

    private static dbConnect instance;

    protected dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    public static synchronized dbConnect getInstance(Context context) {
        if (instance == null) {
            instance = new dbConnect(context.getApplicationContext());
            instance.setWriteAheadLoggingEnabled(false);
        }
        return instance;
    }

    public static String getUniversityTableName() {
        return UNIVERSITY_TABLE;
    }
    public static String getDepartamentTable() {
        return DEPARTAMENT_TABLE;
    }
    public static String getUniversityID() {
        return UID;
    }
    public static String getUniversityName() {
        return UNAME;
    }
    public static String getDepartamentID() {
        return DEPARTAMENTID;
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON;");

        String createUserTable = "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIRSTNAME + " TEXT NOT NULL , "
                + LASTNAME + " TEXT NOT NULL, "
                + PASSWORD + " TEXT NOT NULL, "
                + EMAIL + " TEXT UNIQUE, "
                + ROLE + " TEXT NOT NULL DEFAULT 'user', "
                + DEPARTAMENTID + " INTEGER NOT NULL, "
                + UID + " INTEGER NOT NULL, "
                + PHONE_NUMBER + " TEXT, "
                + GENDER + " TEXT, "
                + PROFILE_IMAGE + " TEXT, "
                + ISBLOCKED + " BOOLEAN, "

                + "FOREIGN KEY(" + DEPARTAMENTID + ") REFERENCES " + DEPARTAMENT_TABLE + "(" + DEPARTAMENTID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + UID + ") REFERENCES " + UNIVERSITY_TABLE + "(" + UID + ") ON DELETE CASCADE)";
        db.execSQL(createUserTable);


        String createUniversityTable = "CREATE TABLE IF NOT EXISTS  " + UNIVERSITY_TABLE + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UNAME + " TEXT NOT NULL UNIQUE)";
        db.execSQL(createUniversityTable);

        String createDepartmentTable = "CREATE TABLE IF NOT EXISTS  " + DEPARTAMENT_TABLE + " ("
                + DEPARTAMENTID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DEPARTAMENTNAME + " TEXT NOT NULL, "
                + UID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + UID + ") REFERENCES " + UNIVERSITY_TABLE + "(" + UID + ") ON DELETE CASCADE)";
        db.execSQL(createDepartmentTable);


        String createArticleTable = "CREATE TABLE IF NOT EXISTS " + ARTICLES_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ARTICLE_USER_ID + " INTEGER NOT NULL, "
                + ARTICLE_TITLE + " TEXT NOT NULL, "
                + ARTICLE_CONTENT + " TEXT NOT NULL, "
                + ARTICLE_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + ARTICLE_CATEGORY + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + ARTICLE_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleTable);

        String createArticleImageTable = "CREATE TABLE IF NOT EXISTS " + ARTICLE_IMAGES_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + IMAGE_URL + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleImageTable);

        String createArticleTagTable = "CREATE TABLE IF NOT EXISTS " + ARTICLE_TAGS_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + TAG + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleTagTable);

        String createArticleCommentTable = "CREATE TABLE IF NOT EXISTS " + ARTICLE_COMMENTS_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_FK + " INTEGER NOT NULL, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + PARENT_ID + " INTEGER, "
                + "FOREIGN KEY(" + USER_ID_FK + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + PARENT_ID + ") REFERENCES " + ARTICLE_COMMENTS_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleCommentTable);

        String createArticleRepostTable = "CREATE TABLE IF NOT EXISTS " + ARTICLE_REPOST_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_FK + " INTEGER NOT NULL, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(" + USER_ID_FK + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleRepostTable);

        String createArticleLikeTable = "CREATE TABLE IF NOT EXISTS " + ARTICLE_LIKES_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_FK + " INTEGER NOT NULL, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + CREATED_AT_LIKE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(" + USER_ID_FK + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleLikeTable);
    }
    public boolean registerUser(String firstName, String lastName, String email, String password, String phoneNumber, String gender, int departmentId, int universityId, String profileImage, boolean isBlocked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIRSTNAME, firstName);
        values.put(LASTNAME, lastName);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(PHONE_NUMBER, phoneNumber);
        values.put(GENDER, gender);
        values.put(DEPARTAMENTID, departmentId);
        values.put(UID, universityId);
        values.put(PROFILE_IMAGE, profileImage);
        values.put(ISBLOCKED, isBlocked);

        long result = db.insert(USERS_TABLE, null, values);
        db.close();

        return result != -1;
    }
    public void insertTestData() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(FIRSTNAME, "John");
        values.put(LASTNAME, "Doe");
        values.put(EMAIL, "john.doe@example.com");
        values.put(PASSWORD, "password123");
        values.put(PHONE_NUMBER, "1234567890");
        values.put(GENDER, "Male");
        values.put(DEPARTAMENTID, 1);
        values.put(UID, 1); // Example university ID
        values.put(PROFILE_IMAGE, "default_image_url"); // Example profile image URL
        values.put(ISBLOCKED, false); // User is not blocked
        db.insert(USERS_TABLE, null, values);

        values.clear();

        values.put(FIRSTNAME, "Jane");
        values.put(LASTNAME, "Smith");
        values.put(EMAIL, "jane.smith@example.com");
        values.put(PASSWORD, "password456");
        values.put(PHONE_NUMBER, "9876543210");
        values.put(GENDER, "Female");
        values.put(DEPARTAMENTID, 2);
        values.put(UID, 2);
        values.put(PROFILE_IMAGE, "default_image_url");
        values.put(ISBLOCKED, false);
        db.insert(USERS_TABLE, null, values);
        values.clear();

        values.put(UID, 1);
        values.put(UNAME, "Universiteti i Prishtinës");
        db.insert(UNIVERSITY_TABLE,null, values);
        values.clear();

        values.put(UNAME, "Universiteti Kadri Zeka");
        values.put(UID, 2);
        db.insert(UNIVERSITY_TABLE,null, values);
        values.clear();

        values.put(DEPARTAMENTID, 1);
        values.put(DEPARTAMENTNAME, "Inxhinieri Kompjuterike dhe Softuerike");
        values.put(UID, 1);
        db.insert(DEPARTAMENT_TABLE,null, values);
        values.clear();

        values.put(DEPARTAMENTID, 2);
        values.put(DEPARTAMENTNAME, "Elektroenergjetikë");
        values.put(UID, 1);
        db.insert(DEPARTAMENT_TABLE,null, values);
        values.clear();

        values.put(DEPARTAMENTNAME, "Elektronik, Automatik Robotik");
        values.put(DEPARTAMENTID, 3);
        values.put(UID, 1);
        db.insert(DEPARTAMENT_TABLE,null, values);
        values.clear();


    }
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ? AND password = ?", new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + USERS_TABLE + " RENAME TO temp_Users");

        onCreate(db);

        db.execSQL("INSERT INTO " + USERS_TABLE + " (firstname, lastname, email, password, phone_number, gender, departamentid, uid, profile_image, isblocked) " +
                "SELECT firstname, lastname, email, password, phone_number, gender, departamentid, uid, profile_image, isblocked FROM temp_Users");

    }

    public void backupDatabase(Context context) {
        try {
            File currentDB = context.getDatabasePath(test);

            File backupDir = new File(context.getExternalFilesDir(null), "Backup");
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            File backupDB = new File(backupDir, dbName);

            try (FileInputStream fis = new FileInputStream(currentDB);
                 FileOutputStream fos = new FileOutputStream(backupDB)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            System.out.println("Backup successful! Saved to: " + backupDB.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Backup failed: " + e.getMessage());
        }
    }
    public void restoreDatabase(Context context) {
        try {
            File backupDB = new File(context.getExternalFilesDir(null) + "/Backup/" + dbName);

            File currentDB = context.getDatabasePath(dbName);

            try (FileInputStream fis = new FileInputStream(backupDB);
                 FileOutputStream fos = new FileOutputStream(currentDB)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            System.out.println("Restore successful!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Restore failed: " + e.getMessage());
        }
    }


    public List<Article> getUserArticles(int userId) {
        List<Article> articles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ARTICLES_TABLE + " WHERE " + ARTICLE_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(ARTICLE_TITLE));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex(ARTICLE_CONTENT));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(ARTICLE_CATEGORY));
                @SuppressLint("Range") String createdAt = cursor.getString(cursor.getColumnIndex(ARTICLE_CREATED_AT));

//                articles.add(new Article(id, userId, title, content, category, createdAt));
                articles.add(new Article(id,userId,title,content,category,createdAt));
            } while (cursor.moveToNext());
        }//sdsd
        cursor.close();
        db.close();
        return articles;
    }

}
