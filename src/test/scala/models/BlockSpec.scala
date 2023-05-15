package io.iohk.atala.swetest
package models


import utils.Base._
import utils.Sha256

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers


class BlockSpec extends AnyFlatSpec with Matchers {

  val transaction1: Transaction = Transaction("Some random message")
  val transaction2: Transaction = Transaction("Another random message")
  val transaction3: Transaction = Transaction("A third random message")
  val transactions: Seq[Transaction] = Seq(transaction1, transaction2, transaction3)

  val index = 1
  val parentHash: Hash = Hash(Array[Byte](0, 0, 0, 0))
  val miningTargetNumber = Number(100)
  val nonce = 12345L

  "cryptoHash" should "compute the correct cryptographic hash" in {
    val block = Block(index, parentHash, transactions, miningTargetNumber, nonce)
    val expectedHash = Sha256(
      index.toString.getBytes,
      parentHash.bytes,
      transaction1.data.getBytes,
      transaction2.data.getBytes,
      transaction3.data.getBytes,
      miningTargetNumber.toByteArray,
      nonce.toString.getBytes
    )
    block.cryptoHash.bytes mustEqual expectedHash.bytes
  }

  "verifyThisHasBeenMinedProperly" should "not throw an exception if the block has been mined properly" in {
    val index = 1
    val parentHash = Sha256.ZeroHash
    val transactions = Seq(Transaction("Transaction 1"))
    val miningTargetNumber = Miner.StdMiningTargetNumber
    val block = Miner.mineNextBlock(index, parentHash, transactions, miningTargetNumber)


    noException should be thrownBy block.verifyThisHasBeenMinedProperly()
  }

  it should "throw an AssertionError if the block's hash is greater than or equal to the mining target number" in {
    val invalidMiningTargetNumber = Number(10)
    val block = Block(index, parentHash, transactions, invalidMiningTargetNumber, nonce)
    assertThrows[BlockNotMinedProperlyException](block.verifyThisHasBeenMinedProperly())
  }
}

