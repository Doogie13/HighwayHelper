package com.lambda.modules

import com.lambda.HighwayHelper
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.items.block
import com.lambda.client.util.items.throwAllInSlot
import com.lambda.client.util.threads.safeListener
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * @author Doogie13
 * @since 12/05/2022
 */
internal object NetherrackEject : PluginModule(
    name = "NetherrackEject",
    category = Category.MISC,
    description = "Ejects netherrack from your inventory",
    pluginMain = HighwayHelper
) {

    private val keep by setting("Stacks", 1,0..36,1,description = "The amount of stacks to keep")
    private val delay by setting("Delay", 1, 0..10, 1)

    private var ticksLeft = 0

    var active = false

    init {

        safeListener<TickEvent.ClientTickEvent> {

            if (--ticksLeft <= 0) {

                val stacks = arrayListOf<Int>()

                for (i in 0..50) {

                    if (mc.player.inventory.getStackInSlot(i).item.block == Blocks.NETHERRACK) {

                        stacks.add(i)

                    }
                }

                if (stacks.size >= keep + 1) {

                    active = true

                    val i = stacks[keep.coerceIn(0 until stacks.size)]

                    val j : Int = if (i < 9) i + 36 else i

                    throwAllInSlot(NetherrackEject, j)

                    ticksLeft = delay

                } else {
                    active = false
                }
            }
        }
    }
}