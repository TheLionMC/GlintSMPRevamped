package com.glintsmp.emotion.Emotions.Trigger;

public enum EmotionTrigger {
    //Deaths
    PLAYER_DEATH_BY_PLAYER, /** Triggered **/
    PLAYER_KILLED_PLAYER, /** Triggered **/
    PET_KILLED, /** Triggered **/

    //Damage
    PLAYER_DAMAGED_BY_PLAYER, /** Triggered **/

    //EVENT, These will be called once we start creating events
    PLAYER_LOST_EVENT,
    PLAYER_WINS_EVENT,

    //Calm
    PLAYER_EATS_FOOD, /** Triggered **/
    PLAYER_SLEEPS, /** Triggered **/
    PLAYER_REGEN_HEALTH, /** Triggered **/
    PLAYER_LISTENS_TO_MUSIC_DISC,

    //Utils
    PLAYER_TOOL_BREAK,
    PLAYER_EXPERIENCES_LAG_SPIKE,
    PLAYER_ITEM_STOLEN,
}
