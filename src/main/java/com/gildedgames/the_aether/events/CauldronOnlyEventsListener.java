/*
 * Copyright 2015-2020 Rivoreo
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 */

package com.gildedgames.the_aether.events;

import com.gildedgames.the_aether.world.AetherWorldProvider;
import com.gildedgames.the_aether.Aether;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CauldronOnlyEventsListener {
	public CauldronOnlyEventsListener() {
		log = LogManager.getLogger();
	}

	private Logger log;

	@SubscribeEvent
	public void on_world_load(WorldEvent.Load event) {
		World world = event.world;
		if(!(world.provider instanceof AetherWorldProvider)) return;

		WorldInfo wi = world.getWorldInfo();
		int world_id = world.provider.dimensionId;
		if(!wi.getWorldName().equals("DIM" + String.valueOf(world_id))) return;

		log.warn("Unloading Aether world to workaround Cauldron world name bug");
		try {
			Class<?> bukkit_world_class = Class.forName("org.bukkit.World");
			Method get_world_method = World.class.getDeclaredMethod("getWorld");
			Object craftbukkit_world = get_world_method.invoke(world);
			if(!bukkit_world_class.isInstance(craftbukkit_world)) {
				throw new ReflectiveOperationException(craftbukkit_world.getClass().getName() + " is not compatible with org.bukkit.World");
			}
			Field server_field = MinecraftServer.class.getDeclaredField("server");
			Class<?> craftserver_class = server_field.getType();
			String craftserver_class_name = craftserver_class.getName();
			if(!craftserver_class.getName().endsWith(".CraftServer")) {
				throw new ReflectiveOperationException("class name mismatch: " + craftserver_class_name);
			}
			Object craftserver = server_field.get(FMLCommonHandler.instance().getMinecraftServerInstance());
			Method unload_world_method = craftserver_class.getDeclaredMethod("unloadWorld", bukkit_world_class, boolean.class);
			unload_world_method.invoke(craftserver, craftbukkit_world, false);
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
			net.minecraftforge.common.DimensionManager.unloadWorld(world_id);
		}
	}
}
