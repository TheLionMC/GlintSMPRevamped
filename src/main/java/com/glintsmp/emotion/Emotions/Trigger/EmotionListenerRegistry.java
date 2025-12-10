package com.glintsmp.emotion.Emotions.Trigger;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class EmotionListenerRegistry {

    public static void registerAll() {
        Reflections reflections = new Reflections("com.glintsmp.emotion.Emotions.Trigger.Listeners");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(EmotionListener.class);

        for (Class<?> clazz : classes) {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Object created = constructor.newInstance();

                if (created instanceof Listener listener)
                    Bukkit.getPluginManager().registerEvents(listener, GlintSMP.getInstance());
                else GlintSMP.logger.warning("Class is not a listener: " + clazz.getSimpleName());

            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
