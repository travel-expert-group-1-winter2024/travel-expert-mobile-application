package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.dto.agent.AgentDetailsResponseDTO;
import com.example.travelexpertmobileapplication.dto.generic.GenericApiResponse;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.AgentAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /**
     * This method is called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} returns.
     * This method is used to grab any needed elements by ID, set up event listeners and handle any logic
     * that requires the main view to be ready.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instantiating a bundle to pass on the agent data to to editProfileFragment
        // Preventing the need to make another API call.
        Bundle bundle = new Bundle();

        // With the fragment inflated, grabbing TextView id's.
        TextView agentGreeting = view.findViewById(R.id.agentGreeting);
        TextView textViewFirstName = view.findViewById(R.id.textFieldFirstName);
        TextView textViewMiddleInitial = view.findViewById(R.id.textFieldMiddleInitial);
        TextView textViewLastName = view.findViewById(R.id.textFieldLastName);
        TextView textViewPhoneNumber = view.findViewById(R.id.textFieldBusPhoneNumber);
        TextView textViewEmail = view.findViewById(R.id.textFieldEmail);
        TextView textViewPosition = view.findViewById(R.id.textFieldPosition);

        //Grabbing Button Id
        MaterialButton btnEditProfile = view.findViewById(R.id.btnEditProfile);

        //Edit Profile OnClick handler
        btnEditProfile.setOnClickListener(v -> {
            openEditProfileFragment(bundle);
        });

        // Handling JWT errors.
        String token = SharedPrefUtil.getToken(requireContext());
        if (token == null) {
            Toast.makeText(requireContext(), "Authentication token missing", Toast.LENGTH_SHORT).show();
        } else {
            Timber.tag("Token Debug").d("Token being sent: Bearer %s", token);
        }

        AgentAPIService agentAPIService = ApiClient.getClient().create(AgentAPIService.class);
        Call<GenericApiResponse<AgentDetailsResponseDTO>> call = agentAPIService.getMyAgentInfo("Bearer " + token);

        call.enqueue(new Callback<GenericApiResponse<AgentDetailsResponseDTO>>() {
            @Override
            public void onResponse(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Response<GenericApiResponse<AgentDetailsResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AgentDetailsResponseDTO agentInfo = response.body().getData();
                    Timber.tag("onResponse Call").d(String.valueOf(response));

                    //Bundling up the Agent info to pass to EditProfileFragment
                    bundle.putLong("id", agentInfo.getId());
                    bundle.putString("firstName", agentInfo.getAgtFirstName());
                    bundle.putString("middleInitial", agentInfo.getAgtMiddleInitial());
                    bundle.putString("lastName", agentInfo.getAgtLastName());
                    bundle.putString("busPhone", agentInfo.getAgtBusPhone());
                    bundle.putString("email", agentInfo.getAgtEmail());
                    bundle.putString("position", agentInfo.getAgtPosition());

                    //Setting the agent info into the waiting and available TextViews.
                    agentGreeting.setText(String.format("Every detail matters,%s", agentInfo.getAgtFirstName()));
                    textViewFirstName.setText(agentInfo.getAgtFirstName());
                    textViewMiddleInitial.setText(agentInfo.getAgtMiddleInitial());
                    textViewLastName.setText(agentInfo.getAgtLastName());
                    textViewPhoneNumber.setText(agentInfo.getAgtBusPhone());
                    textViewEmail.setText(agentInfo.getAgtEmail());
                    textViewPosition.setText(agentInfo.getAgtPosition());
                } else {
                    Timber.tag("FAILED TO FETCH").d(String.valueOf("Failed to fetch Agent Information! " + response));
                    Toast.makeText(requireContext(), "Failed to fetch Agent Information!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Throwable t) {
                Timber.tag("onFailure:").e("Api call failed: %s", t.getMessage());
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * This method is responsible for starting the process to inflate the Agent Edit Profile fragment.
     * It instantiates a new fragment, sets a bundle as arguments and starts/commits the transaction.
     *
     * @param bundle A bundle containing the agent information collected from the API call in the {@link #onViewCreated(View, Bundle)}
     *               method.
     */
    private void openEditProfileFragment(Bundle bundle) {

        Fragment editProfileFragment = new EditProfileFragment();
        editProfileFragment.setArguments(bundle);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
}//class