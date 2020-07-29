package com.github.kr328.clash.service

import android.annotation.SuppressLint
import com.github.kr328.clash.core.Clash
import com.github.kr328.clash.core.model.General
import com.github.kr328.clash.core.model.ProxyGroupWrapper
import com.github.kr328.clash.service.data.ProfileDao
import com.github.kr328.clash.service.data.SelectedProxyDao
import com.github.kr328.clash.service.data.SelectedProxyEntity
import com.github.kr328.clash.service.transact.IStreamCallback
import com.github.kr328.clash.service.transact.ParcelableContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ClashManager(parent: CoroutineScope) :
    IClashManager.Stub(), CoroutineScope by parent {

    override fun setProxyMode(mode: String?) {
        Clash.setProxyMode(requireNotNull(mode))
    }

    override fun queryProxyGroups(): ProxyGroupWrapper {
        return ProxyGroupWrapper(Clash.queryProxyGroups())
    }

    override fun queryGeneral(): General {
        return Clash.queryGeneral()
    }

    override fun setSelector(proxy: String?, selected: String?) {
        require(proxy != null && selected != null)

        launch {
            val current = ProfileDao.queryActive() ?: return@launch

            SelectedProxyDao.setSelectedForProfile(SelectedProxyEntity(current.id, proxy, selected))
        }

        Clash.setSelector(proxy, selected)
    }

    override fun queryBandwidth(): Long {
        val data = Clash.queryBandwidth()

        return data.download + data.upload
    }

    @SuppressLint("NewApi")
    override fun performHealthCheck(group: String?, callback: IStreamCallback?) {
        require(group != null && callback != null)

        Clash.performHealthCheck(group).whenComplete { _, u ->
            if (u != null)
                callback.completeExceptionally(u.message)
            else
                callback.complete()
        }
    }

    override fun registerLogListener(key: String?, callback: IStreamCallback?) {
        requireNotNull(key)
        requireNotNull(callback)

        callback.asBinder().linkToDeath({
            Clash.unregisterLogReceiver(key)
        }, 0)

        Clash.registerLogReceiver(key) {
            try {
                callback.send(ParcelableContainer(it))
            } catch (e: Exception) {
                Clash.unregisterLogReceiver(key)
            }
        }
    }

    override fun unregisterLogListener(key: String?) {
        requireNotNull(key)

        Clash.unregisterLogReceiver(key)
    }
}