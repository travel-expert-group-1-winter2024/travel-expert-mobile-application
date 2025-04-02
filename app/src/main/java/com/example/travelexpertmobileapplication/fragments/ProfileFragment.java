package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Agent;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.AgentAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Finding all necessary elements by ID

        MaterialButton btnEditProfile = view.findViewById(R.id.btnEditProfile);


       //Edit Profile OnClick handler

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment editProfileFragment = new EditProfileFragement();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, editProfileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // With the fragment inflated, grabbing TextView id's.
        TextView textViewFirstName = view.findViewById(R.id.textFieldFirstName);
        TextView textViewMiddleInitial = view.findViewById(R.id.textFieldMiddleInitial);
        TextView textViewLastName = view.findViewById(R.id.textFieldLastName);
        TextView textViewPhoneNumber = view.findViewById(R.id.textFieldBusPhoneNumber);
        TextView textViewEmail = view.findViewById(R.id.textFieldEmail);
        TextView textViewPosition = view.findViewById(R.id.textFieldPosition);

        // Handling JWT errors.
        String token = SharedPrefUtil.getToken(requireContext());
        if (token == null){
            Toast.makeText(requireContext(), "Authentication token missing", Toast.LENGTH_SHORT).show();
        }

        AgentAPIService agentAPIService = ApiClient.getClient().create(AgentAPIService.class);
        Call<Agent> call = agentAPIService.getMyAgentInfo("Bearer " + token);

        call.enqueue(new Callback<Agent>() {


            @Override
            public void onResponse(Call<Agent> call, Response<Agent> response) {
                if (response.isSuccessful() && response.body() != null ) {
                    Agent agent = response.body();
                    Timber.tag("onResponse Call").d(String.valueOf(response));
                    

                    textViewFirstName.setText(agent.getAgtFirstName());
                    textViewMiddleInitial.setText(agent.getAgtMiddleInitial());
                    textViewLastName.setText(agent.getAgtLastName());
                    textViewPhoneNumber.setText(agent.getAgtBusPhone());
                    textViewEmail.setText(agent.getAgtEmail());
                    textViewPosition.setText(agent.getAgtPosition());
                } else {
                    Timber.tag("FAILED TO FETCH").d(String.valueOf("Failed to fetch Agent Information! " + response));
                    Toast.makeText(requireContext(), "Failed to fetch Agent Information!", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Agent> call, Throwable t) {
                Timber.tag("onFailure:").e("Api call failed: %s", t.getMessage());
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}//class