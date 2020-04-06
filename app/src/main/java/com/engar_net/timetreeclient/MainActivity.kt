package com.engar_net.timetreeclient

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engar_net.timetreeclient.api.v1.api.authorization.params.AuthorizeParams
import com.engar_net.timetreeclient.api.v1.api.calendars.params.CalendarsParams
import com.engar_net.timetreeclient.databinding.ActivityMainBinding
import com.engar_net.timetreeclient.model.type.Include
import com.engar_net.timetreeclient.ui.AuthorizeActivity
import com.engar_net.timetreeclient.util.Container
import com.engar_net.timetreeclient.view.CalendarRecyclerViewAdapter
import com.engar_net.timetreeclient.wrapper.TimeTreeApiWrapper
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    data class OAuthSetting(
        val clientId: String,
        val clientSecret: String,
        val redirectUrl: String
    ) {
        val isEmpty: Boolean get() = (clientId.isEmpty() || clientSecret.isEmpty() || redirectUrl.isEmpty())
    }

    // TODO: OAuthアプリケーションの情報を設定
    private val setting = OAuthSetting(
        clientId = "",
        clientSecret = "",
        redirectUrl = ""
    )

    // TODO: パーソナルアクセストークンを設定
    private val personalAccessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.adapter = CalendarRecyclerViewAdapter("カレンダー一覧", listOf()) { position ->
            startActivity(
                CalendarActivity.createIntent(
                    this,
                    binding.adapter!!.items[position]
                )
            )
        }
        binding.loginButton.setOnClickListener {
            handleLoginButtonClicked()
        }

        binding.accessTokenButton.setOnClickListener {
            handleAccessTokenButtonClicked()
        }
    }

    private fun handleLoginButtonClicked() {
        // 未設定の場合は処理を実施しない
        if (setting.isEmpty) return

        AuthorizeParams(
            clientId = setting.clientId,
            clientSecret = setting.clientSecret,
            redirectUrl = setting.redirectUrl,
            state = UUID.randomUUID().toString(),
            codeVerifier = UUID.randomUUID().toString()
        ).let { AuthorizeActivity.createIntent(this, it) }.let {
            startActivityForResult(it, AuthorizeActivity.REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AuthorizeActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val accessToken = data?.extras?.getString(AuthorizeActivity.KEY_ACCESS_TOKEN) ?: return
            Container.timeTreeApiWrapper = TimeTreeApiWrapper(accessToken = accessToken)

            loadCalendars()
        }
    }

    private fun handleAccessTokenButtonClicked() {
        // 未設定の場合は処理を実施しない
        if (personalAccessToken.isEmpty()) return
        Container.timeTreeApiWrapper = TimeTreeApiWrapper(accessToken = personalAccessToken)

        loadCalendars()
    }

    private fun loadCalendars() {
        runBlocking {
            CalendarsParams(
                Include.Calendars(
                    labels = true,
                    members = true
                )
            ).runCatching {
                Container.timeTreeApiWrapper.calendars(this)
            }.onSuccess { calendars ->
                binding.adapter!!.items = calendars
                binding.adapter!!.notifyDataSetChanged()
            }.onFailure {
                print(it)
            }
        }
    }
}
