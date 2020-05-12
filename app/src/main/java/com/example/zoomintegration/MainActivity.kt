package com.example.zoomintegration

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zoomintegration.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import us.zoom.sdk.*


class MainActivity : AppCompatActivity(), Constants, ZoomSDKInitializeListener,
    MeetingServiceListener, ZoomSDKAuthenticationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val zoomSDK: ZoomSDK = ZoomSDK.getInstance()
        if (savedInstanceState == null) {
            val zoomSDK: ZoomSDK = ZoomSDK.getInstance()

            if (savedInstanceState == null) {
                zoomSDK.initialize(
                    this,
                    Constants.APP_KEY,
                    Constants.APP_SECRET,
                    Constants.WEB_DOMAIN,
                    this
                )
            }
        }
    }

    fun onClickBtnJoinMeeting(view: View) {

        val meetingNo: String = meetingID.text.toString().trim()

        if (meetingNo.length == 0) {
            Toast.makeText(
                this,
                "You need to enter a meeting number/ vanity id which you want to join.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val zoomSDK = ZoomSDK.getInstance()

        if (!zoomSDK.isInitialized) {
            return
        }

        val meetingService = zoomSDK.meetingService

        val opts = JoinMeetingOptions()

        // Some available options
        //		opts.no_driving_mode = true;
        //		opts.no_invite = true;
        //		opts.no_meeting_end_message = true;
        //		opts.no_titlebar = true;
        //		opts.no_bottom_toolbar = true;
        //		opts.no_dial_in_via_phone = true;
        //		opts.no_dial_out_to_phone = true;
        //		opts.no_disconnect_audio = true;
        //		opts.no_share = true;
        //		opts.invite_options = InviteOptions.INVITE_VIA_EMAIL + InviteOptions.INVITE_VIA_SMS;
        //		opts.no_audio = true;
        //		opts.no_video = true;
        //		opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE;
        //		opts.no_meeting_error_message = true;
        //		opts.participant_id = "participant id";

        val params = JoinMeetingParams()
        params.displayName = "Zoom User"
        params.meetingNo = meetingNo

        meetingService.joinMeetingWithParams(this, params, opts)
    }

    override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
        Log.i(
            "MainActivity.TAG",
            "onZoomSDKInitializeResult, errorCode=$errorCode, internalErrorCode=$internalErrorCode"
        )

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(
                this,
                "Failed to initialize Zoom SDK. Error: $errorCode, internalErrorCode=$internalErrorCode",
                Toast.LENGTH_LONG
            )
        } else {
            Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show()
            registerMeetingServiceListener()
        }
    }

    private fun registerMeetingServiceListener() {
        val zoomSDK = ZoomSDK.getInstance()
        val meetingService = zoomSDK.meetingService
        meetingService?.addListener(this)
    }

    override fun onZoomSDKLoginResult(p0: Long) {
    }

    override fun onZoomIdentityExpired() {
    }

    override fun onZoomSDKLogoutResult(p0: Long) {
    }

    override fun onZoomAuthIdentityExpired() {
    }

    override fun onMeetingStatusChanged(p0: MeetingStatus?, p1: Int, p2: Int) {
    }
}
