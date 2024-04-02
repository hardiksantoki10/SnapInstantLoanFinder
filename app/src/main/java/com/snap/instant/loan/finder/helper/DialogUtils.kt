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
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.LoanProviderActivity
import com.snap.instant.loan.finder.activity.SplashActivity
import com.snap.instant.loan.finder.apimodule.pojoModel.CategoryRes
import com.snap.instant.loan.finder.apimodule.pojoModel.LoanProviderRes
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
                Log.d(
                    "calculateLaonAmount",
                    "btnCalculate binding.txtLoanType.text ${binding.txtLoanType.text}"
                )
                dataList.forEach {
                    Log.d(
                        "calculateLaonAmount",
                        "btnCalculate binding.txtLoanType.text ${it.title}"
                    )
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

    fun showOkDialog(
        context: Context,
        title: String,
        message: String,
        onClickCallback: () -> Unit
    ) {
        val alertDialog = AlertDialog.Builder(context).create()

        alertDialog.setTitle(title)
        alertDialog.setMessage(message)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, _ ->
            // Handle the "OK" button click
            onClickCallback.invoke()
            dialog.dismiss()
        }

        alertDialog.show()
    }

    fun showTwoOptionDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClick: () -> Unit
    ) {
        /*val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButtonText) { dialog, _ ->
            // Handle positive button click
            onPositiveClick()
            dialog.dismiss()
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButtonText) { dialog, _ ->
            // Handle negative button click
            dialog.dismiss()
        }

        alertDialog.show()*/



        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                onPositiveClick.invoke()
            }
            negativeButton(text = negativeButtonText) {
            }
        }



    }

    private fun calculateLaonAmount(activity: Activity, id1: String) {
        Log.d("calculateLaonAmount", "id1 $id1")
        activity.startActivity(Intent(activity, LoanProviderActivity::class.java).apply {
            putExtra("companyID", id1)
        })
    }

    fun Activity.openApp() {
        val newIntent = Intent(this, SplashActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
        overridePendingTransition(0, 0)
        finish()
    }
}