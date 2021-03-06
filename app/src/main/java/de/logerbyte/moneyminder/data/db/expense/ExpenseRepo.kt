package de.logerbyte.moneyminder.data.db.expense

import de.logerbyte.moneyminder.data.db.ExpenseDatabase
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by logerom on 08.08.18.
 */
@Singleton
class ExpenseRepo @Inject constructor(private val mExpenseDatabase: ExpenseDatabase) : ExpenseAPI {
    override fun getAllExpense(): Observable<List<Expense>> {
        return Observable.fromCallable { mExpenseDatabase.expenseDao().selectAll() }
    }

    override fun insertCashItemIntoDB(expense: Expense): Observable<Boolean> {
        return Observable.fromCallable {
            mExpenseDatabase.expenseDao().insert(expense)
            true
        }
    }

    override fun deleteCashItem(id: Long): Observable<Boolean> {
        return Observable.fromCallable {
            mExpenseDatabase.expenseDao().delete(id)
            true
        }
    }

    override fun updateCashItem(expense: Expense): Observable<Boolean> {
        return Observable.fromCallable {
            mExpenseDatabase.expenseDao().update(expense)
            true
        }
    }

    override fun getCategories(): Observable<List<String>> {
        return Observable.fromCallable { mExpenseDatabase.expenseDao().selectDistinctCategory() }
    }

    override fun expensesWithCategories(checkedCategories: Set<String>): Observable<MutableList<Expense>>? {
        return Observable.fromCallable { mExpenseDatabase.expenseDao().expensesWithCategories(checkedCategories.toTypedArray()) }
    }
}