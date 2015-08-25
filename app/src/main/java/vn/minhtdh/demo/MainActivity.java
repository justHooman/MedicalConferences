package vn.minhtdh.demo;

import android.content.Intent;
import android.content.IntentSender;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.lang.ref.WeakReference;

import vn.minhtdh.demo.db.DbHelper;
import vn.minhtdh.demo.db.DbUtils;
import vn.minhtdh.demo.feature.admin.AdminFrag;
import vn.minhtdh.demo.feature.conference.ConferencesFrag;
import vn.minhtdh.demo.feature.user.UsersFrag;
import vn.minhtdh.demo.frag.BaseFrag;
import vn.minhtdh.demo.frag.DrawerFrag;
import vn.minhtdh.demo.frag.ProgressFrag;
import vn.minhtdh.demo.model.User;

public class MainActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    final String TAG = MainActivity.class.getSimpleName();

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private DrawerFrag mDrawerFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        mDrawerFrag = new DrawerFrag();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.left_drawer, mDrawerFrag).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeSignUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked(v);
                break;
            case R.id.sign_out_button:
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                showSignedOutUI();
                break;
            case R.id.btn_admin_conferences:
                onAdminClick(v);
                break;
            case R.id.btn_admin_users:
                onUsersClick(v);
                break;
            case R.id.btn_conference:
                onConferenceClick(v);
                break;
            default:
                break;
        }
    }

    public static final int MAIN_CONTENT_HOLDER = R.id.main_content;
    private void onUsersClick(View v) {
        Log.d(TAG, "onUsersClick");
        Fragment oldFrag = getSupportFragmentManager().findFragmentById(MAIN_CONTENT_HOLDER);
        if (oldFrag instanceof UsersFrag)
            return;
        UsersFrag frag = new UsersFrag();
        BaseFrag.Option opt = frag.generateDefaultOption();
        opt.setPlaceHolder(MAIN_CONTENT_HOLDER);
        opt.action = BaseFrag.Option.ACTION_REPLACE;
        opt.addBackStack = false;
        frag.move(getSupportFragmentManager(), frag, opt);
    }

    private void onAdminClick(View v) {
        Log.d(TAG, "onAdminClick");
        Fragment oldFrag = getSupportFragmentManager().findFragmentById(MAIN_CONTENT_HOLDER);
        if (oldFrag instanceof AdminFrag)
            return;
        AdminFrag frag = new AdminFrag();
        BaseFrag.Option opt = frag.generateDefaultOption();
        opt.setPlaceHolder(MAIN_CONTENT_HOLDER);
        opt.action = BaseFrag.Option.ACTION_REPLACE;
        opt.addBackStack = false;
        frag.move(getSupportFragmentManager(), frag, opt);
    }

    private void onConferenceClick(View v) {
        Log.d(TAG, "onConferenceClick");
        Fragment oldFrag = getSupportFragmentManager().findFragmentById(MAIN_CONTENT_HOLDER);
        if (oldFrag instanceof ConferencesFrag)
            return;
        ConferencesFrag frag = new ConferencesFrag();
        BaseFrag.Option opt = frag.generateDefaultOption();
        opt.setPlaceHolder(MAIN_CONTENT_HOLDER);
        opt.action = BaseFrag.Option.ACTION_REPLACE;
        opt.addBackStack = false;
        frag.move(getSupportFragmentManager(), frag, opt);
    }

    private void showSignedOutUI() {
        if (mDrawerFrag != null) {
            mDrawerFrag.setUser(null, null);
        }
    }

    ProgressFrag mProgressFrag;

    private void showSigninProgress() {
        if (mProgressFrag == null || !mProgressFrag.getShowsDialog()) {
            if (mProgressFrag == null) {
                mProgressFrag = new ProgressFrag();
                mProgressFrag.title = getString(R.string.signingin);
            }
            mProgressFrag.show(getSupportFragmentManager(), ProgressFrag.class.getSimpleName());
        }
    }

    private void dismissSignInProgess() {
        if (mProgressFrag != null && mProgressFrag.getShowsDialog()) {
            mProgressFrag.dismiss();
        }
        mProgressFrag = null;
    }

    private void resumeSignUI() {
        Person currentPerson = null;
        if (mGoogleApiClient.isConnected()) {
            currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        }
        if (currentPerson == null) {
            if (mIsResolving) {
                showSigninProgress();
            }
        }
    }

    private void onSignInClicked(View v) {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        showSigninProgress();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.

                Log.d(TAG, "onConnectionFailed: error dialog" + connectionResult);
//                showErrorDialog(connectionResult);
                // TODO
            }
        } else {
            // Show the signed-out UI

            Log.d(TAG, "onConnectionFailed: signout ui" + connectionResult);
            showSignedOutUI();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        // Show the signed-in UI
        // TODO
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (currentPerson != null) {

            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            SQLiteDatabase db = DbHelper.getIns().getReadableDatabase();
            User user = new DbUtils().queryUser(db, email);
            if (user != null) {
                String personName = currentPerson.getDisplayName();
                user.displayName = personName;
                if (mDrawerFrag != null) {
                    mDrawerFrag.setUser(user, currentPerson);
                }
            }
        }

    }

    private void addText(LinearLayout ll, String content) {
        TextView tv = new TextView(this);
        tv.setText(content);
        ll.addView(tv);
    }

    @Override
    public void onConnectionSuspended(final int i) {
        // TODO
//        mTitle.setText("onConnectionSuspended");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
