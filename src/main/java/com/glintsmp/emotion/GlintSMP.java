package com.glintsmp.emotion;

import com.glintsmp.emotion.Commands.Command;
import com.glintsmp.emotion.Commands.Commands.Emotion.DecreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Emotion.IncreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustAdd;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustCheck;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustList;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustRemove;
import com.glintsmp.emotion.Managers.ActionbarManager;
import com.glintsmp.emotion.Managers.EmotionManager;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipDecay;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipEventHandler;
import com.glintsmp.emotion.Managers.RelationshipManager;
import com.glintsmp.emotion.Trust.TrustManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class GlintSMP extends JavaPlugin {

    public static final MiniMessage miniMessage = MiniMessage.builder().build();

    public static Logger logger;
    private static GlintSMP instance;

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("Emotions are running high!");
        instance = this;

        EmotionManager.initialize(this);
        TrustManager.initialize(this);
        RelationshipManager.initialize(this);
        ActionbarManager.initialize(this);

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
        RelationshipDecay.start(this);


        // Event Handlers
        Bukkit.getPluginManager().registerEvents(new RelationshipEventHandler(this), this);
    }

    @Override
    public void onDisable() {
        logger.info("Emotions are flowing away...");
    }

    public static GlintSMP getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin instance is not initialized yet!");
        }
        return instance;
    }
}
