package com.muxiyun.bika.ui.notifications

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.muxiyun.bika.base.BaseViewModel
import com.muxiyun.bika.bean.NotificationsBean
import com.muxiyun.bika.network.RetrofitUtil
import com.muxiyun.bika.network.base.BaseHeaders
import com.muxiyun.bika.network.base.BaseObserver
import com.muxiyun.bika.network.base.BaseResponse

class NotificationsViewModel (application: Application) : BaseViewModel(application) {
    var page = 0

    val liveData: MutableLiveData<BaseResponse<NotificationsBean>> by lazy {
        MutableLiveData<BaseResponse<NotificationsBean>>()
    }

    fun getNotifications() {
        page++
        RetrofitUtil.service.notificationsGet(
            page.toString(),
            BaseHeaders("users/notifications?page=$page", "GET").getHeaderMapAndToken()
        )
            .doOnSubscribe(this)
            .subscribe(object : BaseObserver<NotificationsBean>() {
                override fun onSuccess(baseResponse: BaseResponse<NotificationsBean>) {
                    liveData.postValue(baseResponse)
                }

                override fun onCodeError(baseResponse: BaseResponse<NotificationsBean>) {
                    page--
                    liveData.postValue(baseResponse)
                }

            })
    }
}