package com.example.twittertest.ui.edit

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Application
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.twittertest.R
import com.example.twittertest.TwitterConst
import com.example.twittertest.database.AppDatabase
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.database.UserToken
import com.example.twittertest.databinding.FragmentEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditFragment : Fragment() {

    companion object {
        fun newInstance() = EditFragment()
    }

    private lateinit var viewModel: EditViewModel
    lateinit var binding: FragmentEditBinding
    val isLoggedIn = false
    val args:EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_edit, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getInstance(application).tweetScheduleDao
        val userTokenDao = AppDatabase.getInstance(application).userTokenDao
        val tweetId = args.tweetScheduleId

        Log.i("EditFragment", "${args.tweetScheduleId}")

        val viewModelFactory = EditViewModelFactory(datasource, userTokenDao, tweetId, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(EditViewModel::class.java)
        binding.editViewModel = viewModel

//        viewModel.tweetContent.observe(viewLifecycleOwner, Observer{
//            binding.editTextTweet.setText(it)
//        })



//        setCurrentDate()
//        viewModel.tweetContent.observe(viewLifecycleOwner, Observer<String>{tweetContent ->
//            Log.i(tag, tweetContent)
//        })

        // clickklistener
        binding.textDate.setOnClickListener{
            editTextDateOnClick()
        }

        binding.textTime.setOnClickListener{
            editTextTimeOnClick()
        }

        binding.buttonSave.setOnClickListener{
            btnSaveOnClick(application)
        }

        binding.buttonSchedule.setOnClickListener{
            btnScheduleOnClick(application)
        }

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)
        // TODO: Use the ViewModel
    }

    @SuppressLint("NewApi")
    private fun btnSaveOnClick(application: Application){
        val tweetContent = binding.editTextTweet.text.toString()
        Log.i(this.tag, "btnOnClick: content = ${tweetContent}")

        binding.editViewModel?.onSave(tweetContent)

        binding.editTextTweet.setText("")
        Toast.makeText(application,"Saved as a draft.",Toast.LENGTH_SHORT).show()

    }

    private fun btnScheduleOnClick(application: Application){

        if(!isLoggedIn){
           Log.i(tag, "you are not logged in.")
            getRequestToken()
        }

        val tweetContent = binding.editTextTweet.text.toString()

        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val dateTimeString = "${binding.textDate.text} ${binding.textTime.text}"
        val scheduleDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormat)

        binding.editViewModel?.onSchedule(tweetContent, scheduleDateTime)

        binding.editTextTweet.setText("")
        Toast.makeText(application,"Scheduled Tweet: ${dateTimeString}.",Toast.LENGTH_SHORT).show()

    }
    private fun editTextDateOnClick(){

        Log.i(tag, "editTextDateOnClick")
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { dpd, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            binding.textDate.setText("${year}/${(monthOfYear+1).toString().padStart(2,'0')}/${dayOfMonth.toString().padStart(2,'0')}")
        }, year, month, day)

        dpd.show()

    }

    private fun editTextTimeOnClick(){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            binding.textTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun setCurrentDate(){
       val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        val dateString = dateFormat.format(calendar.time)
        Log.i(tag, "dateString = ${dateString}")

        binding.textDate.text = dateString

    }


    // Twitter Log in.
    lateinit var twitter: Twitter

    private fun getRequestToken() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val builder = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConst.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConst.CONSUMER_SECRET)
                .setIncludeEmailEnabled(true)
            val config = builder.build()
            val factory = TwitterFactory(config)
            twitter = factory.instance
            try {
                val requestToken = twitter.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    setupTwitterWebviewDialog(requestToken.authorizationURL)
                }
            } catch (e: IllegalStateException) {
                Log.e("ERROR: ", e.toString())
            }
        }
    }

    lateinit var twitterDialog: Dialog

    // Show twitter login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebviewDialog(url: String) {
        twitterDialog = Dialog(this.context!!)
        val webView = WebView(this.context)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    inner class TwitterWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(TwitterConst.CALLBACK_URL)) {
                Log.d("Authorization URL: ", request?.url.toString())
                handleUrl(request?.url.toString())

                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString().contains(TwitterConst.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // Get the oauth_verifier
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val token = withContext(Dispatchers.IO) {
                    twitter.getOAuthAccessToken(oauthVerifier)
                }
                storeUserToken(token)
            }
        }

        suspend fun storeUserToken(accessToken: AccessToken) {
            val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }

            val userId = usr.id.toString()
            val name = usr.screenName
            val token = accessToken.token
            val tokenSecret = accessToken.tokenSecret

            Log.i("storeUserToken", token)
            Log.i("storeUserToken", tokenSecret)

            val userToken = UserToken(userId, name, token, tokenSecret)
            withContext(Dispatchers.IO) { binding.editViewModel?.storeUserToken(userToken) }

        }

        private fun checkLoggedIn(){
            val token = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
        }

    }
}