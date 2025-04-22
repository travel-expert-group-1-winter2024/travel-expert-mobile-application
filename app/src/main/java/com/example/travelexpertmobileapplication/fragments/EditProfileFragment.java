package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.dto.agent.AgentDetailsResponseDTO;
import com.example.travelexpertmobileapplication.dto.agent.AgentUpdateDetailRequestDTO;
import com.example.travelexpertmobileapplication.dto.generic.ErrorInfo;
import com.example.travelexpertmobileapplication.dto.generic.GenericApiResponse;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.AgentAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;
import com.example.travelexpertmobileapplication.utils.SignOutUtil;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

//    TODO: Set up Properties, so that they match the bundle
//    TODO: Set up onCreate to accept arguments from bundle
//    TODO: Set up onCreateView, with elements by ID and event listeners.
//    TODO: Display information brought over from ProfileFragment on TextViews
//    TODO: Set up Save Button event listener to call method saveUpdatedData
//    TODO: saveUpdatedData method will save the new data to the db and re-inflate the Profile Page.
//    TODO: Implement back button to return to the previous Fragment if user decides not to update data.

    private final Map<TextView, String> initialAgentData = new HashMap<>();
    private final Map<TextView, Boolean> textChangedMap = new HashMap<>();
    private Long id;
    private String firstName;
    private TextView textFieldFirstName;
    private String middleInitial;
    private TextView textFieldMiddleInitial;
    private String lastName;
    private TextView textFieldLastName;
    private String busPhone;
    private TextView textFieldBusPhone;
    private String email;
    private TextView textFieldEmail;
    private String position;
    private TextView textFieldPosition;
    private String agentImageUrl;
    private ImageView ivAgentProfilePic;
    private Button btnSignOut;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    public static EditProfileFragment newInstance(
            Long id,
            String firstName,
            String middleInitial,
            String lastName,
            String busPhone,
            String email,
            String position) {

        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();

        args.putLong("id", id);
        args.putString("firstName", firstName);
        args.putString("middleInitial", middleInitial);
        args.putString("lastName", lastName);
        args.putString("busPhone", busPhone);
        args.putString("email", email);
        args.putString("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            id = getArguments().getLong("id");
            firstName = getArguments().getString("firstName");
            middleInitial = getArguments().getString("middleInitial");
            lastName = getArguments().getString("lastName");
            busPhone = getArguments().getString("busPhone");
            email = getArguments().getString("email");
            position = getArguments().getString("position");
            agentImageUrl = getArguments().getString("agentImageUrl");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Loading bundle data after the view has been inflated
        // Grabbing TextView Id's.
        textFieldFirstName = view.findViewById(R.id.textFieldFirstName);
        textFieldMiddleInitial = view.findViewById(R.id.textFieldMiddleInitial);
        textFieldLastName = view.findViewById(R.id.textFieldLastName);
        textFieldBusPhone = view.findViewById(R.id.textFieldBusPhoneNumber);
        textFieldEmail = view.findViewById(R.id.textFieldEmail);
        textFieldPosition = view.findViewById(R.id.textFieldPosition);
        ivAgentProfilePic = view.findViewById(R.id.ivAgentProfilePic);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(v -> SignOutUtil.signOut(requireContext()));

        //Grabbing Button id;
        MaterialButton saveChanges = view.findViewById(R.id.btnSaveChanges);

        //Setting the original data from the Profile Fragment.
        textFieldFirstName.setText(firstName);
        textFieldMiddleInitial.setText(middleInitial);
        textFieldLastName.setText(lastName);
        textFieldBusPhone.setText(busPhone);
        textFieldEmail.setText(email);
        textFieldPosition.setText(position);

        // setting agent profile pictrure
        Glide.with(requireContext())
                .load(agentImageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(ivAgentProfilePic);

        //? Check for changes in the existing String objects
        //? If changes, activate the saveChanges button
        //? If no changes, or changes are reverted to original state, deactivate button.

        //Storing the initial Agent Data in the HashMap
        initialAgentData.put(textFieldFirstName, firstName);
        initialAgentData.put(textFieldMiddleInitial, middleInitial);
        initialAgentData.put(textFieldLastName, lastName);
        initialAgentData.put(textFieldBusPhone, busPhone);
        initialAgentData.put(textFieldEmail, email);
        initialAgentData.put(textFieldPosition, position);

        //Initializing textChangedMap to keep track of changes in the six different TextViews
        textChangedMap.put(textFieldFirstName, false);
        textChangedMap.put(textFieldMiddleInitial, false);
        textChangedMap.put(textFieldLastName, false);
        textChangedMap.put(textFieldBusPhone, false);
        textChangedMap.put(textFieldEmail, false);
        textChangedMap.put(textFieldPosition, false);

        // Central TextWatcher for all TextViews
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No use for this method, leaving blank as its necessary for the TextWatcher
                return;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Directly compare text of each TextView with its initial value
                if (charSequence != null) {
                    String updatedText = charSequence.toString();
                    if (textFieldFirstName.getText().toString().equals(initialAgentData.get(textFieldFirstName))) {
                        textChangedMap.put(textFieldFirstName, false);
                    } else {
                        textChangedMap.put(textFieldFirstName, true);
                    }
                    if (textFieldMiddleInitial.getText().toString().equals(initialAgentData.get(textFieldMiddleInitial))) {
                        textChangedMap.put(textFieldMiddleInitial, false);
                    } else {
                        textChangedMap.put(textFieldMiddleInitial, true);
                    }
                    // Add similar checks for the other TextViews (LastName, BusPhone, Email, Position)
                    // Finally, check if any change occurred
                    if (hasAnyTextChanged()) {
                        activateButton(saveChanges);
                    } else {
                        deactivateButton(saveChanges);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //No use for this method, leaving blank as its necessary for the TextWatcher
                return;
            }
        };

        textFieldFirstName.addTextChangedListener(textWatcher);
        textFieldMiddleInitial.addTextChangedListener(textWatcher);
        textFieldLastName.addTextChangedListener(textWatcher);
        textFieldBusPhone.addTextChangedListener(textWatcher);
        textFieldEmail.addTextChangedListener(textWatcher);
        textFieldPosition.addTextChangedListener(textWatcher);

        //Check if Text has changed
        if (hasAnyTextChanged()) {
            activateButton(saveChanges);
        } else deactivateButton(saveChanges);


        saveChanges.setOnClickListener(v -> {
            saveAgentInfo();

        });


    }

    private void deactivateButton(MaterialButton saveChanges) {
        Timber.tag("DEACTIVATING SAVE BUTTON").e("Button deactivated");
        saveChanges.setEnabled(false);
        saveChanges.setAlpha(0.5f);
    }

    private void activateButton(MaterialButton saveChanges) {
        Timber.tag("ACTIVATING SAVE BUTTON");
        saveChanges.setEnabled(true);
        saveChanges.setAlpha(1.0f);
    }

    private boolean hasAnyTextChanged() {
        for (Boolean hasChanged : textChangedMap.values()) {
            if (hasChanged) {
                Timber.tag("EDIT PROFILE: CHANGES DETECTED");
                return true;
            }
        }
        return false;
    }

    private void saveAgentInfo() {
        //Grab the values from the TextView
        String updatedFirstName = textFieldFirstName.getText().toString();
        String updatedMiddleInitial = textFieldMiddleInitial.getText().toString();
        String updatedLastName = textFieldLastName.getText().toString();
        String updatedBusPhone = textFieldBusPhone.getText().toString();
        String updatedEmail = textFieldEmail.getText().toString();
        String updatedPosition = textFieldPosition.getText().toString();

        AgentUpdateDetailRequestDTO updatedAgentInfo = new AgentUpdateDetailRequestDTO(
                updatedFirstName,
                updatedMiddleInitial,
                updatedLastName,
                updatedBusPhone,
                updatedEmail,
                updatedPosition
        );

        //Grabbing Token
        String token = SharedPrefUtil.getToken(requireContext());
        if (token == null) {
            Toast.makeText(requireContext(), "Authentication token missing", Toast.LENGTH_SHORT).show();
            return;
        }


        AgentAPIService agentAPIService = ApiClient.getClient().create(AgentAPIService.class);
        Call<GenericApiResponse<AgentDetailsResponseDTO>> call = agentAPIService.updateAgentInfo("Bearer " + token, id, updatedAgentInfo);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Response<GenericApiResponse<AgentDetailsResponseDTO>> response) {
                if (response.isSuccessful()) {
                    //Toasting to notify user that their changes were saved successfully
                    Toast.makeText(getContext(), "Awesome! Profile Saved Successfully", Toast.LENGTH_LONG).show();
                    inflateProfileFragment();
                } else {
                    if (response.body() != null && response.body().getErrors() != null) {
                        // Error Handling
                        List<ErrorInfo> errors = response.body().getErrors();
                        String errorMessage = errors.isEmpty() ? "Failed to update profile" : errors.get(0).getDetail();
                        //Tiiiiiiiimmmmmmbbbbbeeeerrrr logging
                        Timber.e("Profile update failed: %s", errorMessage);
                        //Toasting to alert user that there were errors;
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Throwable t) {
                Timber.e("Network Failure");
                Toast.makeText(getContext(), "Network failure, try again later", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void inflateProfileFragment() {
        Fragment profileFragment = new ProfileFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profileFragment);
        transaction.commit();
    }
}//class