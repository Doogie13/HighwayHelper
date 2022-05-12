package com.lambda

import com.lambda.client.plugin.api.Plugin
import com.lambda.modules.AutoDisconnect
import com.lambda.modules.EmptyShulkerEject
import com.lambda.modules.NetherrackEject

internal object HighwayHelper : Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here
        modules.add(AutoDisconnect)
        modules.add(EmptyShulkerEject)
        modules.add(NetherrackEject)
    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}