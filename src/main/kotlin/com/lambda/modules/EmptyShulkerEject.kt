package com.lambda.modules

import com.lambda.HighwayHelper
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.items.throwAllInSlot
import com.lambda.client.util.text.MessageSendHelper
import com.lambda.client.util.threads.safeListener
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemShulkerBox
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent


/**
 * @author Doogie13
 * @since 11/05/2022
 */
internal object EmptyShulkerEject : PluginModule(
    name = "EmptyShulkerEject",
    category = Category.MISC,
    description = "Ejects empty shulker boxes.",
    pluginMain = HighwayHelper
) {

    private val delay by setting("Delay", 1, 0..10, 1)
    private val announce by setting("Announce", false)
    private val fiveB by setting("Unstacked", false, description = "Ejects shulkers which are not stacked, useful for 5b5t.org")

    private var ticksLeft = 0

    init {

        safeListener<ClientTickEvent> {

            if (NetherrackEject.active)
                return@safeListener

            if (--ticksLeft <= 0) {

                for (i in 0..50) {

                    val stack = mc.player.inventory.getStackInSlot(i)

                    if (stack.item is ItemShulkerBox) {

                        if (isEmpty(stack) || fiveB && stack.count == 1) {

                            val j = if (i < 9) i + 36 else i

                            throwAllInSlot(j)
                            if (announce)
                                MessageSendHelper.sendChatMessage("Ejected empty shulker box $j")
                            ticksLeft = delay
                            return@safeListener

                        }

                    }
                }
            }
        }
    }

    private fun isEmpty(itemStack: ItemStack): Boolean {

        try {
            val contentItems = NonNullList.withSize(27, ItemStack.EMPTY)
            ItemStackHelper.loadAllItems(itemStack.tagCompound!!.getCompoundTag("BlockEntityTag"), contentItems)

            for (i in contentItems.indices) {

                if (contentItems[i] != ItemStack.EMPTY) {
                    return false
                }

            }

            return true
        } catch (e: java.lang.NullPointerException) {
            return itemStack.item is ItemShulkerBox
        }

    }

}