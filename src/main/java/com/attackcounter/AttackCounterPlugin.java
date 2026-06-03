package com.attackcounter;

import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Actor;
import net.runelite.api.Hitsplat;
import net.runelite.api.NPC;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Attack Counter",
	description = "Displays a pip overlay tracking attacks in a configurable cycle"
)
public class AttackCounterPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AttackCounterOverlay overlay;

	@Inject
	private AttackCounterConfig config;

	@Getter
	private int attacksInCycle = 0;

	@Getter
	private boolean inFight = false;

	@Provides
	AttackCounterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AttackCounterConfig.class);
	}

	@Override
	protected void startUp()
	{
		attacksInCycle = 0;
		inFight = false;
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		attacksInCycle = 0;
		inFight = false;
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
	{
		final Actor actor = hitsplatApplied.getActor();
		if (!(actor instanceof NPC))
		{
			return;
		}

		final Hitsplat hitsplat = hitsplatApplied.getHitsplat();
		if (!hitsplat.isMine())
		{
			return;
		}

		if (config.trackingMode() == AttackCounterConfig.TrackingMode.CUSTOM)
		{
			final Set<Integer> trackedIds = parseNpcIds(config.npcIds());
			if (trackedIds.isEmpty() || !trackedIds.contains(((NPC) actor).getId()))
			{
				return;
			}
		}

		inFight = true;
		attacksInCycle++;

		if (attacksInCycle >= config.cycleLength())
		{
			attacksInCycle = 0;
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		if (config.trackingMode() == AttackCounterConfig.TrackingMode.CUSTOM)
		{
			final Set<Integer> trackedIds = parseNpcIds(config.npcIds());
			if (trackedIds.contains(npcDespawned.getNpc().getId()))
			{
				attacksInCycle = 0;
				inFight = false;
			}
		}
		else
		{
			if (inFight)
			{
				attacksInCycle = 0;
				inFight = false;
			}
		}
	}

	private Set<Integer> parseNpcIds(String raw)
	{
		if (raw == null || raw.trim().isEmpty())
		{
			return Collections.emptySet();
		}
		try
		{
			return Arrays.stream(raw.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.map(Integer::parseInt)
				.collect(Collectors.toSet());
		}
		catch (NumberFormatException e)
		{
			return Collections.emptySet();
		}
	}
}
