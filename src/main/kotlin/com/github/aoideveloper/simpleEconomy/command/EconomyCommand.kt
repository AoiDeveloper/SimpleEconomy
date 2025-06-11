package com.github.aoideveloper.simpleEconomy.command

import com.github.aoideveloper.simpleEconomy.SimpleEconomy
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * デバッグ用に実装したコマンドです。
 */
object EconomyCommand: CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>?
    ): Boolean {
        if(sender !is Player) return false
        if(args?.size != 1) return false
        val amount = args[0]?.toDoubleOrNull() ?: return false
        SimpleEconomy.service.deposit(sender.uniqueId, amount)
        sender.sendMessage("$amount 追加しました。")
        return true
    }
}