package com.snap.instant.loan.finder.activity.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.activity.MainActivity
import com.snap.instant.loan.finder.activity.base.BaseFragment
import com.snap.instant.loan.finder.adapter.SliderAdapter
import com.snap.instant.loan.finder.apimodule.pojoModel.HomeRes
import com.snap.instant.loan.finder.databinding.FragmentCategoryBinding
import com.snap.instant.loan.finder.databinding.HomeListProviderItemBinding
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import com.snap.instant.loan.finder.apimodule.pojoModel.CategoryRes
import com.snap.instant.loan.finder.databinding.CategoryListItemBinding
import kotlinx.coroutines.launch
import java.util.Collections


class CategoryFragment : BaseFragment() {
    lateinit var binding: FragmentCategoryBinding
    private var companiesList: ArrayList<CategoryRes.Data> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentCategoryBinding.bind(
                inflater.inflate(
                    R.layout.fragment_category,
                    container,
                    false
                )
            )
        return binding.root
        // Inflate the layout for this fragment

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {
        binding.rvCategoryList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategoryList.adapter = CategoryListAdapter()
        getCategoryData()
    }

    private fun getCategoryData() {
        showProgress()
        lifecycleScope.launch(Dispatchers.IO) {
            (activity as MainActivity).apiRepository.getCategory().onSuccess {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.string()
                    Log.d("getHomeData", "onSuccess responce $res")
                    val homeRes = Gson().fromJson(res, CategoryRes::class.java)
                    if (homeRes.success) {
                        companiesList.clear()
                        companiesList.addAll(homeRes.data)
                        binding.rvCategoryList.adapter?.notifyDataSetChanged()
                        //setData(homeRes)
                    }
                }
            }.onFailure {
                lifecycleScope.launch(Dispatchers.Main) {
                    hideProgress()
                    val res = it.message
                    Log.d("getHomeData", "onFailure responce ${res}")
                }
            }
        }
    }

    inner class CategoryListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ContentItemHolder(
                CategoryListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun getItemCount(): Int {
            return companiesList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as ContentItemHolder
            holder.binding.txtBankName.text = companiesList[holder.adapterPosition].title
            holder.binding.txtStartingFrom.text = companiesList[holder.adapterPosition].details
            Glide.with(holder.binding.ivBankImage).load(companiesList[holder.adapterPosition].icon_image).into(holder.binding.ivBankImage)
        }

        inner class ContentItemHolder(val binding: CategoryListItemBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}