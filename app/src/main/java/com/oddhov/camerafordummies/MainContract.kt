package com.oddhov.camerafordummies

/**
 * Created by sammy on 09/11/2017.
 */

interface MainContract {
    interface View {

    }

    interface Presenter {
        fun subscribe()

        fun unsubscribe()
    }

    interface Repo  {

    }
}