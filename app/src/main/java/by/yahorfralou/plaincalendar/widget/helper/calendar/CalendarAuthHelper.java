package by.yahorfralou.plaincalendar.widget.helper.calendar;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.io.IOException;
import java.util.Collections;

import by.yahorfralou.plaincalendar.widget.configure.ConfigureActivity;
import by.yahorfralou.plaincalendar.widget.helper.PrefHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetCalendarApp.LOGCAT;

public class CalendarAuthHelper {
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private Context ctx;
    private GoogleAccountCredential credential;

    public CalendarAuthHelper(Context ctx) {
        this.ctx = ctx;

        credential = GoogleAccountCredential.usingOAuth2(ctx,
                Collections.singletonList(CalendarScopes.CALENDAR_READONLY)).setBackOff(new ExponentialBackOff());
    }

    public void getResultsFromApi() throws IOException {
        if (! isGooglePlayServicesAvailable()) {
            Log.e(LOGCAT, "Google Play service not available");
            acquireGooglePlayServices();
        } else if (credential.getSelectedAccountName() == null) {
            Log.i(LOGCAT, "No preferred account. Let choose an account");
            chooseAccount();
        } else if (! isDeviceOnline()) {
            throw new IOException("No Internet connection at the moment.");
        } else {
            Log.i(LOGCAT, "Starting Request Task for Calendar");
            new MakeRequestTask(credential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() throws IOException {
        if (EasyPermissions.hasPermissions(
                ctx, Manifest.permission.GET_ACCOUNTS)) {
            Log.i(LOGCAT, "Already have permissions for GET_ACCOUNTS");
            String accountName = PrefHelper.getAccountName(ctx);
            if (accountName != null) {
                credential.setSelectedAccountName(accountName);
                Log.i(LOGCAT, "Call for API with accountName " + accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                Log.i(LOGCAT, "Start a dialog to choose account");
                startActivityForResult(
                        credential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            Log.i(LOGCAT, "Request the GET_ACCOUNTS permission via a user dialog");
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr != null ? connMgr.getActiveNetworkInfo() : null;
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(ctx);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(ctx);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
}
