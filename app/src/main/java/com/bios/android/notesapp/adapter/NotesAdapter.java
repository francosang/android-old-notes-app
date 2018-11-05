package com.bios.android.notesapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bios.android.notesapp.R;
import com.bios.android.notesapp.dto.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sistemas on 29/10/2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private OnNoteSelectedListener onNoteSelectedListener;
    private OnNoteDetailListener onDetailListener;

    public NotesAdapter() {
        this.notes = new ArrayList<>();
    }

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View elementoTitular = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);

        return new NoteViewHolder(elementoTitular);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder view, int pos) {
        view.bind(notes.get(pos));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setOnNoteSelectedListener(OnNoteSelectedListener onNoteSelectedListener) {
        this.onNoteSelectedListener = onNoteSelectedListener;
    }

    public void setOnDetailListener(OnNoteDetailListener onDetailListener) {
        this.onDetailListener = onDetailListener;
    }

    public interface OnNoteSelectedListener {
        void onClick(Note note);
    }

    public interface OnNoteDetailListener {
        void onDetail(Note note);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView titulo;
        private TextView content;

        public NoteViewHolder(View item) {
            super(item);

            titulo = item.findViewById(R.id.item_nota_tv_titulo);
            content = item.findViewById(R.id.item_nota_tv_content);
        }

        public void bind(final Note note) {
            titulo.setText(note.getName());
            content.setText(note.getContent());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteSelectedListener != null) {
                        onNoteSelectedListener.onClick(note);
                    }
                }
            });
        }
    }
}
