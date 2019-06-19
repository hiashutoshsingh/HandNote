package ashu.com.notes.View;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import ashu.com.notes.DB.DBHelper;
import ashu.com.notes.Helper.LoggedInSharedPreference;
import ashu.com.notes.Model.Notes;
import ashu.com.notes.R;
import ashu.com.notes.Utils.NotesItemListener;


public class NotesFragment extends Fragment {

    private NotesAdapter mAdapter;
    private List<Notes> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private DBHelper db;
    private Button logout;
    private String google_email_id;
    GoogleSignInClient mGoogleSignInClient;


    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        noNotesView = view.findViewById(R.id.empty_notes_view);
        logout = view.findViewById(R.id.button_logout);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        google_email_id = acct.getFamilyName().trim();
        LoggedInSharedPreference.setGoogleUserId(getActivity(), google_email_id);
//        TABLE_NAME = LoggedInSharedPreference.getGoogleUserId(getActivity());
//        Toast.makeText(getActivity(), "table name: " + TABLE_NAME, Toast.LENGTH_SHORT).show();
        db = new DBHelper(getActivity());
        notesList.addAll(db.getAllNotes());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        recyclerView.addOnItemTouchListener(new NotesItemListener(getActivity(),
                recyclerView, new NotesItemListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (LoggedInSharedPreference.getLoggedInStatus(getActivity())) {

                                    LoggedInSharedPreference.setLoggedInStatus(getActivity(), false);
                                    LoggedInSharedPreference.setGoogleUserId(getActivity(), "");
                                    Fragment someFragment = new LoginFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frameLayout, someFragment);
                                    transaction.addToBackStack(null);
                                    Toast.makeText(getActivity(), "Logged out!", Toast.LENGTH_SHORT).show();
                                    transaction.commit();

                                } else
                                    Toast.makeText(getActivity(), "Please Login Again!", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

        return view;
    }

    private void createNote(String note) {
        long id = db.insertNote(note);

        Notes n = db.getNote(id);

        if (n != null) {
            notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }
    }

    private void updateNote(String note, int position) {
        Notes n = notesList.get(position);
        n.setNote(note);
        db.updateNote(n);
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    private void deleteNote(int position) {
        db.deleteNote(notesList.get(position));
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    private void showActionsDialog(final int position) {
        CharSequence charSequences[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select");
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }

    private void showNoteDialog(final boolean shouldUpdate, final Notes notes, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.notes);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.new_note) : getString(R.string.edit_note));

        if (shouldUpdate && notes != null) {
            inputNote.setText(notes.getNote());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(getActivity(), "Add Note", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && notes != null) {
                    updateNote(inputNote.getText().toString(), position);
                } else {
                    createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private void toggleEmptyNotes() {
        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }


}
