package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.adapters.PackageAdapter;
import com.example.travelexpertmobileapplication.adapters.PastTripsAdapter;
import com.example.travelexpertmobileapplication.model.Package;
import com.example.travelexpertmobileapplication.model.PastTripsModel;
import com.example.travelexpertmobileapplication.model.SupplierContact;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.BookingDetailAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class ModernPastTripsFragment extends Fragment {
    private RecyclerView recyclerView;

    private PastTripsAdapter adapter;
    private List<PastTripsModel> pastTripsModelList = new ArrayList<>();
    private ImageView btnBack;


    public ModernPastTripsFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PastTripsAdapter(pastTripsModelList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modern_past_trips, container, false);
        recyclerView = view.findViewById(R.id.pastTrips_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PastTripsAdapter(pastTripsModelList, getContext());
        btnBack = view.findViewById(R.id.btnBack);
        // Register the callback
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back press here
                // For example, navigate back or perform any action
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Set up your button click listener
        btnBack.setOnClickListener(v -> {
            // Optionally, you can also call the back press handler directly
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        });

        fetchPastTrips();



        return view;
    }

    private void fetchPastTrips() {
        BookingDetailAPIService apiService = ApiClient.getClient().create(BookingDetailAPIService.class);
        Call<List<PastTripsModel>> call = apiService.getPastTrips();

        call.enqueue(new Callback<List<PastTripsModel>>() {
            @Override
            public void onResponse(Call<List<PastTripsModel>> call, Response<List<PastTripsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pastTripsModelList.clear();
                    //tripDataL.clear();


                    String agentIdStr = SharedPrefUtil.getAgentId(requireContext());
                    if (agentIdStr == null || agentIdStr.trim().isEmpty()) {
                        Toast.makeText(getContext(), "Agent ID not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (PastTripsModel trip : response.body()) {
                        if (trip.getAgentId() == Integer.parseInt(agentIdStr)) {
                            pastTripsModelList.add(trip);
                            //tripDataL.add(trip.getFirstName() + " " + trip.getLastName() + "\n" + trip.getDescription());
                        }
                    }
//                    if (pastTripsModelList.isEmpty()) {
//                        tripDataL.add("No Trips found");
//                    }


                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to get data!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PastTripsModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}