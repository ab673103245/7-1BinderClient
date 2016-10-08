package qianfeng.a7_1binderclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button addbtn;
    private IBinder mRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addbtn = ((Button) findViewById(R.id.add));

        Intent intent = new Intent("myaddservice");
        intent.setPackage("qianfeng.a7_1binderhost");
        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mRemote = service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        boolean b = bindService(intent, conn, Service.BIND_AUTO_CREATE);
        addbtn.setEnabled(b);
    }

    public void add(View view) {


        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        data.writeInterfaceToken("MyBinder");// 顺序问题啊卧槽！！！！！！!!!!!!!!!
        data.writeInt(18);
        data.writeInt(91);
//        data.enforceInterface("MyBinder");

        try {
            mRemote.transact(1,data,reply,0); // 注意这里是同步的，就是先执行这行代码，执行完里面的所有需要调用的方法，才会执行下一行
            int result = reply.readInt();
            Log.d("google-my:", "add: result:" + result);
            data.recycle();
            reply.recycle();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
