package com.example.cc;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "userInfo.db";
    public static final String TABLE_NAME_USERS = "users";
    public static final String TABLE_NAME_CATEGORIES = "categories";
    public static final String TABLE_NAME_MODULES = "modules";
    public static final String TABLE_NAME_TUTORPROFILE = "tutorProfile";
    public static final String TABLE_NAME_STUDENTPROFILE = "studentProfile";
    public static final String TABLE_NAME_TUTORMODULES = "tutorModules";
    public static final String TABLE_NAME_BOOKING = "booking";
    public static final String TABLE_NAME_CONFIRMEDBOOKING = "confirmedBooking";
    public static final String TABLE_NAME_FAQ = "faq";
    public static final String TABLE_NAME_PRICE = "price";
    public static final String TABLE_NAME_LOCATION = "location";
    public static final String TABLE_NAME_REVIEW = "review";

    // Review Table Columns
    private static final String COL_REVIEW_ID = "ID";
    private static final String COL_REVIEW_TUTOR_NAME = "TUTOR_NAME";
    private static final String COL_REVIEW_TEXT = "REVIEW_TEXT";
    private static final String COL_REVIEW_RATING = "RATING";

    // Location Table Columns
    private static final String COL_LOCATION_TUTOR_NAME = "TUTOR_NAME";
    private static final String COL_LOCATION_LATITUDE = "LATITUDE";
    private static final String COL_LOCATION_LONGITUDE = "LONGITUDE";
    private static final String COL_LOCATION_ADDRESS = "ADDRESS";

    //FAQs Table Columns
    public static final String COL_FAQ_ID = "ID";
    public static final String COL_FAQ_QUESTION = "QUESTION";
    public static final String COL_FAQ_ANSWER = "ANSWER";

    // Prices Table Columns
    public static final String COL_PRICE_ID = "id";
    public static final String COL_PRICE_LABEL = "label";
    public static final String COL_PRICE_AMOUNT = "amount";

    // Users Table Columns
    public static final String COL_USER_ID = "ID";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_USERTYPE = "USERTYPE";

    // Categories Table Columns
    public static final String COL_CATEGORY_ID = "ID";
    public static final String COL_CATEGORY_NAME = "NAME";

    // Modules Table Columns
    public static final String COL_MODULE_ID = "ID";
    public static final String COL_MODULE_NAME = "NAME";
    public static final String COL_CATEGORY_ID_FK = "CATEGORY_ID";

    // TutorProfile Table Columns
    public static final String COL_TUTOR_NAME = "NAME";
    public static final String COL_TUTOR_FIRSTNAME = "FIRSTNAME";
    public static final String COL_TUTOR_LASTNAME = "LASTNAME";
    public static final String COL_TUTOR_PHONENUMBER = "PHONENUMBER";

    // StudentProfile Table Columns
    public static final String COL_STUDENT_NAME = "NAME";
    public static final String COL_STUDENT_FIRSTNAME = "FIRSTNAME";
    public static final String COL_STUDENT_LASTNAME = "LASTNAME";
    public static final String COL_STUDENT_PHONENUMBER = "PHONENUMBER";

    //TutorModules Table Column
    public static final String COL_TUTORMODULE_TUTORID = "TUTOR_ID";
    public static final String COL_TUTORMODULE_MODULEID = "MODULE_ID";

    //Booking requests Table Column
    public static final String COL_BOOKING_ID = "BOOKING_ID";
    public static final String COL_BOOKING_TUTORNAME = "TUTORNAME";
    public static final String COL_BOOKING_STUDENTNAME = "STUDENTNAME";
    public static final String COL_BOOKING_MODULENAME = "MODULENAME";
    public static final String COL_BOOKING_DATE = "DATE";
    public static final String COL_BOOKING_TIME = "TIME";
    public static final String COL_BOOKING_DURATION = "DURATION";
    public static final String COL_BOOKING_STATUS = "STATUS";

    //Booking confirmed Table Column
    public static final String COL_CONFIRMEDBOOKING_ID = "BOOKING_ID";
    public static final String COL_CONFIRMEDBOOKING_TUTORNAME = "TUTORNAME";
    public static final String COL_CONFIRMEDBOOKING_STUDENTNAME = "STUDENTNAME";
    public static final String COL_CONFIRMEDBOOKING_MODULENAME = "MODULENAME";
    public static final String COL_CONFIRMEDBOOKING_DATE = "DATE";
    public static final String COL_CONFIRMEDBOOKING_TIME = "TIME";
    public static final String COL_CONFIRMEDBOOKING_DURATION = "DURATION";
    public static final String COL_CONFIRMEDBOOKING_STATUS = "STATUS";

    public static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT, " +
                COL_USERTYPE + " TEXT)");

        // Create Categories Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_CATEGORIES + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT NOT NULL)");

        // Create Modules Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_MODULES + " (" +
                COL_MODULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MODULE_NAME + " TEXT NOT NULL, " +
                COL_CATEGORY_ID_FK + " INTEGER, " +
                "FOREIGN KEY (" + COL_CATEGORY_ID_FK + ") REFERENCES " + TABLE_NAME_CATEGORIES + "(" + COL_CATEGORY_ID + "))");

        // Create TutorProfile Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_TUTORPROFILE + " (" +
                COL_TUTOR_NAME + " TEXT, " +
                COL_TUTOR_FIRSTNAME + " TEXT, " +
                COL_TUTOR_LASTNAME + " TEXT, " +
                COL_TUTOR_PHONENUMBER + " TEXT, " +
                "FOREIGN KEY (" + COL_TUTOR_NAME + ") REFERENCES " + TABLE_NAME_USERS + "(" + COL_USERNAME + "))");

        // Create Tutor_Modules Table (Junction Table)
        db.execSQL("CREATE TABLE " + TABLE_NAME_TUTORMODULES + " (" +
                COL_TUTORMODULE_TUTORID + " TEXT, " +
                COL_TUTORMODULE_MODULEID + " TEXT, " +
                "FOREIGN KEY (" + COL_TUTORMODULE_TUTORID + ") REFERENCES " + TABLE_NAME_USERS + "(" + COL_USER_ID + "), " +
                "FOREIGN KEY (" + COL_TUTORMODULE_MODULEID + ") REFERENCES " + TABLE_NAME_MODULES + "(" + COL_MODULE_ID + "))");

        // Create StudentProfile Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_STUDENTPROFILE + " (" +
                COL_STUDENT_NAME + " TEXT, " +
                COL_STUDENT_FIRSTNAME + " TEXT, " +
                COL_STUDENT_LASTNAME + " TEXT, " +
                COL_STUDENT_PHONENUMBER + " TEXT, " +
                "FOREIGN KEY (" + COL_STUDENT_NAME + ") REFERENCES " + TABLE_NAME_USERS + "(" + COL_USERNAME + "))");

        // Create Booking Requests Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_BOOKING + " (" +
                COL_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOKING_TUTORNAME + " TEXT NOT NULL, " +
                COL_BOOKING_STUDENTNAME + " TEXT NOT NULL, " +
                COL_BOOKING_MODULENAME + " TEXT NOT NULL, " +
                COL_BOOKING_DATE + " TEXT NOT NULL, " +
                COL_BOOKING_TIME + " TEXT NOT NULL, " +
                COL_BOOKING_DURATION + " TEXT NOT NULL, " +
                COL_BOOKING_STATUS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + COL_BOOKING_MODULENAME + ") REFERENCES " + TABLE_NAME_MODULES + "(" + COL_MODULE_NAME + "), " +
                "FOREIGN KEY (" + COL_BOOKING_TUTORNAME + ") REFERENCES " + TABLE_NAME_TUTORPROFILE + "(" + COL_TUTOR_NAME + "), " +
                "FOREIGN KEY (" + COL_BOOKING_STUDENTNAME + ") REFERENCES " + TABLE_NAME_STUDENTPROFILE + "(" + COL_STUDENT_NAME + "))");

        // Create Booking Confirmed Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_CONFIRMEDBOOKING + " (" +
                COL_CONFIRMEDBOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CONFIRMEDBOOKING_TUTORNAME + " TEXT NOT NULL, " +
                COL_CONFIRMEDBOOKING_STUDENTNAME + " TEXT NOT NULL, " +
                COL_CONFIRMEDBOOKING_MODULENAME + " TEXT NOT NULL, " +
                COL_CONFIRMEDBOOKING_DATE + " TEXT NOT NULL, " +
                COL_CONFIRMEDBOOKING_TIME + " TEXT NOT NULL, " +
                COL_CONFIRMEDBOOKING_DURATION + " TEXT NOT NULL, " +
                COL_CONFIRMEDBOOKING_STATUS + " INTEGER, " +
                "FOREIGN KEY (" + COL_BOOKING_MODULENAME + ") REFERENCES " + TABLE_NAME_MODULES + "(" + COL_MODULE_NAME + "), " +
                "FOREIGN KEY (" + COL_BOOKING_TUTORNAME + ") REFERENCES " + TABLE_NAME_TUTORPROFILE + "(" + COL_TUTOR_NAME + "), " +
                "FOREIGN KEY (" + COL_BOOKING_STUDENTNAME + ") REFERENCES " + TABLE_NAME_STUDENTPROFILE + "(" + COL_STUDENT_NAME + "))");

        // Create FAQ Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_FAQ + " (" +
                COL_FAQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FAQ_QUESTION + " TEXT NOT NULL, " +
                COL_FAQ_ANSWER + " TEXT NOT NULL)");

        // Create Price Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_PRICE + " (" +
                COL_PRICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRICE_LABEL + " TEXT NOT NULL, " +
                COL_PRICE_AMOUNT + " TEXT NOT NULL)");

        // Create Location Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_LOCATION + " (" +
                COL_LOCATION_TUTOR_NAME + " TEXT, " +
                COL_LOCATION_LATITUDE + " REAL, " +
                COL_LOCATION_LONGITUDE + " REAL, " +
                COL_LOCATION_ADDRESS + " TEXT, " +
                "FOREIGN KEY (" + COL_LOCATION_TUTOR_NAME + ") REFERENCES " + TABLE_NAME_TUTORPROFILE + "(" + COL_TUTOR_NAME + "))");

        // Create Review Table
        db.execSQL("CREATE TABLE " + TABLE_NAME_REVIEW + " (" +
                COL_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REVIEW_TUTOR_NAME + " TEXT, " +
                COL_REVIEW_TEXT + " TEXT, " +
                COL_REVIEW_RATING + " INTEGER, " +
                "FOREIGN KEY (" + COL_REVIEW_TUTOR_NAME + ") REFERENCES " + TABLE_NAME_TUTORPROFILE + "(" + COL_TUTOR_NAME + "))");

        // Insert initial data into Categories and Modules tables
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TUTORPROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENTPROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TUTORMODULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BOOKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONFIRMEDBOOKING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAQ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REVIEW);
        onCreate(db);
    }

    public Cursor getTutorLocation(String tutorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT LATITUDE, LONGITUDE, ADDRESS FROM Location WHERE TUTOR_NAME = ?", new String[]{tutorName});
    }

    public void insertPrices() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if there are any rows in the price table
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_PRICE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0); // Get the count of rows

            // If there are no rows, populate the table
            if (count == 0) {
                ContentValues values = new ContentValues();
                String[] prices = {"Free", "R100", "R150", "R200"};

                for (String price : prices) {
                    values.put(COL_PRICE_LABEL, price);
                    values.put(COL_PRICE_AMOUNT, convertPriceToAmount(price));
                    db.insert(TABLE_NAME_PRICE, null, values);
                }
            }
            cursor.close();
        }
        db.close();
    }


    private double convertPriceToAmount(String price) {
        switch (price) {
            case "Free":
                return 0.0;
            case "R100":
                return 100.0;
            case "R150":
                return 150.0;
            case "R200":
                return 200.0;
            default:
                return 0.0;
        }
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insert initial categories
        db.execSQL("INSERT INTO " + TABLE_NAME_CATEGORIES + " (" + COL_CATEGORY_NAME + ") VALUES ('C#'), ('CSS'), ('JAVA'), ('PYTHON'), ('HTML'), ('C++'), ('SQL'), ('RUBY'), ('SWIFT'), ('R')");

        // Insert initial modules
        db.execSQL("INSERT INTO " + TABLE_NAME_MODULES + " (" + COL_MODULE_NAME + ", " + COL_CATEGORY_ID_FK + ") VALUES " +
                "('WRAV101', 1), ('WRAV102', 1), ('IRUD301', 1), " +
                "('WDV201', 5), ('WDV202', 5), " +
                "('GMDN101', 6), ('GMDN102', 6), ('DGD301', 6), " +
                "('BGTA101', 7), ('DTMG401', 7), " +
                "('INDA102', 8), ('ADTA302', 8), " +
                "('INRB201', 10), ('ANRB302', 10), " +
                "('INOS302', 9), ('IOS202', 9), ('CNF01', 9), " +
                "('IWDV201', 2), ('ACSD301', 2), " +
                "('IDBD201', 3), ('IMOB301', 3), " +
                "('IAPP301', 4), ('IEMT302', 4)");
    }

    public boolean insertData(String username, String password, String userType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_USERTYPE, userType);
        long result = db.insert(TABLE_NAME_USERS, null, contentValues);
        return result != -1;
    }

    public boolean insertReview(String tutorname, String text, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_REVIEW_TUTOR_NAME, tutorname);
        contentValues.put(COL_REVIEW_TEXT, text);
        contentValues.put(COL_REVIEW_RATING, rating);
        long result = db.insert(TABLE_NAME_REVIEW, null, contentValues);
        return result != -1;
    }

    public boolean insertProfile(String username, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TUTOR_NAME, username);
        contentValues.put(COL_TUTOR_FIRSTNAME, firstName);
        contentValues.put(COL_TUTOR_LASTNAME, lastName);
        contentValues.put(COL_TUTOR_PHONENUMBER, phoneNumber);
        long result = db.insert(TABLE_NAME_TUTORPROFILE, null, contentValues);
        return result != -1;
    }

    public boolean insertStudentProfile(String username, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STUDENT_NAME, username);
        contentValues.put(COL_STUDENT_FIRSTNAME, firstName);
        contentValues.put(COL_STUDENT_LASTNAME, lastName);
        contentValues.put(COL_STUDENT_PHONENUMBER, phoneNumber);
        long result = db.insert(TABLE_NAME_STUDENTPROFILE, null, contentValues);
        return result != -1;
    }
    public void insertFAQ(String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FAQ_QUESTION, question);
        values.put(COL_FAQ_ANSWER, answer);
        db.insert(TABLE_NAME_FAQ, null, values);
        db.close();
    }
    public boolean insertBooking(String tutorName, String studentName, String moduleName, String date, String time, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOKING_TUTORNAME, tutorName);
        contentValues.put(COL_BOOKING_STUDENTNAME, studentName);
        contentValues.put(COL_BOOKING_MODULENAME, moduleName);
        contentValues.put(COL_BOOKING_DATE, date);
        contentValues.put(COL_BOOKING_TIME, time);
        contentValues.put(COL_BOOKING_DURATION, duration);
        long result = db.insert(TABLE_NAME_BOOKING, null, contentValues);
        return result != -1;
    }

    // Insert location into the Location table
    public boolean insertLocationData(String tutorName, double latitude, double longitude, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LOCATION_TUTOR_NAME, tutorName);
        values.put(COL_LOCATION_LATITUDE, latitude);
        values.put(COL_LOCATION_LONGITUDE, longitude);
        values.put(COL_LOCATION_ADDRESS, address);

        long result = db.insert(TABLE_NAME_LOCATION, null, values);
        return result != -1;  // If result is -1, insertion failed
    }

    // Update TutorModule Table
    public boolean updateProfile(String username, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TUTOR_FIRSTNAME, firstName);
        contentValues.put(COL_TUTOR_LASTNAME, lastName);
        contentValues.put(COL_TUTOR_PHONENUMBER, phoneNumber);

        // Update the tutor's profile where the username matches
        int result = db.update(TABLE_NAME_TUTORPROFILE, contentValues, COL_TUTOR_NAME + " = ?", new String[]{username});

        // Check if the update was successful (result > 0 means the update was successful)
        return result > 0;
    }

    public boolean updateStudentProfile(String username, String firstName, String lastName, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STUDENT_FIRSTNAME, firstName);
        contentValues.put(COL_STUDENT_LASTNAME, lastName);
        contentValues.put(COL_STUDENT_PHONENUMBER, phoneNumber);

        // Update the student's profile where the username matches
        int result = db.update(TABLE_NAME_STUDENTPROFILE, contentValues, COL_STUDENT_NAME + " = ?", new String[]{username});

        // Check if the update was successful (result > 0 means the update was successful)
        return result > 0;
    }

    public Integer deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("users", "ID = ?", new String[]{id});
    }

    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE USERNAME=? AND PASSWORD=?", new String[]{username, password});
    }

    public List<String> getAllPrices() {
        List<String> prices = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Corrected the table name and column name in the query
        Cursor cursor = db.query(TABLE_NAME_PRICE, // Use the correct table name
                new String[]{COL_PRICE_LABEL}, // Use the correct column name
                null, null, null, null, null);

        if (cursor != null) {
            try {
                // Use getColumnIndexOrThrow to avoid issues with missing columns
                int labelIndex = cursor.getColumnIndexOrThrow(COL_PRICE_LABEL);
                while (cursor.moveToNext()) {
                    prices.add(cursor.getString(labelIndex));
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return prices;
    }

    public Cursor getFAQ() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_FAQ, null);
    }
    public Cursor getUserProfile(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE USERNAME=?", new String[]{username});
    }

    public Cursor getTutorProfile(String tutorUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_TUTORPROFILE + " WHERE NAME=?", new String[]{tutorUsername});
    }

    public Cursor getStudentProfile(String studentUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_STUDENTPROFILE + " WHERE NAME=?", new String[]{studentUsername});
    }

    public Cursor getAllBookings() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_BOOKING, null);
    }


    public Cursor getBookingsByTutorName(String tutorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_BOOKING + " WHERE " + COL_BOOKING_TUTORNAME + " = ? AND " + COL_BOOKING_STATUS + " = 0",
                new String[]{tutorName});
    }

    public Cursor getConfirmedBooking(String tutorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_BOOKING +
                        " WHERE " + COL_BOOKING_TUTORNAME + " = ?" +
                        " AND " + COL_BOOKING_STATUS + " = 1",
                new String[]{tutorName});
    }

    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS, null);
    }

    public Cursor getAllModules() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_MODULES, null);
    }

    public Cursor getAllTutorProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_TUTORPROFILE, null);
    }

    public Cursor getAllStudentProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_STUDENTPROFILE, null);
    }

    public Cursor getAllTutorModules() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_TUTORMODULES, null);
    }

    public Cursor getAllReviews() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_REVIEW, null);
    }

    public Cursor getAllLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_LOCATION, null);
    }

    // Method to delete all modules from the modules table
    public void deleteAllModules() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_MODULES, null, null);
    }

    // Retrieve all FAQs from the database
    public List<FAQ> getAllFAQs() {
        List<FAQ> faqList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Adjust the table name and column names according to your database structure
        Cursor cursor = db.rawQuery("SELECT " + COL_FAQ_QUESTION + ", " + COL_FAQ_ANSWER + " FROM " + TABLE_NAME_FAQ, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Ensure correct column names and indices
                int questionIndex = cursor.getColumnIndex(COL_FAQ_QUESTION);
                int answerIndex = cursor.getColumnIndex(COL_FAQ_ANSWER);

                if (questionIndex != -1 && answerIndex != -1) {
                    String question = cursor.getString(questionIndex);
                    String answer = cursor.getString(answerIndex);
                    faqList.add(new FAQ(question, answer));
                } else {
                    Log.e("DatabaseHelper", "Error: Column index not found");
                }

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return faqList;
    }

    // Method to get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CATEGORIES, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    // Method to get modules by category ID
    public List<Module> getModulesByCategory(int categoryId) {
        List<Module> modules = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_MODULES + " WHERE " + COL_CATEGORY_ID_FK + " = ?", new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            do {
                // Ensure the column indices are correct
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COL_MODULE_ID));
                @SuppressLint("Range") int categoryIdFromDb = cursor.getInt(cursor.getColumnIndex(COL_CATEGORY_ID_FK));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COL_MODULE_NAME));
                Module module = new Module(id, categoryIdFromDb, name);
                modules.add(module);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modules;
    }

    // Method to get tutor ID by username
    public int getTutorIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USER_ID + " FROM " + TABLE_NAME_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int tutorId = cursor.getInt(cursor.getColumnIndex(COL_USER_ID));
            cursor.close();
            return tutorId;
        }
        cursor.close();
        return -1; // Return -1 if not found
    }

    // Method to get student ID by username
    public int getStudentIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USER_ID + " FROM " + TABLE_NAME_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int studentId = cursor.getInt(cursor.getColumnIndex(COL_USER_ID));
            cursor.close();
            return studentId;
        }
        cursor.close();
        return -1; // Return -1 if not found
    }

    public boolean assignTutorToModule(int tutorId, int moduleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tutor_id", tutorId);
        contentValues.put("module_id", moduleId);
        long result = db.insert("tutorModules", null, contentValues);
        return result != -1;
    }

    public List<String> getModuleNames() {
        List<String> moduleNames = new ArrayList<>();
        Cursor cursor = getAllModules();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COL_MODULE_NAME));
                moduleNames.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return moduleNames;
    }

    public int getModuleIdByName(String moduleName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_MODULE_ID + " FROM " + TABLE_NAME_MODULES + " WHERE " + COL_MODULE_NAME + " = ?", new String[]{moduleName});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int moduleId = cursor.getInt(cursor.getColumnIndex(COL_MODULE_ID));
            cursor.close();
            return moduleId;
        }
        cursor.close();
        return -1;  // Return -1 if not found
    }

    public ArrayList<TutorProfile> getTutorsByModule(String moduleId) {
        ArrayList<TutorProfile> tutorProfiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch tutor profiles by module
        String query = "SELECT tp." + COL_TUTOR_NAME + ", tp." + COL_TUTOR_FIRSTNAME + ", tp." + COL_TUTOR_LASTNAME + ", tp." + COL_TUTOR_PHONENUMBER +
                " FROM " + TABLE_NAME_TUTORPROFILE + " tp" +
                " INNER JOIN " + TABLE_NAME_USERS + " u ON tp." + COL_TUTOR_NAME + " = u." + COL_USERNAME +
                " INNER JOIN " + TABLE_NAME_TUTORMODULES + " tm ON u." + COL_USER_ID + " = tm." + COL_TUTORMODULE_TUTORID +
                " WHERE tm." + COL_TUTORMODULE_MODULEID + " = ?";

        // Log the query being executed
        Log.d("DatabaseHelper", "Executing query: " + query + " with moduleId: " + moduleId);

        Cursor cursor = db.rawQuery(query, new String[]{moduleId});

        if (cursor.moveToFirst()) {
            do {
                String tutorName = cursor.getString(cursor.getColumnIndexOrThrow(COL_TUTOR_NAME));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COL_TUTOR_FIRSTNAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_TUTOR_LASTNAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_TUTOR_PHONENUMBER));

                // Log the tutor details
                Log.d("DatabaseHelper", "Tutor Found: " + tutorName + ", " + firstName + " " + lastName);

                tutorProfiles.add(new TutorProfile(tutorName, firstName, lastName, phoneNumber));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tutorProfiles;
    }

    public boolean confirmBooking(String tutorName, String studentName, String moduleName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOKING_STATUS, 1);

        // Update the booking status where tutor name, student name, and module name match
        int result = db.update(TABLE_NAME_BOOKING, contentValues,
                COL_BOOKING_TUTORNAME + " = ? AND " + COL_BOOKING_STUDENTNAME + " = ? AND " + COL_BOOKING_MODULENAME + " = ?",
                new String[]{tutorName, studentName, moduleName});
        return result > 0;
    }

    public boolean declineBooking(String tutorName, String studentName, String moduleName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOKING_STATUS, 2);

        // Update the booking status where tutor name, student name, and module name match
        int result = db.update(TABLE_NAME_BOOKING, contentValues,
                COL_BOOKING_TUTORNAME + " = ? AND " + COL_BOOKING_STUDENTNAME + " = ? AND " + COL_BOOKING_MODULENAME + " = ?",
                new String[]{tutorName, studentName, moduleName});
        return result > 0;
    }

    // Method to update booking status based on booking ID
    public void updateBookingStatusInDatabase(int bookingId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_STATUS, status); // Set new status (0 = pending, 1 = confirmed, 2 = declined)

        // Update the booking status where the booking ID matches
        int rowsAffected = db.update(TABLE_NAME_BOOKING, values, COL_BOOKING_ID + " = ?", new String[]{String.valueOf(bookingId)});
        db.close();

        // Optional: Log or handle if no rows were affected
        if (rowsAffected == 0) {
            Log.d("DatabaseHelper", "No booking found with ID: " + bookingId);
        }
    }

    public String getLoggedInTutorName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String TutorName = null;

        Cursor cursor = db.rawQuery("SELECT " + COL_USERNAME + " FROM " + TABLE_NAME_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COL_USERNAME));
            TutorName = name;
        }

        cursor.close();
        return TutorName;
    }

    public String getLoggedInStudentName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String studentName = null;

        Cursor cursor = db.rawQuery("SELECT " + COL_USERNAME + " FROM " + TABLE_NAME_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COL_USERNAME));
            studentName = name;
        }

        cursor.close();
        return studentName;
    }

    // Method to get all bookings made by the logged-in student
    public Cursor getStudentBookings(String studentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_BOOKING +
                " WHERE " + COL_BOOKING_STUDENTNAME + " = ?";
        return db.rawQuery(query, new String[]{studentName});
    }

    public Cursor getReviewsForTutor(String tutorName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_REVIEW +
                " WHERE " + COL_REVIEW_TUTOR_NAME + " = ?";
        return db.rawQuery(query, new String[] { tutorName });
    }

}
