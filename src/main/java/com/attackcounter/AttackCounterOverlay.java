package com.attackcounter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Singleton
public class AttackCounterOverlay extends Overlay
{
	private static final int PIP_SIZE = 5;
	private static final int PIP_GAP = 3;
	private static final int EMPTY_PIP_ALPHA = 60;

	private final Client client;
	private final AttackCounterPlugin plugin;
	private final AttackCounterConfig config;

	@Inject
	public AttackCounterOverlay(Client client, AttackCounterPlugin plugin, AttackCounterConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.enableOverlay())
		{
			return null;
		}

		if (!plugin.isInFight())
		{
			return null;
		}

		if (client.getLocalPlayer() == null)
		{
			return null;
		}

		final int height = client.getLocalPlayer().getLogicalHeight() + config.heightOffset() + 5;
		final LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
		if (localLocation == null)
		{
			return null;
		}

		final Point canvasPoint = Perspective.localToCanvas(
			client,
			localLocation,
			client.getTopLevelWorldView().getPlane(),
			height
		);
		if (canvasPoint == null)
		{
			return null;
		}

		final int cycleLength = config.cycleLength();
		final int totalWidth = cycleLength * PIP_SIZE + (cycleLength - 1) * PIP_GAP;
		final int startX = canvasPoint.getX() - totalWidth / 2;
		final int startY = canvasPoint.getY() - PIP_SIZE - 4;

		final int filled = plugin.getAttacksInCycle();
		final Color pipColor = config.pipColor();
		final Color emptyColor = new Color(
			pipColor.getRed(),
			pipColor.getGreen(),
			pipColor.getBlue(),
			EMPTY_PIP_ALPHA
		);

		for (int i = 0; i < cycleLength; i++)
		{
			final boolean isFilled = i < filled;

			if (!isFilled && !config.showEmptyPips())
			{
				continue;
			}

			graphics.setColor(isFilled ? pipColor : emptyColor);
			graphics.fillOval(
				startX + i * (PIP_SIZE + PIP_GAP),
				startY,
				PIP_SIZE,
				PIP_SIZE
			);
		}

		return null;
	}
}
