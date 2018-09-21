package de.logerbyte.moneyminder.cashsummary;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.logerbyte.moneyminder.cashsummary.cashadapter.CashAdapter;
import de.logerbyte.moneyminder.cashsummary.cashadapter.CashAdapterItemViewModel;
import de.logerbyte.moneyminder.db.AppDatabaseManager;
import de.logerbyte.moneyminder.db.expense.Expense;
import de.logerbyte.moneyminder.util.ConvertUtil;
import de.logerbyte.moneyminder.util.DigitUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by logerom on 28.07.18.
 */

public class CashSummaryViewModel extends AndroidViewModel implements CashAdapter.Listener {

    private final CashAdapter cashAdapter;
    private final AppDatabaseManager appDatabaseManager;
    // fixme: 15.08.18 observable from cashItem instead every own field?
    private ObservableField<String> cashDate = new ObservableField<>();
    private ObservableField<String> cashName = new ObservableField<>();
    private ObservableField<String> cashInEuro = new ObservableField<>();
    private ArrayList<CashAdapterItemViewModel> cashList = new ArrayList<>();
    private ObservableField<String> totalExpenses = new ObservableField<>();

    public boolean dotDelete;
    String newText = new String();
    private CashAdapterItemViewModel.ActivityListener cashSummaryActivity;

    // fixme: 14.08.18 add live data in view and viewModel which updates the "view observable"

    public CashSummaryViewModel(@NonNull Application application) {
        super(application);
        appDatabaseManager = new AppDatabaseManager(application);
        cashAdapter = new CashAdapter(appDatabaseManager);
        cashAdapter.setLisener(this);
        totalExpenses.set(String.valueOf(0));
    }

    // FIXME: 13.09.18 source out loadExpesne from adapter and viewModel in base class
    private void loadExpenseList() {
        appDatabaseManager.getAllExpense()
                .subscribeOn(Schedulers.io())
                .map(expenses -> sortExpenses(expenses))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenses -> {
                    cashList = ConvertUtil.expensesToCashItems(expenses);
                    addCashToTotal(expenses);

                    cashAdapter.setList(cashList);
                    cashAdapter.notifyDataSetChanged();
                });
    }

    private List<Expense> sortExpenses(List<Expense> expenses) {
        // FIXME: 22.09.18 sort as util class
        Collections.sort(expenses, (o1, o2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            Date d1 = null, d2 = null;

            try{
                d1 = sdf.parse(o1.cashDate);
                d2 = sdf.parse(o2.cashDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return d1.compareTo(d2);
        });
        Collections.reverse(expenses);
        return expenses;
    }

    public void onClickAddCash(View view){
        Double date = DigitUtil.commaToDot(cashInEuro.get()) == "" ? 0.00 :
                Double.valueOf(DigitUtil.commaToDot(cashInEuro.get()));

        Expense expense = new Expense(null, cashName.get(), cashDate.get(),date);
        appDatabaseManager.insertCashItemIntoDB(expense)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aBoolean -> loadExpenseList());
        // fixme: 14.08.18 what is when error?

        clearInputField();
    }

    public void onTextChanged(Editable s) {
        if (dotDelete) {
            dotDelete = false;
            return;
        }
        // fixme: 02.09.18 make util class with that dot delete logic

        if (s.length() < newText.length()) {
            char charAt = newText.toString().charAt(newText.toString().length()-1);
            if (charAt == '.') {
                dotDelete = true;
                s.delete(s.toString().length() -1, s.toString().length());
            }
            newText = s.toString();
            return;
        }

        if (s.toString().length() == 2 || s.toString().length() == 5) {
            s.append(".");
            newText = s.toString();
            return;
        }

        newText = s.toString();
    }

    private void clearInputField() {
        cashDate.set("");
        cashName.set("");
        cashInEuro.set("");
    }

    private void addCashToTotal(List<Expense> cashList) {
        totalExpenses.set(DigitUtil.getCashTotal(cashList));
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

    @Override
    public void onLoadedExpenses(List<Expense> expenses) {
        addCashToTotal(expenses);
    }

    public void setCashSummaryActivity(
            CashAdapterItemViewModel.ActivityListener cashSummaryActivity) {
        this.cashSummaryActivity = cashSummaryActivity;
        cashAdapter.setActivityListener(cashSummaryActivity);
    }
}

