package ashu.com.notes.View;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        notesFragment = new NotesFragment();
        loginFragment = new LoginFragment();
        FragmentManager manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();


        if (LoggedInSharedPreference.getLoggedInStatus(getApplicationContext())) {
            transaction.add(R.id.frameLayout, notesFragment, "notes_frag").commit();

        } else {
            transaction.add(R.id.frameLayout, loginFragment, "login_frag").commit();
        }
    }


}
