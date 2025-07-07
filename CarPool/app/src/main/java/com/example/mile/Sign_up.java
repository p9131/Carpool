package com.example.mile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
// Import the necessary libraries
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mile.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sign_up#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Sign_up extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText editTextFirstName,editTextLastName,editTextEmail, editTextPassword;
    private Button btnRegister;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseHelp db= FirebaseHelp.getInstance();;

    FirebaseFirestore fire;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sign_up.
     */
    // TODO: Rename and change types and number of parameters
    public static Sign_up newInstance(String param1, String param2) {
        Sign_up fragment = new Sign_up();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Sign_up() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);

    }


    RadioGroup radioGroup;
    RadioButton radioButton;
    Boolean isStudent=true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEmail = view.findViewById(R.id.signUpEmail);
        editTextPassword = view.findViewById(R.id.signUpPassword);
        editTextLastName = view.findViewById(R.id.signUpLastName);
        editTextFirstName = view.findViewById(R.id.signUpFirstName);
        btnRegister = view.findViewById(R.id.signUpbtn);

        radioGroup = view.findViewById(R.id.accountTypeRadio);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            radioButton = view.findViewById(checkedId);
            isStudent= radioButton.getText().toString().trim().equals("Student");
        });

        btnRegister.setOnClickListener(v -> registerUser());

    }


    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();



        // Reset previous error messages
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        // Validate email
        if (isStudent) {
            if (!isValidScholarEmail(email)) {
                editTextEmail.setError("Please use a scholar email address with the format NNpNNNN@eng.asu.edu.eg");
                editTextEmail.requestFocus();
                return;
            }
        }
        else {
            if (!isValidEmail(email)) {
                editTextEmail.setError("Please use a valid email format");
                editTextEmail.requestFocus();
                return;
            }
        }

        // Validate password
        if (!isValidPassword(password)) {
            editTextPassword.setError("Invalid password. Must be at least 6 characters long.");
            editTextPassword.requestFocus();
            return;
        }

        // Perform user registration
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    Intent tohome = new Intent(getActivity(),home.class);
                    Intent todriver = new Intent(getActivity(),driver.class);
                    if (task.isSuccessful()) {
                        if (isStudent) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User user = new User(firebaseUser != null ? firebaseUser.getUid() : "", firstName + " " + lastName, email,true);
                            db.addUser(user);
                        }
                        else {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User user = new User(firebaseUser.getUid(), firstName + " " + lastName, email,false);
                            db.addDriver(user);
                        }
                        Toast.makeText(getActivity(), "Successful Sign Up. Go back to Login page", Toast.LENGTH_SHORT).show();
                    } else {
                        String error=getErrorMessage(task.getException());
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            editTextEmail.setError("Email address already used by another account.");
            editTextEmail.requestFocus();
            return "Email address already used";
        } else if (exception instanceof FirebaseAuthWeakPasswordException) {
            return "Weak password";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "Invalid email or password format";
        } else {
            return "Registration failed. Please try again.";
        }
    }

    private boolean isValidScholarEmail(String email) {
        String emailRegex = "^[0-9]{2}p[0-9]{4}@eng\\.asu\\.edu\\.eg$";

        int currentYear = (Calendar.getInstance().get(Calendar.YEAR))%100;

        if (email.matches(emailRegex)) {
            int enrollmentYear = Integer.parseInt(email.substring(0, 2));
            int oldestYear = currentYear-10;
            return enrollmentYear <= currentYear && enrollmentYear >= oldestYear;
        }

        return false;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }


}