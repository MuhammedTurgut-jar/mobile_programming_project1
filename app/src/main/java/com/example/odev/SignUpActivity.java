package com.example.odev;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.widget.DatePicker;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    Button signUp,imageSelect;
    ImageButton calendar;
    EditText name, surname, birthDate, userName, passWord, rePassWord, phone;
    Uri imageUri=null;
    DatePickerDialog datePickerDialog;
    Calendar calendarView;
    int year, month, dayOfMonth;
    boolean textStatus, userStatus, formatStatus,passStatus;
    static final int SELECT_IMAGE=12;
    ImageView imageView;
    DatabaseHelper db ;
    Image image;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = new DatabaseHelper(this);
        image=new Image();
        defineVariable();
        defineListeners();


    }

    public boolean editTextCheck()
    {
        if(name.getText().toString().equals("") || surname.getText().toString().equals("") || birthDate.getText().toString().equals("") || userName.getText().toString().equals("") || passWord.getText().toString().equals("") || rePassWord.getText().toString().equals("")|| phone.getText().toString().equals("") )
            return false;

        else
            return true;

    }


    public boolean passWordCheck () {

        return passWord.getText().toString().equals(rePassWord.getText().toString());

    }

    boolean userNameFormatCheck (String email) {
        String expression = "^[\\w\\-]([.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public void defineVariable() {

        name = (EditText) findViewById(R.id.Name);
        surname = (EditText) findViewById(R.id.Surname);
        birthDate = (EditText) findViewById(R.id.BirthDate);
        userName = (EditText) findViewById(R.id.userName);
        passWord = (EditText) findViewById(R.id.Pass);
        rePassWord = (EditText) findViewById(R.id.rePass);
        phone = (EditText) findViewById(R.id.phone);
        signUp = (Button) findViewById(R.id.signUp);
        imageSelect = (Button) findViewById(R.id.imageSelect);
        calendar = (ImageButton) findViewById(R.id.calendarButton);
        imageView=(ImageView) findViewById(R.id.imageUser);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uriToBitmap(imageUri);



        }
        else if(resultCode==RESULT_CANCELED){
            Toast.makeText(SignUpActivity.this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap img = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            image.setImage(img);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void defineListeners() {
        imageSelect.setOnClickListener((v) -> {
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,SELECT_IMAGE);
        });
        calendar.setOnClickListener((v) -> {

            calendarView = Calendar.getInstance();
            year = calendarView.get(Calendar.YEAR);
            month = calendarView.get(Calendar.MONTH);
            dayOfMonth = calendarView.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    birthDate.setText(day + "." + (month+1) + "." + year);
                }
            }, year, month, dayOfMonth);
            datePickerDialog.show();

        });

        signUp.setOnClickListener((v) -> {
            if (imageUri!=null)
            {
                textStatus=editTextCheck();

                if (textStatus)
                {

                    passStatus=passWordCheck();
                    if(passStatus)
                    {
                        formatStatus = userNameFormatCheck(userName.getText().toString());

                        if (formatStatus)
                        {
                            userStatus = db.checkUser(userName.getText().toString());


                            if (!userStatus)
                            {
                                User user = new User();
                                user.setName(name.getText().toString());
                                user.setSurname(surname.getText().toString());
                                user.setBirth(birthDate.getText().toString());
                                user.setPhone(phone.getText().toString());
                                user.setEmail(userName.getText().toString());
                                user.setPassword(passWord.getText().toString());
                                db.addUser(user);
                                image.setUserId(db.getUser(user.getEmail()).getId());
                                db.storeImage(image);

                                Toast.makeText(SignUpActivity.this, "Successful!", Toast.LENGTH_SHORT).show();

                                Intent intent;
                                intent = new Intent(v.getContext(), MainActivity.class);
                                startActivity(intent);

                            }
                            else {
                                userName.setText("");
                                Toast.makeText(SignUpActivity.this, "This username/E-mail has been used before!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            userName.setText("");
                            Toast.makeText(SignUpActivity.this, "Username/E-mail is invalid!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        passWord.setText("");
                        rePassWord.setText("");
                        Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SignUpActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                }
            }
            else  {
                Toast.makeText(SignUpActivity.this, "Select image please!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

