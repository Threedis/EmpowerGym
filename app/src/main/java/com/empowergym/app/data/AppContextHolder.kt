package com.empowergym.app.data

import android.content.Context

/** Holds a reference to the application context so the repository can persist data
 *  without every screen needing to thread a Context through. Set once in MainActivity.onCreate. */
object AppContextHolder {
    lateinit var appContext: Context
}
