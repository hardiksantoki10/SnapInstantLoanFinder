package com.snap.instant.loan.finder.helper

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.snap.instant.loan.finder.BuildConfig
import com.snap.instant.loan.finder.R
import com.snap.instant.loan.finder.databinding.SnackbarBinding
import kotlin.math.pow

object Utils {
    const val DEVELOPER_ACC = "https://play.google.com/store/apps/developer?id=justapps"

    const val ACTION_UPDATE_FAVOURITE_STATE = "ACTION_UPDATE_FAVOURITE_STATE"

    const val ACTION_DOWNLOAD_INAPP = "ACTION_DOWNLOAD_INAPP"

    var SHARE_TEXT = ""

    fun getGson(): Gson {
        return GsonBuilder().enableComplexMapKeySerialization().serializeNulls()
            .disableHtmlEscaping().generateNonExecutableJson().setLenient().setPrettyPrinting()
            .create()
    }

    fun getScreenWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun dpToPx(dp: Float): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    fun getAppVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    fun getAppVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getAppPackageName(): String {
        return BuildConfig.APPLICATION_ID
    }

    fun convertPxToDp(context: Context, value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics
        ).toInt()
    }

    fun openKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun closeKeyboard(activity: Activity, view: View) {
        val inputManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun keyboardStatus(activity: Activity): Boolean {
        val imm by lazy { activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
        val windowHeightMethod =
            InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
        return windowHeightMethod.invoke(imm) as Int > 0
    }


    fun showSnackBar(activity: Context, view: View, content: String) {
        try {
            val snackView = View.inflate(activity, R.layout.snackbar, null)
            val binding = SnackbarBinding.bind(snackView)
            val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
            (snackbar.view as ViewGroup).removeAllViews()
            (snackbar.view as ViewGroup).addView(binding.root)
            snackbar.view.setPadding(8, 8, 8, 8)
            snackbar.view.elevation = 0f
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    activity, android.R.color.transparent
                )
            )
            binding.tvTitle.text = content
            snackbar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSnackBar(
        activity: Context,
        view: View,
        content: String,
        actionButton: String = "",
        duration: Int = Snackbar.LENGTH_LONG,
        onClickListener: View.OnClickListener? = null,
    ): Snackbar {
        val snackView = View.inflate(activity, R.layout.snackbar, null)
        val binding = SnackbarBinding.bind(snackView)
        val snackbar = Snackbar.make(view, "", duration)
        try {
            (snackbar.view as ViewGroup).removeAllViews()
            (snackbar.view as ViewGroup).addView(binding.root)
            snackbar.view.setPadding(8, 8, 8, 8)
            snackbar.view.elevation = 0f
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    activity, android.R.color.transparent
                )
            )
            binding.tvTitle.text = content

            if (actionButton.isNotEmpty()) {
                binding.tvRetry.isVisible = true
                binding.tvRetry.text = actionButton
                binding.tvRetry.setOnClickListener { p0 ->
                    snackbar.dismiss()
                    onClickListener?.onClick(p0)
                }
            }
            snackbar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return snackbar
    }

    private var toast: Toast? = null
    fun Context.showToast(activity: Activity, message: String) {
        if (toast != null)
            toast!!.cancel()
        toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        if (toast != null) {
            toast!!.show()
        }
    }

    fun rateApp(context: Context, measurement: String) {
        val uri = Uri.parse("market://details?id=" + context.packageName + measurement)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.

        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )


        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName + measurement)
                )
            )
        }
    }

    fun rateAppWithPackage(context: Context, packageName: String) {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.

        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )

        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)
                )
            )
        }
    }

    fun openBrowser(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val broseUrl = if (!url.startsWith("http")) {
                "https://$url"
            } else if (url.startsWith("http:/", ignoreCase = true)) {
                url.replace("http:/", "https:/")
            } else url
            intent.data = Uri.parse(broseUrl)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRandomString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(length) { charset.random() }.joinToString("")
    }

    fun removeExtraZero(context: Context, priceText: String): String {
        try {
            if (priceText.endsWith(".00", ignoreCase = true)
                || priceText.endsWith(",00", ignoreCase = true)
            ) {
                return priceText.dropLast(3)
            }
            return priceText
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return priceText
    }

    fun isAppInstalled(context: Context, packageName: String): Boolean {
        return isPackageInstalled(context, packageName)
    }

    private fun isPackageInstalled(context: Context, packageName: String?): Boolean {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName!!) ?: return false
        val list =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.isNotEmpty()
    }

    fun calculateLoan(
        loanAmount: Double,
        annualInterestRate: Double,
        loanTermInMonths: Double,
        calculateLoanCallback: (Double, Double, Double) -> Unit
    ) {
        val monthlyInterestRate = annualInterestRate / 1200
        val denominator = (1 - (1 + monthlyInterestRate).pow(-loanTermInMonths))

        val monthlyPayment = loanAmount * (monthlyInterestRate / denominator)
        val totalPayments = monthlyPayment * loanTermInMonths
        val totalInterest = totalPayments - loanAmount

        calculateLoanCallback.invoke(monthlyPayment, totalPayments, totalInterest)

    }
}