package com.mirafgantalpur.onset;


import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private Context context;
    private boolean doesUserExistAlready = true;
    FirebaseHelper(Context context) {
        this.context = context;
    }



}
