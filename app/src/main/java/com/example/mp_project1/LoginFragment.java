package com.example.mp_project1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {


    NavController navController;
    EditText edt_email, edt_pass;
    Button btn_login,btn_register;

    FirebaseUser currentUser;
    private FirebaseAuth fireAuth;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fireAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        edt_email = view.findViewById(R.id.Emailid);
        edt_pass = view.findViewById(R.id.Password);
        btn_login = view.findViewById(R.id.MainLoginBtn);

        btn_register = view.findViewById(R.id.registerButton);

        navController = Navigation.findNavController(getActivity(),R.id.host_fragment);


        btn_login.setOnClickListener(view2 ->{

                String email = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();
                loginUser(email,pass);


        });

        btn_register.setOnClickListener(view1 -> {
            navController.navigate(R.id.registerFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LoginFragment","onStart Called!");

        currentUser = fireAuth.getCurrentUser();

        if (currentUser != null)
        {
            updateUI(currentUser);
            Toast.makeText(getActivity().getApplicationContext(),"User Already Signing",Toast.LENGTH_LONG).show();
        }
    }

    public void loginUser(String email, String pass)
    {
        fireAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(getActivity(), task -> {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity().getApplicationContext(),"Login Success!", Toast.LENGTH_SHORT).show();
                        currentUser = fireAuth.getCurrentUser();
                        updateUI(currentUser);
                    }else {
                        Toast.makeText(getActivity().getApplicationContext(),"Authenticate Failed!", Toast.LENGTH_SHORT).show();
                    }

                });
    }



    public void updateUI(FirebaseUser user)
    {
        Bundle b = new Bundle();
        b.putParcelable("user",user);
//     navController.navigate(R.id.defaultFragment,b);
       Intent i = new Intent(getContext(),WelcomeScreen.class);
       i.putExtra("message","Welcome");
       startActivity(i,b);

    }
}
