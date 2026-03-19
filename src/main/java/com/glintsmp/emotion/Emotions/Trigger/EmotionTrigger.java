package com.glintsmp.emotion.Emotions.Trigger;

import com.glintsmp.emotion.Utils.Objects.Cooldown;
import com.glintsmp.emotion.Utils.Objects.CooldownSupplier;
import org.bukkit.entity.Player;

public enum EmotionTrigger {
    //Deaths
    PLAYER_DEATH_BY_PLAYER(simpleCooldown(1200)), /** Triggered **/
    PLAYER_KILLED_PLAYER(simpleCooldown(1200)), /** Triggered **/
    PLAYER_DEATH_EXPLOSION(null), /** Triggered **/
    PET_KILLED(simpleCooldown(200)), /** Triggered **/
    PLAYER_WITNESS_DEATH(null), /** Triggered **/
    PLAYER_BETRAYED_EVENT(simpleCooldown(86400)), //Cooldown 1 day.

    //Damage
    PLAYER_DAMAGED_BY_PLAYER(null), /** Triggered **/
    PLAYER_DAMAGED_BY_MOB(null), /** Triggered **/
    PLAYER_ENTER_COMBAT(simpleCooldown(300)),
    PLAYER_STRUCK_BY_LIGHTNING(simpleCooldown(3600)),
    HEALTH_DROP(null), /** Triggered **/

    //Chats
    PLAYER_CHATS(null), /** Triggered **/
    PLAYER_RECEIVES_MESSAGE(null), /** Triggered **/
    PLAYER_RECEIVE_ADVANCEMENT_NORMAL(null), /** Triggered **/
    PLAYER_RECEIVE_ADVANCEMENT_TOAST(null), /** Triggered **/
    PLAYER_ENTER_END(null), /** Triggered **/
    PLAYER_ENTER_NETHER(null),

    //TODO: EVENT, These will be called once we start creating events
    PLAYER_LOST_EVENT(null),
    PLAYER_WINS_EVENT(null),

    //TODO: Add custom bosses
    // Note: We could use the api here to so we don't have to do some wierd name shit
    PLAYER_DEFEATS_BOSS(null), /** Triggered **/

    //Calm
    PLAYER_EATS_FOOD(null), /** Triggered **/
    PLAYER_SLEEPS(null), /** Triggered **/
    PLAYER_REGEN_HEALTH(simpleCooldown(3600)), /** Triggered **/
    PLAYER_LISTENS_TO_MUSIC_DISC(null), /** Triggered **/
    PLAYER_FISH(simpleCooldown(20)),

    //Trust
    PLAYER_TRUST_ADD(simpleCooldown(3600)),  /** Triggered **/
    PLAYER_TRUST_REMOVE(simpleCooldown(3600)),  /** Triggered **/

    //Fear
    NEAR_WARDEN(null), /** Triggered **/

    //Ticks
    ALONE_MINUTE(null), /** Triggered **/
    WITH_PLAYER_MINUTE(null),  /** Triggered **/
    GENERAL_DECREASE(null), /** Triggered **/
    GENERAL_INCREASE(null), /** Triggered **/

    //Utils
    PLAYER_TOOL_BREAK(null), /** Triggered **/
    PLAYER_EXPERIENCES_LAG_SPIKE(null), /** Triggered **/
    PLAYER_ITEM_STOLEN(null); /** Triggered **/

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
