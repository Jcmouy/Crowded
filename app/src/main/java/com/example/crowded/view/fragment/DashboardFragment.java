package com.example.crowded.view.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.AttrRes;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.crowded.R;
import com.example.crowded.helper.PrefManager;
import com.example.crowded.util.FormatDate;
import com.example.crowded.util.GpsLoc;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.JsonObject;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double latitude, longitude;
    private String direccion, latStandar, lonStandar;
    private TextView txtDireccion;
    private LinearLayout lin, lin3,lin4,layout_filtro_estudiante, search_box;

    private CardView cardInitalAddress;

    private CameraPosition mCameraPosition;
    private LatLng location;
    private LatLng locationFilter;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = "MapsActivity";
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private GpsLoc gpsLoc;

    private PrefManager pref;

    private Button btnSeleccionarDireccion, btnGuardarDireccion, btnVerificarDireccion;
    private InputMethodManager imm = null;

    private Marker asigMarker;
    private ArrayList<Marker> list_markerAsig = new ArrayList<>();

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(requireContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        search_box = (LinearLayout) view.findViewById(R.id.lin_search_box);
        cardInitalAddress = (CardView) view.findViewById(R.id.card_initial_address);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapNav);
        mapFragment.getMapAsync(this);

        pref = new PrefManager(getActivity());
        imm = (InputMethodManager) getActivity().getSystemService(requireContext().INPUT_METHOD_SERVICE);

        SearchView searchAsig = view.findViewById(R.id.search_mat);
        txtDireccion = view.findViewById(R.id.txtDireccion);
        btnVerificarDireccion = view.findViewById(R.id.btnVerificarDireccion);

        getBundle();

        btnGuardarDireccion = view.findViewById(R.id.btnSaveAddress);
        btnSeleccionarDireccion = view.findViewById(R.id.btnFindaddress);
        lin = view.findViewById(R.id.layout_Aquiresides);
        lin3 = view.findViewById(R.id.layout_lin3);
        lin4 = view.findViewById(R.id.layout_lin4);
        layout_filtro_estudiante = view.findViewById(R.id.layout_filtro_estudiante);

        btnGuardarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerLatLon(latitude,longitude);
            }
        });

        btnVerificarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ingresarDir();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        searchAsig.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast t = Toast.makeText(requireContext(), "test", Toast.LENGTH_SHORT);
                t.show();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int i = newText.trim().length();
                if (i == 0){
                    clearData();
                }
                return false;
            }
        });

        searchAsig.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast t = Toast.makeText(requireContext(), "close", Toast.LENGTH_SHORT);
                t.show();

                return false;
            }
        });

        // Habilitar para utilizar smartphone
        /////////////////////////////////////////////////////////
        /*

        if (pref.getLatUser() != null && pref.getLngUser() != null){
            initalLoginScreen();
        }else{
            Toast.makeText(getContext(),"Ingresé su dirección por favor",Toast.LENGTH_LONG).show();
        }

         */
        /////////////////////////////////////////////////////////

        // Habilitar para Emular Andorid
        /////////////////////////////////////////////////////////

        // pref.createLogin("78","Alumno Prueba","AlumPru","jccc@gmail.com","+59899471402", ConstantTipoUsuario.ESTUDIANTE);
        initalLoginScreen();

        /////////////////////////////////////////////////////////

        return view;
    }

    public void obtenerLatLon(double latitude, double longitude){
        final double lat = latitude;
        final double lng = longitude;
        String mobile = pref.getMobileNumber();

        pref.setLatLngUser(String.valueOf(lat),String.valueOf(lng));
        initalLoginScreen();
        Toast.makeText(getActivity(), getResources().getString(R.string.msg_dire_ing), Toast.LENGTH_SHORT).show();
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null){
            latitude = Double.valueOf(Objects.requireNonNull(bundle.getString("latitude")));
            longitude = Double.valueOf(Objects.requireNonNull(bundle.getString("longitude")));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nav_work_map, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Actual position in the world map
        LatLng actual = new LatLng(-34.85805009727135, -56.221547068399886);
        mMap.addMarker(new MarkerOptions().position(actual).title("Tu ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(actual));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(actual.latitude,actual.longitude),DEFAULT_ZOOM));

        /*
        mMap.addMarker(new MarkerOptions().position(p1).title(direccion));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p1));
         */

    }

    public void pantallaBuscarDireccion(boolean buscar){
        if (buscar){
            lin3.setVisibility(View.GONE);
            lin4.setVisibility(View.VISIBLE);
        }else{
            lin3.setVisibility(View.VISIBLE);
            lin4.setVisibility(View.GONE);
        }
    }

    public void updateLocation(boolean filter){
        LatLng loc;
        if (!filter){
            if (location == null) {
                gpsLoc = new GpsLoc();
                gpsLoc.getDeviceLocation(requireContext());
                Double lat = Double.valueOf(gpsLoc.getLatitude());
                Double lon = Double.valueOf(gpsLoc.getLongitude());
                location = new LatLng(lat, lon);
            }
            loc = location;
        }else {
            loc = locationFilter;
        }
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (loc != null) {
            mMap.addMarker(new MarkerOptions().position(loc).title("Ubicación Actual"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.latitude,loc.longitude),DEFAULT_ZOOM));

        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        }
    }

    public void ingresarDir() throws IOException {

        direccion = txtDireccion.getText().toString();

        Geocoder coder = new Geocoder(getActivity());
        LatLng p1 = null;

        gpsLoc = new GpsLoc();
        gpsLoc.getDeviceLocation(getActivity());
        latStandar = gpsLoc.getLatitude();
        lonStandar = gpsLoc.getLongitude();

        if (!direccion.equals("")){
            List<Address> address = coder.getFromLocationName(direccion, 1);
            if (address == null) {
                LatLng standar = new LatLng(Double.valueOf(latStandar), Double.valueOf(lonStandar));
            } else {
                Address locationA = address.get(0);
                location = new LatLng(locationA.getLatitude(), locationA.getLongitude());
                updateLocation(false);
                pantallaBuscarDireccion(false);
            }
        }

    }


    public void initalLoginScreen() {
        cardInitalAddress.setVisibility(View.GONE);
        search_box.setVisibility(View.VISIBLE);
    }

    public void clearData(){
        clearMapMarker();
    }

    public void clearMapMarker() {
        for (int i = 0; i < list_markerAsig.size(); i++ ) {
            asigMarker = list_markerAsig.get(i);
            asigMarker.remove();
        }
        list_markerAsig = new ArrayList<>();
    }


}
