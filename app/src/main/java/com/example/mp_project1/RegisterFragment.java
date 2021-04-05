package com.example.mp_project1;

import android.app.Person;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {

    EditText register_email,pswd,con_pswd,full_name;
    TextView textview,dobtextview;
    TextView signupimg;
    RadioGroup radiogroup;
    DatePicker dob;
    Button register_btn;

    private FirebaseAuth fireauth;
    private FirebaseFirestore firestore;
    private NavController navController;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fireauth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register_email = view.findViewById(R.id.register_email);
        pswd = view.findViewById(R.id.edt_register_password);
        con_pswd = view.findViewById(R.id.confirm_password);
        full_name = view.findViewById(R.id.fullname);
        textview = view.findViewById(R.id.textView1);
        dobtextview = view.findViewById(R.id.textview2);
        signupimg = view.findViewById(R.id.textsignup);
        dob = view.findViewById(R.id.date);
        radiogroup = view.findViewById(R.id.groupradio);
        EditText city = (EditText) view.findViewById(R.id.city);
        register_btn = view.findViewById(R.id.signup);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);
             register_btn.setOnClickListener(view1 -> {


                 String email = register_email.getText().toString();
                 String pass = pswd.getText().toString();
                 String name = full_name.getText().toString();
                 String city1 = city.getText().toString();

                 com.example.mp_project1.PersonalDetails details = new com.example.mp_project1.PersonalDetails(email,pass,name,city1);
registerUser(details);
             });
    }
      public boolean checkFields(){

        if(TextUtils.isEmpty(register_email.getText().toString())){

            register_email.setError("Email is required");
            register_email.requestFocus();
            return true;

        }

        else  if(TextUtils.isEmpty(pswd.getText().toString())){

              pswd.setError("Password is required");
              pswd.requestFocus();
              return true;

          }

         else if(TextUtils.isEmpty(con_pswd.getText().toString())){

              con_pswd.setError("Confirm Password is required");
              con_pswd.requestFocus();
              return true;

          }
          else if(TextUtils.isEmpty(full_name.getText().toString())){

              con_pswd.setError("Enter your Full name is required");
              con_pswd.requestFocus();
              return true;

          }


          return false;
      }



    public void registerUser(com.example.mp_project1.PersonalDetails details)
    {
        fireauth.createUserWithEmailAndPassword(details.getEmail(),details.getPassword())
                .addOnCompleteListener(getActivity(), task -> {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = fireauth.getCurrentUser();
                        writeFireStore(details, firebaseUser);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Registration Error!",Toast.LENGTH_LONG).show();
                    }

                });
    }

    public void writeFireStore(com.example.mp_project1.PersonalDetails details, FirebaseUser firebaseUser)
    {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("Name",details.getName());
        userMap.put("Email",details.getEmail());
        userMap.put("City",details.getCity());

        firestore.collection("User").document(firebaseUser.getUid())
                .set(userMap).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(getActivity().getApplicationContext(),"Registration Success!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                navController.navigate(R.id.loginFragment);
            }else
            {
                Toast.makeText(getActivity().getApplicationContext(),"FireStore Error!",Toast.LENGTH_LONG).show();
            }
        });

    }

}
