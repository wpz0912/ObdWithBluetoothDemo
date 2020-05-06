package com.wpz.obddemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wpz.obddemo.obd.commands.SpeedObdCommand;
import com.wpz.obddemo.obd.commands.control.CommandEquivRatioObdCommand;
import com.wpz.obddemo.obd.commands.control.DtcNumberObdCommand;
import com.wpz.obddemo.obd.commands.control.TroubleCodesObdCommand;
import com.wpz.obddemo.obd.commands.engine.EngineRPMObdCommand;
import com.wpz.obddemo.obd.commands.engine.MassAirFlowObdCommand;
import com.wpz.obddemo.obd.commands.fuel.FuelEconomyObdCommand;
import com.wpz.obddemo.obd.commands.fuel.FuelLevelObdCommand;
import com.wpz.obddemo.obd.commands.fuel.FuelTrimObdCommand;
import com.wpz.obddemo.obd.commands.temperature.AmbientAirTemperatureObdCommand;
import com.wpz.obddemo.obd.commands.temperature.EngineCoolantTemperatureObdCommand;
import com.wpz.obddemo.obd.enums.AvailableCommandNames;
import com.wpz.obddemo.obd.enums.FuelTrim;
import com.wpz.obddemo.obd.reader.IPostListener;
import com.wpz.obddemo.obd.reader.activity.ConfigActivity;
import com.wpz.obddemo.obd.reader.io.ObdCommandJob;
import com.wpz.obddemo.obd.reader.io.ObdGatewayService;
import com.wpz.obddemo.obd.reader.io.ObdGatewayServiceConnection;
import com.wpz.obddemo.obd_view.Dashboard_View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ObdControlActivity extends AppCompatActivity {


    private static final String TAG = "ObdControlActivity";

    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_stop)
    TextView tvStop;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.dashboard_view_rpm)
    Dashboard_View dashboardViewRpm;
    @BindView(R.id.dashboard_view_speed)
    Dashboard_View dashboardViewSpeed;
    @BindView(R.id.dashboard_view_tem)
    Dashboard_View dashboardViewTem;


    /*
     * TODO put description
     */
    static final int NO_BLUETOOTH_ID = 0;
    static final int BLUETOOTH_DISABLED = 1;
    static final int NO_GPS_ID = 2;
    static final int START_LIVE_DATA = 3;
    static final int STOP_LIVE_DATA = 4;
    static final int SETTINGS = 5;
    static final int COMMAND_ACTIVITY = 6;
    static final int TABLE_ROW_MARGIN = 7;
    static final int NO_ORIENTATION_SENSOR = 8;
    @BindView(R.id.tv_mass_air)
    TextView tvMassAir;
    @BindView(R.id.tv_amb_air_tem)
    TextView tvAmbAirTem;

    private Handler mHandler = new Handler();

    /**
     * Callback for ObdGatewayService to update UI.
     */
    private IPostListener mListener = null;
    private Intent mServiceIntent = null;
    private ObdGatewayServiceConnection mServiceConnection = null;

    private SensorManager sensorManager = null;
    private Sensor orientSensor = null;
    private SharedPreferences prefs = null;

    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;

    private boolean preRequisites = true;

    private int speed = 1;
    private String maf = "";
    private float ltft = 0;
    private double equivRatio = 1;
    private float tem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obd_control);
        ButterKnife.bind(this);
        obdDataListener();
        startSensorService();
        obdControlBtn();

    }

    private void startSensorService() {
    /*
     *
     * Validate GPS service.
     */
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.getProvider(LocationManager.GPS_PROVIDER) == null) {
//			/*
//			 * TODO for testing purposes we'll not make GPS a pre-requisite.
//			 */
//            // preRequisites = false;
//            showDialog(NO_GPS_ID);
//        }

		/*
         * Validate Bluetooth service.
		 */
        // Bluetooth device exists?
        final BluetoothAdapter mBtAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (mBtAdapter == null) {
            preRequisites = false;
            showDialog(NO_BLUETOOTH_ID);
        } else {
            // Bluetooth device is enabled?
            if (!mBtAdapter.isEnabled()) {
                preRequisites = false;
                showDialog(BLUETOOTH_DISABLED);
            }
        }

		/*
         * Get Orientation sensor.
		 */
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        List<Sensor> sens = sensorManager
//                .getSensorList(Sensor.TYPE_ORIENTATION);
//        if (sens.size() <= 0) {
//            showDialog(NO_ORIENTATION_SENSOR);
//        } else {
//            orientSensor = sens.get(0);
//        }

        // validate app pre-requisites
        if (preRequisites) {
            /*
             * Prepare service and its connection
			 */
            mServiceIntent = new Intent(this, ObdGatewayService.class);
            mServiceConnection = new ObdGatewayServiceConnection();
            mServiceConnection.setServiceListener(mListener);

            // bind service
            Log.d(TAG, "Binding service..");
            bindService(mServiceIntent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    private void obdDataListener() {
        mListener = new IPostListener() {
            public void stateUpdate(ObdCommandJob job) {
                String cmdName = job.getCommand().getName();
                String cmdResult = job.getCommand().getFormattedResult();
                Log.e(TAG, FuelTrim.LONG_TERM_BANK_1.getBank() + " equals " + cmdName + "?");
                if (AvailableCommandNames.ENGINE_RPM.getValue().equals(cmdName)) {
                    dashboardViewRpm.setRealTimeValue(((EngineRPMObdCommand) job.getCommand()).getRPM(), true, 100);
                    Log.e(TAG, "stateUpdate: rpm" + cmdResult);
                } else if (AvailableCommandNames.SPEED.getValue().equals(cmdName)) {
                    speed = ((SpeedObdCommand) job.getCommand())
                            .getMetricSpeed();
                    dashboardViewSpeed.setRealTimeValue(speed, true, 100);
                    Log.e(TAG, "stateUpdate: speed" + speed);
                } else if (AvailableCommandNames.MAF.getValue().equals(cmdName)) {
                    maf = ((MassAirFlowObdCommand) job.getCommand()).getFormattedResult();
                    tvMassAir.setText(maf);
                    Log.e(TAG, "stateUpdate: maf" + maf);
                } else if (AvailableCommandNames.ENGINE_COOLANT_TEMP.getValue().equals(cmdName)) {
                    tem = ((EngineCoolantTemperatureObdCommand) job.getCommand()).getTemperature();
                    dashboardViewTem.setRealTimeValue(tem, true, 100);
                    Log.e(TAG, "stateUpdate: " + tem);
                } else if (AvailableCommandNames.AMBIENT_AIR_TEMP.getValue().equals(cmdName)) {
                    tvAmbAirTem.setText(((AmbientAirTemperatureObdCommand) job.getCommand()).getFormattedResult());
                    Log.e(TAG, "stateUpdate: AmbientAirTemperature" + cmdResult);
                } else if (AvailableCommandNames.DTC_NUMBER.getValue().equals(cmdName)) {
                    int count = ((DtcNumberObdCommand) job.getCommand()).getTotalAvailableCodes();
                    final ObdCommandJob trouble = new ObdCommandJob(new TroubleCodesObdCommand(count));
                    mServiceConnection.addJobToQueue(trouble);
                } else if (AvailableCommandNames.TROUBLE_CODES.getValue().equals(cmdName)) {
                    String formattedResult = ((TroubleCodesObdCommand) job.getCommand()).formatResult();
                    Log.e(TAG, "stateUpdate: " + formattedResult);
                } else {
                    Log.e(TAG, "stateUpdate: " + cmdResult);
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseWakeLockIfHeld();
        mServiceIntent = null;
        mServiceConnection = null;
        mListener = null;
        mHandler = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Pausing..");
        releaseWakeLockIfHeld();
    }

    /**
     * If lock is held, release. Lock will be held when the service is running.
     */
    private void releaseWakeLockIfHeld() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Resuming..");

//        sensorManager.registerListener(orientListener, orientSensor,
//                SensorManager.SENSOR_DELAY_UI);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
                "ObdReader");
    }

    private void startLiveData() {
        Log.d(TAG, "Starting live data..");

        if (!mServiceConnection.isRunning()) {
            Log.d(TAG, "Service is not running. Going to start it..");
            startService(mServiceIntent);
        }

        // start command execution
        mHandler.post(mQueueCommands);

        // screen won't turn off until wakeLock.release()
        wakeLock.acquire();
    }

    private void stopLiveData() {
        Log.d(TAG, "Stopping live data..");

        if (mServiceConnection.isRunning())
            stopService(mServiceIntent);

        // remove runnable
        mHandler.removeCallbacks(mQueueCommands);

        releaseWakeLockIfHeld();
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        switch (id) {
            case NO_BLUETOOTH_ID:
                build.setMessage("Sorry, your device doesn't support Bluetooth.");
                return build.create();
            case BLUETOOTH_DISABLED:
                build.setMessage("You have Bluetooth disabled. Please enable it!");
                return build.create();
            case NO_GPS_ID:
                build.setMessage("Sorry, your device doesn't support GPS.");
                return build.create();
            case NO_ORIENTATION_SENSOR:
                build.setMessage("Orientation sensor missing?");
                return build.create();
        }
        return null;
    }

    /**
     *
     */
    private Runnable mQueueCommands = new Runnable() {
        public void run() {
            /*
             * If values are not default, then we have values to calculate MPG
			 */
//            Log.e(TAG, "SPD:" + speed + ", MAF:" + maf + ", LTFT:" + ltft);
//            if (speed > 1 && maf > 1 && ltft != 0) {
//                FuelEconomyWithMAFObdCommand fuelEconCmd = new FuelEconomyWithMAFObdCommand(
//                        FuelType.DIESEL, speed, maf, ltft, false /* TODO */);
//                TextView tvMpg = (TextView) findViewById(R.id.fuel_econ_text);
//                String liters100km = String.format("%.2f", fuelEconCmd.getLitersPer100Km());
//                tvMpg.setText("" + liters100km);
//                Log.e(TAG, "FUELECON:" + liters100km);
//            }

            if (mServiceConnection.isRunning())
                queueCommands();

            // run again in 2s
            mHandler.postDelayed(mQueueCommands, 1000);
        }
    };

    /**
     *
     */
    private void queueCommands() {
        final ObdCommandJob airTemp = new ObdCommandJob(
                new AmbientAirTemperatureObdCommand());
        final ObdCommandJob speed = new ObdCommandJob(new SpeedObdCommand());
        final ObdCommandJob fuelEcon = new ObdCommandJob(
                new FuelEconomyObdCommand());
        final ObdCommandJob rpm = new ObdCommandJob(new EngineRPMObdCommand());
        final ObdCommandJob maf = new ObdCommandJob(new MassAirFlowObdCommand());
        final ObdCommandJob fuelLevel = new ObdCommandJob(
                new FuelLevelObdCommand());
        final ObdCommandJob ltft1 = new ObdCommandJob(new FuelTrimObdCommand(
                FuelTrim.LONG_TERM_BANK_1));
        final ObdCommandJob ltft2 = new ObdCommandJob(new FuelTrimObdCommand(
                FuelTrim.LONG_TERM_BANK_2));
        final ObdCommandJob stft1 = new ObdCommandJob(new FuelTrimObdCommand(
                FuelTrim.SHORT_TERM_BANK_1));
        final ObdCommandJob stft2 = new ObdCommandJob(new FuelTrimObdCommand(
                FuelTrim.SHORT_TERM_BANK_2));
        final ObdCommandJob equiv = new ObdCommandJob(new CommandEquivRatioObdCommand());
        final ObdCommandJob engine_tem = new ObdCommandJob(new EngineCoolantTemperatureObdCommand());
        final ObdCommandJob dtc = new ObdCommandJob(new DtcNumberObdCommand());

        mServiceConnection.addJobToQueue(airTemp);
        mServiceConnection.addJobToQueue(engine_tem);
        mServiceConnection.addJobToQueue(speed);
        mServiceConnection.addJobToQueue(dtc);
        // mServiceConnection.addJobToQueue(fuelEcon);
        mServiceConnection.addJobToQueue(rpm);
        mServiceConnection.addJobToQueue(maf);
        mServiceConnection.addJobToQueue(fuelLevel);
//		mServiceConnection.addJobToQueue(equiv);
        mServiceConnection.addJobToQueue(ltft1);
        // mServiceConnection.addJobToQueue(ltft2);
        // mServiceConnection.addJobToQueue(stft1);
        // mServiceConnection.addJobToQueue(stft2);
    }

    public boolean obdControlBtn() {
        // validate if preRequisites are satisfied.
        if (preRequisites) {
            if (mServiceConnection.isRunning()) {
                tvStart.setEnabled(false);
                tvStop.setEnabled(true);
                tvSetting.setEnabled(false);
//                commandItem.setEnabled(false);
            } else {
                tvStop.setEnabled(false);
                tvStart.setEnabled(true);
                tvSetting.setEnabled(true);
//                commandItem.setEnabled(false);
            }
        } else {
            tvStart.setEnabled(false);
            tvStop.setEnabled(false);
            tvSetting.setEnabled(false);
//            commandItem.setEnabled(false);
        }

        return true;
    }

    @OnClick({R.id.tv_start, R.id.tv_stop, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start:
                startLiveData();
                break;
            case R.id.tv_stop:
                stopLiveData();
                break;
            case R.id.tv_setting:
                Intent intent = new Intent(this, ConfigActivity.class);
                startActivity(intent);
                break;
        }
    }
}
