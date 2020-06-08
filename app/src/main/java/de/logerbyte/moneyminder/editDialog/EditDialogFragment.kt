package de.logerbyte.moneyminder.editDialog

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import de.logerbyte.moneyminder.R
import de.logerbyte.moneyminder.databinding.BaseDialogBinding
import de.logerbyte.moneyminder.databinding.FrameCashBinding
import de.logerbyte.moneyminder.db.AppDatabaseManager
import de.logerbyte.moneyminder.dialogs.BaseDialogFragment
import de.logerbyte.moneyminder.dialogs.DialogViewListener
import de.logerbyte.moneyminder.screens.cashsummary.cashadapter.AdapterCallBack
import de.logerbyte.moneyminder.screens.cashsummary.cashadapter.DayExpenseViewModel
import de.logerbyte.moneyminder.viewModels.CashViewModel
import kotlinx.android.synthetic.main.frame_cash.*
import javax.inject.Inject


open class EditDialogFragment : BaseDialogFragment(), EditDialogCallback {

    lateinit var cashView: View
    lateinit var adapterCallback: AdapterCallBack
    lateinit var cash: DayExpenseViewModel
    val cashViewModel = CashViewModel()
    lateinit var editDialogViewModel1: EditDialogViewModel1

    @Inject
    lateinit var appDatabaseManager: AppDatabaseManager

    // TODO: 2019-11-07 use same for add cash + set name of button generally
    // TODO-SW: new categories exist for add cash. But not for edit cash.
    // Add category list from db
    override fun additionalContentViewBinding(viewBinding: BaseDialogBinding) {
        editDialogViewModel1 = EditDialogViewModel1(appDatabaseManager, this, context, cashViewModel, this)
        cashView = LayoutInflater.from(context).inflate(R.layout.frame_cash, null, false)
        DataBindingUtil.bind<FrameCashBinding>(cashView!!).let { it!!.viewModel = cashViewModel }
        viewBinding.dialogContainer.addView(cashView)
        cashViewModel.setCash(cash)
        editDialogViewModel1.setAdapter(adapterCallback)
    }


    override fun setDialogBaseActionButtonListener(): DialogViewListener {
        return editDialogViewModel1
        // TODO: 2019-11-19 create baseDialogViewAction (for cancel and ok)
    }

    override fun initCategories(categories: ArrayList<String>) {
        custom_searchlist.list = categories
    }
}