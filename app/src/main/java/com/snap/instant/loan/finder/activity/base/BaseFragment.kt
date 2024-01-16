package com.snap.instant.loan.finder.activity.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment

@Keep
abstract class BaseFragment : Fragment() {
    var activity: BaseActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as BaseActivity
    }

    open fun showProgress() {
        activity?.showProgress()
    }
    open fun hideProgress() {
        activity?.hideProgress()
    }
}
