package com.glintsmp.emotion;

import com.glintsmp.emotion.Commands.Command;
import com.glintsmp.emotion.Commands.Commands.Emotion.DecreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Emotion.IncreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustAdd;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustCheck;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustList;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustRemove;
import com.glintsmp.emotion.Emotions.EmotionManager;
import com.glintsmp.emotion.Trust.TrustManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class GlintSMP extends JavaPlugin {

    public static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("Emotions are running high!");

        EmotionManager.initialize(this);
        TrustManager.initialize(this);

        // Commands
        Command emotionCommand = new Command();
        Command trustCommand = new Command();

        emotionCommand.registerSubCommand(new IncreaseEmotion());
        emotionCommand.registerSubCommand(new DecreaseEmotion());

        trustCommand.registerSubCommand(new TrustAdd());
        trustCommand.registerSubCommand(new TrustRemove());
        trustCommand.registerSubCommand(new TrustList());
        trustCommand.registerSubCommand(new TrustCheck());

        Objects.requireNonNull(getCommand("Emotion")).setExecutor(emotionCommand);
        Objects.requireNonNull(getCommand("trust")).setExecutor(trustCommand);

        // Listeners
    }

    @Override
    public void onDisable() {
        logger.info("Emotions are flowing away...");
    }
}
