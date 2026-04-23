/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.firmwareversion

import android.content.Context
import androidx.preference.Preference
import com.android.settings.R
import com.android.settingslib.DeviceInfoUtils
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.preference.PreferenceBinding
import java.io.File

// LINT.IfChange
class KernelVersionPreference : PreferenceMetadata, PreferenceSummaryProvider, PreferenceBinding {

    private var isFullVersionShown = false

    override val key: String
        get() = "kernel_version"

    override val title: Int
        get() = R.string.kernel_version

    override fun getSummary(context: Context): CharSequence? =
        DeviceInfoUtils.getFormattedKernelVersion(context)

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        
        preference.isSelectable = true
        preference.isCopyingEnabled = true

        preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (isFullVersionShown) {
                preference.summary = DeviceInfoUtils.getFormattedKernelVersion(preference.context)
                isFullVersionShown = false
            } else {
                preference.summary = getFullKernelVersion()
                isFullVersionShown = true
            }
            true
        }
    }

    private fun getFullKernelVersion(): String {
        return try {
            File("/proc/version").readText().trim()
        } catch (e: Exception) {
            "Unavailable"
        }
    }
}
// LINT.ThenChange(KernelVersionPreferenceController.java)
