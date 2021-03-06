package de.logerbyte.moneyminder.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dagger.android.support.DaggerDialogFragment
import de.logerbyte.moneyminder.R
import de.logerbyte.moneyminder.databinding.BaseDialogBinding


abstract class BaseDialogFragment : DaggerDialogFragment(), DialogCallback {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = DataBindingUtil.inflate(inflater, R.layout.base_dialog, container, false)
                as BaseDialogBinding
        additionalContentViewBinding(viewBinding)
        viewBinding!!.dialogViewModelListener = setDialogBaseActionButtonListener()
        return viewBinding.root
    }

    abstract fun additionalContentViewBinding(viewBinding: BaseDialogBinding)
    abstract fun setDialogBaseActionButtonListener(): BaseDialogActionListener

    override fun dismissDialog() {
        dismiss()
    }
}