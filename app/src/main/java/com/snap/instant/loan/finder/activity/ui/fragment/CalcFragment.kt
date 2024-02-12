package com.snap.instant.loan.finder.activity.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.MainActivity
import com.snap.instant.loan.finder.activity.base.BaseFragment
import com.snap.instant.loan.finder.apimodule.pojoModel.CalcRes
import com.snap.instant.loan.finder.databinding.FragmentCalculatorBinding
import com.snap.instant.loan.finder.helper.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CalcFragment : BaseFragment() {

    lateinit var binding: FragmentCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.bind(
            inflater.inflate(
                R.layout.fragment_calculator,
                container,
                false
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    val values = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    private fun showSingleValueChooserDialog(items: Array<String>) {
        SingleValueChooserDialogs.show(
            requireContext(),
            items,
            "Select tenure",
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
                activity?.let { it1 -> requireActivity().showToast(it1, "Please add loan amount") }
            } else if (binding.edtTime.text.isNullOrEmpty()) {
                activity?.let { it1 -> requireActivity().showToast(it1, "Please add loan tenure") }
            } else if (binding.etRate.text.isNullOrEmpty()) {
                activity?.let { it1 -> requireActivity().showToast(it1, "Please add loan rate") }
            } else {
                showProgress()
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

                }
            }

        }
    }

    private fun setData(homeRes: CalcRes) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.txtMonthlyAmount.text = homeRes.data?.monthlyPayment.toString()
            binding.txtTotalInterest.text = homeRes.data?.totalInterest.toString()
            binding.txtTotalAmountPayable.text = homeRes.data?.totalPayable.toString()
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