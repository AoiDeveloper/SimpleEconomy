package com.github.aoideveloper.simpleEconomy.database

import org.jetbrains.exposed.v1.core.Table

object AccountBalances : Table("balances") {
    val accountId = uuid("account_id")
    val balance = double("balance")

    override val primaryKey = PrimaryKey(accountId)
}