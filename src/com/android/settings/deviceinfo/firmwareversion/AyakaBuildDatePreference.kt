/*
 * Copyright (C) 2025 AyakaUI
 */

package com.android.settings.deviceinfo.firmwareversion

import android.content.Context
import android.os.SystemProperties
import com.android.settings.R
import com.android.settingslib.metadata.PreferenceAvailabilityProvider
import com.android.settingslib.metadata.PreferenceMetadata
import com.android.settingslib.metadata.PreferenceSummaryProvider
import com.android.settingslib.metadata.preferencesapi.preconditions.PreconditionStability

class AyakaBuildDatePreference :
    PreferenceMetadata,
    PreferenceAvailabilityProvider,
    PreferenceSummaryProvider {

    private val KEY_BUILD_DATE_PROP = "ro.build.date"

    override val key: String
        get() = "os_build_date"

    override val purpose: Int
        get() = R.string.build_date

    override val title: Int
        get() = R.string.build_date

    override val availabilityDescription: String
        get() = "Requires build date property"

    override fun getAvailabilityStability(): PreconditionStability =
        PreconditionStability.STABLE_UNTIL_APK_UPDATE

    override fun isAvailable(context: Context): Boolean =
        SystemProperties.get(KEY_BUILD_DATE_PROP).isNotEmpty()

    override fun getSummary(context: Context): CharSequence =
        SystemProperties.get(
            KEY_BUILD_DATE_PROP,
            context.getString(R.string.unknown)
        )
}
