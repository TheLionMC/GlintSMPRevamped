package com.glintsmp.emotion.Emotions.Trigger;

import com.glintsmp.emotion.Utils.Objects.Cooldown;
import com.glintsmp.emotion.Utils.Objects.CooldownSupplier;
import org.bukkit.entity.Player;

public enum EmotionTrigger {
    //Deaths
    PLAYER_DEATH_BY_PLAYER(emptyCooldown()), /** Triggered **/
    PLAYER_KILLED_PLAYER(emptyCooldown()), /** Triggered **/
    PET_KILLED(simpleCooldown(200)), /** Triggered **/

    //Damage
    PLAYER_DAMAGED_BY_PLAYER(null), /** Triggered **/

    //Chats
    PLAYER_CHATS(null), /** Triggered **/
    PLAYER_RECEIVES_MESSAGE(null), /** Triggered **/

    //EVENT, These will be called once we start creating events
    PLAYER_LOST_EVENT(null),
    PLAYER_WINS_EVENT(null),

    //Calm
    PLAYER_EATS_FOOD(null), /** Triggered **/
    PLAYER_SLEEPS(null), /** Triggered **/
    PLAYER_REGEN_HEALTH(null), /** Triggered **/
    PLAYER_LISTENS_TO_MUSIC_DISC(null), /** Triggered **/

    //Trust
    PLAYER_TRUST_ADD(null),  /** Triggered **/
    PLAYER_TRUST_REMOVE(null),  /** Triggered **/

    //Ticks
    ALONE_MINUTE(null), /** Triggered **/
    WITH_PLAYER_MINUTE(null),  /** Triggered **/

    //Utils
    PLAYER_TOOL_BREAK(null), /** Triggered **/
    PLAYER_EXPERIENCES_LAG_SPIKE(null), /** Triggered **/
    PLAYER_ITEM_STOLEN(null);

    private final CooldownSupplier supplier;

    EmotionTrigger(CooldownSupplier supplier) {
        this.supplier = supplier;
    }

    public Cooldown applyCooldown(Player player, Object[] context) {
        return supplier.accept(player, context);
    }

    public CooldownSupplier getSupplier() {
        return supplier;
    }

    public static CooldownSupplier simpleCooldown(long time) {
        return (p, c) -> new Cooldown(time);
    }

    public static CooldownSupplier emptyCooldown() {
        return (p, c) -> new Cooldown(0);
    }
}
