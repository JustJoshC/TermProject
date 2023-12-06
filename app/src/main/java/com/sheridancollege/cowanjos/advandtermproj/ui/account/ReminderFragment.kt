package com.sheridancollege.cowanjos.advandtermproj.ui.account

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.sheridancollege.cowanjos.advandtermproj.R
import com.sheridancollege.cowanjos.advandtermproj.databinding.FragmentReminderBinding
import java.util.Calendar
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [reminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class reminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReminderBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        createNotificationChannel()

        binding.setReminder.setOnClickListener {
            scheduleNotification()
            view?.findNavController()?.navigate(R.id.action_reminderFragment_to_navigation_account)
        }

        binding.cancel.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_reminderFragment_to_navigation_account)
        }

        return binding.root
    }

    //Creating the notification with user specified title and desc
    private fun createNotificationChannel(){
        val name = "Notif Channel"
        val desc = "In charge of workout reminders"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)

        channel.description = desc
        channel.name = name
        val notificationManager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    //Setting the notifications date and time
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(){
        val intent = Intent(context, Notification::class.java)
        val title = binding.reminderName.text.toString()
        val desc = binding.reminderDesc.text.toString()

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, desc)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pendingIntent)
        showAlert(time, title, desc)
    }

    //creates a confirmation message that the notification has been made
    private fun showAlert(time: Long, title: String, message: String){
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getDateFormat(context)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context)

        AlertDialog.Builder(context).setTitle("Notification Scheduled").setMessage(
            "Title: " + title + "" + "\nDesc: " + message +
            "\nDate: " + dateFormat.format(date) + "\nTime:" + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    // collects the time in milliseconds from the time picker
    private fun getTime(): Long {
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year,month,day,hour,minute)

        return calendar.timeInMillis
    }
}