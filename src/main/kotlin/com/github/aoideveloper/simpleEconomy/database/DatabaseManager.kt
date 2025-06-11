package com.github.aoideveloper.simpleEconomy.database

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File

class DatabaseManager(private val dataFolder: File) {

    fun connect() {
        // プラグインのデータフォルダに `economy.db` という名前でデータベースファイルを作成
        val dbFile = File(dataFolder, "economy.db")

        Database.connect("jdbc:sqlite:${dbFile.absolutePath}", "org.sqlite.JDBC")

        // トランザクション内でテーブルを作成
        transaction {
            SchemaUtils.create(AccountBalances)
        }
    }
}
