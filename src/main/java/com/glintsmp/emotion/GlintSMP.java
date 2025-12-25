package com.glintsmp.emotion;

import com.glintsmp.emotion.Commands.Command;
import com.glintsmp.emotion.Commands.Commands.Ability.AbilityActivate;
import com.glintsmp.emotion.Commands.Commands.Emotion.DecreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Emotion.IncreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustAdd;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustCheck;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustList;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustRemove;
import com.glintsmp.emotion.Emotions.Trigger.EmotionListenerRegistry;
import com.glintsmp.emotion.Listeners.CustomEvents;
import com.glintsmp.emotion.Managers.*;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipDecay;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipEventHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        LifeManager.initialize(this);

        EmotionListenerRegistry.registerAll();

        // Commands
        Command emotionCommand = new Command();
        Command trustCommand = new Command();
        Command abilityCommand = new Command();

        //registering sub commands
        emotionCommand.registerSubCommand(new IncreaseEmotion());
        emotionCommand.registerSubCommand(new DecreaseEmotion());

        trustCommand.registerSubCommand(new TrustAdd());
        trustCommand.registerSubCommand(new TrustRemove());
        trustCommand.registerSubCommand(new TrustList());
        trustCommand.registerSubCommand(new TrustCheck());
        //registering sub commands
        
        //finally actually applying the registration
        abilityCommand.registerSubCommand(new AbilityActivate());

        Objects.requireNonNull(getCommand("Emotion")).setExecutor(emotionCommand);
        Objects.requireNonNull(getCommand("trust")).setExecutor(trustCommand);
        Objects.requireNonNull(getCommand("Ability")).setExecutor(abilityCommand);

        // Listeners
        RelationshipDecay.start(this);

        // Event Handlers
        Bukkit.getPluginManager().registerEvents(new RelationshipEventHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new CustomEvents(), this);
    }

    @Override
    public void onDisable() {
        logger.info("Emotions are flowing away...");
    }

    public static GlintSMP getInstance() {
        if (instance == null)
            throw new IllegalStateException("Plugin instance is not initialized yet!");
        return instance;
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(getInstance(), delay);
    }
}
