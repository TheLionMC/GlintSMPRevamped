package com.glintsmp.emotion;

import com.glintsmp.emotion.Commands.Command;
import com.glintsmp.emotion.Commands.Commands.Emotion.DecreaseEmotion;
import com.glintsmp.emotion.Emotions.EmotionManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class GlintSMP extends JavaPlugin {

    public static Logger logger;

    @Override
    public void onEnable() {
        EmotionManager.initialize(this);
        logger = getLogger();
        logger.info("Emotions are running high!");

        // Commands
        Command emotionCommand = new Command();
        emotionCommand.registerSubCommand(new DecreaseEmotion());
        emotionCommand.registerSubCommand(new DecreaseEmotion());

        Objects.requireNonNull(getCommand("Emotion")).setExecutor(emotionCommand);

        // Listeners
    }

    @Override
    public void onDisable() {
        logger.info("Emotions are flowing away...");
    }
}
