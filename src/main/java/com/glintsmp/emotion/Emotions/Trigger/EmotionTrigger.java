package com.glintsmp.emotion.Emotions.Trigger;

public enum EmotionTrigger {
    //Deaths
    PLAYER_DEATH_BY_PLAYER,
    PLAYER_KILLED_PLAYER,
    PET_KILLED_EVENT,

    //Damage
    PLAYER_DAMAGED_BY_PLAYER,

    //EVENT
    PLAYER_LOST_EVENT,
    PLAYER_WINS_EVENT,

    //Calm
    PLAYER_EATS_FOOD,
    PLAYER_SLEEPS,
    PLAYER_REGEN_HEALTH,
    PLAYER_LISTENS_TO_MUSIC_DISC,

    //Utils
    PLAYER_TOOL_BREAK,
    PLAYER_EXPERIENCES_LAG_SPIKE,
    PLAYER_ITEM_STOLEN,
}
