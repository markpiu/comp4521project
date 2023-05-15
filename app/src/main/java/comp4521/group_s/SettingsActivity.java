package comp4521.group_s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    static FirebaseAuth auth;

    static FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            Preference resetPassword = findPreference("pref_key_reset_password");
            resetPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    String email = user.getEmail();
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "We have sent you an email to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Error Occurs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return true;
                }
            });

            SwitchPreferenceCompat pushNotifications = findPreference("pref_key_push_notifications");
            pushNotifications.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isPushNotificationsOn = (Boolean) newValue;
                    SharedPreferences sharedPref =
                            preference.getSharedPreferences();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("pref_key_notifications", isPushNotificationsOn);
                    editor.apply();

                    return true;
                }
            });

            SwitchPreferenceCompat syncData = findPreference("pref_key_sync");
            syncData.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isSyncOn = (Boolean) newValue;
                    SharedPreferences sharedPref =
                            preference.getSharedPreferences();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("pref_key_Sync", isSyncOn);
                    editor.apply();
                    return true;
                }
            });

        }

    }
}