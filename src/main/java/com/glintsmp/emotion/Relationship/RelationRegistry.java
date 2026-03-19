package com.glintsmp.emotion.Relationship;

import com.glintsmp.emotion.GlintSMP;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class RelationRegistry {

    public static void registerAll() {
        Reflections reflections = new Reflections("com.glintsmp.emotion.Relationship.Listeners");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RelationListener.class);

        for (Class<?> clazz : classes) {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Object created = constructor.newInstance();

                if (created instanceof Listener listener)
                    Bukkit.getPluginManager().registerEvents(listener, GlintSMP.getInstance());

                try {
                    Method method = clazz.getDeclaredMethod("init");
                    method.invoke(created);
                } catch (NoSuchMethodException ignore) {}
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
