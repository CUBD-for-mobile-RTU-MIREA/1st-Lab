package ru.realityfamily.partyapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.realityfamily.partyapp.DI.ServiceLocator;
import ru.realityfamily.partyapp.Domain.Model.Party;
import ru.realityfamily.partyapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        ServiceLocator.getInstance().initBase(getApplication());

        Uri income = getIntent().getData();
        if (income != null) {
            if (income.toString().contains("rf.party_app")) {
                String[] parts = income.toString().split("/");
                String id = parts[parts.length - 1];
                ServiceLocator.getInstance().getRepository().findVerifiedParty(id, this).observe(this, (Party party) -> {
                    if (party != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Party", ServiceLocator.getInstance().getGson().toJson(party));

                        Navigation.findNavController(mBinding.navHostFragment).navigate(
                                R.id.action_partyList_to_partyFragment,
                                bundle
                        );
                    }
                });
            } else if (income.toString().contains("code")) {
                String[] parts = income.toString().split("\\?");
            }
        }
    }
}