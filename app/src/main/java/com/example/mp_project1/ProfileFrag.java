package com.example.mp_project1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class ProfileFrag extends Fragment {
    private TextView Name,email,dob,city,gender;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private FirebaseUser currentUser;


    public ProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Profile");

        Name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        dob = view.findViewById(R.id.dob);
        city = view.findViewById(R.id.city);
        gender = view.findViewById(R.id.gender);

        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        getUserData();
    }

    private void getUserData() {


        currentUser = firebaseAuth.getCurrentUser();

        DocumentReference doc = fireStore.collection("User").document(currentUser.getUid());

        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    String Name1 = documentSnapshot.getString("Name");
                    String Email = documentSnapshot.getString("Email");
                    String Gender = documentSnapshot.getString("Gender");
                    String Birthday = documentSnapshot.getString("Date of Birth");
                    String City = documentSnapshot.getString("City");

                    dob.setText(Birthday);
                    city.setText(City);
                    email.setText(Email);
                    gender.setText(Gender);
                    Name.setText(Name1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Data import error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
