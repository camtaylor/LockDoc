package com.example.lockdoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/*
	 * This activity checks for a saved pin in Shared Preferences then either
	 * starts the Create Account activity or prompts for a pin depending if the
	 * user has created an account yet
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		logIn(findViewById(R.id.create_account_button));
	}

	public void logIn(View v) {
		// Method is activated on Login Click. Will check for correct pin, then
		// respond
		EditText pin = (EditText) findViewById(R.id.pin);
		String attempted = pin.getText().toString();
		// get preferences
		String savedPin = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getString("pin", null);

		if (savedPin != null) {
			// User has an account
			if (attempted.equals(savedPin)) {
				Intent login = new Intent(this, ActionOptionsActivity.class);
				startActivity(login);
			} else {
				Toast.makeText(getApplicationContext(), "Incorrect Pin",
						Toast.LENGTH_SHORT).show();
			}
		}
		// else create account
		else {
			Intent createAccount = new Intent(this, CreateAccountActivity.class);
			startActivity(createAccount);
		}

	}

	// TODO remove this method. For test purposes only!!
	public void deletePin(View v) {
		// changes pin to null
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit().putString("pin", null).commit();

		EditText pinEnter = (EditText) findViewById(R.id.pin);
		pinEnter.setText("");
	}
}
