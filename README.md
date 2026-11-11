# Dragon Egg Champion Minecraft Plugin

Dragon Egg Champion is a Paper Minecraft plugin that grants players carrying a dragon egg Strength II, Regeneration I, Resistance II, Speed II, and Saturation I, and announces the egg holder in chat every 2 minutes and 30 seconds.

## Install

1. Build the plugin with Java 21 and Maven by running `mvn package`.
2. Copy `target/plugin.jar` into your Paper server's `plugins` folder.
3. Restart the server.

## Use

1. Place a dragon egg in your player inventory to receive its potion effects. The plugin checks your inventory, not only your hand.
2. Keep the egg in your inventory to maintain the effects.
3. The server announces who has the egg every 2 minutes and 30 seconds. If nobody has it, chat says so.

The plugin blocks cursor and drag placement of the egg into an ender chest. Shift-click transfers from a player's inventory are not blocked by the current implementation.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/bf2a9b33-de01-4bda-81b3-6bf2831cb72f?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=bf2a9b33-de01-4bda-81b3-6bf2831cb72f
