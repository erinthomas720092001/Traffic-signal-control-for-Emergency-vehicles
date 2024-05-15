package com.project.boatnavigation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.boatnavigation.MapsActivity;
import com.project.boatnavigation.databinding.ActivityAddEmergencyContactBinding;
import com.project.boatnavigation.manager.preference.AppPreference;

public class AddEmergencyNo  extends AppCompatActivity {
    private ActivityAddEmergencyContactBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddEmergencyContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.editTextPhone.setText(AppPreference.getMobileNumber(AddEmergencyNo.this));
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreference.saveMobileNumber(binding.editTextPhone.getText().toString(),getApplicationContext());
                Toast.makeText(AddEmergencyNo.this, "Emergency number saved",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
