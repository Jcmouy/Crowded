package com.example.crowded.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowded.BuildConfig;
import com.example.crowded.Data.remote.RemoteUtils;
import com.example.crowded.R;
import com.example.crowded.dto.Io.UsuarioServiceDto;
import com.example.crowded.dto.PaisDto;
import com.example.crowded.dto.PersonaDto;
import com.example.crowded.dto.UsuarioDto;
import com.example.crowded.helper.PrefManager;
import com.example.crowded.io.LoginData;
import com.example.crowded.io.PersonaServiceIO;
import com.example.crowded.io.SmsServiceIO;
import com.example.crowded.io.TokenInterceptor;
import com.example.crowded.io.UsuarioServiceIO;
import com.example.crowded.repository.PaisService;
import com.example.crowded.repository.PersonaService;
import com.example.crowded.repository.UsuarioService;
import com.example.crowded.service.HttpService;
import com.example.crowded.util.CheckPermission;
import com.example.crowded.util.FormatDate;
import com.example.crowded.util.Utils;
import com.example.crowded.util.constants.ConstantApiService;
import com.example.crowded.view.model.Injection;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = SmsActivity.class.getSimpleName();

    private static final String OTP_DELIMITER = ":";

    private static final int READ_SMS = 100;
    private static final int RECEIVE_SMS = 101;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Button btnRequestSms, btnVerifyOtp, btnTeacherAccess, facebook_btn, google_btn;
    private EditText inputName, inputEmail, inputOtp, inputUsername, etFecha;
    private ProgressBar progressBar;
    private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private LinearLayout layoutEditMobile;
    private CountryCodePicker ccp;
    private String mobile, photoUrlPath, codigoPais, nombrePais;
    private CheckPermission checKPer;
    private SmsServiceIO mSmsServiceIO;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount acct;
    private static final int RC_SIGN_IN = 9001;

    private LoginButton mFacebookSignInButton;
    private CallbackManager mFacebookCallbackManager;
    private AccessToken accessToken;
    private TokenInterceptor tokenInt;

    private PersonaService personaService;
    private UsuarioService usuarioService;
    private PaisService paisService;

    private PersonaServiceIO mPersonaServiceIo;
    private UsuarioServiceIO mUsuarioServiceIo;

    private FormatDate formatDate;

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private static final String CERO = "0";
    private static final String BARRA = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        personaService = Injection.providePersonaService(getApplicationContext());
        usuarioService = Injection.provideUsuarioService(getApplicationContext());
        paisService = Injection.providePaisService(getApplicationContext());

        formatDate = new FormatDate(getApplicationContext(), new WeakReference<Activity>(this));

        viewPager = findViewById(R.id.viewPagerVertical);
        inputUsername = findViewById(R.id.inputUserName);
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        // inputMobile = findViewById(R.id.inputMobile);
        inputOtp = findViewById(R.id.inputOtp);
        btnRequestSms = findViewById(R.id.btn_request_sms);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        progressBar = findViewById(R.id.progressBar);
        btnEditMobile = findViewById(R.id.btn_edit_mobile);
        txtEditMobile = findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = findViewById(R.id.layout_edit_mobile);
        btnTeacherAccess = findViewById(R.id.btn_teacher_access);

        mSmsServiceIO = RemoteUtils.getSmsService();

        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        btnTeacherAccess.setOnClickListener(this);

        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);

        pref = new PrefManager(this);

        // pref.clearSession();

        //Google Login
        google_btn = findViewById(R.id.g_button);

        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Facebook Login
        AppEventsLogger.activateApp(getApplication());

        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        mFacebookCallbackManager = CallbackManager.Factory.create();

        facebook_btn = findViewById(R.id.f_button);
        mFacebookSignInButton = findViewById(R.id.btn_facebook_login_button);
        mFacebookSignInButton.registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        //TODO: Use the Profile class to get information about the current user.
                        accessToken= loginResult.getAccessToken();

                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        if (object != null) {
                                            try {
                                                String first_name = object.getString("first_name");
                                                String last_name = object.getString("last_name");
                                                String email = object.getString("email");

                                                initDialogInfoUser(first_name,last_name,email);
                                            }
                                            catch (JSONException | NullPointerException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString(
                                "fields",
                                "id, name, first_name, last_name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        handleSignInResult(null);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(SmsActivity.class.getCanonicalName(), error.getMessage());
                        handleSignInResult(null);
                    }
                }
        );

        facebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFacebookSignInButton.performClick();
            }
        });

        boolean prueba = pref.isLoggedIn();

        // Checking for user session
        // if user is already logged in, take him to main activity

        //Habilitar para andorid emulator
        ///////////////////////////////////////////////////////

        /*
        Intent intent = new Intent(SmsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();

         */

        /////////////////////////////////////////////////////////

        //Habilitar para smartphone
        /////////////////////////////////////////////////////////


        if (pref.isLoggedIn()) {
            Intent intent = new Intent(SmsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }


        /////////////////////////////////////////////////////////


        adapter = new ViewPagerAdapter();
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(3);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }
    }

    private void handleSignInResult(Callable<Void> voidCallable) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_details_err), Toast.LENGTH_SHORT).show();
    }

    private void signInWithGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewPager.setCurrentItem(0, true);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_request_sms:
                checKPer = new CheckPermission();
                checKPer.checkPermission(Manifest.permission.READ_SMS, READ_SMS, getApplicationContext(), this);
                checKPer.checkPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS, getApplicationContext(), this);

                validateForm();
                break;

            case R.id.btn_verify_otp:
                verifyOtp();
                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(2);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;

            case R.id.date_icon_txt:
                obtenerFecha();
                break;
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        recogerFecha.show();
    }

    /**
     * Validating user details form
     */
    private void validateForm() {
        String username = inputUsername.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        // validating empty name and email
        if (name.length() == 0 || username.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_details_err), Toast.LENGTH_SHORT).show();
            return;
        }

        // validating mobile number
        // it should be of 12 digits length
        if (ccp.isValidFullNumber()) {

            // request for sms
            progressBar.setVisibility(View.VISIBLE);

            // saving the mobile number in shared preferences
            pref.setMobileNumber(mobile);

            // requesting for sms
            requestForSMS(username, name, email, mobile);

        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.reg_student_mobile_err), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method initiates the SMS request on the server
     *
     * @param name   user name
     * @param email  user email address
     * @param mobile user valid mobile number
     */
    private void requestForSMS(final String username, final String name, final String email, final String mobile) {

        mSmsServiceIO.sent_sms(username,name,email,mobile).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("SmsActivity", "RequestSMS successful");

                    // boolean flag saying device is waiting for sms
                    pref.setIsWaitingForSms(true);

                    // moving the screen to next pager item i.e otp screen
                    viewPager.setCurrentItem(3);
                    txtEditMobile.setText(pref.getMobileNumber());
                    layoutEditMobile.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();


                }else if(response.errorBody() != null){
                    progressBar.setVisibility(View.GONE);

                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);

                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void verifyOtp() {
        String otp = inputOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            startMain(true);
        }

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            startMain(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                // Sign in succeeded, proceed with account
                acct = task.getResult();

                String firstName = acct.getGivenName();
                String lastName = acct.getFamilyName();
                String email = acct.getEmail();
                photoUrlPath = acct.getPhotoUrl().toString();

                initDialogInfoUser(firstName,lastName,email);

            } else {
                startMain(false);
            }
        }

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initDialogInfoUser(String first_name, String last_name, String email) {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.info_user, null);

        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);
        EditText inputGivName = dialogView.findViewById(R.id.giv_edt);
        EditText inputFamName = dialogView.findViewById(R.id.fam_edt);
        etFecha = (EditText) dialogView.findViewById(R.id.date_input_txt);
        EditText inputEmail = dialogView.findViewById(R.id.editTextTextEmailAddress);
        EditText inputMob = dialogView.findViewById(R.id.inputMobile);
        ImageButton ibObtenerFecha = (ImageButton) dialogView.findViewById(R.id.date_icon_txt);
        ccp = dialogView.findViewById(R.id.ccp_user);

        LoginData loginData = new LoginData();
        loginData.setUsername(getResources().getString(R.string.user_default));
        loginData.setPassword(getResources().getString(R.string.password_default));
        // tokenInt = new TokenInterceptor();
        mUsuarioServiceIo = RemoteUtils.getUsuarioServiceIo();
        getUserToken(loginData);

        inputGivName.setText(first_name);
        inputFamName.setText(last_name);
        inputEmail.setText(email);

        ccp.setAutoDetectedCountry(true);
        ibObtenerFecha.setOnClickListener(this);
        ccp.registerCarrierNumberEditText(inputMob);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acct != null) {
                    mGoogleSignInClient.signOut();
                }else if (accessToken != null){
                    LoginManager.getInstance().logOut();
                }
                dialogBuilder.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logUser(inputGivName.getText().toString(), inputFamName.getText().toString(), etFecha.getText().toString(),
                        inputGivName.getText().toString() +""+ inputFamName.getText().toString(), inputEmail.getText().toString(), ccp.getFullNumberWithPlus());

                startMain(true);
                dialogBuilder.dismiss();
            }
        });

        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // Toast.makeText(getApplicationContext(), "Updated " + ccp.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
                if (isValidNumber){
                    mobile = ccp.getFullNumberWithPlus();
                    codigoPais = ccp.getSelectedCountryCode();
                    nombrePais = ccp.getSelectedCountryName();
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void startMain(boolean init) {
        if (init){
            Intent intent = new Intent(SmsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Could not sign in", Toast.LENGTH_SHORT).show();
        }
    }

    public void logUser (String name, String lastName, String birthday, String userName, String email, String mobile){
        int lastInsertedId = usuarioService.getLastInsertedIdUsuario();
        pref.createLogin(String.valueOf(lastInsertedId + 1), name +" "+ lastName , name +""+ lastName, email, mobile);

        mPersonaServiceIo = RemoteUtils.getPersonaServiceIo();

        int lastInsertedIdPersona = personaService.getLastInsertedIdPersona();
        Date date = formatDate.formatDateCalendar(birthday);
        LocalDate dateBirth = LocalDate.from(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()));

        PaisDto pais = new PaisDto(codigoPais,nombrePais);
        paisService.insertOrUpdatePais(pais);
        pais.setId(paisService.getLastInsertedIdPais());

        //PersonaDto persona = new PersonaDto(lastInsertedIdPersona + 1, Utils.randomStringGenerator(10), name, lastName, dateBirth, email, mobile, pais);
        PersonaDto persona = new PersonaDto(lastInsertedIdPersona + 1, Utils.randomStringGenerator(10), name, lastName, dateBirth, email, mobile, pais);
        UsuarioDto usuario = new UsuarioDto(lastInsertedId + 1, persona, photoUrlPath);

        Set<String> authorities = new HashSet<String>();
        authorities.add(ConstantApiService.AUTHORITIES_ROLE_USER);
        UsuarioServiceDto usuarioServiceDto = new UsuarioServiceDto(null, name+""+lastName, name, lastName,
                email, photoUrlPath, true, ConstantApiService.LANG_KEY_ES, null, null, null, null, authorities);


        registerUsuario(usuarioServiceDto);
        registerPersona(persona);

        personaService.insertOrUpdatePersona(persona);
        usuarioService.insertOrUpdateUsuario(usuario);
    }

    private void getUserToken(LoginData loginData) {
        mUsuarioServiceIo.authenticateUser(loginData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    JsonObject json = new JsonObject();
                    json = response.body();

                    String token = json.get("id_token").getAsString();
                    pref.setUserToken(token);
                    TokenInterceptor.setSessionToken(token);

                }else if(response.errorBody() != null){
                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private void registerUsuario(UsuarioServiceDto usuServiceDto) {
        mUsuarioServiceIo.createUser(usuServiceDto).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("SmsActivity", "RequestSMS successful");
                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();

                }else if(response.errorBody() != null){
                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private void registerPersona(PersonaDto persona) {
        mPersonaServiceIo.createPersona(persona).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("SmsActivity", "RequestSMS successful");
                    Toast.makeText(getApplicationContext(), "SMS sent", Toast.LENGTH_SHORT).show();

                }else if(response.errorBody() != null){
                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }
}

class ViewPagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public Object instantiateItem(View collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.layout_start_app;
                break;
            case 1:
                resId = R.id.layout_teacher_start;
                break;
            case 2:
                resId = R.id.layout_sms;
                break;
            case 3:
                resId = R.id.layout_otp;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}


