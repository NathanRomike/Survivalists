package com.eyecuelab.survivalists.ui;

import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eyecuelab.survivalists.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewCampaignActivity extends AppCompatActivity implements View.OnClickListener {
    private int mDifficultyLevel;
    private int mCampaignLength;
    private int mPartySize;
    private boolean mConfirmingSettings = true;
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> lengths = new ArrayList<>();
    private ArrayList<String> invitedPlayers = new ArrayList<>();

    private ListView mInvitePlayersListView;
    private Context mContext;

    @Bind(R.id.difficultySeekBar) SeekBar difficultySeekBar;
    @Bind(R.id.campaignLengthSeekBar) SeekBar lengthSeekBar;
    @Bind(R.id.partySizeSeekBar) SeekBar partySeekBar;
    @Bind(R.id.difficultyDescription) TextView difficultyTextView;
    @Bind(R.id.lengthText) TextView lengthTextView;
    @Bind(R.id.invitePlayersListView) ListView invitePlayerListView;
    @Bind(R.id.infoListView) ListView infoListView;
    @Bind(R.id.confirmationButton) Button confirmationButton;
    @Bind(R.id.settingsField) PercentRelativeLayout settingsLayout;
    @Bind(R.id.settingsConfirmedSection) PercentRelativeLayout settingConfirmationLayout;
    @Bind(R.id.infoSection) PercentRelativeLayout generalInfoLayout;
    @Bind(R.id.teamBuildingSection) PercentRelativeLayout playerInvitationLayout;
    @Bind(R.id.difficultyConfirmedText) TextView difficultyConfirmedTextView;
    @Bind(R.id.lengthConfirmedText) TextView lengthConfirmedTextView;
    @Bind(R.id.partySizeText) TextView partyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification and navigation bars
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_new_campaign);

        ButterKnife.bind(this);
        confirmationButton.setOnClickListener(this);

        descriptions.add("Walk in the park");
        descriptions.add("Walk the line");
        descriptions.add("Walk the talk");
        lengths.add("15");
        lengths.add("30");
        lengths.add("45");

        initiateSeekBars();

        ArrayAdapter<String> infoAdapter = new ArrayAdapter<>(NewCampaignActivity.this, R.layout.info_list_item, getResources().getStringArray(R.array.difficultyDescriptions));
        infoListView.setAdapter(infoAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmationButton:
                if (mConfirmingSettings == true) {
                    loadAvailablePlayers();
                } else if (mPartySize == invitedPlayers.size()){
                    sendInvitations();
                    Toast.makeText(NewCampaignActivity.this, "Invitations sent", Toast.LENGTH_LONG).show();
                } else {
                    int remainingInvites = mPartySize - invitedPlayers.size();
                    Toast.makeText(NewCampaignActivity.this, "You need to select " + remainingInvites + " more players.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void loadAvailablePlayers() {
        mConfirmingSettings = false;
        settingsLayout.setVisibility(View.GONE);
        settingConfirmationLayout.setVisibility(View.VISIBLE);
        generalInfoLayout.setVisibility(View.GONE);
        playerInvitationLayout.setVisibility(View.VISIBLE);

        int remainingInvites = mPartySize - invitedPlayers.size();
        confirmationButton.setText(remainingInvites + " invitations remaining...");

        difficultyConfirmedTextView.setText("Difficulty: " + descriptions.get(mDifficultyLevel));
        lengthConfirmedTextView.setText("Length: " + lengths.get(mCampaignLength) + " Days");

        String[] players = new String[] {"This Nose Knows", "Hello", "Testing", "This Nose Knows", "Hello", "Testing", "This Nose Knows", "Hello", "Testing",};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.player_list_item, R.id.playerNameTextView, players);

        invitePlayerListView.setAdapter(adapter);
        invitePlayerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                invitedPlayers.add(String.valueOf(position));
            }
        });
    }

    public void sendInvitations() {
        confirmationButton.setText("Awaiting Responses...");
        confirmationButton.setTextColor(getColor(android.R.color.darker_gray));
    }

    public void initiateSeekBars() {
        difficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressTotal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressTotal = progress;
                difficultyTextView.setText(descriptions.get(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDifficultyLevel = progressTotal;
            }
        });

        lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressTotal = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lengthTextView.setText(lengths.get(progress) + " Days");
                progressTotal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCampaignLength = progressTotal;
            }
        });

        partySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressTotal = 2;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int currentCount = progress + 2;
                partyTextView.setText(currentCount + " Players");
                progressTotal = progress + 2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPartySize = progressTotal;
                Toast.makeText(NewCampaignActivity.this, "" + mPartySize, Toast.LENGTH_LONG).show();
            }
        });
    }
}