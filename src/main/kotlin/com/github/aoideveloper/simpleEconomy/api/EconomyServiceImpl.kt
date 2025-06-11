package com.github.aoideveloper.simpleEconomy.api

import java.util.UUID

class EconomyServiceImpl : EconomyAPI<UUID, Double> {
    private val balances = mutableMapOf<UUID, Double>()

    override fun getBalance(accountId: UUID): Double {
        return balances.getOrDefault(accountId, 0.0)
    }

    override fun deposit(
        accountId: UUID,
        amount: Double,
    ): Double {
        if (amount < 0) {
            throw IllegalArgumentException("Deposit amount must be non-negative.")
        }
        val newBalance = balances.getOrDefault(accountId, 0.0) + amount
        balances[accountId] = newBalance
        return newBalance
    }

    override fun withdraw(
        accountId: UUID,
        amount: Double,
    ): Boolean {
        // 1. 不正な値のチェック (Validation)
        if (amount < 0) {
            throw IllegalArgumentException("Withdrawal amount must be non-negative.")
        }

        // 2. 現在の残高を取得
        val currentBalance = getBalance(accountId) // 内部のgetBalanceを使えばOK

        // 3. 残高が十分かチェック
        if (currentBalance < amount) {
            return false // 残高不足なら失敗
        }

        // 4. 残高を更新
        balances[accountId] = currentBalance - amount

        // 5. 成功を返す
        return true
    }

    override fun transfer(
        senderId: UUID,
        recipientId: UUID,
        amount: Double,
    ): Boolean {
        if (amount < 0) {
            throw IllegalArgumentException("Transfer amount must be non-negative.")
        }
        if (senderId == recipientId) {
            throw IllegalArgumentException("Sender and recipient cannot be the same.")
        }
        if (amount == 0.0) {
            return true
        }

        val withdrawalSuccess = withdraw(senderId, amount)

        if (withdrawalSuccess) {
            deposit(recipientId, amount)
            return true
        }
        return false
    }
}
