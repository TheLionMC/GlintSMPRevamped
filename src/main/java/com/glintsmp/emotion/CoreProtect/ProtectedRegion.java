package com.glintsmp.emotion.CoreProtect;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.List;

public record ProtectedRegion(World world, BoundingBox boundingBox, List<Material> whitelist) {
}
