/*
 * Copyright (C) 2025 MicaOS
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
import com.android.settingslib.preference.PreferenceBinding

class AyakaMaintainerPreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider,
    PreferenceBinding {

    private val KEY_AYAKA_MAINTAINER = "ro.ayaka.maintainer"

    override val key: String
        get() = "ayaka_maintainer"

    override val title: Int
        get() = R.string.ayaka_maintainer

    override fun intent(context: Context): Intent? {
        val maintainer = context.getMaintainer()
        if (maintainer == context.getString(R.string.unknown) || maintainer.isEmpty()) {
            return null
        }
        return Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse("https://github.com/$maintainer"))
    }

    override fun isAvailable(context: Context) = context.getMaintainer().isNotEmpty()

    override fun getSummary(context: Context) = context.getMaintainer()

    private fun Context.getMaintainer(): String =
        SystemProperties.get(KEY_AYAKA_MAINTAINER, getString(R.string.unknown))

    override fun bind(preference: Preference, metadata: PreferenceMetadata) {
        super.bind(preference, metadata)
        preference.isCopyingEnabled = true
    }
}
