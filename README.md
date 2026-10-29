# c00l gui v1.2.0 — source project

A small original Minecraft Java client-style ClickGUI project targeting NeoForge 1.21.11.

## In this ZIP
- Custom dark/cyan ClickGUI opened with Right Shift.
- Toggles for low graphics settings, minimal particles, entity culling, and FPS/RAM HUD.
- UI toggles reserved for Fullbright, Entity Glow, and Ore ESP prototypes.
- Low graphics preset is applied once on world join: 2-chunk render/simulation distance, Fast graphics, clouds off, ambient occlusion off.

## Important status
**This ZIP contains source code only. No compiled JAR has been built or tested.**
The Fullbright, Entity Glow, and Ore ESP switches are UI prototypes only in this version; they do not yet implement their visual effects. Ore ESP's intended future design is to inspect only already-loaded client chunks, never request or force-load chunks. A future implementation must also avoid scanning the entire loaded world every frame.

The build may need API adjustments after running Gradle because the project has not been compiled in this environment. It requires network access to resolve Gradle, Minecraft, and NeoForge dependencies.

## Build
Use a 64-bit Java 21 JDK and Gradle. From this directory run:

    gradle build

If successful, the JAR appears under `build/libs/`.

## Install
Copy the resulting JAR to the `mods` folder for a NeoForge 1.21.11 profile. Test in a separate profile/world first.

## Low-memory suggestions for Galaxy A03 Core
- Try a 768–1024 MiB Java heap if Pojav permits; increase only if Java heap errors require it.
- Render distance 2, simulation distance 2, minimal particles, clouds off.
- Close background apps before launch.
- These settings do not guarantee smooth performance on 2 GB RAM.
