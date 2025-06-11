package com.github.aoideveloper.simpleEconomy

import com.github.aoideveloper.simpleEconomy.api.EconomyServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.random.Random

class EconomyServiceTest {
    @Test
    fun `口座の初期残高は0`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val id = UUID.randomUUID()

        // Act
        val balance = economyService.getBalance(id)

        // Assert
        assertEquals(balance, 0.0)
    }

    @Test
    fun `口座に300を2回入金した場合、残高が600になる`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val id = UUID.randomUUID()

        // Act
        economyService.deposit(id, 300.0)
        economyService.deposit(id, 300.0)
        val balance = economyService.getBalance(id)

        // Assert
        assertEquals(balance, 600.0)
    }

    @Test
    fun `預金額に負の値を指定した場合、IllegalArgumentExceptionをスローすること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        val negativeAmount = Random.nextDouble(-300.0, -0.1)

        // Act
        val exception =
            assertThrows<IllegalArgumentException> {
                economyService.deposit(playerId, negativeAmount)
            }

        // Assert
        assertEquals("Deposit amount must be non-negative.", exception.message)

        // Act
        // 例外がスローされた後、残高が変動していないことを確認
        val balance = economyService.getBalance(playerId)
        // Assert
        assertEquals(0.0, balance, "不正な預金操作の後、残高は変動してはならない")
    }

    @Test
    fun `十分な残高がある場合、引き出しに成功し残高が減少すること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        economyService.deposit(playerId, 1000.0) // 事前に1000円入金

        // Act
        val result = economyService.withdraw(playerId, 200.0) // 200円引き出す

        // Assert
        assertTrue(result, "引き出しは成功するはず")
        assertEquals(800.0, economyService.getBalance(playerId), "残高は800になるはず")
    }

    @Test
    fun `残高不足の場合、引き出しに失敗し残高が変動しないこと`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        economyService.deposit(playerId, 100.0) // 事前に100円入金

        // Act
        val result = economyService.withdraw(playerId, 500.0) // 500円引き出そうとする

        // Assert
        assertFalse(result, "残高不足のため引き出しは失敗するはず")
        assertEquals(100.0, economyService.getBalance(playerId), "残高は変動しないはず")
    }

    @Test
    fun `引き出し額に負の値を指定した場合、IllegalArgumentExceptionをスローすること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        economyService.deposit(playerId, 1000.0)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            economyService.withdraw(playerId, -100.0)
        }

        // Assert
        assertEquals(1000.0, economyService.getBalance(playerId), "残高は変動しないはず")
    }

    @Test
    fun `残高全額を引き出した場合、成功し残高が0になること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        economyService.deposit(playerId, 500.0)

        // Act
        val result = economyService.withdraw(playerId, 500.0)

        // Assert
        assertTrue(result)
        assertEquals(0.0, economyService.getBalance(playerId))
    }

    @Test
    fun `0を引き出した場合、成功し残高は変動しないこと`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        economyService.deposit(playerId, 1000.0)

        // Act
        val result = economyService.withdraw(playerId, 0.0)

        // Assert
        assertTrue(result)
        assertEquals(1000.0, economyService.getBalance(playerId))
    }

    @Test
    fun `残高が十分な場合、送金に成功し両者の残高が正しく更新されること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        economyService.deposit(senderId, 1000.0) // 送金元に1000円

        // Act
        val result = economyService.transfer(senderId, recipientId, 300.0)

        // Assert
        assertTrue(result, "送金は成功するはず")
        assertEquals(700.0, economyService.getBalance(senderId), "送金元の残高は700になるはず")
        assertEquals(300.0, economyService.getBalance(recipientId), "受取先の残高は300になるはず")
    }

    @Test
    fun `送金元の残高が不足している場合、送金に失敗し両者の残高が変動しないこと`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        economyService.deposit(senderId, 100.0) // 送金元に100円

        // Act
        val result = economyService.transfer(senderId, recipientId, 500.0)

        // Assert
        assertFalse(result, "残高不足のため送金は失敗するはず")
        assertEquals(100.0, economyService.getBalance(senderId), "送金元の残高は変動しないはず")
        assertEquals(0.0, economyService.getBalance(recipientId), "受取先の残高も変動しないはず")
    }

    @Test
    fun `自分自身に送金しようとした場合、IllegalArgumentExceptionをスローすること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val playerId = UUID.randomUUID()
        economyService.deposit(playerId, 1000.0)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            economyService.transfer(playerId, playerId, 100.0)
        }
        assertEquals(1000.0, economyService.getBalance(playerId), "残高は変動しないはず")
    }

    @Test
    fun `送金額に負の値を指定した場合、IllegalArgumentExceptionをスローすること`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        economyService.deposit(senderId, 1000.0)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            economyService.transfer(senderId, recipientId, -100.0)
        }
    }

    @Test
    fun `0を送金した場合、成功を返し両者の残高は変動しないこと`() {
        // Arrange
        val economyService = EconomyServiceImpl()
        val senderId = UUID.randomUUID()
        val recipientId = UUID.randomUUID()
        economyService.deposit(senderId, 1000.0)

        // Act
        val result = economyService.transfer(senderId, recipientId, 0.0)

        // Assert
        assertTrue(result)
        assertEquals(1000.0, economyService.getBalance(senderId))
        assertEquals(0.0, economyService.getBalance(recipientId))
    }
}
