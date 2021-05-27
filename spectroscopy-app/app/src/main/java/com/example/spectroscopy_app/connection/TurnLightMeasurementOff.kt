package com.example.spectroscopy_app.connection

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header

class TurnLightMeasurementOff {
    fun turnLightMeasurementOff() : Int {
        val client = AsyncHttpClient()
        val params = RequestParams()

        client["http://${Constant.ipAddress}/2", params, object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header?>?, res: String?) {
                // called when response HTTP status is "200 OK"
                Constant.lightOffCheck = 1
            }

            override fun onFailure(statusCode: Int, headers: Array<Header?>?, res: String?, t: Throwable?) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Constant.lightOffCheck = 0
            }
        }]
        return Constant.lightOffCheck
    }
}