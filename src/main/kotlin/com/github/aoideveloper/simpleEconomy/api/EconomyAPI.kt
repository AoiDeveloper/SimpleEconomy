package com.github.aoideveloper.simpleEconomy.api

interface EconomyAPI<IdentifierType, NumberType> {
    /** 指定された口座の残高を返します。 **/
    fun getBalance(accountId: IdentifierType): NumberType

    /** 指定された口座にamountだけ振り込み、振込後の残高を返します。 **/
    fun deposit(
        accountId: IdentifierType,
        amount: NumberType,
    ): NumberType

    /** 指定された口座から、amountだけ引き出します。もし残高がamountよりも少ない場合、引き出しは実行されません。引き出しに成功したかを返します。 **/
    fun withdraw(
        accountId: IdentifierType,
        amount: NumberType,
    ): Boolean

    /** 送り元の口座から、送り先の口座にamountだけ移動させます。送り元の口座の残高がamountよりも少ない場合には、移動は実行されません。移動に成功したかを返します。 **/
    fun transfer(
        senderId: IdentifierType,
        recipientId: IdentifierType,
        amount: NumberType,
    ): Boolean
}
