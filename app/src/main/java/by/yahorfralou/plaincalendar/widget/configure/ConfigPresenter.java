package by.yahorfralou.plaincalendar.widget.configure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.Collections;

import by.yahorfralou.plaincalendar.widget.helper.calendar.CalendarAuthHelper;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetCalendarApp.LOGCAT;

public class ConfigPresenter {

    private IConfigureView view;
    private Context ctx;


    private CalendarAuthHelper authHelper;

    public ConfigPresenter(IConfigureView view, Context context) {
        this.view = view;
        this.ctx = context;

        authHelper = new CalendarAuthHelper(context);
    }

    public void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            Log.e(LOGCAT, "Google Play service not available");
            acquireGooglePlayServices();
        } else if (credential.getSelectedAccountName() == null) {
            Log.i(LOGCAT, "No preferred account. Let choose an account");
            chooseAccount();
        } else if (! isDeviceOnline()) {
            // TODO make better output
            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show();
        } else {
            Log.i(LOGCAT, "Starting Request Task for Calendar");
            new ConfigureActivity.MakeRequestTask(credential).execute();
        }
    }

    public void start(Exception lastError) {

        if (lastError instanceof GooglePlayServicesAvailabilityIOException) {
            showGooglePlayServicesAvailabilityErrorDialog(
                    ((GooglePlayServicesAvailabilityIOException) lastError)
                            .getConnectionStatusCode());
        } else if (lastError instanceof UserRecoverableAuthIOException) {
            startActivityForResult(
                    ((UserRecoverableAuthIOException) lastError).getIntent(),
                    ConfigureActivity.REQUEST_AUTHORIZATION);
        } else {
            Log.e(LOGCAT, "Error getting events! ", lastError);
            txtOutput.setText("The following error occurred:\n"
                    + lastError.getMessage());
        }
    }

}
