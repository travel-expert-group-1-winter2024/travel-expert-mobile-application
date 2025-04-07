package com.example.travelexpertmobileapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Customer;
import com.example.travelexpertmobileapplication.model.PastTripsModel;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.BookingDetailAPIService;
import com.example.travelexpertmobileapplication.network.api.PackageAPIService;
import com.example.travelexpertmobileapplication.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastTripsFragment extends Fragment {

    ListView lvPastTrips;
    private ArrayAdapter<String> adapter;
    private List<PastTripsModel> tripDataList = new ArrayList<>();
    private List<String> tripDataL = new ArrayList<>();


    public PastTripsFragment() {
        // Required empty public constructor
    }

    public static PastTripsFragment newInstance(String param1, String param2) {
        PastTripsFragment fragment = new PastTripsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_trips, container, false);
        lvPastTrips = view.findViewById(R.id.lvPastTrips);

        // ✅ Initialize adapter before fetching data
//        tripDataL = new ArrayList<>();
//        tripDataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, tripDataL);
        lvPastTrips.setAdapter(adapter);

        fetchPastTrips(); // Fetch API data

        lvPastTrips.setOnItemClickListener((parent, view1, position, id) -> setupItemClickListener(position));

        return view;
    }

    private void fetchPastTrips() {
        BookingDetailAPIService apiService = ApiClient.getClient().create(BookingDetailAPIService.class);
        Call<List<PastTripsModel>> call = apiService.getPastTrips();

        call.enqueue(new Callback<List<PastTripsModel>>() {
            @Override
            public void onResponse(Call<List<PastTripsModel>> call, Response<List<PastTripsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tripDataList.clear();
                    tripDataL.clear();

                    String agentIdStr = SharedPrefUtil.getAgentId(requireContext());
                    if (agentIdStr == null || agentIdStr.trim().isEmpty()) {
                        Toast.makeText(getContext(), "Agent ID not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (PastTripsModel trip : response.body()) {
                        if (trip.getAgentId() == Integer.parseInt(agentIdStr)) {
                            tripDataList.add(trip);
                            tripDataL.add(trip.getFirstName() + " " + trip.getLastName() + "\n" + trip.getDescription());
                        }
                    }
                    if (tripDataList.isEmpty()) {
                        tripDataL.add("No Trips found");
                    }

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


    private void setupItemClickListener(int position) {
        if (position < 0 || position >= tripDataList.size()) return;

        PastTripsModel selectedTrip = tripDataList.get(position);
            if (tripDataList != null && !tripDataList.isEmpty() && position < tripDataList.size()) {
                PastTripsModel selectedBooking = tripDataList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("bookingDetailId", selectedBooking.getBookingDetailId());
                bundle.putInt("itineraryNo", selectedBooking.getItineraryNo());
                bundle.putString("tripStart", selectedBooking.getTripStart() != null ? selectedBooking.getTripStart() : "");
                bundle.putString("tripEnd", selectedBooking.getTripEnd() != null ? selectedBooking.getTripEnd() : "");
                bundle.putString("description", selectedBooking.getDescription() != null ? selectedBooking.getDescription() : "");
                bundle.putString("destination", selectedBooking.getDestination() != null ? selectedBooking.getDestination() : "");
                bundle.putDouble("basePrice", selectedBooking.getBasePrice() != 0 ? selectedBooking.getBasePrice() : 0.0);
                bundle.putDouble("agencyCommission", selectedBooking.getAgencyCommission() != 0 ? selectedBooking.getAgencyCommission() : 0.0);
                bundle.putInt("agentid", selectedBooking.getAgentId());
                bundle.putInt("customerid", selectedBooking.getCustomerId());
                bundle.putString("firstname", selectedBooking.getFirstName() != null ? selectedBooking.getFirstName() : "");
                bundle.putString("lastname", selectedBooking.getLastName() != null ? selectedBooking.getLastName() : "");
                bundle.putString("region", selectedBooking.getRegion() != null ? selectedBooking.getRegion() : "");
                bundle.putString("className", selectedBooking.getClassName() != null ? selectedBooking.getClassName() : "");
                bundle.putString("fee", selectedBooking.getFee() != null ? selectedBooking.getFee() : "");
                bundle.putString("product", selectedBooking.getProduct() != null ? selectedBooking.getProduct() : "");
                bundle.putString("supplier", selectedBooking.getSupplier() != null ? selectedBooking.getSupplier() : "");

                PastTripDetailsFragment fragment = new PastTripDetailsFragment();
                fragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment); // Replace with the container ID in your activity
                transaction.addToBackStack(null);
                transaction.commit();
                // Navigate to the next fragment/activity
            } else {
                Log.e("PastTripsFragment", "Invalid list access: position " + position + " in list of size " + (tripDataList != null ? tripDataList.size() : 0));
                Toast.makeText(getContext(), "Trip data not available", Toast.LENGTH_SHORT).show();
            }
    }







}