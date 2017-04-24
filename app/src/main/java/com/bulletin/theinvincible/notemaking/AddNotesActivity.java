package com.bulletin.theinvincible.notemaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNotesActivity extends AppCompatActivity {

    public Button addNOtes;
    EditText editNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_notes);
        editNotes = (EditText) findViewById(R.id.editNotes);
        addNOtes = (Button) findViewById(R.id.addNotes);

        addNOtes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notes = editNotes.getText().toString();
                if (notes.equals("")) {

                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Strings.INTENT_NOTES_STRING, notes);
                    setResult(Strings.INTENT_RESULT_CODE, intent);
                    finish();

                }
            }
        });
    }


}
