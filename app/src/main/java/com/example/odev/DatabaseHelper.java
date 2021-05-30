package com.example.odev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;



public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 7;


    private static final String DATABASE_NAME = "UserManager.db";


    private static final String TABLE_USER = "user";
    private static final String TABLE_QUESTION = "question";
    private static final String TABLE_IMAGE = "image";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_SURNAME = "user_surname";
    private static final String COLUMN_USER_BIRTH = "user_birth";
    private static final String COLUMN_USER_PHONE = "user_phone";
    private static final String COLUMN_USER_IMG = "user_img";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String COLUMN_QUESTION_ID = "question_id";
    private static final String COLUMN_QUESTION_QUESTION = "question_question";
    private static final String COLUMN_QUESTION_A = "question_a";
    private static final String COLUMN_QUESTION_B = "question_b";
    private static final String COLUMN_QUESTION_C = "question_c";
    private static final String COLUMN_QUESTION_D = "question_d";
    private static final String COLUMN_QUESTION_ANSWER = "question_answer";
    private static final String COLUMN_QUESTION_LEVEL = "question_level";

    private static final String COLUMN_IMAGE_ID = "image_id";
    private static final String COLUMN_IMAGE_USER_ID = "image_user_id";
    private static final String COLUMN_IMAGE_IMAGE_BITMAP = "image_bitmap";


    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"+ COLUMN_USER_SURNAME + " TEXT,"+ COLUMN_USER_BIRTH + " TEXT,"+ COLUMN_USER_PHONE + " TEXT," + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + "("
            + COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_QUESTION_QUESTION + " TEXT,"+ COLUMN_QUESTION_A + " TEXT,"+ COLUMN_QUESTION_B + " TEXT,"+ COLUMN_QUESTION_C + " TEXT,"+ COLUMN_QUESTION_D + " TEXT,"+ COLUMN_QUESTION_ANSWER + " TEXT,"
             + COLUMN_QUESTION_LEVEL + " TEXT" + ")";

    private String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("
            + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_IMAGE_USER_ID + " INTEGER," + COLUMN_IMAGE_IMAGE_BITMAP + " BLOB" + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_QUESTION_TABLE = "DROP TABLE IF EXISTS " + TABLE_QUESTION;
    private String DROP_IMAGE_TABLE = "DROP TABLE IF EXISTS " + TABLE_IMAGE;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_IMAGE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_QUESTION_TABLE);
        db.execSQL(DROP_IMAGE_TABLE);

        onCreate(db);

    }


    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_SURNAME, user.getSurname());
        values.put(COLUMN_USER_BIRTH, user.getBirth());
        values.put(COLUMN_USER_PHONE, user.getPhone());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());


        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public boolean checkUser(String email) {


        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();


        String selection = COLUMN_USER_EMAIL + " = ?";


        String[] selectionArgs = {email};


        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    public boolean checkUser(String email, String password) {
        
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";


        String[] selectionArgs = {email, password};


        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public User getUser(String email) {

        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_SURNAME,
                COLUMN_USER_BIRTH,
                COLUMN_USER_PHONE,
                COLUMN_USER_EMAIL,
                COLUMN_USER_PASSWORD,
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_USER_EMAIL + " = ?" ;


        String[] selectionArgs = {email};


        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

                User user = new User();
        if (cursor.moveToFirst()) {
            do {

                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setSurname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_SURNAME)));
                user.setBirth(cursor.getString(cursor.getColumnIndex(COLUMN_USER_BIRTH)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return user;
    }
    public Question getQuestion(Integer id) {

        String[] columns = {
                COLUMN_QUESTION_ID,
                COLUMN_QUESTION_QUESTION,
                COLUMN_QUESTION_A,
                COLUMN_QUESTION_B,
                COLUMN_QUESTION_C,
                COLUMN_QUESTION_D,
                COLUMN_QUESTION_ANSWER,
                COLUMN_QUESTION_LEVEL
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_QUESTION_ID + " = ?" ;


        String[] selectionArgs = {id.toString()};


        Cursor cursor = db.query(TABLE_QUESTION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Question question = new Question();
        if (cursor.moveToFirst()) {
            do {

                question.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ID))));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_QUESTION)));
                question.setA(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_A)));
                question.setB(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_B)));
                question.setC(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_C)));
                question.setD(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_D)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ANSWER)));
                question.setLevel(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_LEVEL)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return question;
    }
    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_QUESTION, question.getQuestion());
        values.put(COLUMN_QUESTION_A, question.getA());
        values.put(COLUMN_QUESTION_B, question.getB());
        values.put(COLUMN_QUESTION_C, question.getC());
        values.put(COLUMN_QUESTION_D, question.getD());
        values.put(COLUMN_QUESTION_ANSWER, question.getAnswer());
        values.put(COLUMN_QUESTION_LEVEL, question.getLevel());

        db.insert(TABLE_QUESTION, null, values);
        db.close();

    }
    public void updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_QUESTION, question.getQuestion());
        values.put(COLUMN_QUESTION_A, question.getA());
        values.put(COLUMN_QUESTION_B, question.getB());
        values.put(COLUMN_QUESTION_C, question.getC());
        values.put(COLUMN_QUESTION_D, question.getD());
        values.put(COLUMN_QUESTION_ANSWER, question.getAnswer());
        values.put(COLUMN_QUESTION_LEVEL, question.getLevel());
        // updating row
        db.update(TABLE_QUESTION, values, COLUMN_QUESTION_ID + " = ?",
                new String[]{String.valueOf(question.getId())});
        db.close();
    }
    public void deleteQuestion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTION, COLUMN_QUESTION_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public ArrayList<Question> getAllQuestion() {

        String[] columns = {
                COLUMN_QUESTION_ID,
                COLUMN_QUESTION_QUESTION,
                COLUMN_QUESTION_A,
                COLUMN_QUESTION_B,
                COLUMN_QUESTION_C,
                COLUMN_QUESTION_D,
                COLUMN_QUESTION_ANSWER,
                COLUMN_QUESTION_LEVEL
        };

        String sortOrder =
                COLUMN_QUESTION_ID + " ASC";
        ArrayList<Question> questionList = new ArrayList<Question>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTION,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ID))));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_QUESTION)));
                question.setA(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_A)));
                question.setB(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_B)));
                question.setC(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_C)));
                question.setD(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_D)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ANSWER)));
                question.setLevel(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_LEVEL)));

                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return questionList;
    }

    public ArrayList<Question> getQuizQuestions(int diffLevel) {

        String[] columns = {
                COLUMN_QUESTION_ID,
                COLUMN_QUESTION_QUESTION,
                COLUMN_QUESTION_A,
                COLUMN_QUESTION_B,
                COLUMN_QUESTION_C,
                COLUMN_QUESTION_D,
                COLUMN_QUESTION_ANSWER,
                COLUMN_QUESTION_LEVEL
        };

        String sortOrder =
                COLUMN_QUESTION_ID + " ASC";
        ArrayList<Question> questionList = new ArrayList<Question>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_QUESTION_LEVEL + " = ?" ;
        String[] selectionArgs = {String.valueOf(diffLevel)};
        Cursor cursor = db.query(TABLE_QUESTION,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ID))));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_QUESTION)));
                question.setA(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_A)));
                question.setB(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_B)));
                question.setC(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_C)));
                question.setD(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_D)));
                question.setAnswer(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_ANSWER)));
                question.setLevel(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION_LEVEL)));

                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return questionList;
    }


    public void storeImage (Image image) {

            SQLiteDatabase db = this.getWritableDatabase();
            Bitmap imageToStoreBitmap = image.getImage();
            ByteArrayOutputStream byteToArray = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteToArray);

            byte[] imageInBytes= byteToArray.toByteArray();

            ContentValues values = new ContentValues();

            values.put(COLUMN_IMAGE_ID, image.getUserId());
            values.put(COLUMN_IMAGE_IMAGE_BITMAP, imageInBytes);
            values.put(COLUMN_IMAGE_USER_ID,image.getUserId());

            db.insert(TABLE_IMAGE, null, values);

    }
    public Image getImage (User user) {

        String[] columns = {
                COLUMN_IMAGE_ID,
                COLUMN_IMAGE_USER_ID,
                COLUMN_IMAGE_IMAGE_BITMAP,

        };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_IMAGE_USER_ID + " = ?" ;


        String[] selectionArgs = {String.valueOf(user.getId())};
        Cursor cursor = db.query(TABLE_IMAGE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Image image = new Image();
        if (cursor.moveToFirst()) {
            do {

                image.setImageId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_ID))));
                image.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_USER_ID))));
                byte [] bytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE_IMAGE_BITMAP));
                Bitmap imageBitMap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                image.setImage(imageBitMap);


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return image;
    }
}