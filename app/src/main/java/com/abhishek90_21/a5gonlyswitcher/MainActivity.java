package com.abhishek90_21.a5gonlyswitcher;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

/**
 * PhoneInfoOpenerActivity
 *
 * A small utility Activity that attempts to directly open the system "Phone information 1"
 * (Radio/Phone info for the first SIM slot) screen. It tries several known/likely Settings
 * activity component names and common extras that some OEMs accept to indicate which SIM slot
 * or subscription to display. If none of the component guesses succeed it falls back to
 * opening the dialer pre-filled with the testing code "*#*#4636#*#*" so the user can run
 * the Testing/Phone info menu manually.
 *
 * IMPORTANT:
 * - This Activity only opens the system UI. It does NOT change radio/network settings programmatically.
 *   Modem/network configuration (e.g., setting NR-only) requires system/carrier privileges or root.
 * - Component names differ by OEM and Android build. If this doesn't work on your device,
 *   tell me the OEM/model and I can add vendor-specific component names known to work there.
 */
public class MainActivity extends AppCompatActivity {

    // SIM slot we want to open. Many devices label "Phone information 1" for slot index 0.
    private static final int SLOT_TO_OPEN = 0;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();

// Create a new ad view.
        ViewGroup adContainerView = findViewById(R.id.ad_view_container);
        AdView adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        // Replace ad container with new ad view.

// Request an anchored adaptive banner with a width of 360.
        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360));

// Replace ad container with new ad view.
        adContainerView.removeAllViews();
        adContainerView.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);




        Button btnOpen5G = findViewById(R.id.btn_open_5g);

        btnOpen5G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean opened = openPhoneInfoForSlot(SLOT_TO_OPEN);

                if (!opened) {
                    // fallback: open dialer with testing code
                    openTestingDialer();
                }

                // Close this helper activity right away (we only act as a launcher)
                finish();
            }
        });

    }

    /**
     * Attempts to open various known/likely Phone/Radio info Settings components.
     * Returns true if one was successfully started.
     */
    private boolean openPhoneInfoForSlot(int slotIndex) {
        // Candidate component class names (OEM/ROM dependent). This list is intentionally broad.
        String[][] components = new String[][]{
                // AOSP / common guesses
                {"com.android.settings", "com.android.settings.RadioInfo"},
                {"com.android.settings", "com.android.settings.TestingSettings"},
                {"com.android.settings", "com.android.settings.Settings$TestingSettingsActivity"},
                {"com.android.settings", "com.android.settings.Settings$RadioInfo"},
                {"com.android.settings", "com.android.settings.Settings$PhoneInfoActivity"},
                // Phone package guesses
                {"com.android.phone", "com.android.phone.RadioInfo"},
                {"com.android.phone", "com.android.phone.settings.RadioInfo"},
                // Some vendor/package guesses (may or may not exist)
                {"com.samsung.android.settings", "com.samsung.android.settings.RadioInfo"},
                {"com.samsung.android.settings", "com.samsung.android.settings.TestingSettings"},
                {"com.mediatek.settings", "com.mediatek.settings.RadioInfo"},
                {"com.huawei.android", "com.android.settings.RadioInfo"}
        };

        // Common extras keys that some OEMs accept for slot/subscription selection.
        String[] slotKeys = new String[] {
                SubscriptionManager.EXTRA_SLOT_INDEX, // standardized constant: "slot"
                "slot", "simslot", "simSlot", "slot_id", "slotIndex",
                "phone", "phone_id", "subscription", "subId", "sub_id"
        };

        for (String[] comp : components) {
            String pkg = comp[0];
            String cls = comp[1];

            // Try without extras first
            Intent plain = new Intent(Intent.ACTION_MAIN);
            plain.setComponent(new ComponentName(pkg, cls));
            plain.addCategory(Intent.CATEGORY_LAUNCHER);
            if (tryStartIntent(plain)) return true;

            // Try with several extras that might indicate SIM slot/subId
            for (String key : slotKeys) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setComponent(new ComponentName(pkg, cls));
                i.addCategory(Intent.CATEGORY_LAUNCHER);

                // Put both integer and string forms (some OEMs expect different types)
                try { i.putExtra(key, slotIndex); } catch (Throwable ignored) {}
                try { i.putExtra(key, String.valueOf(slotIndex)); } catch (Throwable ignored) {}

                // Some devices accept "subId" or "subscription" with subscription id rather than slot.
                // We cannot reliably get a subscription id here without querying Telephony APIs,
                // but many vendor implementations accept 0/1 as a subId as well.
                try { i.putExtra("subId", slotIndex); i.putExtra("subscription", slotIndex); } catch (Throwable ignored) {}

                if (tryStartIntent(i)) return true;
            }
        }

        // No component succeeded
        return false;
    }

    /**
     * Tries to start the provided Intent if resolvable. Returns true on success.
     */
    private boolean tryStartIntent(Intent intent) {
        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Use FLAG_ACTIVITY_NEW_TASK when starting from contexts that might need it.
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        } catch (ActivityNotFoundException ignored) {
            // not available on this device/ROM
        } catch (Exception e) {
            // Toast.makeText(this, "Failed to open Phone Info: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * Fallback: open the phone dialer pre-filled with the testing code *#*#4636#*#*
     * so the user can run the Testing / Phone info menu manually.
     */
    private void openTestingDialer() {
        try {
            String code = "*#*#4636#*#*";
            Uri uri = Uri.parse("tel:" + Uri.encode(code));
            Intent dial = new Intent(Intent.ACTION_DIAL, uri);
            dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (dial.resolveActivity(getPackageManager()) != null) {
                startActivity(dial);
            } else {
                Toast.makeText(this, "No dialer found to open testing code.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open testing dialer: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}