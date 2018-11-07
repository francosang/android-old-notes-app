package com.bios.android.notesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.bios.android.notesapp.dto.Note;
import com.bios.android.notesapp.dto.User;
import com.bios.android.notesapp.util.GsonUtils;
import com.bios.android.notesapp.util.URLs;

import org.json.JSONObject;

import static com.bios.android.notesapp.activity.NotesActivity.EXTRA_USER_ID;

public class NoteActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText contentEdit;
    private EditText nombreEdit;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        this.requestQueue = Volley.newRequestQueue(this);

        this.nombreEdit = (EditText) findViewById(R.id.activity_notas_tv_name);
        this.contentEdit = (EditText) findViewById(R.id.activity_notas_tv_content);

        Button saveNoteButton = (Button) findViewById(R.id.activity_notas_btn_save);
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        this.user = (User) getIntent().getSerializableExtra(EXTRA_USER_ID);
    }

    public void saveNote() {
        String url = URLs.NOTES;

        Note note = new Note();
        note.setContent(contentEdit.getText().toString());
        note.setName(nombreEdit.getText().toString());
        note.setId_user(user.getId());

        JSONObject jsonObject = GsonUtils.toJSON(note);

        Log.i("JSON", jsonObject.toString());
        Log.i("URL", url);

        JsonObjectRequest saveNoteRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        saveSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error();
                    }
                }
        );

        requestQueue.add(saveNoteRequest);
    }

    private void error() {
        Toast.makeText(this, "Error...", Toast.LENGTH_LONG).show();

    }

    private void saveSuccess() {
        Toast.makeText(this, "Nota creada correctamente", Toast.LENGTH_LONG).show();
        finish();
    }
}
