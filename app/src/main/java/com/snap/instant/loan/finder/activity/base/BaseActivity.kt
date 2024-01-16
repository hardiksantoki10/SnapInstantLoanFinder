package com.snap.instant.loan.finder.activity.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.snap.instant.loan.finder.R
import java.util.regex.Matcher
import java.util.regex.Pattern

@Keep
abstract class BaseActivity : AppCompatActivity() {



    lateinit var activity: Activity

    private var mLoadingDialog: Dialog? = null

    lateinit var mSnackBar: Snackbar

    val PERMISSION_ID = 42
    val LOCATION_SERVICE = 300



    private lateinit var timer : CountDownTimer




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this

    }


    open fun showProgress() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = Dialog(this, R.style.progressTransDialog)
                mLoadingDialog!!.setContentView(R.layout.view_progress_dialog)
            }
            val imageView = mLoadingDialog!!.findViewById<ImageView>(R.id.ivLoader)
            Glide.with(this).asGif().load(R.drawable.ic_progress).into(imageView)

            mLoadingDialog!!.setCancelable(false)
            mLoadingDialog!!.show()
        } catch (e: Exception) {
        }
    }

    open fun hideProgress() {
        try {
            if (mLoadingDialog != null && mLoadingDialog!!.isShowing)
                mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        } catch (e: Exception) {

        }
    }

    open fun isValidEmail(target: CharSequence?): Boolean {
        if(target == ""){
            return true
        }else{
            return emailPatterns.matcher(target!!).matches()
        }
    }
    var emailPatterns = Pattern.compile("[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+")

    val EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    )
    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    )

    open fun isNotValidEditText(editText: EditText): Boolean {
        return editText.text == null || editText.text.toString().trim().isEmpty()
    }

    open fun isValidNumber(s: String?): Boolean {

        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        val p: Pattern = Pattern.compile("(0|91)?[6-9][0-9]{9}")

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        val m: Matcher = p.matcher(s!!)
        return m.find() && m.group().equals(s)
    }

    open fun showToast(context: Context,message: String?) {
        try {

            val layout: View = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null)

            val tv = layout.findViewById<View>(R.id.tvName) as TextView
            tv.text = message
            val toast = Toast(applicationContext)
            toast.setGravity(Gravity.BOTTOM, 0, 100)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout

            val toastDurationInMilliSeconds = 1000

            // Set the countdown to display the toast
            val toastCountDown: CountDownTimer
            toastCountDown = object : CountDownTimer(toastDurationInMilliSeconds.toLong(), 1000 /*Tick duration*/) {
                override fun onTick(millisUntilFinished: Long) {
                    toast.show()
                }

                override fun onFinish() {
                    toast.cancel()
                }
            }

            // Show the toast and starts the countdown
            toast.show()
            toastCountDown.start()

        } catch (e: Exception) {
        }
    }

}
