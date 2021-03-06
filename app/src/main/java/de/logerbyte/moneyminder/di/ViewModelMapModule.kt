package de.logerbyte.moneyminder.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import de.logerbyte.moneyminder.cashOverview.CashSummaryViewModel
import de.logerbyte.moneyminder.cashOverview.menu.filter.FilterDialogVM
import de.logerbyte.moneyminder.cashOverview.viewModels.CashViewModel

@Module
abstract class ViewModelMapModule {

    @Binds
    @IntoMap
    @ViewModelKey(CashSummaryViewModel::class)
    abstract fun intoMapCashSummaryViewModel(cashSummaryViewModel: CashSummaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CashViewModel::class)
    abstract fun intoMapCashViewModel(cashViewModel: CashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterDialogVM::class)
    abstract fun intoMapFilterDialogVM(filterDialogVM: FilterDialogVM): ViewModel
}