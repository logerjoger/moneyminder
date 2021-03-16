package de.logerbyte.moneyminder.presentation.cashadapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import de.logerbyte.moneyminder.R
import de.logerbyte.moneyminder.data.viewItem.DayExpenseViewItem
import de.logerbyte.moneyminder.data.viewItem.DayExpenseViewViewItem
import de.logerbyte.moneyminder.data.viewItem.ExpenseListViewItem
import de.logerbyte.moneyminder.data.viewItem.ExpenseListViewType
import de.logerbyte.moneyminder.data.viewItem.SummaryMonthViewItem
import de.logerbyte.moneyminder.presentation.dialog.dialogDelete.DeleteDialogFragment
import de.logerbyte.moneyminder.domain.database.expense.Expense
import de.logerbyte.moneyminder.databinding.AdapterEntryBinding
import de.logerbyte.moneyminder.databinding.AdapterEntryPlusSummaryBinding
import de.logerbyte.moneyminder.domain.ExpenseDataManager
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Created by logerom on 28.07.18.
 */

class CashAdapter(
    val onDeleteClicked: () -> View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var layoutInflater: LayoutInflater
    var items = listOf<ExpenseListViewItem>()
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is DayExpenseViewItem -> ExpenseListViewType.DAY_ITEM.ordinal
            is SummaryMonthViewItem -> ExpenseListViewType.MONTH_ITEM.ordinal
            else -> -1
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == ExpenseListViewType.DAY_ITEM.ordinal)
            DayExpenseViewHolder(AdapterEntryBinding.inflate(layoutInflater))
        else MonthExpenseViewHolder(AdapterEntryPlusSummaryBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is DayExpenseViewHolder -> {holder.bind(items[position] as DayExpenseViewItem)}
            is MonthExpenseViewHolder -> holder.bind(items[position] as SummaryMonthViewItem)
        }
    }

    inner class DayExpenseViewHolder(val binding: AdapterEntryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(viewItem: DayExpenseViewItem){
            binding.vmCashItem = viewItem
            binding.buDelete.setOnClickListener { onDeleteClicked }
        }
    }

    inner class MonthExpenseViewHolder(val binding: AdapterEntryPlusSummaryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(viewItem: SummaryMonthViewItem){
            binding.viewItem = viewItem
        }
    }
}
