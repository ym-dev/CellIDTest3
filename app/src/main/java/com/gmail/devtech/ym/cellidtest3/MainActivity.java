package com.gmail.devtech.ym.cellidtest3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

//電話情報の取得
public class MainActivity extends AppCompatActivity {
    private final static int WC=LinearLayout.LayoutParams.WRAP_CONTENT;
    private TextView textView;

    //初期化
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //レイアウトの生成
        LinearLayout layout=new LinearLayout(this);
        layout.setBackgroundColor(Color.rgb(255,255,255));
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        //テキストビューの生成
        textView=new TextView(this);
        textView.setText("TelephonyEx");
        textView.setTextSize(16.0f);
        textView.setTextColor(Color.rgb(0,0,0));
        textView.setLayoutParams(new LinearLayout.LayoutParams(WC,WC));
        layout.addView(textView);

        //電話情報の取得
        TelephonyManager telManager=(TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        String str="";
        str+="電話番号:"+telManager.getLine1Number()+"\n";
        str+="SIM国コード:"+telManager.getSimCountryIso()+"\n";
        str+="SIMシリアル番号:"+telManager.getSimSerialNumber()+"\n";
        str+="デバイスID:"+telManager.getDeviceId()+"\n";
        textView.setText(str);
    }

    //アプリの再開
    @Override
    protected void onResume() {
        super.onResume();

        //電話情報の受信開始
        TelephonyManager telManager=(TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneStateListener,
//                PhoneStateListener.LISTEN_CALL_STATE|
//                        PhoneStateListener.LISTEN_SERVICE_STATE|
//                        PhoneStateListener.LISTEN_SIGNAL_STRENGTH|
                        PhoneStateListener.LISTEN_CELL_LOCATION);
    }

    //アプリの一時停止
    @Override
    protected void onPause() {
        super.onPause();

/*
        //電話情報の受信停止
        TelephonyManager telManager=(TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneStateListener,PhoneStateListener.LISTEN_NONE);
*/
    }

    //電話情報を受信するためのリスナー
    public PhoneStateListener phoneStateListener=new PhoneStateListener() {
        /*//電話コール状態の変化時に呼ばれる
        @Override
        public void onCallStateChanged(int state, String number) {
            String str="電話コール状態:";
            if (state==TelephonyManager.CALL_STATE_RINGING) str+="電話着信";
            if (state==TelephonyManager.CALL_STATE_OFFHOOK) str+="通話開始";
            if (state==TelephonyManager.CALL_STATE_IDLE)    str+="電話終了";
            str+=" "+number;
            textView.setText(textView.getText()+"\n"+str);
        }

        //サービス状態の変化時に呼ばれる
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            String str="サービス状態:";
            int state=serviceState.getState();
            if (state==ServiceState.STATE_EMERGENCY_ONLY) str+="エマージェンシーのみ";
            if (state==ServiceState.STATE_IN_SERVICE)     str+="サービス内";
            if (state==ServiceState.STATE_OUT_OF_SERVICE) str+="サービス外";
            if (state==ServiceState.STATE_POWER_OFF)      str+="電源オフ";
            textView.setText(textView.getText()+"\n"+str);
            super.onServiceStateChanged(serviceState);
        }

        //通信強度の変化時に呼ばれる
        @Override
        public void onSignalStrengthChanged(int asu) {
            String str="通信強度:"+String.valueOf(-113+2*asu)+"dBm";
            textView.setText(textView.getText()+"\n"+str);
        }
*/
        //基地局の変化時に呼ばれる
        @Override
        public void onCellLocationChanged(CellLocation location) {
            String str="";
            //GSMの基地局情報
            if (location instanceof GsmCellLocation) {
                GsmCellLocation loc=(GsmCellLocation)location;
                str+="CID:"+loc.getCid()+"\n";
                str+="LAC:"+loc.getLac()+"\n";
            }
            //CDMAの基地局情報
            else if(location instanceof CdmaCellLocation) {
                CdmaCellLocation loc=(CdmaCellLocation)location;
                str+="BaseStationId:"+loc.getBaseStationId()+"\n";
                str+="BaseStationLatitude:"+loc.getBaseStationLatitude()+"\n";
                str+="BaseStationLongitude:"+loc.getBaseStationLongitude()+"\n";
                str+="NetworkId:"+loc.getNetworkId()+"\n";
                str+="SystemId:"+loc.getSystemId()+"\n";
            }
            textView.setText(textView.getText()+"\n"+str);
        }
    };
}