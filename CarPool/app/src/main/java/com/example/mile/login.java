package com.example.mile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mile.User.Password;
import com.example.mile.User.User;
import com.example.mile.User.UserDao;
import com.example.mile.User.UserDatabase;
import com.example.mile.User.UserRepo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class login extends Fragment implements UserRepo.GetUserCallback,UserRepo.GetPassCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    FirebaseUser user=auth.getCurrentUser();
    FirebaseHelp fire=FirebaseHelp.getInstance();

    public UserRepo userRepo;



    public login() {
    }

    Button loginbtn;

    public void clicktext(View view){
        TextView signUptxt = view.findViewById(R.id.signUptxt);
        String text = "Don't have an account? Sign up";

        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                FragmentManager ak = requireActivity().getSupportFragmentManager();
                FragmentTransaction ft = ak.beginTransaction();
                Sign_up frag = new Sign_up();
                ft.replace(R.id.fragmentContainerView1, frag);
                ft.addToBackStack(null);
                ft.commit();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.black, getActivity().getTheme())); // Change to your desired color
                ds.setUnderlineText(false); // Disable underline
            }
        };

        spannableString.setSpan(clickableSpan, text.indexOf("Sign up"), text.indexOf("Sign up") + "Sign up".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        signUptxt.setMovementMethod(LinkMovementMethod.getInstance());
        signUptxt.setText(spannableString);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    private boolean isValidScholarEmail(String email) {
        String emailRegex = "^[0-9]{2}p[0-9]{4}@eng\\.asu\\.edu\\.eg$";


        return email.matches(emailRegex);
    }

    @Override
    public void onStart() {
        super.onStart();
        Context context = getContext();
        if (context!=null) {
            UserDatabase userDatabase = UserDatabase.getInstance(getContext());
            UserDao userDao = userDatabase.userDao();
            userRepo = UserRepo.getInstance(userDao);
        }

        if (user!=null){
            if (isValidScholarEmail(user.getEmail())) {
                addToRoom("users", user.getUid());
                startActivity(new Intent(getActivity(), home.class));
            }
            else {
                addToRoom("drivers", user.getUid());
                startActivity(new Intent(getActivity(), driver.class));
            }
        }
    }

    @Override
    public void onUserLoaded(User user, Password existingPass) {

            if (user != null) {
                // User loaded successfully
                String password = editTextPassword.getText().toString().trim();
                Log.d("UserLoaded", "Name: " + user.getName() + ", Email: " + user.getEmail());
                Log.d("kfc2",existingPass.getPassword());
            } else {
                // User not found or other error
                Log.d("UserLoaded", "User not found or error");
            }
        }

    @Override
    public void onReturn(boolean value,User user) {
        if (value){
            Intent intent = new Intent(getActivity(), home.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        else{
        }
    }


    public void addToRoom(String type, String uid){
        fire.getUserById(type).thenAccept(user -> {
            if (user != null) {
                String password = editTextPassword.getText().toString().trim();
                userRepo.login(user,password ,this);
            } else {
                Log.d("Get user profile","2");
            }
        }).exceptionally(ex -> {
            Log.e("Get user profile", "Error getting user", ex);
            return null; // You can return a default value or handle it as needed
        });


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextEmail = view.findViewById(R.id.loginEmail);
        editTextPassword = view.findViewById(R.id.loginPassword);
        loginbtn = view.findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(view1 -> signInUser());

        clicktext(view);

    }


    public void signInUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Intent tohome = new Intent(getActivity(), home.class);
        Intent todriver = new Intent(getActivity(), driver.class);

        if (!FirebaseHelp.isNetworkConnected(getContext())){
            userRepo.verifyUserCredentials(email,password,this);
        }
        else
        {
            if (email.equals("stest") && password.equals("stest")){
                startActivity(tohome);
            }
            else if (email.equals("dtest") && password.equals("dtest")) {
                startActivity(todriver);
            }

        // Reset previous error messages
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        // Validate email
        if (!isValidEmail(email)) {
            editTextEmail.setError("Invalid email address");
            editTextEmail.requestFocus();
            return;
        }

        // Validate password
        if (!isValidPassword(password)) {
            editTextPassword.setError("Invalid password");
            editTextPassword.requestFocus();
            return;
        }

        boolean isStudenLogging = email.substring(email.lastIndexOf('@')).equals("@eng.asu.edu.eg");

        // Perform user login
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (isStudenLogging){
                            addToRoom("users",user.getUid());
                            startActivity(tohome);
                        }
                        else {
                            addToRoom("drivers", user.getUid());
                            startActivity(todriver);
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Login failed. Please check your credentials and try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }



}
