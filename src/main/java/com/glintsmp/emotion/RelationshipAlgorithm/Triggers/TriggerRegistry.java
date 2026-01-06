package com.glintsmp.emotion.RelationshipAlgorithm.Triggers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Instantiates all Trigger subclasses so they are referenced and available at runtime.
 * This also provides a small lookup for triggers by id.
 */
public final class TriggerRegistry {
    private static final Map<String, Trigger> REGISTRY = new HashMap<>();

    private TriggerRegistry() {}

    public static void registerAll() {
        REGISTRY.clear();

        // Positive triggers (match files)
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Bonding());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.BuildingTogether());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Celebrating());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Complimenting());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.CookTogether());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.CoveringFire());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.ExploreTogether());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.FarmingTogether());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.FightingWithEachOther());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.GiftExchange());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Gifting());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Guiding());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.HelpingHand());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.LearningTogether());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Mentoring());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.PlanTogether());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Rescue());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.SharedQuest());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.SharedVictory());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.SharingKnowledge());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Sparring());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Supporting());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Teaching());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Teamwork());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Positive.Trading());

        // Negative triggers (match files)
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Abandoning());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Arson());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Backstabbing());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Backtalk());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Betrayal());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.BlockingHelp());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Cheating());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Corruption());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.DenyingAid());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Disturbing());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Excluding());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Exploiting());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.FriendlyFire());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Griefing());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Harassment());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Hoarding());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Ignoring());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Insulting());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.KillingEachOther());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Lying());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Mocking());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Pickpocket());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Poisoning());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.RuiningBuild());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Rumors());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Sabotaging());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Scamming());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Stealing());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Threatening());
        add(new com.glintsmp.emotion.RelationshipAlgorithm.Triggers.Triggers.Negative.Vandalism());
    }

    private static void add(Trigger t) {
        if (t == null) return;
        REGISTRY.put(t.getId(), t);
    }

    public static Trigger getById(String id) {
        return REGISTRY.get(id);
    }

    public static Collection<Trigger> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
