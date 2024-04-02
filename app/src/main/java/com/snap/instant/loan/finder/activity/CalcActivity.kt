package com.snap.instant.loan.finder.activity

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.snap.instant.loan.finder.activity.base.BaseActivity
import com.snap.instant.loan.finder.apimodule.pojoModel.CalcRes
import com.snap.instant.loan.finder.databinding.ActivityCalculatorBinding
import com.snap.instant.loan.finder.helper.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CalcActivity : BaseActivity() {

    lateinit var binding: ActivityCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val interestRate = intent?.extras?.getString("interestRate", "").orEmpty()
        if (interestRate.isNotEmpty()) {
            interestRate.toIntOrNull()?.let { rate ->
                binding.etRate.setText(rate.toString())
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        initList()
    }

    val values = Array(248) { (it + 1).toString() }
    private fun showSingleValueChooserDialog(items: Array<String>) {
        SingleValueChooserDialogs.show(
            this@CalcActivity,
            items,
            "Select term",
            object : SingleValueChooserDialogs.OnItemSelectedListener {
                override fun onItemSelected(selectedItem: String?) {
                    lifecycleScope.launch {
                        binding.edtTime.text = selectedItem
                    }
                }
            })
    }

    private fun initList() {
        binding.llTime.setOnClickListener {
            showSingleValueChooserDialog(values)
        }

        binding.btnCalculate.setOnClickListener {
            if (binding.edAmount.text.isNullOrEmpty()) {
                activity.let { it1 -> showToast(it1, "Please add loan amount") }
            } else if (binding.edtTime.text.isNullOrEmpty()) {
                activity.let { it1 -> showToast(it1, "Please add loan term") }
            } else if (binding.etRate.text.isNullOrEmpty()) {
                activity.let { it1 -> showToast(it1, "Please add loan rate") }
            } else {
                Utils.calculateLoan(
                    binding.edAmount.text.toString().toDouble(),
                    binding.etRate.text.toString().toDouble(),
                    binding.edtTime.text.toString().toDouble()
                ) { monthlyPayment, totalPayments, totalInterest ->
                    setData(monthlyPayment,totalPayments,totalInterest)
                }
                /*showProgress()
                lifecycleScope.launch(Dispatchers.IO) {
                    (activity as MainActivity).apiRepository.loanCalculate(
                        binding.edAmount.text.toString(),
                        binding.etRate.text.toString(),
                        binding.edtTime.text.toString()
                    ).onSuccess {
                        lifecycleScope.launch(Dispatchers.Main) {
                            hideProgress()
                            val res = it.string()
                            Log.d("getProfileData", "onSuccess responce $res")

                            val homeRes = Gson().fromJson(res, CalcRes::class.java)

                            if (homeRes.success == true) {
                                setData(homeRes)
                            }
                        }
                    }.onFailure {
                        lifecycleScope.launch(Dispatchers.Main) {
                            hideProgress()
                            Log.d("getProfileData", "onFailure responce ${it.message}")
                        }
                    }
                }*/
            }
        }
    }
    private fun setData(monthlyPayment: Double, totalPayments: Double, totalInterest: Double) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.txtMonthlyAmount.text = "$ " + String.format("%.2f", monthlyPayment)
            binding.txtTotalInterest.text = "$ " + String.format("%.2f", totalInterest)
            binding.txtTotalAmountPayable.text = "$ " + String.format("%.2f", totalPayments)
        }
    }
    private fun setData(homeRes: CalcRes) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.txtMonthlyAmount.text = "$ " + homeRes.data?.monthlyPayment.toString()
            binding.txtTotalInterest.text = "$ " + homeRes.data?.totalInterest.toString()
            binding.txtTotalAmountPayable.text = "$ " + homeRes.data?.totalPayable.toString()
        }
    }

    class SingleValueChooserDialog(
        context: Context,
        title: String,
        values: Array<String>,
        listener: OnValueSelectedListener
    ) {

        private var selectedValue: String? = null
        private val valueSelectedListener: OnValueSelectedListener = listener

        init {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)

            builder.setSingleChoiceItems(values, -1) { _, which ->
                selectedValue = values[which]
            }

            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                valueSelectedListener.onValueSelected(selectedValue)
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        interface OnValueSelectedListener {
            fun onValueSelected(value: String?)
        }
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


}