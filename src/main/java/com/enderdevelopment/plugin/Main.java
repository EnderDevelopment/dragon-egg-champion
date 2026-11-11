package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener {
    private static final long ANNOUNCEMENT_INTERVAL_TICKS = 3000L;
    private static final int EFFECT_DURATION_TICKS = 40;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, this::updateDragonEggPower, 0L, 20L);
        Bukkit.getScheduler().runTaskTimer(this, this::announceHolder, ANNOUNCEMENT_INTERVAL_TICKS,
                ANNOUNCEMENT_INTERVAL_TICKS);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeEffects(player);
        }
    }

    private void updateDragonEggPower() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasDragonEgg(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, EFFECT_DURATION_TICKS, 1, true, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, EFFECT_DURATION_TICKS, 0, true, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, EFFECT_DURATION_TICKS, 1, true, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, EFFECT_DURATION_TICKS, 1, true, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, EFFECT_DURATION_TICKS, 0, true, false, true));
            } else {
                removeEffects(player);
            }
        }
    }

    private void removeEffects(Player player) {
        player.removePotionEffect(PotionEffectType.STRENGTH);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.RESISTANCE);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.SATURATION);
    }

    private boolean hasDragonEgg(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isDragonEgg(item)) return true;
        }
        return false;
    }

    private boolean isDragonEgg(ItemStack item) {
        return item != null && item.getType() == Material.DRAGON_EGG;
    }

    private void announceHolder() {
        Player holder = Bukkit.getOnlinePlayers().stream().filter(this::hasDragonEgg).findFirst().orElse(null);
        Component message = holder == null
                ? Component.text("Nobody currently has the dragon egg.", NamedTextColor.GOLD)
                : Component.text(holder.getName() + " has the dragon egg.", NamedTextColor.GOLD);
        Bukkit.broadcast(message);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || !isEnderChest(event.getView().getTopInventory())) return;
        boolean placingEgg = isDragonEgg(event.getCursor())
                || (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && isDragonEgg(event.getCurrentItem()));
        if (placingEgg && event.getRawSlot() < event.getView().getTopInventory().getSize()) {
            event.setCancelled(true);
            player.sendMessage(Component.text("The dragon egg cannot be placed in an ender chest.", NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || !isEnderChest(event.getView().getTopInventory())
                || !isDragonEgg(event.getOldCursor())) return;
        int topSize = event.getView().getTopInventory().getSize();
        if (event.getRawSlots().stream().anyMatch(slot -> slot < topSize)) {
            event.setCancelled(true);
            player.sendMessage(Component.text("The dragon egg cannot be placed in an ender chest.", NamedTextColor.RED));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(this, this::updateDragonEggPower);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        removeEffects(event.getPlayer());
    }

    private boolean isEnderChest(Inventory inventory) {
        return inventory.getType() == InventoryType.ENDER_CHEST;
    }
}
