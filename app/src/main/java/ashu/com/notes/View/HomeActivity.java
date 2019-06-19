package ashu.com.notes.View;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (LoggedInSharedPreference.getLoggedInStatus(getApplicationContext())) {
                                    LoggedInSharedPreference.setLoggedInStatus(getApplicationContext(), false);
                                    Toast.makeText(HomeActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                                    finish();
                                } else
                                    Toast.makeText(HomeActivity.this, "Please Login Again!", Toast.LENGTH_SHORT).show();

                            }
                        });

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
