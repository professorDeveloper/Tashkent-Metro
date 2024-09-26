package com.azamovhudstc.tashkentmetro.utils

import android.os.CountDownTimer


class ResendTimerUtil(

    private val oonTick: (String) -> Unit,
    private val oonFinish: () -> Unit
) {
    private var timer: CountDownTimer? = null
    private var timeLeft: Long = 0
    private val durationMillis: Long = 60_000
    private val intervalMillis: Long = 1_000

    fun start() {
        timer?.cancel()
        timeLeft = durationMillis
        timer = object : CountDownTimer(timeLeft, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished

                val  a = if ((millisUntilFinished / 1000) >= 10) "0:${millisUntilFinished / 1000}" else "0:0${millisUntilFinished / 1000}"

                oonTick(a)
            }

            override fun onFinish() {
                oonFinish()
            }
        }.start()
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }
}
