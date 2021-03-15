package de.logerbyte.moneyminder.data.viewItem

import android.text.Editable
import androidx.databinding.ObservableField
import de.logerbyte.moneyminder.presentation.cashadapter.DayExpenseViewModel


class CashViewItem {
    // todo X: observable field copy in mapper
    val cashDate = ObservableField<String>()
    val cashName = ObservableField<String>()
    var cashAmount = ObservableField<String>()
    var cashCategory = ObservableField<String>("")
    var cashPerson = ObservableField<String>()

    var entryId: Long = 0
    var newDateText = ""
    var dateDotDelete = false

    fun setCash(item: DayExpenseViewModel) {
        this.entryId = item.entryId
        this.cashDate.set(item.cashDate.get())
        this.cashName.set(item.cashName.get())
        this.cashAmount.set(item.cashInEuro.get())
        this.cashCategory.set(item.cashCategory.get())
        this.cashPerson.set(item.cash)
    }

    fun onDateTextChanged(s: Editable) {
        if (dateDotDelete) {
            dateDotDelete = false
            return
        }
        // fixme: 02.09.18 make util class with that dot delete logic

        if (s.length < newDateText.length) {
            val charAt = newDateText.toString().get(newDateText.toString().length - 1)
            if (charAt == '.') {
                dateDotDelete = true
                s.delete(s.toString().length - 1, s.toString().length)
            }
            newDateText = s.toString()
            return
        }

        if (s.toString().length == 2 || s.toString().length == 5) {
            s.append(".")
            newDateText = s.toString()
            return
        }
        newDateText = s.toString()
    }

    fun isAllSet(): Boolean {
        return !(isSomeElementNull(listOf(cashDate.get(), cashName.get(), cashCategory.get(), cashAmount.get(), cashPerson.get())))
    }

    private fun isSomeElementNull(listOf: List<String?>): Boolean {
        for (element in listOf) {
            if (element.isNullOrEmpty()) {
                return true
            }
        }
        return false
    }
}