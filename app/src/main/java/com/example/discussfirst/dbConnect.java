package com.example.discussfirst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dbConnect extends SQLiteOpenHelper {
    private static final String dbName = "UniConnectDB.db";
    private static final int dbVersion = 1;

    private static final String USERS_TABLE = "User";
    private  static  final String UNIVERSITY_TABLE = "University";
    private  static  final String DEPARTAMENT_TABLE = "Departament";
    private static final String ARTICLES_TABLE = "Article";
    private static final String ARTICLE_REPOST_TABLE = "Article_Repost_Table";
    private static final String ARTICLE_IMAGES_TABLE = "Article_Image";
    private static final String ARTICLE_TAGS_TABLE = "Article_Tag";
    private static final String ARTICLE_COMMENTS_TABLE = "Article_Comment";
    private static final String ARTICLE_LIKES_TABLE = "Article_Like";

    // Tabela per user

    private static final String ID = "id";
    private static final String USER_ID = "userId";
    private static final String USERNAME = "userName";
    private static final String FIRSTNAME = "firstName";
    private static final String LASTNAME = "lastName";
    private static final String GENDER ="gender";
    private static final String PROFILE_IMAGE = "image";
    private static final String ISBLOCKED = "isBlocked";
    private static final String PHONE_NUMBER ="phoneNumber";

    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ROLE = "role";
    //////////////////////////////////////////
    //Tabela per universitet
    private static String UID = "universityId";
    private static String UNAME ="universityname";
    // Tabela per departament
    private static String DEPARTAMENTID = "departmentId";

    private static String DEPARTAMENTNAME = "departmentname";
/////////////////////////////////////////////
    // Tabela Artikuj
    private static final String ARTICLE_USER_ID = "userId";
    private static final String ARTICLE_CONTENT = "content";
    private static final String ARTICLE_CREATED_AT = "createdAt";
    private static final String ARTICLE_CATEGORY = "category";

    private static final String IMAGE_URL = "image_url";
    private static final String ARTICLE_ID = "articleId";

    private static final String TAG = "tag";

    private static final String USER_ID_FK = "userId";
    private static final String CREATED_AT = "createdAt";
    private static final String PARENT_ID = "parentId";

  /// //////////////////////////////////////////////////////
    // Tabela per Article_Like
    private static final String CREATED_AT_LIKE = "createdAt";

    public dbConnect(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON;");

        String createUserTable = "CREATE TABLE " + USERS_TABLE + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIRSTNAME + " TEXT NOT NULL UNIQUE, "
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


        String createUniversityTable = "CREATE TABLE " + UNIVERSITY_TABLE + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UNAME + " TEXT NOT NULL UNIQUE)";
        db.execSQL(createUniversityTable);

        String createDepartmentTable = "CREATE TABLE " + DEPARTAMENT_TABLE + " ("
                + DEPARTAMENTID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DEPARTAMENTNAME + " TEXT NOT NULL, "
                + UID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + UID + ") REFERENCES " + UNIVERSITY_TABLE + "(" + UID + ") ON DELETE CASCADE)";
        db.execSQL(createDepartmentTable);


        String createArticleTable = "CREATE TABLE " + ARTICLES_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ARTICLE_USER_ID + " INTEGER NOT NULL, "
                + ARTICLE_CONTENT + " TEXT NOT NULL, "
                + ARTICLE_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + ARTICLE_CATEGORY + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + ARTICLE_USER_ID + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleTable);

        String createArticleImageTable = "CREATE TABLE " + ARTICLE_IMAGES_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + IMAGE_URL + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleImageTable);

        String createArticleTagTable = "CREATE TABLE " + ARTICLE_TAGS_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + TAG + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleTagTable);

        String createArticleCommentTable = "CREATE TABLE " + ARTICLE_COMMENTS_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_FK + " INTEGER NOT NULL, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + PARENT_ID + " INTEGER, "
                + "FOREIGN KEY(" + USER_ID_FK + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + PARENT_ID + ") REFERENCES " + ARTICLE_COMMENTS_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleCommentTable);

        String createArticleRepostTable = "CREATE TABLE " + ARTICLE_REPOST_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_FK + " INTEGER NOT NULL, "
                + ARTICLE_ID + " INTEGER NOT NULL, "
                + CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(" + USER_ID_FK + ") REFERENCES " + USERS_TABLE + "(" + USER_ID + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(" + ARTICLE_ID + ") REFERENCES " + ARTICLES_TABLE + "(" + ID + ") ON DELETE CASCADE)";
        db.execSQL(createArticleRepostTable);

        String createArticleLikeTable = "CREATE TABLE " + ARTICLE_LIKES_TABLE + " ("
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

        // Insert test data for User 1
        values.put(FIRSTNAME, "John");
        values.put(LASTNAME, "Doe");
        values.put(EMAIL, "john.doe@example.com");
        values.put(PASSWORD, "password123");
        values.put(PHONE_NUMBER, "1234567890");
        values.put(GENDER, "Male");
        values.put(DEPARTAMENTID, 1); // Example department ID
        values.put(UID, 1); // Example university ID
        values.put(PROFILE_IMAGE, "default_image_url"); // Example profile image URL
        values.put(ISBLOCKED, false); // User is not blocked
        db.insert(USERS_TABLE, null, values);

        // Clear ContentValues before inserting the second user
        values.clear();

        // Insert test data for User 2
        values.put(FIRSTNAME, "Jane");
        values.put(LASTNAME, "Smith");
        values.put(EMAIL, "jane.smith@example.com");
        values.put(PASSWORD, "password456");
        values.put(PHONE_NUMBER, "9876543210");
        values.put(GENDER, "Female");
        values.put(DEPARTAMENTID, 2); // Example department ID
        values.put(UID, 2); // Example university ID
        values.put(PROFILE_IMAGE, "default_image_url");
        values.put(ISBLOCKED, false); // User is not blocked
        db.insert(USERS_TABLE, null, values);

        // Close the database after inserting
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_REPOST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_LIKES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_COMMENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_TAGS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_IMAGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }
}
