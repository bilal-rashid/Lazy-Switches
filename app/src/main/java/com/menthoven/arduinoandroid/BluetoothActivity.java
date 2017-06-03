package com.menthoven.arduinoandroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.menthoven.arduinoandroid.utils.AppUtils;
import com.menthoven.arduinoandroid.utils.Constant;

import java.lang.ref.WeakReference;
import java.util.HashMap;

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
    @Bind(R.id.button6)
    FButton button6;
    @Bind(R.id.button7)
    FButton button7;
    @Bind(R.id.button8)
    FButton button8;
    @Bind(R.id.button9)
    FButton button9;
    @Bind(R.id.button10)
    FButton button10;
    MenuItem reconnectButton;
    Snackbar snackTurnOn;
    private boolean showMessagesIsChecked = true;
    private boolean autoScrollIsChecked = true;
    public static boolean showTimeIsChecked = true;
    Drawable bulb_on, bulb_off, fan_on, fan_off, plug_on, plug_off;
    public HashMap<String,Drawable> buttons_on,buttons_off;
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


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setSupportActionBar(toolbar);

        myHandler handler = new myHandler(BluetoothActivity.this);


        assert getSupportActionBar() != null; // won't be null, lint error
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        device = getIntent().getExtras().getParcelable(Constants.EXTRA_DEVICE);

        bluetoothService = new BluetoothService(handler, device);


        bulb_on = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bulb_on);
        bulb_off = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bulb_off);
        bulb_off.setBounds(0, 0, 60, 60);
        bulb_on.setBounds(0, 0, 60, 60);
        fan_on = ContextCompat.getDrawable(getApplicationContext(), R.drawable.fan_on);
        fan_off = ContextCompat.getDrawable(getApplicationContext(), R.drawable.fan_off);
        fan_off.setBounds(0, 0, 60, 60);
        fan_on.setBounds(0, 0, 60, 60);
        plug_on = ContextCompat.getDrawable(getApplicationContext(), R.drawable.plug_on);
        plug_off = ContextCompat.getDrawable(getApplicationContext(), R.drawable.plug_off);
        plug_on.setBounds(0, 0, 60, 60);
        plug_off.setBounds(0, 0, 60, 60);

        buttons_off=new HashMap<String, Drawable>();
        buttons_on=new HashMap<String, Drawable>();
        populateHashMap( Constant.BUTTON_1);
        populateHashMap( Constant.BUTTON_2);
        populateHashMap( Constant.BUTTON_3);
        populateHashMap( Constant.BUTTON_4);
        populateHashMap( Constant.BUTTON_5);
        populateHashMap( Constant.BUTTON_6);
        populateHashMap( Constant.BUTTON_7);
        populateHashMap( Constant.BUTTON_8);

        setLongClickListener(button1, Constant.BUTTON_1);
        setLongClickListener(button2, Constant.BUTTON_2);
        setLongClickListener(button3, Constant.BUTTON_3);
        setLongClickListener(button4, Constant.BUTTON_4);
        setLongClickListener(button5, Constant.BUTTON_5);
        setLongClickListener(button6, Constant.BUTTON_6);
        setLongClickListener(button7, Constant.BUTTON_7);
        setLongClickListener(button8, Constant.BUTTON_8);

        buttonOff(button1,Constant.BUTTON_1);
        buttonOff(button2,Constant.BUTTON_2);
        buttonOff(button3,Constant.BUTTON_3);
        buttonOff(button4,Constant.BUTTON_4);
        buttonOff(button5,Constant.BUTTON_5);
        buttonOff(button6,Constant.BUTTON_6);
        buttonOff(button7,Constant.BUTTON_7);
        buttonOff(button8,Constant.BUTTON_8);

        setTitle(device.getName());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button1.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button1, Constant.BUTTON_1);
                    sendMessage("A");

                } else {
                    buttonOff(button1, Constant.BUTTON_1);
                    sendMessage("a");

                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button2.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button2, Constant.BUTTON_2);
                    sendMessage("B");

                } else {
                    buttonOff(button2, Constant.BUTTON_2);
                    sendMessage("b");

                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button3.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button3, Constant.BUTTON_3);
                    sendMessage("C");

                } else {
                    buttonOff(button3, Constant.BUTTON_3);
                    sendMessage("c");

                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button4.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button4, Constant.BUTTON_4);
                    sendMessage("D");

                } else {
                    buttonOff(button4, Constant.BUTTON_4);
                    sendMessage("d");

                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button5.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button5, Constant.BUTTON_5);
                    sendMessage("E");

                } else {
                    buttonOff(button5, Constant.BUTTON_5);
                    sendMessage("e");

                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button6.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button6, Constant.BUTTON_6);
                    sendMessage("F");

                } else {
                    buttonOff(button6, Constant.BUTTON_6);
                    sendMessage("f");

                }
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button7.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button7, Constant.BUTTON_7);
                    sendMessage("G");

                } else {
                    buttonOff(button7, Constant.BUTTON_7);
                    sendMessage("g");

                }
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button8.getText().toString().equalsIgnoreCase("OFF")) {
                    buttonOn(button8, Constant.BUTTON_8);
                    sendMessage("H");

                } else {
                    buttonOff(button8, Constant.BUTTON_8);
                    sendMessage("h");

                }
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOn(button1, Constant.BUTTON_1);
                buttonOn(button2, Constant.BUTTON_2);
                buttonOn(button3, Constant.BUTTON_3);
                buttonOn(button4, Constant.BUTTON_4);
                buttonOn(button5, Constant.BUTTON_5);
                buttonOn(button6, Constant.BUTTON_6);
                buttonOn(button7, Constant.BUTTON_7);
                buttonOn(button8, Constant.BUTTON_8);
                sendMessage("ABCDEFGH");
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonOff(button1, Constant.BUTTON_1);
                buttonOff(button2, Constant.BUTTON_2);
                buttonOff(button3, Constant.BUTTON_3);
                buttonOff(button4, Constant.BUTTON_4);
                buttonOff(button5, Constant.BUTTON_5);
                buttonOff(button6, Constant.BUTTON_6);
                buttonOff(button7, Constant.BUTTON_7);
                buttonOff(button8, Constant.BUTTON_8);
                sendMessage("abcdefgh");
            }
        });

    }

    private void populateHashMap(String buttonKey) {
        String key=device.getName()+"_"+buttonKey;
        int button_icon=AppUtils.getButtonIcon(getApplicationContext(),key);
        if(button_icon==3){
            buttons_off.put(key,plug_off);
            buttons_on.put(key,plug_on);
        }else if(button_icon==2){
            buttons_off.put(key,fan_off);
            buttons_on.put(key,fan_on);
        }else if(button_icon==1){
            buttons_off.put(key,bulb_off);
            buttons_on.put(key,bulb_on);
        }else {
            buttons_off.put(key,null);
            buttons_on.put(key,null);
        }

    }

    public void buttonOn(FButton button, String buttonKey) {
        String key=device.getName()+"_"+buttonKey;
        button.setText("ON");
        button.setCompoundDrawables(buttons_on.get(key), null, null, null);
        button.setButtonColor(getResources().getColor(R.color.colorOn));
    }

    public void buttonOff(FButton button, String buttonKey) {
        String key=device.getName()+"_"+buttonKey;
        button.setText("OFF");
        button.setCompoundDrawables(buttons_off.get(key), null, null, null);
        button.setButtonColor(getResources().getColor(R.color.colorItem));
    }

    public void setLongClickListener(FButton button, final String key) {

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final FButton btn = (FButton) view;
                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(BluetoothActivity.this);
                builderSingle.setTitle("Select Tag");

                final ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(BluetoothActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Light");
                arrayAdapter.add("Fan");
                arrayAdapter.add("Socket");
                arrayAdapter.add("None");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equals("Light")) {
                            AppUtils.saveButtonIcon(getApplicationContext(), device.getName()+"_"+key, 1);
                            if (btn.getText().toString().equalsIgnoreCase("ON")) {
                                btn.setCompoundDrawables(bulb_on, null, null, null);
                            } else {
                                btn.setCompoundDrawables(bulb_off, null, null, null);
                            }
                            populateHashMap(key);
                        } else if (strName.equals("Fan")) {
                            AppUtils.saveButtonIcon(getApplicationContext(), device.getName()+"_"+key, 2);
                            if (btn.getText().toString().equalsIgnoreCase("ON")) {
                                btn.setCompoundDrawables(fan_on, null, null, null);
                            } else {
                                btn.setCompoundDrawables(fan_off, null, null, null);
                            }
                            populateHashMap(key);
                        } else if (strName.equals("Socket")) {
                            AppUtils.saveButtonIcon(getApplicationContext(), device.getName()+"_"+key, 3);
                            if (btn.getText().toString().equalsIgnoreCase("ON")) {
                                btn.setCompoundDrawables(plug_on, null, null, null);
                            } else {
                                btn.setCompoundDrawables(plug_off, null, null, null);
                            }
                            populateHashMap(key);
                        } else {
                            AppUtils.saveButtonIcon(getApplicationContext(), device.getName()+"_"+key, 0);
                            btn.setCompoundDrawables(null, null, null, null);
                            populateHashMap(key);
                        }

                    }
                });
                builderSingle.show();
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        bluetoothService.connect();
        Log.d(Constants.TAG, "Connecting");
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

//            byte[] send = message.getBytes();
//            bluetoothService.write(send);
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
                            activity.setStatus("Error");
                            activity.reconnectButton.setVisible(true);
                            activity.toolbalProgressBar.setVisibility(View.GONE);
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
                    Snackbar.make(activity.coordinatorLayout, msg.getData().getString(Constants.SNACKBAR), Snackbar.LENGTH_LONG)
                            .setAction("Connect", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activity.reconnect();
                                }
                            }).show();

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
        menu.findItem(R.id.checkable_show_messages).setVisible(false);
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
                showMessagesIsChecked = !item.isChecked();
                item.setChecked(showMessagesIsChecked);
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
        reconnectButton.setVisible(false);
        bluetoothService.stop();
        bluetoothService.connect();
    }

}
