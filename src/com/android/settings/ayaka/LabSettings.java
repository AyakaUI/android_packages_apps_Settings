/*
 * Copyright (C) 2025 MicaOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.ayaka;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class LabSettings extends SettingsPreferenceFragment {

    private static final String KEYBOX_DATA_KEY = "keybox_data_setting";
    private static final String PIF_DATA_KEY = "pif_data_setting";
    private static final String VIEW_PIF_PROPS_KEY = "view_pif_props";

    private ActivityResultLauncher<Intent> mKeyboxFilePickerLauncher;
    private ActivityResultLauncher<Intent> mPifFilePickerLauncher;
    private KeyboxDataPreference mKeyboxDataPreference;
    private PifDataPreference mPifDataPreference;
    private Preference mViewPifPropertiesPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lab_settings);

        mKeyboxFilePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Preference pref = findPreference(KEYBOX_DATA_KEY);
                    if (pref instanceof KeyboxDataPreference) {
                        ((KeyboxDataPreference) pref).handleFileSelected(uri);
                    }
                }
            }
        );

        mPifFilePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Preference pref = findPreference(PIF_DATA_KEY);
                    if (pref instanceof PifDataPreference) {
                        ((PifDataPreference) pref).handleFileSelected(uri);
                    }
                }
            }
        );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mKeyboxDataPreference = findPreference(KEYBOX_DATA_KEY);
        mPifDataPreference = findPreference(PIF_DATA_KEY);
        mViewPifPropertiesPreference = findPreference(VIEW_PIF_PROPS_KEY);

        if (mKeyboxDataPreference != null) {
            mKeyboxDataPreference.setFilePickerLauncher(mKeyboxFilePickerLauncher);
        }

        if (mPifDataPreference != null) {
            mPifDataPreference.setFilePickerLauncher(mPifFilePickerLauncher);
        }

        if (mViewPifPropertiesPreference != null) {
            mViewPifPropertiesPreference.setOnPreferenceClickListener(preference -> {
                showPifPropsDialog();
                return true;
            });
        }
    }

    private void showPifPropsDialog() {
        String fetchedPif = Settings.Secure.getString(getContext().getContentResolver(), "fetched_pif");
        String pifData = Settings.Secure.getString(getContext().getContentResolver(), "pif_data");

        StringBuilder sb = new StringBuilder();
        sb.append("--- Auto-updated PIF (GitHub) ---\n");
        if (fetchedPif != null && !fetchedPif.isEmpty()) {
            try {
                JSONObject json = new JSONObject(fetchedPif);
                sb.append(json.toString(4));
            } catch (JSONException e) {
                sb.append(fetchedPif);
            }
        } else {
            sb.append("No data downloaded automatically.\n");
        }

        sb.append("\n\n--- Manually Imported PIF ---\n");
        if (pifData != null && !pifData.isEmpty()) {
            try {
                JSONObject json = new JSONObject(pifData);
                sb.append(json.toString(4));
            } catch (JSONException e) {
                sb.append(pifData);
            }
        } else {
            sb.append("No manual JSON imports.");
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Play Integrity Fix Status")
                .setMessage(sb.toString())
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM;
    }
}
