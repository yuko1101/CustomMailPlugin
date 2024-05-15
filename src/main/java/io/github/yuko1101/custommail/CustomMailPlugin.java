package io.github.yuko1101.custommail;

import emu.lunarcore.LunarCore;
import emu.lunarcore.command.Command;
import emu.lunarcore.plugin.Plugin;
import org.slf4j.Logger;

import java.io.File;
import java.net.URLClassLoader;

@SuppressWarnings("unused")
public class CustomMailPlugin extends Plugin {
    public CustomMailPlugin(Plugin.Identifier identifier, URLClassLoader classLoader, File dataFolder, Logger logger) {
        super(identifier, classLoader, dataFolder, logger);
    }

    public void onEnable() {
        LunarCore.getCommandManager().registerCommand(new CustomMailCommand());
    }

    public void onDisable() {
        LunarCore.getCommandManager().unregisterCommand(CustomMailCommand.class.getAnnotation(Command.class).label());
    }
}