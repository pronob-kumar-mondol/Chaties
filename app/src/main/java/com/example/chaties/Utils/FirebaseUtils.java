package com.example.chaties.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils {

    //    It will return current user id
    public static String curentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


//    It will return current user details if there is any
    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(curentUserID());
    }

    //Checks if the user is logged in

    public static boolean isLoggedIn(){
        if (curentUserID() != null) {
            return true;
        }
        return false;
    }


}
