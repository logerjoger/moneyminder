package de.logerbyte.moneyminder.cashsummary;

import android.arch.lifecycle.ViewModel;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import de.logerbyte.moneyminder.db.AppDatabaseManager;
import de.logerbyte.moneyminder.util.ConvertUtil;
import de.logerbyte.moneyminder.util.DigitUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by logerom on 28.07.18.
 */

public class CashSummaryViewModel extends ViewModel{
    private final CashAdapter cashAdapter;
    private final AppDatabaseManager appDatabaseManager;
    private ObservableField<String> cashDate = new ObservableField<>();
    private ObservableField<String> cashName = new ObservableField<>();
    private ObservableField<String> cashInEuro = new ObservableField<>();
    private ArrayList<CashItem> cashList = new ArrayList<>();
    private ObservableField<String> totalExpenses = new ObservableField<>();

    public CashSummaryViewModel(CashSummaryActivity cashSummaryActivity) {
        // TODO: 30.07.18 load cash list from database
        appDatabaseManager = new AppDatabaseManager(cashSummaryActivity);
        cashAdapter = new CashAdapter();

        initExpenseList();
        totalExpenses.set(String.valueOf(0));
    }

    private void initExpenseList() {
        appDatabaseManager.getAllExpense()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenses -> cashList = ConvertUtil.convert(expenses));
    }

    public void onClickAddCash(View view){
        setCashItem();
        addCashToTotal();
        cashAdapter.setList(cashList);
        cashAdapter.notifyDataSetChanged();
    }

    private void addCashToTotal() {
        Double euro = Double.valueOf(DigitUtil.commaToDot(totalExpenses.get())) + Double.valueOf(DigitUtil.commaToDot(cashInEuro.get()));
        totalExpenses.set(String.valueOf(Math.floor(euro * 100) / 100));
    }

    private void setCashItem() {
        cashList.add(new CashItem(cashDate.get(), cashName.get(), cashInEuro.get()));
    }


    @BindingAdapter({"adapter"})
    public static void setAdapter(RecyclerView recyclerView, CashAdapter cashAdapter) {
        recyclerView.setAdapter(cashAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public CashAdapter getCashAdapter() {
        return cashAdapter;
    }

    public ObservableField<String> getCashName() {
        return cashName;
    }

    public ObservableField<String> getCashInEuro() {
        return cashInEuro;
    }

    public ObservableField<String> getCashDate() {
        return cashDate;
    }

    public ObservableField<String> getTotalExpenses() {
        totalExpenses.set(DigitUtil.dotToComma(totalExpenses.get()));
        return totalExpenses;
    }
}

