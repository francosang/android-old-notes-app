package com.bios.android.notesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bios.android.notesapp.R;
import com.bios.android.notesapp.adapter.NotesAdapter;
import com.bios.android.notesapp.dto.Note;
import com.bios.android.notesapp.dto.User;
import com.bios.android.notesapp.util.GsonUtils;
import com.bios.android.notesapp.util.URLs;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra-user-id";

    private User user;
    private NotesAdapter adapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newNote();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        requestQueue = Volley.newRequestQueue(this);

        adapter = new NotesAdapter();
        adapter.setOnNoteSelectedListener(new NotesAdapter.OnNoteSelectedListener() {
            @Override
            public void onClick(Note note) {


            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_notes_rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        User user = (User) getIntent().getSerializableExtra(EXTRA_USER_ID);
        if (user != null) {
            this.user = user;
        }
    }

    private void newNote() {
        Intent intent = new Intent(this, NotaActivity.class);
        intent.putExtra(NotesActivity.EXTRA_USER_ID, user);

        startActivity(intent);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        if (this.user != null) {
            outState.putSerializable(EXTRA_USER_ID, this.user);
        }

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        User user = (User) savedInstanceState.getSerializable(EXTRA_USER_ID);
        if (user != null) {
            this.user = user;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        retriveNotes();
    }

    private void retriveNotes() {
        String userId = String.valueOf(user.getId());

        String url = URLs.USER_NOTES.replace("{user_id}", userId);

        Log.i("URL", url);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        updateNotes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NOTES", "Error obteniendo notas: " + error.getMessage());
                    }
                });

        requestQueue.add(request);
    }

    private void updateNotes(String response) {
        List<Note> notes = GsonUtils.parseList(response, Note[].class);
        adapter.setNotes(notes);
        adapter.notifyDataSetChanged();
    }

}
