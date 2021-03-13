package de.logerbyte.moneyminder.di.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import de.logerbyte.moneyminder.cashOverview.addCashDialog.AddCashDialogFragment
import de.logerbyte.moneyminder.cashOverview.viewModels.CashViewModel
import de.logerbyte.moneyminder.di.FragmentScope
import javax.inject.Named

@Module
abstract class AddCashDialogModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    abstract fun addCashDialogFragmet(): AddCashDialogFragment

    @Module
    class InjectViewModel {
        @Provides
        @FragmentScope
        @Named("ANDROID_VIEWMODEL")
        fun provideAddCashDialogViewModel(
                factory: ViewModelProvider.Factory,
                injector: AddCashDialogFragment
        ) = ViewModelProviders.of(injector, factory).get(CashViewModel::class.java)
    }
}
