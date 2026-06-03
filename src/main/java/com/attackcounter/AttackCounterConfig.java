package com.attackcounter;

import java.awt.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup("attackcounter")
public interface AttackCounterConfig extends Config
{
	@ConfigSection(
		name = "Overlay",
		description = "Overlay appearance settings",
		position = 0
	)
	String OVERLAY_SECTION = "overlay";

	@ConfigSection(
		name = "Tracking",
		description = "NPC tracking settings",
		position = 1
	)
	String TRACKING_SECTION = "tracking";

	@ConfigItem(
		position = 0,
		keyName = "enableOverlay",
		name = "Enable Overlay",
		description = "Show the attack cycle pip overlay",
		section = OVERLAY_SECTION
	)
	default boolean enableOverlay()
	{
		return true;
	}

	@ConfigItem(
		position = 1,
		keyName = "pipColor",
		name = "Pip Color",
		description = "Color of the filled pips",
		section = OVERLAY_SECTION
	)
	default Color pipColor()
	{
		return new Color(201, 161, 28);
	}

	@ConfigItem(
		position = 2,
		keyName = "showEmptyPips",
		name = "Show Empty Pips",
		description = "Show unfilled pips for attacks not yet made in the current cycle",
		section = OVERLAY_SECTION
	)
	default boolean showEmptyPips()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = "heightOffset",
		name = "Height Offset",
		description = "Vertical offset for the pip overlay relative to the player",
		section = OVERLAY_SECTION
	)
	@Range(min = -100, max = 100)
	default int heightOffset()
	{
		return 0;
	}

	@ConfigItem(
		position = 0,
		keyName = "trackingMode",
		name = "Tracking Mode",
		description = "All NPCs: track any NPC you attack. Custom: only track specified NPC IDs.",
		section = TRACKING_SECTION
	)
	default TrackingMode trackingMode()
	{
		return TrackingMode.ALL;
	}

	@ConfigItem(
		position = 1,
		keyName = "npcIds",
		name = "NPC IDs",
		description = "Comma-separated list of NPC IDs to track (only used in Custom mode)",
		section = TRACKING_SECTION
	)
	default String npcIds()
	{
		return "";
	}

	@ConfigItem(
		position = 2,
		keyName = "cycleLength",
		name = "Cycle Length",
		description = "Number of attacks in one cycle before the counter resets",
		section = TRACKING_SECTION
	)
	@Range(min = 1, max = 20)
	default int cycleLength()
	{
		return 6;
	}

	@Getter
	@AllArgsConstructor
	enum TrackingMode
	{
		ALL("All NPCs"),
		CUSTOM("Custom");

		private final String name;

		@Override
		public String toString()
		{
			return name;
		}
	}
}