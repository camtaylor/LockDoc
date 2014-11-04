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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		logIn(findViewById(R.id.create_account_button));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void logIn(View v) {
		// Activated on Login Click. Will check for correct pin, then respond
		EditText pin = (EditText) findViewById(R.id.pin);
		String attempted = pin.getText().toString();
		// get preferences
		String savedPin = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext()).getString("pin", null);

		if (savedPin != null) {
			// Verifies pin number
			if (attempted.equals(savedPin)) {
				// intent to open file list
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
		
		EditText pinEnter = (EditText)findViewById(R.id.pin);
		pinEnter.setText("");
	}
}
