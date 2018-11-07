package com.bios.android.notesapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bios.android.notesapp.R;
import com.bios.android.notesapp.dto.Login;
import com.bios.android.notesapp.dto.User;
import com.bios.android.notesapp.util.GsonUtils;
import com.bios.android.notesapp.util.URLs;

import org.json.JSONObject;

/**
 * A login screen that offers login via emailEditText/passwordEditText.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        requestQueue = Volley.newRequestQueue(this);

        nameEditText = (EditText) findViewById(R.id.activity_register_et_name);
        emailEditText = (EditText) findViewById(R.id.activity_register_et_email);
        passwordEditText = (EditText) findViewById(R.id.activity_register_et_pass);

        Button save = (Button) findViewById(R.id.activity_register_btn_save);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save() {
        // Reset errors.
        nameEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        } else if (!isNameValid(password)) {
            nameEditText.setError(getString(R.string.error_invalid_name));
            focusView = nameEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            User login = new User();
            login.setName(name);
            login.setEmail(email);
            login.setPass(password);

            register(login);
        }
    }

    private void register(User login) {
        String url = URLs.USERS;

        JSONObject jsonObject = GsonUtils.toJSON(login);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(request);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.trim().length() >= 8;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.trim().length() >= 3;
    }

    private boolean isNameValid(String name) {
        return name.trim().length() >= 3;
    }

}

