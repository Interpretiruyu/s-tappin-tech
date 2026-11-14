package com.example.examplemod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "stap", value = Dist.CLIENT)
public class STapHandler {

    private static final int DURATION_TICKS = 2; // 1 тик = 50 мс
    private static int ticksLeft = 0;

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getEntity() == mc.player && mc.player.level().isClientSide) {
            ticksLeft = DURATION_TICKS;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) {
            ticksLeft = 0;
            return;
        }

        if (ticksLeft > 0) {
            mc.options.keyDown.setDown(true);   // S
            mc.options.keyUp.setDown(false);    // W
            ticksLeft--;

            if (ticksLeft == 0) {
                restore(mc.options.keyDown, mc);
                restore(mc.options.keyUp, mc);
            }
        }
    }

    // возвращаем клавишу в её реальное (физическое) состояние
    private static void restore(KeyMapping key, Minecraft mc) {
        long window = mc.getWindow().getWindow();
        key.setDown(InputConstants.isKeyDown(window, key.getKey().getValue()));
    }
}
