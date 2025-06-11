package com.github.aoideveloper.simpleEconomy

import com.github.aoideveloper.simpleEconomy.api.EconomyServiceImpl
import com.github.aoideveloper.simpleEconomy.database.DatabaseManager
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class SimpleEconomy : JavaPlugin() {
    private lateinit var databaseManager: DatabaseManager

    companion object {
        lateinit var service: EconomyServiceImpl
        lateinit var plugin: Plugin
    }

    override fun onEnable() {
        plugin = this

        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        databaseManager = DatabaseManager(dataFolder)
        databaseManager.connect()

        service = EconomyServiceImpl()
        service.loadAllBalances()

        logger.info("SimpleEconomy has been enabled.")
    }

    override fun onDisable() {
        service.saveAllBalances()

        logger.info("SimpleEconomy has been disabled.")
    }
}
