package com.snap.instant.loan.finder.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.LoanProviderActivity
import com.snap.instant.loan.finder.apimodule.pojoModel.CategoryRes
import com.snap.instant.loan.finder.databinding.BottomLoanDialogBinding
import com.snap.instant.loan.finder.helper.Utils.showToast


object DialogUtils {
    abstract class DialogClick {
        open fun onPositiveClick() {}
        open fun onNegativeClick() {}

        open fun onReplace() {}
        open fun onSaveAsNew() {}
    }


    enum class ShowSaveItemOptionDialog {
        SHOW_ALL, SHOW_DELETE, SHOW_EDIT_SHARE
    }

    object SingleValueChooserDialogs {
        fun show(
            context: Context?,
            items: Array<String>,
            title: String,
            listener: OnItemSelectedListener?
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
                .setItems(items) { dialog, which ->
                    val selectedItem = items[which]
                    listener?.onItemSelected(selectedItem)
                    dialog.dismiss()
                }
            builder.create().show()
        }

        interface OnItemSelectedListener {
            fun onItemSelected(selectedItem: String?)
        }
    }

    private fun showSingleValueChooserDialog(
        context: Context,
        items: Array<String>,
        txtLoanType: TextView
    ) {
        SingleValueChooserDialogs.show(
            context,
            items,
            "Select Loan Type",
            object : SingleValueChooserDialogs.OnItemSelectedListener {
                override fun onItemSelected(selectedItem: String?) {
                    txtLoanType.text = selectedItem
                }
            })
    }

    fun Activity.showSaveItemOptionDialog(dataList: List<CategoryRes.Data>) {
        val dialog = Dialog(this, R.style.DialogSlideAnim)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        val binding: BottomLoanDialogBinding =
            BottomLoanDialogBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(binding.root)

        val stringArray: Array<String> = dataList.map { it.title }.toTypedArray()

        binding.llLoanType.setOnClickListener {
            showSingleValueChooserDialog(
                this@showSaveItemOptionDialog,
                stringArray,
                binding.txtLoanType
            )
        }
        binding.btnCalculate.setOnClickListener {
            if (binding.txtLoanType.text.isNullOrEmpty()) {
                showToast(this, "Please select loan type")
            } else if (binding.edAmount.text.isNullOrEmpty()) {
                showToast(this, "Please add loan amount")
            } else {
                Log.d("calculateLaonAmount", "btnCalculate binding.txtLoanType.text ${binding.txtLoanType.text}")
                dataList.forEach {
                    Log.d("calculateLaonAmount", "btnCalculate binding.txtLoanType.text ${it.title}")
                    if (it.title == binding.txtLoanType.text.toString()) {
                        Log.d("calculateLaonAmount", "btnCalculate id1 ${it.id}")
                        calculateLaonAmount(this@showSaveItemOptionDialog, it.id)
                    }
                }
            }
        }


        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun calculateLaonAmount(activity: Activity, id1: String) {
        Log.d("calculateLaonAmount", "id1 $id1")
        activity.startActivity(Intent(activity, LoanProviderActivity::class.java).apply {
            putExtra("companyID", id1)
        })
    }
}