/*
 * Copyright 2019 Nicolas Maltais
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maltaisn.recurpicker.picker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.maltaisn.recurpicker.Recurrence
import com.maltaisn.recurpicker.getCallback
import java.util.Calendar

/**
 * Dialog used to select a date.
 */
internal class DateDialogFragment : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(state: Bundle?): Dialog {
        val context = requireContext()

        val args = requireArguments()
        val date = args.getLong(KEY_DATE)
        val minDate = args.getLong(KEY_MIN_DATE)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        // Since this is not a material component, colorAccent should be defined as either
        // colorPrimary or colorSecondary in styles.xml. (see demo app)
        val dialog = DatePickerDialog(context, { _, year, month, day ->
            calendar.set(year, month, day)
            calendar.setToStartOfDay()
            getCallback<Callback>()?.onDateDialogConfirmed(calendar.timeInMillis)
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DATE])

        val datePicker = dialog.datePicker
        if (minDate != Recurrence.DATE_NONE) {
            calendar.timeInMillis = minDate
            calendar.setToStartOfDay()
            datePicker.minDate = calendar.timeInMillis
        }

        return dialog
    }

    private fun Calendar.setToStartOfDay() {
        this[Calendar.HOUR_OF_DAY] = 0
        this[Calendar.MINUTE] = 0
        this[Calendar.SECOND] = 0
        this[Calendar.MILLISECOND] = 0
    }

    interface Callback {
        fun onDateDialogConfirmed(date: Long)
    }

    companion object {

        private const val KEY_DATE = "date"
        private const val KEY_MIN_DATE = "min_date"

        fun newInstance(date: Long, minDate: Long): DateDialogFragment {
            val dialog = DateDialogFragment()
            dialog.arguments = bundleOf(
                KEY_DATE to date,
                KEY_MIN_DATE to minDate
            )
            return dialog
        }
    }
}
