package com.example.travelexpertmobileapplication.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelexpertmobileapplication.R;
import com.example.travelexpertmobileapplication.model.Package;
import com.example.travelexpertmobileapplication.model.ProductSupplier;
import com.example.travelexpertmobileapplication.network.ApiClient;
import com.example.travelexpertmobileapplication.network.api.PackageAPIService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class PackageDetailsFragment extends Fragment {

    private EditText etPackageName, etPackageDesc, etPackagePrice, etStartDate, etEndDate, etPkgagencycommission;
    private Button btnEdit, btnDelete, btnSave, btnCancel;
    private LinearLayout llEditButtons;
    private Spinner spinnerProductSuppliers;
    private ListView listViewProductSuppliers;
    private boolean isEditMode = false;
    private Package packageDetails;

    private ImageButton btnBack;
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();
    private List<ProductSupplier> productSuppliers = new ArrayList<>(); // List of all ProductSuppliers
    private List<ProductSupplier> selectedProductSuppliers = new ArrayList<>(); // List of selected ProductSuppliers
    private ArrayAdapter<ProductSupplier> listAdapter;
    private boolean isListEditable = false;
    private ArrayAdapter<ProductSupplier> spinnerAdapter;
    private boolean isSpinnerInitialized = false;


    public PackageDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_details, container, false);

        // Initialize views
        etPackageName = view.findViewById(R.id.etPackageName);
        etPackageDesc = view.findViewById(R.id.etPackageDesc);
        etPackagePrice = view.findViewById(R.id.etPackagePrice);
        etPkgagencycommission = view.findViewById(R.id.etPkgagencycommission);
        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        btnSave = view.findViewById(R.id.btnSave);
        spinnerProductSuppliers = view.findViewById(R.id.spinnerProductSuppliers);
        listViewProductSuppliers = view.findViewById(R.id.listViewProductSuppliers);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        llEditButtons = view.findViewById(R.id.llEditButtons);

        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            // Handle back navigation
            requireActivity().onBackPressed();
        });

        // Set up DatePicker for Start Date
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate, startDateCalendar));

        // Set up DatePicker for End Date
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate, endDateCalendar));

        // Fetch and populate ProductSuppliers
        fetchProductSuppliers();

        // Set up Spinner
        spinnerAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                productSuppliers
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductSuppliers.setAdapter(spinnerAdapter);

        // Set up ListView adapter
        listAdapter = new ArrayAdapter<ProductSupplier>(
                getContext(),
                R.layout.item_product_supplier, // Custom layout for ListView items
                R.id.tvProductSupplier, // TextView to display ProductSupplier name
                selectedProductSuppliers
        ) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // Get the current ProductSupplier
                ProductSupplier productSupplier = getItem(position);

                // Set the ProductSupplier name
                TextView tvProductSupplier = view.findViewById(R.id.tvProductSupplier);
                tvProductSupplier.setText(productSupplier.toString());

                // Handle delete button click
                ImageView ivDelete = view.findViewById(R.id.ivDelete);
                if (isListEditable) {
                    ivDelete.setVisibility(View.VISIBLE); // Hide delete icon in edit/view mode
                } else {
                    ivDelete.setVisibility(View.GONE); // Show delete icon in add mode
                }
                ivDelete.setOnClickListener(v -> {
                    // Remove the item from the list
                    selectedProductSuppliers.remove(position);
                    listAdapter.notifyDataSetChanged(); // Refresh the ListView
                });

                return view;

            }
        };

        listViewProductSuppliers.setAdapter(listAdapter);


        // Handle Spinner item selection
        spinnerProductSuppliers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitialized) {
                    ProductSupplier selectedProductSupplier = (ProductSupplier) parent.getItemAtPosition(position);
                    if (!selectedProductSuppliers.contains(selectedProductSupplier)) {
                        selectedProductSuppliers.add(selectedProductSupplier);
                        Log.e("TAG", "onItemSelected: " + selectedProductSuppliers.toString());
                        listAdapter.notifyDataSetChanged(); // Refresh the ListView
                    }
                } else {
                    // Mark the Spinner as initialized after the first automatic selection
                    isSpinnerInitialized = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Check if we are in edit/view mode or add mode
        Bundle bundle = getArguments();
        if (bundle != null) {
            isEditMode = true;
            btnEdit.setOnClickListener(v -> enterEditMode());
            btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
            btnCancel.setOnClickListener(v -> exitEditMode());
            exitEditMode();
            packageDetails = (Package) bundle.getSerializable("PackageDetails");

            Log.d("Package Details in fragment", String.valueOf((int) bundle.getInt("PackageID")));
            etPackageName.setText(packageDetails.getPkgname());
            etPackageDesc.setText(packageDetails.getPkgdesc());
            etPackagePrice.setText(packageDetails.getPkgbaseprice().toString());
            etPkgagencycommission.setText(packageDetails.getPkgagencycommission().toString());
            // Format and set the start and end dates
            String startDate = formatDate(packageDetails.getPkgstartdate());
            String endDate = formatDate(packageDetails.getPkgenddate());
            etStartDate.setText(startDate);
            etEndDate.setText(endDate);

            // Set the Calendar objects for start and end dates
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                startDateCalendar.setTime(sdf.parse(startDate));
                endDateCalendar.setTime(sdf.parse(endDate));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Populate selected ProductSuppliers
            if (packageDetails.getProductSupplier() != null) {
                selectedProductSuppliers.addAll(packageDetails.getProductSupplier());
                listAdapter.notifyDataSetChanged(); // Refresh the ListView
            }
        }
        else{
            isEditMode = false;
            isListEditable = true;
            setFieldsEnabled(true);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            llEditButtons.setVisibility(View.VISIBLE);
            spinnerProductSuppliers.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(v -> requireActivity().onBackPressed());
            listAdapter.notifyDataSetChanged();
        }

        // Save button click listener
        btnSave.setOnClickListener(v -> {
            if (isEditMode) {
                updatePackage();
            } else {
                addPackage();
            }
        });

        return view;
    }

    private void enterEditMode() {
        // Enable fields
        setFieldsEnabled(true);
        isListEditable = true;
        listAdapter.notifyDataSetChanged();
        // Show Spinner
        spinnerProductSuppliers.setVisibility(View.VISIBLE);
        // Hide Edit/Delete buttons and show Save/Cancel/Delete buttons
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        llEditButtons.setVisibility(View.VISIBLE);
    }

    private void exitEditMode() {
        // Disable fields
        setFieldsEnabled(false);
        isListEditable = false;
        listAdapter.notifyDataSetChanged();
        listViewProductSuppliers.setClickable(false);
        // Hide Spinner
        spinnerProductSuppliers.setVisibility(View.GONE);

        // Show Edit/Delete buttons and hide Save/Cancel/Delete buttons
        btnEdit.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        llEditButtons.setVisibility(View.GONE);
    }

    private void setFieldsEnabled(boolean enabled) {
        etPackageName.setEnabled(enabled);
        etPackageDesc.setEnabled(enabled);
        etPackagePrice.setEnabled(enabled);
        etPkgagencycommission.setEnabled(enabled);
        etStartDate.setEnabled(enabled);
        etEndDate.setEnabled(enabled);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Package")
                .setMessage("Are you sure you want to delete this package?")
                .setPositiveButton("Yes", (dialog, which) -> deletePackage())
                .setNegativeButton("No", null)
                .show();
    }

    // Helper method to format dates
    private String formatDate(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA); // Replace with the format of your input date
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return date; // Return the original date if formatting fails
        }
    }

    private void showDatePickerDialog(EditText editText, Calendar calendar) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText, calendar);
            }
        };

        new DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void updateLabel(EditText editText, Calendar calendar) {
        String dateFormat = "yyyy-MM-dd"; // Format for the date
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    // Fetch ProductSuppliers from the service
    private void fetchProductSuppliers() {
        PackageAPIService apiService = ApiClient.getClient().create(PackageAPIService.class);

        apiService.getAllProductSuppliers().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray productSupplierArray = response.body();  // Response is an array of packages

                    // Parse the JSON array into a list of Package objects
                    List<ProductSupplier> productSuppliersList = new Gson().fromJson(productSupplierArray, new TypeToken<List<ProductSupplier>>() {
                    }.getType());

                    // Clear existing list and add new package data
                    productSuppliers.clear();
                    productSuppliers.addAll(productSuppliersList);

                    spinnerAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("API Error", "Failed to fetch packages: " + t.getMessage());
            }
        });
    }

    private void addPackage() {
        String pkgname = etPackageName.getText().toString();
        String pkgdesc = etPackageDesc.getText().toString();
        String pkgbaseprice = etPackagePrice.getText().toString();
        String pkgagencycommission = etPkgagencycommission.getText().toString();
        String pkgstartdate = etStartDate.getText().toString();
        String pkgenddate = etEndDate.getText().toString();

        // Validate input
        if (pkgname.isEmpty() || pkgdesc.isEmpty() || pkgbaseprice.isEmpty() || pkgstartdate.isEmpty() || pkgenddate.isEmpty() || pkgagencycommission.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate dates
        if (!validateDates(pkgstartdate, pkgenddate)) {
            Toast.makeText(getContext(), "Invalid dates. Start date must be before end date and both must be in the future.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extract productSupplierIds from selectedProductSuppliers
        List<Integer> productsupplierids = new ArrayList<>();
        for (ProductSupplier ps : selectedProductSuppliers) {
            productsupplierids.add(ps.getProductSupplierId()); // Assuming ProductSupplier has a getProductSupplierId() method
        }

        String isopkgstartdate = convertToIsoDate(pkgstartdate);
        String isopkgenddate = convertToIsoDate(pkgenddate);

        // Create an updated Package object
        Package newPackage = new Package(
                0,
                pkgname,
                pkgdesc,
                new BigDecimal(pkgbaseprice),
                new BigDecimal(pkgagencycommission),
                isopkgstartdate,
                isopkgenddate,
                productsupplierids
        );

        // Convert the Package object to a JsonObject
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(newPackage).getAsJsonObject();

        // Remove the "productsSuppliers" property
        jsonObject.remove("productsSuppliers");
        jsonObject.remove("packageid");

        // Call the API to add the package
        PackageAPIService apiService = ApiClient.getClient().create(PackageAPIService.class);
        Call<Package> call = apiService.createPackage(jsonObject);

        call.enqueue(new Callback<Package>() {
            @Override
            public void onResponse(Call<Package> call, Response<Package> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Package created successfully
                    Package createdPackage = response.body();
                    Log.d("API Success", "Package created: " + createdPackage.toString());
                    Toast.makeText(getContext(), "Package created successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate back to the PackageListFragment
                    navigateBackToPackageList();
                } else {
                    // Handle API error
                    Log.e("API Error", "Failed to create package: " + response.code());
                    Toast.makeText(getContext(), "Failed to create package. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Package> call, Throwable t) {
                // Handle network failure
                Log.e("API Error", "Failed to create package: " + t.getMessage());
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateBackToPackageList() {
        // Use FragmentManager to pop the current fragment from the back stack
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    private boolean validateDates(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Check if start date is after today
        if (start.before(today)) {
            return false;
        }

        // Check if end date is after start date
        return end.after(start);
    }

    private String convertToIsoDate(String date) {
        LocalDate localDate = null; // Parse the input date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDate = LocalDate.parse(date);
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC")); // Add time and time zone
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME; // ISO 8601 formatter
            return zonedDateTime.format(formatter); // Format to ISO 8601
        }
        return "";
    }

    private void deletePackage(){
        // Call the API to update the package
        PackageAPIService apiService = ApiClient.getClient().create(PackageAPIService.class);
        Call<Void> call = apiService.deletePackage(packageDetails.getPackageid());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Package deleted successfully
                    Log.d("API Success", "Package deleted successfully");
                    Toast.makeText(getContext(), "Package deleted successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate back to the PackageListFragment or refresh the UI
                    navigateBackToPackageList();
                } else {
                    // Handle API error
                    Log.e("API Error", "Failed to delete package: " + response.code());
                    Toast.makeText(getContext(), "Failed to delete package. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network failure
                Log.e("API Error", "Failed to delete package: " + t.getMessage());
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updatePackage() {
        String pkgname = etPackageName.getText().toString();
        String pkgdesc = etPackageDesc.getText().toString();
        String pkgbaseprice = etPackagePrice.getText().toString();
        String pkgagencycommission = etPkgagencycommission.getText().toString();
        String pkgstartdate = etStartDate.getText().toString();
        String pkgenddate = etEndDate.getText().toString();

        // Validate input
        if (pkgname.isEmpty() || pkgdesc.isEmpty() || pkgbaseprice.isEmpty() || pkgstartdate.isEmpty() || pkgenddate.isEmpty() || pkgagencycommission.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate dates
        if (!validateDates(pkgstartdate, pkgenddate)) {
            Toast.makeText(getContext(), "Invalid dates. Start date must be before end date and both must be in the future.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extract productSupplierIds from selectedProductSuppliers
        List<Integer> productsupplierids = new ArrayList<>();
        for (ProductSupplier ps : selectedProductSuppliers) {
            productsupplierids.add(ps.getProductSupplierId()); // Assuming ProductSupplier has a getProductSupplierId() method
        }

        String isopkgstartdate = convertToIsoDate(pkgstartdate);
        String isopkgenddate = convertToIsoDate(pkgenddate);

        // Create an updated Package object
        Package updatedPackage = new Package(
                packageDetails.getPackageid(), // Use the existing package ID
                pkgname,
                pkgdesc,
                new BigDecimal(pkgbaseprice),
                new BigDecimal(pkgagencycommission),
                isopkgstartdate,
                isopkgenddate,
                productsupplierids
        );

        // Convert the Package object to a JsonObject
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(updatedPackage).getAsJsonObject();

        // Remove the "productsSuppliers" property if it is null
        if (updatedPackage.getProductSupplier() == null) {
            jsonObject.remove("productsSuppliers");
        }

        // Log the updated package for debugging
        Log.d("Updated Package", "Sending package: " + jsonObject.toString());

        // Call the API to update the package
        PackageAPIService apiService = ApiClient.getClient().create(PackageAPIService.class);
        Call<Package> call = apiService.updatePackage(packageDetails.getPackageid(), jsonObject);

        call.enqueue(new Callback<Package>() {
            @Override
            public void onResponse(Call<Package> call, Response<Package> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Package updated successfully
                    Package updatedPackageResponse = response.body();
                    Timber.i("Package updated: %s", updatedPackageResponse.toString());
                    Toast.makeText(getContext(), "Package updated successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate back to the PackageListFragment
                    navigateBackToPackageList();
                } else {
                    // Handle API error
                    Timber.i("Failed to update package: %s", response.code());
                    Toast.makeText(getContext(), "Failed to update package. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Package> call, Throwable t) {
                // Handle network failure
                Timber.e(t, "Failed to update package");
                Toast.makeText(getContext(), "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();

                // Log the full stack trace for debugging
                t.printStackTrace();
            }
        });
    }
}