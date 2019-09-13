package ashu.com.notes.View;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ashu.com.notes.Helper.LoggedInSharedPreference;
import ashu.com.notes.R;

public class HomeActivity extends AppCompatActivity {

    NotesFragment notesFragment;
    LoginFragment loginFragment;
    FragmentTransaction transaction;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        notesFragment = new NotesFragment();
        loginFragment = new LoginFragment();
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();


        transaction.add(R.id.frameLayout, notesFragment, "notes_frag").commit();

    }


}
