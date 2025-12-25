package com.glintsmp.emotion.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public record ItemBuilder(ItemStack itemStack) {

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder displayName(Component displayName) {
        return editItemMeta(meta -> meta.displayName(displayName.decoration(TextDecoration.ITALIC, false)));
    }

    public ItemBuilder displayNameLegacy(String name) {
        return displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
    }

    public ItemBuilder lore(List<Component> lore) {
        return editItemMeta(meta -> meta.lore(lore));
    }

    public ItemBuilder addLore(@NotNull List<Component> lore) {
        return editItemMeta(meta -> {
            if (meta.lore() == null) meta.lore(lore);
            else {
                List<Component> current = new ArrayList<>(Objects.requireNonNull(meta.lore()));
                current.addAll(lore);
                meta.lore(current);
            }
        });
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        return addEnchant(enchantment, level, false);
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        return editItemMeta(meta -> meta.addEnchant(enchantment, level, ignoreLevelRestriction));
    }

    public ItemBuilder addEnchants(List<Pair<Enchantment, Integer>> enchantments) {
        return addEnchants(enchantments, false);
    }

    public ItemBuilder addEnchants(List<Pair<Enchantment, Integer>> enchantments, boolean ignoreLevelRestriction) {
        enchantments.forEach(pair ->
                addEnchant(pair.getLeft(), pair.getRight(), ignoreLevelRestriction));
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        return editItemMeta(meta -> meta.addItemFlags(itemFlag));
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        return editItemMeta(meta -> meta.addItemFlags(itemFlags));
    }

    public ItemBuilder setCustomModelData(int modelData) {
        return editItemMeta(meta -> meta.setCustomModelData(modelData));
    }

    public ItemBuilder setDurability(int amount) {
        return editItemMeta(Damageable.class, meta -> meta.setDamage(amount));
    }

    public ItemBuilder setMaxDurability(int amount) {
        return editItemMeta(Damageable.class, meta -> meta.setMaxDamage(amount));
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        return editItemMeta(meta -> meta.setUnbreakable(unbreakable));
    }

    @SuppressWarnings("unchecked")
    public ItemBuilder setData(NamespacedKey key, PersistentDataType<?, ?> type, Object value) {
        return editItemMeta(meta -> meta.getPersistentDataContainer().set(key, (PersistentDataType<Object, Object>) type, value));
    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        return editItemMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }

    public ItemBuilder skullOwner(OfflinePlayer player) {
        return editItemMeta(SkullMeta.class, meta -> meta.setOwningPlayer(player));
    }

    public ItemBuilder skullOwner(UUID uuid) {
        return editItemMeta(SkullMeta.class, meta -> meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid)));
    }

    public ItemBuilder setPotionType(PotionType type) {
        return editItemMeta(PotionMeta.class, meta -> meta.setBasePotionType(type));
    }

    public ItemBuilder addPotionEffect(PotionEffect effect, boolean overwrite) {
        return editItemMeta(PotionMeta.class, meta -> meta.addCustomEffect(effect, overwrite));
    }

    public ItemBuilder setPotionColor(Color color) {
        return editItemMeta(PotionMeta.class, meta -> meta.setColor(color));
    }

    public ItemBuilder setTrim(ArmorTrim trim) {
        return editItemMeta(ArmorMeta.class, meta -> meta.setTrim(trim));
    }

    public ItemBuilder hideToolTip(boolean hide) {
        return editItemMeta(meta -> meta.setHideTooltip(hide));
    }

    public ItemBuilder setChargedArrow() {
        return editItemMeta(CrossbowMeta.class, meta -> meta.setChargedProjectiles(List.of(new ItemStack(Material.ARROW))));
    }

    public ItemBuilder editItemMeta(Consumer<? super ItemMeta> consumer) {
        itemStack.editMeta(consumer);
        return this;
    }

    public <M extends ItemMeta> ItemBuilder editItemMeta(Class<M> metaClazz, Consumer<M> consumer) {
        itemStack.editMeta(metaClazz, consumer);
        return this;
    }

    public ItemBuilder function(Consumer<ItemBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    public ItemBuilder copy() {
        return new ItemBuilder(itemStack.clone());
    }

    public ItemStack build() {
        return itemStack;
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder of(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemStack simplePotion(PotionType type) {
        return new ItemBuilder(Material.POTION)
                .setPotionType(type)
                .build();
    }

    public static ItemStack simpleSplashPotion(PotionType type) {
        return new ItemBuilder(Material.SPLASH_POTION)
                .setPotionType(type)
                .build();
    }

    public static ItemStack customPotion(PotionEffectType type, int durationTicks, int amplifier) {
        return new ItemBuilder(Material.POTION)
                .addPotionEffect(new PotionEffect(type, durationTicks, amplifier), true)
                .setPotionColor(Color.RED)
                .build();
    }
}