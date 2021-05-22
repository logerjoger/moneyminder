package de.logerbyte.moneyminder.presentation.dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerDialogFragment
import de.logerbyte.moneyminder.data.viewItem.DayExpenseViewItem
import de.logerbyte.moneyminder.databinding.BaseDialogBinding
import de.logerbyte.moneyminder.presentation.PARCEL_KEY


abstract class BaseDialogFragmentv1: DaggerDialogFragment() {

    open fun onCancelClicked(view: View) {
        this.dismiss()
    }
    open fun onOKClicked(view: View) {
        this.dismiss()
    }

    abstract fun viewContent(bundle: Bundle?): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return BaseDialogBinding.inflate(inflater).apply {
            dialogContainer.addView(viewContent(savedInstanceState))}.root
    }


}