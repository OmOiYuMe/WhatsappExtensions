package com.suraj.waext;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhiteListActivity extends AppCompatActivity {
    private ArrayList<String> whitelist;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private WhiteListAdapter whiteListAdapter;
    private Set<String> whitelistSet;
    private ListView lstviewwhitelist;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_list);

        sharedPreferences = getSharedPreferences("myprefs",1);
        editor=sharedPreferences.edit();

        whitelistSet = sharedPreferences.getStringSet("rd_whitelist",new HashSet<String>());

        whitelist = new ArrayList<>(whitelistSet);

        whiteListAdapter = new WhiteListAdapter(getApplicationContext(),whitelist);

        lstviewwhitelist = (ListView)findViewById(R.id.lstviewwhitelistcontacts);

        lstviewwhitelist.setAdapter(whiteListAdapter);

        (findViewById(R.id.fbaddtowhitelist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent();
                intent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.ContactPicker"));
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null){
            String number = data.getExtras().get("contact").toString().split("@")[0];

            whitelistSet.add(number);

            whitelist = new ArrayList<>(whitelistSet);
            lstviewwhitelist.setAdapter(null);
            lstviewwhitelist.setAdapter(new WhiteListAdapter(getApplicationContext(),whitelist));
            editor.putStringSet("rd_whitelist",whitelistSet);
            editor.apply();

        }
    }
}
