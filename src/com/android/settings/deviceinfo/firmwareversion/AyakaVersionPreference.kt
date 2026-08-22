/*
 * Copyright (C) 2025 AyakaUI
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
import android.content.Intent
import android.net.Uri
import android.os.SystemProperties
import androidx.preference.Preference
import com.android.settings.R
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.metadata.preferencesapi.preconditions.PreconditionStability
import com.android.settingslib.preference.PreferenceBinding

class AyakaVersionPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    private val KEY_AYAKA_VERSION = "ro.ayaka.version"

    override val key: String
        get() = "ayaka_version"

    override val purpose: Int
        get() = R.string.ayaka_version

    override val title: Int
        get() = R.string.ayaka_version

    override val availabilityDescription: String
        get() = "Requires AyakaUI version property"

    override fun getAvailabilityStability(): PreconditionStability =
        PreconditionStability.STABLE_UNTIL_APK_UPDATE

    override fun intent(context: Context): Intent? =
        Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://github.com/AyakaUI"))

    override fun isAvailable(context: Context): Boolean =
        getVersion(context).isNotEmpty()

    override fun getSummary(context: Context): CharSequence =
        getVersion(context)

    private fun getVersion(context: Context): String =
        SystemProperties.get(
            KEY_AYAKA_VERSION,
            context.getString(R.string.unknown)
        )

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        preference.isCopyingEnabled = true
    }
}
