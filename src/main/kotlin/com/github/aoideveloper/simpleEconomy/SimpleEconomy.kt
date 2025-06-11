package com.github.aoideveloper.simpleEconomy

import com.github.aoideveloper.simpleEconomy.api.EconomyServiceImpl
import org.bukkit.plugin.java.JavaPlugin

class SimpleEconomy : JavaPlugin() {
    companion object {
        val instance = EconomyServiceImpl()
    }

    override fun onEnable() {
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
