package de.logerbyte.moneyminder.domain.mapper

import de.logerbyte.moneyminder.BUDGET
import de.logerbyte.moneyminder.DATE_PATTERN
import de.logerbyte.moneyminder.data.viewItem.DayExpenseViewItem
import de.logerbyte.moneyminder.data.viewItem.ExpenseListViewItem
import de.logerbyte.moneyminder.data.viewItem.SummaryMonthViewItem
import de.logerbyte.moneyminder.domain.database.expense.Expense
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MonthSummaryItemViewMapper @Inject constructor(
    val sdf: SimpleDateFormat
): BaseMapper<List<Expense>, List<ExpenseListViewItem>> {

    /**
     * Epense list needs to be sorted in days
     */
    override fun map(from: List<Expense>): List<ExpenseListViewItem> {
        val viewItemList = ArrayList<ExpenseListViewItem>()
        var cashInMonth = 0.0

        for (expenseIndex in from.indices) {
            val hasNextItem = expenseIndex + 1 > from.size
            val expense = from[expenseIndex]
            val localDate = LocalDate.parse(expense.cashDate, DateTimeFormatter.ofPattern(DATE_PATTERN))

            viewItemList.add(DayExpenseViewItem(expense.cashDate, expense.cashName, expense.cashInEuro.toString(), expense.category, expense.person))
            cashInMonth += expense.cashInEuro

            if (hasNextItem) {
                val expense1 = from[expenseIndex+1]
                val nextLocalDate = LocalDate.parse(expense1.cashDate)

                if(localDate.month == nextLocalDate.month){
                    viewItemList.add(DayExpenseViewItem(expense1.cashDate, expense1.cashName, expense1.cashInEuro.toString(), expense1.category, expense1.person))
                    cashInMonth += expense1.cashInEuro
                }
            } else{
                viewItemList.add(SummaryMonthViewItem(cashInMonth, BUDGET - cashInMonth))
                cashInMonth = 0.0
            }
        }
        return viewItemList
    }
}