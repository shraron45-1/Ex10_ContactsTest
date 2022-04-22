package com.example.ex10_contactstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView contactsView = (ListView) findViewById(R.id.contacts_list);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contactsList);
        contactsView.setAdapter(adapter);
        //判断用户是否已经授权
        if (ContextCompat.checkSelfPermission(this, Manifest.
                permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //没有授权
            //向用户申请授权
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS},1);
        }else {
            //授权
            readContacts();
        }

        contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String s = contactsList.get(position);
                Intent intent = new Intent(MainActivity.this, CameraTest.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", 1);
                bundle.putString("name", s);
//                bundle.putString("phone", s);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
            //查询联系人数据
            cursor = getContentResolver().query(ContactsContract.
                    CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    //获取联系人姓名
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人手机号
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                adapter.notifyDataSetChanged();//刷新listView
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //授权结构封装在grantResults参数中
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    //同意的话就调用
                    readContacts();
                }else {
                    //不同意就放弃操作，弹出失败的提示
                    Toast.makeText(this,"You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}