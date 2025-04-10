package com.example.travelexpertmobileapplication.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

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
    private TextView agentGreeting;
    private ImageView agentImage;
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
        LinearLayout linearLayout_PastTrips = view.findViewById(R.id.linearyLayout_PastTrips);

        agentGreeting = view.findViewById(R.id.agentGreeting);
        agentImage = view.findViewById(R.id.ivAgentProfilePic);

        /**
         * LinearLayout acting as Packages "button"
         * OnClick Event Listener to load the Packages Fragment
         */

        linearLayout_PastTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding animation to the button to improve user feedback.
                Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                blink.setAnimationListener(new Animation.AnimationListener() {

                    @Override //Needed for setAnimationListener, not needed to implement.
                    public void onAnimationStart(Animation animation) {
                        return;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //Loading the fragment
                        Fragment pastTripsFragment = new PastTripsFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, pastTripsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        return;
                    }
                });

                linearLayout_PastTrips.startAnimation(blink);


            }
        });

        linearLayout_Packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Adding animation to the button to improve user feedback.
                Animation blink = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
                blink.setAnimationListener(new Animation.AnimationListener() {

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
                blink.setAnimationListener(new Animation.AnimationListener() {

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
                blink.setAnimationListener(new Animation.AnimationListener() {

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
                blink.setAnimationListener(new Animation.AnimationListener() {

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handling JWT errors.
        String token = SharedPrefUtil.getToken(requireContext());
        if (token == null) {
            Toast.makeText(requireContext(), "Authentication token missing", Toast.LENGTH_SHORT).show();
        } else {
            Timber.tag("Token Debug").d("Token being sent: Bearer %s", token);
        }

        AgentAPIService agentAPIService = ApiClient.getClient().create(AgentAPIService.class);
        Call<GenericApiResponse<AgentDetailsResponseDTO>> call = agentAPIService.getMyAgentInfo("Bearer " + token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GenericApiResponse<AgentDetailsResponseDTO>> call, Response<GenericApiResponse<AgentDetailsResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AgentDetailsResponseDTO agentInfo = response.body().getData();
                    Timber.tag("onResponse Call").d(String.valueOf(response));

                    // get agent image
                    Call<ResponseBody> callImage = agentAPIService.getAgentPhoto("Bearer " + token, agentInfo.getId().intValue());
                    callImage.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    byte[] imageBytes = response.body().bytes();
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    agentImage.setImageBitmap(bitmap);
                                    agentGreeting.setText(String.format("Welcome back %s", agentInfo.getAgtFirstName()));
                                } catch (Exception e) {
                                    Timber.e(e, "Failed to convert image");
                                }
                            } else {
                                Timber.tag("FAILED TO FETCH").d("Failed to fetch Agent Image! %s", response);
                                Toast.makeText(requireContext(), "Failed to fetch Agent Image!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Timber.tag("onFailure:").e("Api call failed: %s", t.getMessage());
                            Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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
}//class