package com.snap.instant.loan.finder.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.snap.instant.loan.finder.activity.base.BaseActivity
import com.snap.instant.loan.finder.apimodule.ApiRepository
import com.snap.instant.loan.finder.apimodule.pojoModel.CompanyProfileRes
import com.snap.instant.loan.finder.apimodule.pojoModel.HomeRes
import com.snap.instant.loan.finder.databinding.ActivityCompanyProfileBinding
import com.snap.instant.loan.finder.databinding.BenifitListItemBinding
import com.snap.instant.loan.finder.databinding.LoanProviderListItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CompanyProfileActivity : BaseActivity() {
    private var resourcesList: List<CompanyProfileRes.Data.Resource?>? = arrayListOf()
    private var benifitList: List<String?> = arrayListOf()
    var companyID: HomeRes.Company? = null
    lateinit var binding: ActivityCompanyProfileBinding

    @Inject
    lateinit var apiRepository: ApiRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyProfileBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        companyID = intent?.extras?.getParcelable("companyID")
        getCompanyProfile()
        setupAdapters()


        binding.txtBankName.text = companyID?.name.orEmpty()
        binding.txtStartingFrom.text = companyID?.subtitle.orEmpty()
        Glide.with(binding.ivBankImage).load(companyID?.image.orEmpty()).into(binding.ivBankImage)

        binding.ivBenifitArrow.setOnClickListener {
            binding.rvBenifitList.isVisible = !binding.rvBenifitList.isVisible
            setExpandColapse()
        }
        binding.ivProviderArrow.setOnClickListener {
            binding.rvLoanProviderList.isVisible = !binding.rvLoanProviderList.isVisible
            setExpandColapse()
        }
        setExpandColapse()
    }

    private fun setExpandColapse() {
        if (!binding.rvBenifitList.isVisible) {
            binding.ivBenifitArrow.rotation = 180f
        } else {
            binding.ivBenifitArrow.rotation = 0f
        }
        if (!binding.rvLoanProviderList.isVisible) {
            binding.ivProviderArrow.rotation = 180f
        } else {
            binding.ivProviderArrow.rotation = 0f
        }
    }

    private fun setupAdapters() {
        binding.rvLoanProviderList.layoutManager = LinearLayoutManager(this)
        binding.rvBenifitList.layoutManager = LinearLayoutManager(this)

        binding.rvBenifitList.adapter = BenefitListAdapter()
        binding.rvLoanProviderList.adapter = ResourcesListAdapter()
    }


    private fun getCompanyProfile() {
        showProgress()
        lifecycleScope.launch(Dispatchers.IO) {
            apiRepository.getCompanyProfile(companyID?.id.orEmpty()).onSuccess {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.string()
                    val compnRes = Gson().fromJson(res, CompanyProfileRes::class.java)
                    if (compnRes.success == true) {
                        setData(compnRes.data)
                    }
                }
            }.onFailure {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                }
            }
        }

    }

    private fun setData(data: CompanyProfileRes.Data?) {
        binding.llVisitNow.setOnClickListener {

        }

        benifitList = data?.benifits.orEmpty()
        binding.rvBenifitList.adapter?.notifyDataSetChanged()
        resourcesList = data?.resources
        binding.rvLoanProviderList.adapter?.notifyDataSetChanged()

    }

    inner class BenefitListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContentItemHolder(
                BenifitListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun getItemCount(): Int {
            return benifitList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as ContentItemHolder
            holder.binding.txtBankName.text = benifitList[holder.adapterPosition].orEmpty()
        }

        inner class ContentItemHolder(val binding: BenifitListItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    }

    inner class ResourcesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContentItemHolder(
                LoanProviderListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun getItemCount(): Int {
            return resourcesList?.size ?: 0
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as ContentItemHolder
            holder.binding.txtDescription.text =
                resourcesList?.get(holder.adapterPosition)?.details.orEmpty()
        }

        inner class ContentItemHolder(val binding: LoanProviderListItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    }
}