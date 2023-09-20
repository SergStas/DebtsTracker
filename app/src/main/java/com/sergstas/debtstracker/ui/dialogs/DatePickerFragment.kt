package com.sergstas.debtstracker.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

class DatePickerFragment(
    private val onSelected: (Long) -> Unit,
): DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val date = LocalDate
            .of(year, month, day)
            .atTime(23, 59, 59)
            .toEpochSecond(ZoneOffset.UTC)
        onSelected(date)
        dismiss()
    }
}