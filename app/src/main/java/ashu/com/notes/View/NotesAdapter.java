package ashu.com.notes.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ashu.com.notes.Model.Notes;
import ashu.com.notes.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context context;
    private List<Notes> notesList;

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public ImageView dot;

        public NotesViewHolder(View view) {
            super(view);
            note = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.img_note);
        }
    }

    public NotesAdapter(Context context, List<Notes> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Notes notes = notesList.get(position);

        holder.note.setText(notes.getNote());
        holder.dot.setImageResource(R.drawable.outline_note_black_18dp);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
