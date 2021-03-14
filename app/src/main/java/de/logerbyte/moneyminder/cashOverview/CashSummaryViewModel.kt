package de.logerbyte.moneyminder.cashOverview

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.logerbyte.moneyminder.cashOverview.cashadapter.CashAdapter
import de.logerbyte.moneyminder.cashOverview.cashadapter.ExpenseDataManager
import de.logerbyte.moneyminder.data.viewItem.CashViewItem
import de.logerbyte.moneyminder.data.dbItem.expense.Expense
import de.logerbyte.moneyminder.mapper.ExpenseToItemMapper
import de.logerbyte.moneyminder.util.DigitUtil.dotToComma
import de.logerbyte.moneyminder.util.DigitUtil.doubleToString
import de.logerbyte.moneyminder.util.DigitUtil.getCashTotal
import javax.inject.Inject

class CashSummaryViewModel @Inject constructor(val expenseDataManager: ExpenseDataManager,
                                               val mapper: ExpenseToItemMapper) : ViewModel(), CashAdapter.Listener {
    val totalExpenses = ObservableField<Double>()
    private val totalBudget = ObservableField("0,00")
    private val totalSaving = ObservableField<Double?>()
    private val _cashItems = MutableLiveData<List<CashViewItem>>()
    val cashItems: LiveData<List<CashViewItem>> = _cashItems

    // fixme: 14.08.18 add live data in view and viewModel which updates the "view observable"
    init {
        totalExpenses.set(0.0)
        initCashViewItem()
    }

    private fun initCashViewItem() {
        expenseDataManager.loadExpenseList()
            .map { mapper.map(it) }
            .subscribe {_cashItems.value = it}
    }

    private fun addCashToTotal(cashList: List<Expense>) {
        totalExpenses.set(getCashTotal(cashList))
    }

    fun getTotalBudget(): ObservableField<String> {
        totalBudget.set(dotToComma(totalBudget.get()!!))
        return totalBudget
    }

    fun getTotalSaving(): ObservableField<Double?> {
        totalSaving.set(totalSaving.get())
        return totalSaving
    }

    override fun onLoadedExpenses(expenses: List<Expense>, allBudget: Int) {
        addCashToTotal(expenses)
        totalBudget.set(doubleToString(allBudget.toDouble()))
        totalSaving.set(java.lang.Double.valueOf(allBudget.toDouble()) - totalExpenses.get()!!)
    }
}