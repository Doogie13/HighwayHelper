package com.lambda

import com.lambda.client.plugin.api.Plugin
import com.lambda.modules.AutoDisconnect
import com.lambda.modules.EmptyShulkerEject

internal object ByFerroxHighwayHelper : Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here
        modules.add(AutoDisconnect)
        modules.add(EmptyShulkerEject)
    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}