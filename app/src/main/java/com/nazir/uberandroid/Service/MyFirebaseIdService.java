package com.nazir.uberandroid.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nazir.uberandroid.Common.Common;
import com.nazir.uberandroid.Model.Token;

public class MyFirebaseIdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        updateTokenToServer(refreshToken); //need to update RealTime database

    }

    private void updateTokenToServer(String refreshedToken){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(Common.TOKEN_TBL);

        Token token = new Token(refreshedToken);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){ //if already login then must update Token

            tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

        }


    }
}
