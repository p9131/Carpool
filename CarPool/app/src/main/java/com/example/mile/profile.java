package com.example.mile;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mile.User.User;
import com.example.mile.User.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.CountDownLatch;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private FirebaseAuth auth = FirebaseAuth.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button logOutBtn;
    private CountDownLatch latch = new CountDownLatch(1);
    FirebaseHelp db= FirebaseHelp.getInstance();;

    TextView Nametext;
    TextView Emailtext;
    TextView Typetext;

    UserViewModel userViewModel;



    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_profile2, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setUser();
        logOutBtn = view.findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(v -> signOut());

        if (FirebaseHelp.isNetworkConnected(getContext())) {
            userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
            userViewModel.getUserLiveData(db.getCurrentUserId()).observe(getViewLifecycleOwner(), new Observer<User>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        build(view, user.getName(), user.getEmail(), user.isStudent());
                    }
                }
            });

        }
        else {
            Bundle args = getArguments();
            if (args!=null){
                User user = (User) args.getSerializable("user");
                build(view, user.getName(), user.getEmail(), user.isStudent());
            }
            else {
                build(view, "Name", "Email", true);
            }
        }
    }

    void build(View view,String name,String email,boolean isStudent){
        Nametext=view.findViewById(R.id.userNametext);
        Emailtext=view.findViewById(R.id.userEmailtext);
        Typetext=view.findViewById(R.id.userTypetext);
        Nametext.setText(name);
        Emailtext.setText(email);


        if (isStudent){
            Typetext.setText("Student");
        }
        else {
            Typetext.setText("Driver");
        }
    }

    void signOut(){
        auth.signOut();
        startActivity(new Intent(getActivity(),MainActivity.class));
    }
}