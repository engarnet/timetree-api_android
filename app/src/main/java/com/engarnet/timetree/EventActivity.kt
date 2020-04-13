package com.engarnet.timetree

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.engarnet.timetree.api.v1.api.events.params.AddEventsParams
import com.engarnet.timetree.api.v1.api.events.params.DeleteEventsParams
import com.engarnet.timetree.api.v1.api.events.params.EventsParams
import com.engarnet.timetree.api.v1.api.events.params.UpdateEventParams
import com.engarnet.timetree.databinding.ActivityEventBinding
import com.engarnet.timetree.model.TCalendar
import com.engarnet.timetree.model.TEvent
import com.engarnet.timetree.model.type.Category
import com.engarnet.timetree.model.type.Include
import com.engarnet.timetree.util.Container
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class EventActivity : AppCompatActivity() {
    data class ViewModel(
        val eventId: MutableLiveData<String> = MutableLiveData(),
        val isKeep: MutableLiveData<Boolean> = MutableLiveData(),
        val title: MutableLiveData<String> = MutableLiveData(),
        val description: MutableLiveData<String> = MutableLiveData(),
        val allDay: MutableLiveData<Boolean> = MutableLiveData(),
        val startDate: MutableLiveData<String> = MutableLiveData(),
        val startTime: MutableLiveData<String> = MutableLiveData(),
        val endDate: MutableLiveData<String> = MutableLiveData(),
        val endTime: MutableLiveData<String> = MutableLiveData(),
        val location: MutableLiveData<String> = MutableLiveData(),
        val url: MutableLiveData<String> = MutableLiveData()
    ) {
        fun toUpdateParam(context: Context, calendarId: String): UpdateEventParams {
            return UpdateEventParams(eventId.value!!, toAddParam(context, calendarId))
        }

        fun toAddParam(context: Context, calendarId: String): AddEventsParams {
            return if (isKeep.value == true) {
                toAddKeepParams(calendarId)
            } else {
                toAddScheduleParams(context, calendarId)
            }
        }

        private fun toAddScheduleParams(context: Context, calendarId: String): AddEventsParams {
            val start =
                if (allDay.value == true) startDate.value + "00:00" else startDate.value + startTime.value
            val end =
                if (allDay.value == true) endDate.value + "00:00" else endDate.value + endTime.value
            return AddEventsParams.AddScheduleParams(
                calendarId = calendarId,
                title = title.value!!,
                allDay = allDay.value!!,
                startAt = start.toDate(context),
                startTimeZone = TimeZone.getDefault(),
                endAt = end.toDate(context),
                endTimeZone = TimeZone.getDefault(),
                description = description.value,
                location = location.value,
                url = url.value?.let { Uri.parse(it) },
                labelId = "1",
                attendees = listOf()
            )
        }

        private fun toAddKeepParams(calendarId: String): AddEventsParams {
            return AddEventsParams.AddKeepParams(
                calendarId = calendarId,
                title = title.value!!,
                allDay = false, // TODO: Keepの場合allDayはOptionalだがnullを渡すと400エラーになる
                description = description.value,
                location = location.value,
                url = url.value?.let { Uri.parse(it) },
                labelId = "1",
                attendees = listOf()
            )
        }

        @SuppressLint("SimpleDateFormat")
        private fun String.toDate(context: Context): Date {
            val format =
                context.getString(R.string.date_format) + context.getString(R.string.time_format)
            return SimpleDateFormat(format).parse(this)
        }

        companion object {
            fun empty(context: Context): ViewModel {
                return ViewModel().apply {
                    this.eventId.value = ""
                    this.isKeep.value = false
                    this.allDay.value = false
                    this.startDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        Calendar.getInstance()
                    ).toString()
                    this.startTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        Calendar.getInstance()
                    ).toString()
                    this.endDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        Calendar.getInstance()
                    ).toString()
                    this.endTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        Calendar.getInstance()
                    ).toString()
                }
            }

            fun from(context: Context, event: TEvent): ViewModel {
                return ViewModel().apply {
                    this.eventId.value = event.id
                    this.isKeep.value = event.category == Category.Keep
                    this.title.value = event.title
                    this.description.value = event.description
                    this.allDay.value = event.allDay
                    this.startDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        event.startAt
                    ).toString()
                    this.startTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        event.startAt
                    ).toString()
                    this.endDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        event.endAt
                    ).toString()
                    this.endTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        event.endAt
                    ).toString()
                    this.location.value = event.location
                    this.url.value = event.url?.toString()
                }
            }
        }
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityEventBinding>(this, R.layout.activity_event)
    }

    private val calendarId by lazy {
        intent.extras?.get(KEY_CALENDAR_ID) as? String
            ?: throw IllegalStateException("calendarId must is needed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        (intent?.extras?.get(KEY_EVENT_ID) as? String)?.also { eventId ->
            runBlocking {
                EventsParams(
                    calendarId = calendarId,
                    eventId = eventId,
                    include = Include.Events(
                        labels = true,
                        creator = true,
                        attendees = true
                    )
                ).runCatching {
                    Container.timeTreeApiWrapper.events(this)
                }.onSuccess {
                    binding.viewModel = ViewModel.from(this@EventActivity, it)
                }.onFailure {
                    print(it)
                }
            }
        } ?: run {
            binding.viewModel = ViewModel.empty(this)
        }

        binding.addButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                handleAddButtonClicked()
            }
        }
        binding.updateButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                handleUpdateButtonClicked()
            }
        }
        binding.deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                handleDeleteButtonClicked()
            }
        }
        binding.startDateText.setOnClickListener {
            val year = Integer.parseInt(binding.viewModel!!.startDate.value!!.substring(0, 4))
            val month = Integer.parseInt(binding.viewModel!!.startDate.value!!.substring(5, 7)) - 1
            val day = Integer.parseInt(binding.viewModel!!.startDate.value!!.substring(8, 10))
            showDatePickerDialog(year, month, day) {
                binding.viewModel!!.startDate.value = it
                binding.invalidateAll()
            }
        }
        binding.endDateText.setOnClickListener {
            val year = Integer.parseInt(binding.viewModel!!.endDate.value!!.substring(0, 4))
            val month = Integer.parseInt(binding.viewModel!!.endDate.value!!.substring(5, 7)) - 1
            val day = Integer.parseInt(binding.viewModel!!.endDate.value!!.substring(8, 10))
            showDatePickerDialog(year, month, day) {
                binding.viewModel!!.endDate.value = it
                binding.invalidateAll()
            }
        }
        binding.startTimeText.setOnClickListener {
            val hour = Integer.parseInt(binding.viewModel!!.startTime.value!!.substring(0, 2))
            val minute = Integer.parseInt(binding.viewModel!!.startTime.value!!.substring(3, 5))
            showTimePickerDialog(hour, minute) {
                binding.viewModel!!.startTime.value = it
                binding.invalidateAll()
            }

        }
        binding.endTimeText.setOnClickListener {
            val hour = Integer.parseInt(binding.viewModel!!.endTime.value!!.substring(0, 2))
            val minute = Integer.parseInt(binding.viewModel!!.endTime.value!!.substring(3, 5))
            showTimePickerDialog(hour, minute) {
                binding.viewModel!!.endTime.value = it
                binding.invalidateAll()
            }
        }
    }

    private fun showDatePickerDialog(year: Int, month: Int, day: Int, onChanged: (String) -> Unit) {
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, y, m, d ->
                Calendar.getInstance().apply {
                    set(y, m, d)
                }.let {
                    onChanged(
                        DateFormat.format(
                            this@EventActivity.getString(R.string.date_format), it
                        ).toString()
                    )
                }
            }, year, month, day
        ).run {
            show()
        }
    }

    private fun showTimePickerDialog(hour: Int, minute: Int, onChanged: (String) -> Unit) {
        TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, h, m ->
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, h)
                    set(Calendar.MINUTE, m)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.let {
                    onChanged(
                        DateFormat.format(
                            this@EventActivity.getString(R.string.time_format), it
                        ).toString()
                    )
                }
            }, hour, minute, true
        ).run {
            show()
        }
    }

    private suspend fun handleAddButtonClicked() {
        binding.viewModel!!.toAddParam(this, calendarId).runCatching {
            Container.timeTreeApiWrapper.addEvent(this)
        }.onSuccess {
            finish()
        }.onFailure {
            print(it)
        }
    }

    private suspend fun handleUpdateButtonClicked() {
        binding.viewModel!!.toUpdateParam(this, calendarId).runCatching {
            Container.timeTreeApiWrapper.updateEvent(this)
        }.onSuccess {
            finish()
        }.onFailure {
            print(it)
        }
    }

    private suspend fun handleDeleteButtonClicked() {
        DeleteEventsParams(
            calendarId = calendarId,
            eventId = binding.viewModel!!.eventId.value!!
        ).runCatching {
            Container.timeTreeApiWrapper.deleteEvent(this@runCatching)
        }.onSuccess {
            finish()
        }.onFailure {
            print(it)
        }
    }

    companion object {
        private const val KEY_CALENDAR_ID = "key_calendar_id"
        private const val KEY_EVENT_ID = "key_event_id"
        fun createIntent(context: Context, calendar: TCalendar): Intent {
            return Intent(context, EventActivity::class.java).apply {
                putExtra(KEY_CALENDAR_ID, calendar.id)
            }
        }

        fun createIntentWithEvent(context: Context, calendar: TCalendar, event: TEvent): Intent {
            return Intent(context, EventActivity::class.java).apply {
                putExtra(KEY_CALENDAR_ID, calendar.id)
                putExtra(KEY_EVENT_ID, event.id)
            }
        }
    }
}