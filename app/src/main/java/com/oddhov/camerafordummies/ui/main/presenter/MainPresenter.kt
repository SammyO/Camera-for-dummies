package com.oddhov.camerafordummies.ui.main.presenter

import com.oddhov.camerafordummies.MainContract
import javax.inject.Inject

/**
 * Created by sammy on 09/11/2017.
 */

class MainPresenter
@Inject
constructor(private val view: MainContract.View, private val repo: MainContract.Repo) : MainContract.Presenter {

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }
}