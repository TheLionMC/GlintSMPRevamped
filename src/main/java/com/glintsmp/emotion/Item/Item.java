package com.glintsmp.emotion.Item;

import com.glintsmp.emotion.GlintSMP;
import com.glintsmp.emotion.Utils.Objects.Cooldown;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public abstract class Item {

    private final Map<UUID, Cooldown> cooldownMap = new HashMap<>();

    private final String id;
    private final Function<Player, ItemStack> itemSupplier;
    private long cooldownTime = 0;

    public Item(@NotNull String id, Function<Player, @NotNull ItemStack> itemSupplier) {
        this.id = id;
        this.itemSupplier = itemSupplier;
    }

    public Item(@NotNull String id, Function<Player, @NotNull ItemStack> itemSupplier, long cooldownTime) {
        this.id = id;
        this.cooldownTime = cooldownTime;
        this.itemSupplier = itemSupplier;
    }

    public void activate(@NotNull Player player) {
        Cooldown cooldown = getCooldown(player);

        if (cooldown == null || cooldown.hasFinished()) {
            if (onActivate(player))
                setCooldown(player, new Cooldown(cooldownTime));
        } else
            player.sendMessage(Component.text("Item is still on cooldown: " + cooldown.getTimeLeft(),
                    NamedTextColor.RED));
    }

    public String getId() {
        return id;
    }

    @Nullable
    public Cooldown getCooldown(@NotNull Player player) {
        return cooldownMap.get(player.getUniqueId());
    }

    public void setCooldown(@NotNull Player player, @NotNull Cooldown cooldown) {
        cooldownMap.put(player.getUniqueId(), cooldown);
    }

    public long getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(long cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    @NotNull
    public ItemStack getItemStack(Player player) {
        ItemStack itemStack = itemSupplier.apply(player);

        itemStack.editPersistentDataContainer(container ->
                container.set(Objects.requireNonNull(NamespacedKey.fromString(id, GlintSMP.getInstance())),
                        PersistentDataType.STRING, id));

        return itemStack;
    }

    protected abstract boolean onActivate(Player player);
}
