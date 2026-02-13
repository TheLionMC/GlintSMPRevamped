package com.glintsmp.emotion;

import com.glintsmp.emotion.Commands.Command;
import com.glintsmp.emotion.Commands.Commands.Ability.AbilityActivate;
import com.glintsmp.emotion.Commands.Commands.CoreProtect.ProtectCommand;
import com.glintsmp.emotion.Commands.Commands.CoreProtect.ProtectPos1;
import com.glintsmp.emotion.Commands.Commands.CoreProtect.ProtectPos2;
import com.glintsmp.emotion.Commands.Commands.Emotion.DecreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Emotion.IncreaseEmotion;
import com.glintsmp.emotion.Commands.Commands.Ghost.GhostWorldCommand;
import com.glintsmp.emotion.Commands.Commands.Item.GiveItemCommand;
import com.glintsmp.emotion.Commands.Commands.Lives.SetLivesCommand;
import com.glintsmp.emotion.Commands.Commands.Lives.WithdrawCommand;
import com.glintsmp.emotion.Commands.Commands.Testing.PlMessageCommand;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustAdd;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustCheck;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustList;
import com.glintsmp.emotion.Commands.Commands.Trust.TrustRemove;
import com.glintsmp.emotion.CoreProtect.CoreProtectManager;
import com.glintsmp.emotion.Emotions.Trigger.EmotionListenerRegistry;
import com.glintsmp.emotion.Ghost.GhostManager;
import com.glintsmp.emotion.Item.ItemManager;
import com.glintsmp.emotion.Listeners.CustomEvents;
import com.glintsmp.emotion.Listeners.PlayerDeathListener;
import com.glintsmp.emotion.Managers.*;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipDecay;
import com.glintsmp.emotion.RelationshipAlgorithm.RelationshipEventHandler;
import com.glintsmp.emotion.RelationshipAlgorithm.Triggers.TriggerEventHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

public final class GlintSMP extends JavaPlugin {

    public static final MiniMessage miniMessage = MiniMessage.builder().build();

    public static final String CHANNEL = "glint:channel";

    public static Logger logger;
    private static GlintSMP instance;

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.info("Emotions are running high!");
        instance = this;

        EmotionManager.initialize(this);
        TrustManager.initialize(this);
        ItemManager.initialize();
        RelationshipManager.initialize(this);
        ActionbarManager.initialize(this);
        LifeManager.initialize(this);
        CoreProtectManager.initialize(this);
        GhostManager.initialize();

        EmotionListenerRegistry.registerAll();

        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);

        // Commands
        Command emotionCommand = new Command();
        Command trustCommand = new Command();
        Command abilityCommand = new Command();
        Command coreCommand = new Command();
        Command ghostCommand = new Command();
        Command lifesCommand = new Command().setExecutor((s, c, l, a) -> {
            if (!(s instanceof Player player))
                return true;

            int lives = LifeManager.getLives(player.getUniqueId());
            player.sendMessage(Component.text("You currently have " + lives + " lives", TextColor.color(232, 49, 79)));
            return true;
        });

        Command itemCommand = new Command();
        Command testingCommand = new Command();

        //registering sub commands
        emotionCommand.registerSubCommand(new IncreaseEmotion());
        emotionCommand.registerSubCommand(new DecreaseEmotion());

        trustCommand.registerSubCommand(new TrustAdd());
        trustCommand.registerSubCommand(new TrustRemove());
        trustCommand.registerSubCommand(new TrustList());
        trustCommand.registerSubCommand(new TrustCheck());

        abilityCommand.registerSubCommand(new AbilityActivate());

        coreCommand.registerSubCommand(new ProtectCommand());
        coreCommand.registerSubCommand(new ProtectPos1());
        coreCommand.registerSubCommand(new ProtectPos2());

        ghostCommand.registerSubCommand(new GhostWorldCommand());

        lifesCommand.registerSubCommand(new WithdrawCommand());
        lifesCommand.registerSubCommand(new SetLivesCommand());

        itemCommand.registerSubCommand(new GiveItemCommand());

        testingCommand.registerSubCommand(new PlMessageCommand());

        //finally actually applying the registration
        Objects.requireNonNull(getCommand("Emotion")).setExecutor(emotionCommand);
        Objects.requireNonNull(getCommand("Trust")).setExecutor(trustCommand);
        Objects.requireNonNull(getCommand("Ability")).setExecutor(abilityCommand);
        Objects.requireNonNull(getCommand("Core")).setExecutor(coreCommand);
        Objects.requireNonNull(getCommand("Ghost")).setExecutor(ghostCommand);
        Objects.requireNonNull(getCommand("Life")).setExecutor(lifesCommand);
        Objects.requireNonNull(getCommand("Item")).setExecutor(itemCommand);
        Objects.requireNonNull(getCommand("Testing")).setExecutor(testingCommand);

        // Listeners
        RelationshipDecay.start(this);

        //Event Handlers
        Bukkit.getPluginManager().registerEvents(new RelationshipEventHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new CustomEvents(), this);
        Bukkit.getPluginManager().registerEvents(new TriggerEventHandler(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
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

    public static BukkitTask runTaskLater(Runnable runnable, long delay) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskLater(getInstance(), delay);
    }

    public static BukkitTask runTaskTimer(Function<Integer, Boolean> function, long delay, long period) {
        return new BukkitRunnable() {
            int tick = 0;
            @Override
            public void run() {
                if (!function.apply(tick++))
                    cancel();
            }
        }.runTaskTimer(getInstance(), delay, period);
    }

    public static BukkitTask runTaskAsynchronously(Runnable runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(getInstance());
    }
}
