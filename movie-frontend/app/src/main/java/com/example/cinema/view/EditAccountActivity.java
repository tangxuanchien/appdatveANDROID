package com.example.cinema.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinema.R;
import com.example.cinema.api.ApiService;
import com.example.cinema.model.ApiResponse;
import com.example.cinema.model.Register;
import com.example.cinema.model.SignInReponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    private Button btnEditAccount;
    private EditText editName, editPhone, editEmail, editBirthday;
    private RadioGroup editGender;
    private RadioButton editMale, editFemale;
    private SharedPreferences sharedPreferences;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editBirthday = findViewById(R.id.editBirthday);
        editGender = findViewById(R.id.editGender);
        editMale = findViewById(R.id.editMale);
        editFemale = findViewById(R.id.editFemale);
        btnEditAccount = findViewById(R.id.btnEditAccount);
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        loadUserInfo();
        editBirthday.setOnClickListener(v -> showDatePickerDialog());
        btnEditAccount.setOnClickListener(v -> {
            editAccount();
        });
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditAccountActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDay);
                    Date selectedDate = calendar.getTime();
                    String formattedDate = dateFormat.format(selectedDate);
                    editBirthday.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void loadUserInfo() {
        String name = sharedPreferences.getString("userName", "");
        String phone = sharedPreferences.getString("userPhone", "");
        String email = sharedPreferences.getString("userEmail", "");
        String gender = sharedPreferences.getString("userGender", "");
        String birthdayString = sharedPreferences.getString("userBirthday", "");

        editName.setText(name);
        editPhone.setText(phone);
        editEmail.setText(email);

        if (gender.equals("nam")) {
            editMale.setChecked(true);
        } else {
            editFemale.setChecked(true);
        }

        if (!birthdayString.isEmpty()) {
            editBirthday.setText(birthdayString);
        }
    }

    private void editAccount() {
        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();
        String email = editEmail.getText().toString();
        String gender = editMale.isChecked() ? "nam" : "nữ";
        String birthdayStr = editBirthday.getText().toString();
        Date birthday;
        try {
            birthday = dateFormat.parse(birthdayStr);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setMessage("Vui lòng chọn ngày sinh hợp lệ")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            new AlertDialog.Builder(this)
                    .setMessage("User ID is missing. Please log in again.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();
            return;
        }
        Register editAccount = new Register( name, phone, email, gender, birthday);
        ApiService.apiService.editUsers(userId,editAccount).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse != null && apiResponse.getStatus().equals("success")) {
                        finish();
                    } else {
                        String errorMessage = apiResponse != null ? apiResponse.getMessage() : "Unknown error";
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage(errorMessage)
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage(errorBody)
                                .setPositiveButton("OK", null)
                                .show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        new AlertDialog.Builder(EditAccountActivity.this)
                                .setMessage("Error: " + e.getMessage())
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                throwable.printStackTrace();
                new AlertDialog.Builder(EditAccountActivity.this)
                        .setMessage("Error -> " + throwable.getMessage())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }
}