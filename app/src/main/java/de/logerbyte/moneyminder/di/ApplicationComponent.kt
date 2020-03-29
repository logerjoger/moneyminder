package de.logerbyte.moneyminder.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import de.logerbyte.moneyminder.App
import de.logerbyte.moneyminder.addCashDialog.AddCashDialogModule
import de.logerbyte.moneyminder.ui.summaryList.CashSummaryModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            ApplicationModule::class,
            AndroidSupportInjectionModule::class,
            AddCashDialogModule::class,
            CashSummaryModule::class
        ])
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}