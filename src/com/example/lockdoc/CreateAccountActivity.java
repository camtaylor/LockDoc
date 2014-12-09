package com.example.lockdoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.rekap.lockdoc.R;

public class CreateAccountActivity extends ActionBarActivity {
	/*
	 * Activity that creates a user account if there is not one in place
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);
		setTitle("Create Account");
	}

	public void savePin(View v) {

		// get preferences
		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor prefEdit = getPreferences(MODE_PRIVATE).edit();

		EditText createPin = (EditText) findViewById(R.id.pin_create);
		EditText confirmPin = (EditText) findViewById(R.id.pin_confirm);

		String createString = createPin.getText().toString();
		String confirmString = confirmPin.getText().toString();

		// test input for reliability
		if (createString.equals(confirmString)) {
			if (createPin.length() > 2 && createPin.length() < 11) {
				// writes pin to shared preferences
				PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext())
						.edit().putString("pin", createString).commit();
				// return to login
				Intent login = new Intent(this, LoginActivity.class);
				startActivity(login);
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"Pin must be 3 to 10 characters long",
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(getApplicationContext(),
					"Pins must match, please try again", Toast.LENGTH_LONG)
					.show();
		}

	}

}
