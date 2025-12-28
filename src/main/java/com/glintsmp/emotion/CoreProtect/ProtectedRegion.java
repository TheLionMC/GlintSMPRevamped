package com.glintsmp.emotion.CoreProtect;

import org.bukkit.World;
import org.bukkit.util.BoundingBox;

public record ProtectedRegion(World world, BoundingBox boundingBox) {
}
