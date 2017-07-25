package com.menthoven.arduinoandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.menthoven.arduinoandroid.Recievers.AlarmReceiver;
import com.menthoven.arduinoandroid.utils.AppUtils;
import com.menthoven.arduinoandroid.utils.Constant;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class BluetoothActivity extends AppCompatActivity {


    BluetoothService bluetoothService;
    BluetoothDevice device;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_progress_bar)
    ProgressBar toolbalProgressBar;
    @Bind(R.id.coordinator_layout_bluetooth)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.button1)
    FButton button1;
    @Bind(R.id.button2)
    FButton button2;
    @Bind(R.id.button3)
    FButton button3;
    @Bind(R.id.button4)
    FButton button4;
    @Bind(R.id.button5)
    FButton button5;
    @Bind(R.id.button7)
    FButton button7;
    MenuItem reconnectButton;
    Snackbar snackTurnOn;
    private boolean showMessagesIsChecked = true;
    private boolean autoScrollIsChecked = true;
    public static boolean showTimeIsChecked = true;
    public static String State;
    TimePickerDialog timePickerDialog;
    final static int RQS_1 = 1;


    PowerManager.WakeLock mWakeLock;
    public String buttons_state = "abcde";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        ButterKnife.bind(this);

        snackTurnOn = Snackbar.make(coordinatorLayout, "Bluetooth turned off", Snackbar.LENGTH_INDEFINITE)
                .setAction("Turn On", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enableBluetooth();
                    }
                });


        setSupportActionBar(toolbar);

        myHandler handler = new myHandler(BluetoothActivity.this);


        assert getSupportActionBar() != null; // won't be null, lint error
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        device = getIntent().getExtras().getParcelable(Constants.EXTRA_DEVICE);
        State = getIntent().getStringExtra(Constants.STATE_DEVICE);
        bluetoothService = new BluetoothService(handler, device);

        buttonOff(button1);
        buttonOff(button2);
        buttonOff(button3);
        buttonOff(button4);
        buttonOff(button5);
        buttonOff(button7);

        setTitle(device.getName());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button1.getText().toString().contains("OFF")) {
                    buttons_state = "A";
                    buttonOn(button1);
                    buttonOff(button2);
                    buttonOff(button3);
                    buttonOff(button4);
                    buttonOff(button5);

                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button2.getText().toString().contains("OFF")) {
                    buttons_state = "B";
                    buttonOn(button2);
                    buttonOff(button1);
                    buttonOff(button3);
                    buttonOff(button4);
                    buttonOff(button5);

                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button3.getText().toString().contains("OFF")) {
                    buttons_state = "C";
                    buttonOn(button3);
                    buttonOff(button2);
                    buttonOff(button1);
                    buttonOff(button4);
                    buttonOff(button5);

                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button4.getText().toString().contains("OFF")) {
                    buttons_state = "D";
                    buttonOn(button4);
                    buttonOff(button2);
                    buttonOff(button3);
                    buttonOff(button1);
                    buttonOff(button5);
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button5.getText().toString().contains("OFF")) {
                    buttons_state = "E";
                    buttonOn(button5);
                    buttonOff(button2);
                    buttonOff(button3);
                    buttonOff(button4);
                    buttonOff(button1);
                }
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button7.getText().toString().contains("OFF")) {
                    sendMessage("F");
                    buttonOn(button7);

                } else {
                    sendMessage("f");
                    buttonOff(button7);

                }
            }
        });

    }

    public void buttonOn(FButton button) {
        button.setText(button.getText().toString().replace("OFF", "ON"));
        if (button.getId() != R.id.button7) {
            Drawable clock = ContextCompat.getDrawable(getApplicationContext(), R.drawable.clock);
            clock.setBounds(0, 10, 60, 60);
            button.setCompoundDrawables(null, clock, null, null);
        }
        button.setButtonColor(getResources().getColor(R.color.colorOn));
    }

    public void buttonOff(FButton button) {
        button.setText(button.getText().toString().replace("ON", "OFF"));
        button.setCompoundDrawables(null, null, null, null);
        button.setButtonColor(getResources().getColor(R.color.colorItem));
    }


    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(BluetoothActivity.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {
//        Calendar calNow = ;
        long timeInMilis = targetCal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        long seconds = timeInMilis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String time = hours % 24 + " hours," + minutes % 60 + " minutes," + seconds % 60 + " seconds";


        Toast.makeText(getApplicationContext(), "Alarm set to " + (time) + " from now", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra(Constants.EXTRA_DEVICE, device);
        intent.putExtra(Constants.STATE_DEVICE, buttons_state);
//        Log.d("mmeessaaggee","buttons alarm state  "+buttons_state);
        AppUtils.saveAlarmState(getApplicationContext(), Constant.BUTTON_ALARM_STATE, buttons_state);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        Log.d("Alarm", "Onstart");
        bluetoothService.connect();
        Log.d(Constants.TAG, "Connecting");
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        mWakeLock.acquire();
//        KeyguardManager manager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
//        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
//        lock.disableKeyguard();
    }

    public void stopActivity() {
//        mWakeLock.release();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bluetoothService.stop();

            finishAndRemoveTask();
        } else {
            bluetoothService.stop();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bluetoothService != null) {
            bluetoothService.stop();
            Log.d(Constants.TAG, "Stopping");
        }

        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                setStatus("None");
            } else {
                setStatus("Error");
                Snackbar.make(coordinatorLayout, "Failed to enable bluetooth", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableBluetooth();
                            }
                        }).show();
            }
        }

    }


    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != Constants.STATE_CONNECTED) {
            Snackbar.make(coordinatorLayout, "You are not connected", Snackbar.LENGTH_LONG)
                    .setAction("Connect", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reconnect();
                        }
                    }).show();
            return;
        } else {
//            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
//                    .setAction("Connect", new View.OnClickListener() {
//                        @Override public void onClick(View v) {
//                            reconnect();
//                        }
//                    }).show();


            byte[] send = message.getBytes();
            bluetoothService.write(send);

        }
    }


    private static class myHandler extends Handler {
        private final WeakReference<BluetoothActivity> mActivity;

        public myHandler(BluetoothActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            final BluetoothActivity activity = mActivity.get();

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case Constants.STATE_CONNECTED:
                            activity.setStatus("Connected");
                            activity.reconnectButton.setVisible(false);
                            activity.toolbalProgressBar.setVisibility(View.GONE);
//                            Toast.makeText(activity.getApplicationContext(),State,Toast.LENGTH_LONG).show();
                            if (!activity.State.equals("null")) {
                                activity.sendMessage(AppUtils.getAlarmState(activity.getApplicationContext(),
                                        Constant.BUTTON_ALARM_STATE));
                                activity.State = "null";
                                AppUtils.saveAlarmState(activity.getApplicationContext(), Constant.BUTTON_ALARM_STATE,
                                        "null");
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                activity.stopActivity();
                            }

                            break;
                        case Constants.STATE_CONNECTING:
                            activity.setStatus("Connecting");
                            activity.toolbalProgressBar.setVisibility(View.VISIBLE);
                            break;
                        case Constants.STATE_NONE:
                            activity.setStatus("Not Connected");
                            activity.toolbalProgressBar.setVisibility(View.GONE);
                            break;
                        case Constants.STATE_ERROR:
                            try {

                                activity.setStatus("Error");
                                activity.reconnectButton.setVisible(true);
                                activity.toolbalProgressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                            }

                            if (!activity.State.equals("null")) {

                                activity.reconnect();
                            }
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //  ChatMessage messageWrite = new ChatMessage("Me", writeMessage);
                    //activity.addMessageToAdapter(messageWrite);
                    break;
                case Constants.MESSAGE_READ:

                    String readMessage = (String) msg.obj;

                    if (readMessage != null && activity.showMessagesIsChecked) {
                        ChatMessage messageRead = new ChatMessage(activity.device.getName(), readMessage.trim());
                        activity.addMessageToAdapter(messageRead);

                    }
                    break;

                case Constants.MESSAGE_SNACKBAR:
                    try {
                        Snackbar.make(activity.coordinatorLayout, msg.getData().getString(Constants.SNACKBAR), Snackbar.LENGTH_LONG)
                                .setAction("Connect", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        activity.reconnect();
                                    }
                                }).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }


    }

    private void addMessageToAdapter(ChatMessage chatMessage) {


//        Snackbar.make(coordinatorLayout, chatMessage.getMessage(), Snackbar.LENGTH_LONG)
//                .setAction("Connect", new View.OnClickListener() {
//                    @Override public void onClick(View v) {
//                        reconnect();
//                    }
//                }).show();

    }

    private void scrollChatListViewToBottom() {
//        chatListView.post(new Runnable() {
//            @Override
//            public void run() {
//                // Select the last row so it will scroll into view...
//                chatListView.smoothScrollToPosition(chatAdapter.getCount() - 1);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
        reconnectButton = menu.findItem(R.id.action_reconnect);
        menu.findItem(R.id.action_clear).setVisible(false);
        menu.findItem(R.id.checkable_auto_scroll).setVisible(false);
        menu.findItem(R.id.checkable_show_messages).setVisible(true);
        menu.findItem(R.id.checkable_show_time).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                bluetoothService.stop();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_reconnect:
                reconnect();
                return true;
            case R.id.action_clear:
                // chatAdapter.clear();
                return true;
            case R.id.checkable_auto_scroll:
                autoScrollIsChecked = !item.isChecked();
                item.setChecked(autoScrollIsChecked);
                return true;
            case R.id.checkable_show_messages:
                openTimePickerDialog(false);
                return true;
            case R.id.checkable_show_time:
                showTimeIsChecked = !item.isChecked();
                item.setChecked(showTimeIsChecked);
                // chatAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        snackTurnOn.show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        if (snackTurnOn.isShownOrQueued()) snackTurnOn.dismiss();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        reconnect();
                }
            }
        }
    };

    private void setStatus(String status) {
        toolbar.setSubtitle(status);
    }

    private void enableBluetooth() {
        setStatus("Enabling Bluetooth");
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
    }

    private void reconnect() {
        try {
            reconnectButton.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bluetoothService.stop();
            bluetoothService.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
