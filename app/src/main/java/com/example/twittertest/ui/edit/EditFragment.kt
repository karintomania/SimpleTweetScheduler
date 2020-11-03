package com.example.twittertest.ui.edit

import android.annotation.SuppressLint
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.twittertest.R
import com.example.twittertest.database.AppDatabase
import com.example.twittertest.database.TweetScheduleDao
import com.example.twittertest.databinding.FragmentEditBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.lifecycle.Observer

class EditFragment : Fragment() {

    companion object {
        fun newInstance() = EditFragment()
    }

    private lateinit var viewModel: EditViewModel
    lateinit var binding: FragmentEditBinding
    val args:EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_edit, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val application = requireNotNull(this.activity).application
        val datasource = AppDatabase.getInstance(application).tweetScheduleDao
        val tweetId = args.tweetScheduleId

        Log.i("EditFragment", "${args.tweetScheduleId}")

        val viewModelFactory = EditViewModelFactory(datasource, tweetId, application)
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
        val tweetContent = binding.editTextTweet.text.toString()
        Log.i(this.tag, "btnOnClick: content = ${tweetContent}")

        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val dateTimeString = "${binding.textDate.text} ${binding.textTime.text}"
        Log.i(tag, "dateTImeString = ${dateTimeString}")
        val scheduleDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormat)

        Log.i(tag, "dateTIme = ${scheduleDateTime}")

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
}