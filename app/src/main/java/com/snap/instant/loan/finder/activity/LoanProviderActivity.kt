package com.snap.instant.loan.finder.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import com.snap.instant.loan.finder.activity.base.BaseActivity
import com.snap.instant.loan.finder.activity.base.UserLoginDetail
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.apimodule.pojoModel.CompanyProfileRes
import com.snap.instant.loan.finder.apimodule.pojoModel.HomeRes
import com.snap.instant.loan.finder.apimodule.pojoModel.LoanProviderRes
import com.snap.instant.loan.finder.databinding.ActivityCompanyProfileBinding
import com.snap.instant.loan.finder.databinding.ActivityLoanProviderBinding
import com.snap.instant.loan.finder.databinding.HomeListProviderItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoanProviderActivity : BaseActivity() {
    var companyID: String? = null
    lateinit var binding: ActivityLoanProviderBinding

    @Inject
    lateinit var apiRepository: ApiRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanProviderBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        companyID = intent?.extras?.getString("companyID", "").orEmpty()
        Log.d(
            "LoanProviderActivity",
            "companyID $companyID onCreate: ${Hawk.get(UserLoginDetail.REMEMBER_TOKEN, "")}"
        )
        getCompanyProfile()
    }

    private fun getCompanyProfile() {
        showProgress()
        lifecycleScope.launch(Dispatchers.IO) {
            apiRepository.companylistByCategory(companyID!!).onSuccess {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.string()
                    Log.d("LoanProviderActivity", "onSuccess res $res")
                    val compnRes = Gson().fromJson(res, LoanProviderRes::class.java)
                    if (compnRes.success == true) {
                        setData(compnRes.data!!)
                    }
                }
            }.onFailure {
                Log.d("LoanProviderActivity", "onFailure res ${it.message}")
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                }
            }
        }
    }
    private var companiesList: ArrayList<LoanProviderRes.Data?>? = arrayListOf()
    private fun setData(data: List<LoanProviderRes.Data?>) {
        binding.rvLoanProviderList.layoutManager = LinearLayoutManager(this)
        companiesList?.addAll(data)
        binding.rvLoanProviderList.adapter = HomeListAdapter { companyID ->
            startActivity(Intent(this@LoanProviderActivity, CompanyProfileNewActivity::class.java).apply {
                putExtra("companyID", companyID)
            })
        }
    }


    inner class HomeListAdapter(var onClickCallback: (LoanProviderRes.Data?) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContentItemHolder(
                HomeListProviderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun getItemCount(): Int {
            return companiesList?.size!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as ContentItemHolder

            val imageFile = companiesList?.get(holder.adapterPosition)
            Glide.with(holder.binding.ivBankImage).load(imageFile?.image.orEmpty())
                .into(holder.binding.ivBankImage)
            holder.binding.txtBankName.text = imageFile?.name.orEmpty()
            holder.binding.txtStartingFrom.text = imageFile?.subtitle.orEmpty()

            holder.binding.ivArrow.setOnClickListener {
                onClickCallback.invoke(imageFile)

            }
        }

        inner class ContentItemHolder(val binding: HomeListProviderItemBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}