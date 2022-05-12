package com.lambda.modules

import com.lambda.HighwayHelper
import com.lambda.client.event.events.ConnectionEvent
import com.lambda.client.event.listener.listener
import com.lambda.client.module.Category
import com.lambda.client.module.modules.misc.AutoReconnect
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.client.util.threads.safeListener
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.network.play.server.SPacketDisconnect
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

internal object AutoDisconnect : PluginModule(
    name = "AutoDisconnect",
    category = Category.MISC,
    description = "Automatically disconnect after a certain amount of time.",
    pluginMain = HighwayHelper
) {

    private var disconnectTime = 0L

    private var stage = Stage.INACTIVE

    enum class Stage {

        /**
         * On the server
         * */
        ONLINE,

        /**
         * On the main menu
         * */
        INACTIVE
    }

    private var timeMins by setting("Disconnect Delay", 15, 1..60, 1, description = "Time after joining that you disconnect in minutes")

    init {

        listener<ConnectionEvent.Connect> {

            disconnectTime = System.currentTimeMillis() + (timeMins * 60000)
            stage = Stage.ONLINE

        }

        onEnable {

            if (!AutoReconnect.isEnabled) {
                MessageSendHelper.sendChatMessage("Enable AutoReconnect and set it to the desired reconnect time.")
            }

            if (mc.world != null)
                stage = Stage.ONLINE

            if (disconnectTime == 0L)
                disconnectTime = System.currentTimeMillis() + (timeMins * 60000)

        }

        safeListener<ClientTickEvent> {

            if (!AutoReconnect.isEnabled) {
                AutoReconnect.enable()
            }

            // check if we have reached the time to disconnect
            if ((System.currentTimeMillis() - disconnectTime) > 0) {

                connection.handleDisconnect(SPacketDisconnect(TextComponentString("${ChatFormatting.BOLD} AutoDisconnect decided it was time to leave...")))

            }

        }

    }

    override fun getHudInfo(): String {

        // get time in seconds until disconnect time from now
        val time = (disconnectTime - System.currentTimeMillis()) / 1000

        return time.toInt().toString()

    }

}
