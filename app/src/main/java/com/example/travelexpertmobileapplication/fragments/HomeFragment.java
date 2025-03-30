package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.dto.agent.CreateAgentRequestDTO;
import com.example.travelexpertmobileapplication.dto.user.LoginRequestDTO;
import com.example.travelexpertmobileapplication.model.Agent;
import com.example.travelexpertmobileapplication.model.Product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        //Finding all Elements needed
        //LinearLayouts
        LinearLayout linearLayout_Packages = view.findViewById(R.id.linearLayout_Packages);
        LinearLayout linearLayout_Customers = view.findViewById(R.id.linearyLayout_Customers);
        LinearLayout linearLayout_Suppliers = view.findViewById(R.id.linearyLayout_Suppliers);
        LinearLayout linearLayout_Products = view.findViewById(R.id.linearyLayout_Products);



        //System.out.println(loginRequestDTO.getUsername());



        //OnClick Handlers for each LinearLayout acting as a button.

        /**
         * LinearLayout acting as Packages "button"
         * OnClick Event Listener to load the Packages Fragment
         */
        linearLayout_Packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding animation to the button to improve user feedback.
                Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                blink.setAnimationListener(new Animation.AnimationListener(){

                    @Override //Needed for setAnimationListener, not needed to implement.
                    public void onAnimationStart(Animation animation) {
                        return;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Loading the fragment
                        Fragment packagesFragment = new PackagesFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, packagesFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        return;
                    }
                });

                linearLayout_Packages.startAnimation(blink);


            }
        });

        /**
         * LinearLayout acting as Customer "button"
         * OnClick Event Listener to load the Customer Fragment
         */
        linearLayout_Customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                blink.setAnimationListener(new Animation.AnimationListener(){

                    @Override
                    public void onAnimationStart(Animation animation) {
                        return;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Loading the fragment
                        Fragment customerFragment = new CustomerFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, customerFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        return;
                    }
                });

                linearLayout_Customers.startAnimation(blink);

            }
        });

        /**
         * LinearLayout acting as the Suppliers "button"
         * OnClick Event Listener to load the Supplier Fragment
         */
        linearLayout_Suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                blink.setAnimationListener(new Animation.AnimationListener(){

                    @Override //Needed for setAnimationListener, not needed to implement.
                    public void onAnimationStart(Animation animation) {
                        return;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Loading the fragment
                        Fragment supplierFragment = new SupplierFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, supplierFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        return;
                    }
                });
                linearLayout_Suppliers.startAnimation(blink);

            }


        });

        /**
         * LinearLayout acting as the Products "button"
         * OnClick Event Listener to load the Products Fragment
         */
        linearLayout_Products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                blink.setAnimationListener(new Animation.AnimationListener(){

                    @Override //Needed for setAnimationListener, not needed to implement.
                    public void onAnimationStart(Animation animation) {
                        return;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Loading the fragment
                        Fragment productFragment = new ProductFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, productFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        return;
                    }
                });

                linearLayout_Products.startAnimation(blink);

            }
        });



        return view;
    }

    /**
     * WIP, Dynamically load Agents First name.
     */
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        TextView agentGreeting = view.findViewById(R.id.agentGreeting);
//        String agentName = "";
//        Bundle args = getArguments();
//        if(args != null){
//            agentName = args.getString("FirstName", "");
//        }
//
//
//
//        //TextView
//
//        agentGreeting.setText("Hello, " + agentName + "!");
//
//
//    }
}