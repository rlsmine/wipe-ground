package br.com.rillis.wipeground;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class FloorLoop {
    private static BossBar bossBar;
    private static final long minute = 1200L;
    private static String message;

    public static void init(String message_cfg){
        bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
        message = ChatColor.translateAlternateColorCodes('&', message_cfg);
    }

    public static void startLoop(int maxTimeSeconds, int timeBetweenSeconds){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(WipeGround.plugin, () -> {
            if(!Bukkit.getOnlinePlayers().isEmpty()) {
                System.out.println("[WipeGround] Clearing the ground in " + maxTimeSeconds + " seconds.");
                bossBar.setTitle(message.replace("{time}", String.valueOf(maxTimeSeconds)));
                bossBar.setProgress(1.0);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    bossBar.addPlayer(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
                bossBar.setVisible(true);

                for (int i = (maxTimeSeconds-1); i >= 0; i--) {
                    if (i > 0) {
                        updateBossBar(bossBar, i, maxTimeSeconds);
                    } else {
                        clear(bossBar, maxTimeSeconds);
                        deleteBossBar(bossBar, maxTimeSeconds+5);
                    }

                }
            }else{
                System.out.println("[WipeGround] No players online to clear the ground");
            }
        }, 200L, (timeBetweenSeconds + maxTimeSeconds) * 20L);
    }

    private static void updateBossBar(BossBar bossBar, int timeSeconds, int timeTotalSeconds){
        Bukkit.getScheduler().runTaskLater(WipeGround.plugin, () -> {
            bossBar.setTitle(message.replace("{time}", String.valueOf(timeSeconds)));
            bossBar.setProgress((timeSeconds / (timeTotalSeconds * 1.0)));
        }, (timeTotalSeconds - timeSeconds) * 20L);
    }

    private static void clear(BossBar bossBar, int delaySegundos){
        Bukkit.getScheduler().runTaskLater(WipeGround.plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=item]");
            for (Entity entity : Bukkit.getWorld("world").getEntities()) {
                if (entity.getType().equals(EntityType.ARROW)) {
                    entity.remove();
                }
            }

            bossBar.setProgress(0.0);
            bossBar.setTitle(message.replace("{time}", String.valueOf(0)));

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }
        }, delaySegundos * 20L);
    }

    private static void deleteBossBar(BossBar bossBar, int delaySegundos){
        Bukkit.getScheduler().runTaskLater(WipeGround.plugin, () -> {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }, delaySegundos * 20L);
    }
}
