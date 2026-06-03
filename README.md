# Attack Counter

A RuneLite plugin that displays a pip overlay above your player tracking attacks in a configurable cycle.

## Features

- Pip overlay rendered above the player
- Configurable cycle length (default 6)
- Two tracking modes:
  - **All NPCs** — tracks any NPC you attack
  - **Custom** — only tracks specified NPC IDs (comma-separated)
- Configurable pip colour (default = amber)
- Toggle empty pip visibility
- Height offset adjustment to avoid overlap with other overlays
- Resets automatically on NPC despawn

## Usage

Enable the plugin and set your preferred tracking mode in the config panel. For specific NPCs, switch to **Custom** mode and enter the relevant NPC IDs.

## Configuration

| Option | Description | Default |
|--------|-------------|---------|
| Enable Overlay | Show/hide the overlay | On |
| Pip Color | Colour of filled pips | Amber |
| Show Empty Pips | Show unfilled pips | On |
| Height Offset | Vertical position adjustment | 0 |
| Tracking Mode | All NPCs or Custom | All NPCs |
| NPC IDs | Comma-separated IDs (Custom mode) | — |
| Cycle Length | Number of attacks before reset | 6 |
