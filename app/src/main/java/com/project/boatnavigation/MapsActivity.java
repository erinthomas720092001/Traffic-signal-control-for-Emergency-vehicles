package com.project.boatnavigation;

import static com.project.boatnavigation.appconstant.Constant.DB_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.boatnavigation.appconstant.Constant;
import com.project.boatnavigation.databinding.ActivityMapsBinding;
import com.project.boatnavigation.manager.Utils;
import com.project.boatnavigation.manager.preference.AppPreference;
import com.project.boatnavigation.model.FishList;
import com.project.boatnavigation.model.FishModel;
import com.project.boatnavigation.ui.AddEmergencyNo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


//https://roboindia.com/tutorials/bluetooth-terminal-android/#:~:text=Pair%20your%20phone%20with%20HC,to%20pair%20is%20'1234'.
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorListener {

    // private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.SEND_SMS"};
    public static double latitude = 0.0;
    public static double longitude = 0.0;

    ArrayList<Location> locationListBackup;

    Location locationBackup;
    FusedLocationProviderClient fusedClient;
    private LocationRequest mRequest;
    private LocationCallback mCallback;

    ArrayList<Location> locationList;
    TextView lati;
    TextView longi;
    TextView speed;
    TextView distanceText;
    TextView kmPl;// Km/L:
    Button startTrip;
    ImageView add;
    boolean tripIsOn = false;

    Button getDestinationLocation;

    Location mDestinationLocation;

    String thresholdDistance = "0";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PAIRED_DEVICE = 2;
    Button btnListPairedDevices;
    BluetoothAdapter bluetoothAdapter;
    TextView stateBluetooth;

    TextView latiDestination;

    TextView longiDestination;

    private TextView returnedText;
    private String LOG_TAG = "VoiceRecognitionActivity";

    private long lastExecutionTimeMillis;
    private static final long COOLDOWN_PERIOD_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        locationList = new ArrayList<>();
        locationListBackup = new ArrayList<>();
        kmPl = findViewById(R.id.kmPl);
        distanceText = findViewById(R.id.distanceText);
        lati = findViewById(R.id.lati);
        longi = findViewById(R.id.longi);
        speed = findViewById(R.id.speed);
        startTrip = findViewById(R.id.startTrip);
        add = findViewById(R.id.add);

        latiDestination = findViewById(R.id.latiDestination);

        longiDestination = findViewById(R.id.longiDestination);
        getDestinationLocation = findViewById(R.id.getDestinationLocation);
        getDestinationLocation.setOnClickListener(view -> {
            getDestinationLocation();
            showTextDialog();
        });
        startTrip.setOnClickListener(view -> {
            tripIsOn = !tripIsOn;
            locationList.clear();
            if (tripIsOn) {

                startTrip.setText("Stop");
            } else {
                startTrip.setText("Start");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, AddEmergencyNo.class);
                startActivity(intent);
            }
        });

        mCallback = new LocationCallback() {
            //This callback is where we get "streaming" location updates. We can check things like accuracy to determine whether
            //this latest update should replace our previous estimate.
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.d("TAG", "locationResult null");
                    return;
                }
                Log.d("TAG", "received " + locationResult.getLocations().size() + " locations");
                for (Location loc : locationResult.getLocations()) {
                    Log.d("TAG", "\n" + loc.getProvider() + ":Accu:(" + loc.getAccuracy() + "). Lat:" + loc.getLatitude() + ",Lon:" + loc.getLongitude());
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                    locationBackup = loc;
                    setCurrentLocation(loc);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.d("TAG", "locationAvailability is " + locationAvailability.isLocationAvailable());
                super.onLocationAvailability(locationAvailability);
            }
        };

        //permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request permission.
            //However check if we need to show an explanatory UI first
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showRationale();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 2);
            }
        } else {
            //we already have the permission. Do any location wizardry now
            locationWizardry();
        }
        setBlueTooth();

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorMgr.registerListener(this,
                SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_GAME);

        if (allPermissionsGranted()) {

        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {

            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private int REQUEST_CODE_PERMISSIONS = 101;
    SensorManager sensorMgr;

    public void setBlueTooth() {
        btnListPairedDevices = (Button) findViewById(R.id.listpaireddevices);
        btnListPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPairedDevices();
            }
        });

        stateBluetooth = (TextView) findViewById(R.id.bluetoothstate);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        CheckBlueToothState();
    }

    private void CheckBlueToothState() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (bluetoothAdapter == null) {
            stateBluetooth.setText("Bluetooth NOT support");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    stateBluetooth
                            .setText("Bluetooth is currently in device discovery process.");
                } else {
                    stateBluetooth.setText("Bluetooth is Enabled.");
                    btnListPairedDevices.setEnabled(true);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                stateBluetooth.setText("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    String deviceAddr = "";

    public void getPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter
                .getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceBTName = device.getName();
                String deviceBTMajorClass = getBTMajorDeviceClass(device
                        .getBluetoothClass().getMajorDeviceClass());
                // btArrayAdapter.add(deviceBTName + "\n" + deviceBTMajorClass);
                deviceAddr = device.getAddress();
                Log.v("LOG", "11012015 " + deviceAddr);
            }
        }
        sendDataToPairedDevice("a", deviceAddr);
        Toast.makeText(MapsActivity.this,
                "Connected to" + "  " + deviceAddr, Toast.LENGTH_LONG).show();
        if (deviceAddr.length() != 0) {
            //rl.setVisibility(View.VISIBLE);
            //toggleButton.setVisibility(View.VISIBLE);

        }
    }

    private String getBTMajorDeviceClass(int major) {
        switch (major) {
            case BluetoothClass.Device.Major.AUDIO_VIDEO:
                return "AUDIO_VIDEO";
            case BluetoothClass.Device.Major.COMPUTER:
                return "COMPUTER";
            case BluetoothClass.Device.Major.HEALTH:
                return "HEALTH";
            case BluetoothClass.Device.Major.IMAGING:
                return "IMAGING";
            case BluetoothClass.Device.Major.MISC:
                return "MISC";
            case BluetoothClass.Device.Major.NETWORKING:
                return "NETWORKING";
            case BluetoothClass.Device.Major.PERIPHERAL:
                return "PERIPHERAL";
            case BluetoothClass.Device.Major.PHONE:
                return "PHONE";
            case BluetoothClass.Device.Major.TOY:
                return "TOY";
            case BluetoothClass.Device.Major.UNCATEGORIZED:
                return "UNCATEGORIZED";
            case BluetoothClass.Device.Major.WEARABLE:
                return "AUDIO_VIDEO";
            default:
                return "unknown!";
        }
    }

    OutputStream mmOutStream;
    InputStream inputStream;

    private void sendDataToPairedDevice(String message) {
//        Toast.makeText(MapsActivity.this, message + "  ", Toast.LENGTH_LONG)
//                .show();
        byte[] toSend = message.getBytes(Charset.defaultCharset());
        try {
            mmOutStream.write(toSend);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void sendDataToPairedDevice(String message, String adress) {
        byte[] toSend = message.getBytes();
        try {
            Toast.makeText(MapsActivity.this,
                    message + "  " + adress, Toast.LENGTH_LONG).show();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(adress);
            System.out.println("11012015 run  1");
            // BluetoothSocket socket
            // =device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
            BluetoothSocket socket = null;
            Method m = null;
            try {
                m = device.getClass().getMethod("createRfcommSocket",
                        new Class[]{int.class});
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("11012015 exp in 1");
            }
            try {
                socket = (BluetoothSocket) m.invoke(device, 1);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("11012015 exp in 2");
            }
            System.out.println("11012015 run  2");
            mmOutStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            System.out.println("11012015 run  3");
            // bluetoothAdapter.cancelDiscovery();
            System.out.println("11012015 run  4");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            socket.connect();
            System.out.println("11012015 run  5");
            //mmOutStream.write(toSend);
            readData();
            System.out.println("11012015 run  6");
        } catch (Exception e) {
            Log.d("TAG", "11012015 Exception during write", e);
            System.out.println("11012015 exp in 3");
        }
    }

    boolean asyncTask = true;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asyncTask = false;
    }

    String dataFromBluetooth = "";

    public void readData() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] read = new byte[1024];
                    inputStream.read(read);
                    System.out.println("11012015 run  555" + new String(read));
                    dataFromBluetooth = new String(read);

                    if (dataFromBluetooth.contains("FETCH")) {


                        if (locationListBackup.size() == 0) {
                            locationListBackup.add(locationBackup);

                        } else {
                            try {
                                String distance = (locationListBackup.get(0).distanceTo(locationBackup)) + "";
                                sendDataToPairedDevice(distance);
                                //distanceText.setText("Distance: " + distance);
                                locationListBackup.clear();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //mmOutStream.write((latitude+","+longitude).getBytes());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (locationListBackup.size() == 0) {
                            kmPl.setText("Com: " + dataFromBluetooth + "1");
                        } else {
                            kmPl.setText("Com: " + dataFromBluetooth + "2");
                        }
                        readData();

                    }
                });
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void locationWizardry() {
        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        fusedClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String loc = location.getProvider() + ":Accu:(" + location.getAccuracy() + "). Lat:" + location.getLatitude() + ",Lon:" + location.getLongitude();
                    Log.d("TAG", "555 loc" + loc);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    locationBackup = location;
                    setCurrentLocation(location);
                }
            }
        });
        createLocRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mRequest);
        SettingsClient client = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this, 500);
                    } catch (IntentSender.SendIntentException sendEx) {
                    }
                }
            }
        });

        //actually start listening for updates: See on Resume(). It's done there so that conveniently we can stop listening in onPause
    }

    void showTextDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("Threshold ");
        alertDialog.setMessage("Enter threshold distance.");

        final EditText input = new EditText(MapsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        input.setText("20");
        alertDialog.setIcon(R.drawable.ic_launcher_foreground);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        thresholdDistance = input.getText().toString();

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    protected void createLocRequest() {
        mRequest = new LocationRequest();
        mRequest.setInterval(10000);//time in ms; every ~10 seconds
        mRequest.setFastestInterval(5000);
        mRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedClient.removeLocationUpdates(mCallback);
    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        fusedClient.requestLocationUpdates(mRequest, mCallback, null);
    }

    private void showRationale() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("We need this, Just suck it up and grant us the" + "permission :)").setPositiveButton("Sure", (dialogInterface, i) -> {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            dialogInterface.dismiss();
        }).create();
        dialog.show();
    }

    private void setCurrentLocation(Location location) {

        try {

            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastExecutionTimeMillis >= COOLDOWN_PERIOD_MILLIS) {
                // Execute your method here

                // Update lastExecutionTimeMillis
                lastExecutionTimeMillis = currentTimeMillis;
                distanceText.setText("Distance: " + mDestinationLocation.distanceTo(location) + " meters");
                lati.setText("Current Lati: " + String.format("%.2f", latitude));
                longi.setText("Current Long: " + String.format("%.2f", longitude));
                speed.setText("Speed: " + String.format("%.2f", location.getSpeed()));

                if (Integer.parseInt(thresholdDistance) >= mDestinationLocation.distanceTo(location)) {
                    setLocationReached("1");
                } else {
                    setLocationReached("0");
                }
            }
            else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(mMap!=null) {
//            LatLng sydney = new LatLng(latitude, longitude);
//            //mMap.clear();
//            //mMap.addMarker(new MarkerOptions().position(sydney).title("Your Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // mMap = googleMap;
        // getFishList();

    }

    void setLocationReached(String value) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(DB_URL);
        DatabaseReference myRef = database.getReference("location_reached");
        myRef.setValue(value)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //Toast.makeText(MainActivity.this, "Value updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(MainActivity.this, "Failed to update value", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void getDestinationLocation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(DB_URL);
        DatabaseReference myRef = database.getReference("destination_location");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();

                mDestinationLocation = new Location("provider");
                mDestinationLocation.setLatitude(Double.parseDouble((value.split(",")[0])));
                mDestinationLocation.setLongitude(Double.parseDouble((value.split(",")[1])));

                latiDestination.setText("Destination Lati: " + mDestinationLocation.getLatitude() + "");
                longiDestination.setText("Destination Long: " + mDestinationLocation.getLongitude() + "");

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private static final int SHAKE_THRESHOLD = 5000;
    float x, y, z;
    float last_x, last_y, last_z;
    long lastUpdate = 0;

    @Override
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                x = values[SensorManager.DATA_X];
                y = values[SensorManager.DATA_Y];
                z = values[SensorManager.DATA_Z];
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    //showDialog();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(int i, int i1) {

    }

    void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setCancelable(false)
                .setMessage("Do you really want to sent message to the emergency number?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (AppPreference.getMobileNumber(MapsActivity.this).length() != 0) {
                            Utils.sendSMS(MapsActivity.this, AppPreference.getMobileNumber(MapsActivity.this),
                                    Constant.message + Constant.mapUrl + latitude + "," + longitude);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            private static final int AUTO_DISMISS_MILLIS = 6000;

            @Override
            public void onShow(final DialogInterface dialog) {
                final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                final CharSequence negativeButtonText = defaultButton.getText();
                new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        defaultButton.setText(String.format(
                                Locale.getDefault(), "%s (%d)",
                                negativeButtonText,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                        ));
                    }

                    @Override
                    public void onFinish() {
                        if (((AlertDialog) dialog).isShowing()) {
                            if (AppPreference.getMobileNumber(MapsActivity.this).length() != 0) {
                                Utils.sendSMS(MapsActivity.this, AppPreference.getMobileNumber(MapsActivity.this),
                                        Constant.message + Constant.mapUrl + latitude + "," + longitude);
                            }

                            dialog.dismiss();
                        }
                    }
                }.start();
            }
        });
        dialog.show();
    }
}