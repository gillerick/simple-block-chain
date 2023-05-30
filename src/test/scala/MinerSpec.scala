package io.iohk.atala.swetest

import models.Transaction
import utils.Sha256

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers

class MinerSpec extends AnyFlatSpec with Matchers {
  "Miner" should "create a valid Genesis block" in {
    val genesisBlock = Miner.Genesis

    //    Verify the correctness of the Genesis properties
    genesisBlock must have(
      Symbol("index")(0),
      Symbol("parentHash")(Sha256.ZeroHash),
      Symbol("miningTargetNumber")(Miner.StdMiningTargetNumber),
      Symbol("transactions")(List(Transaction("Hello Blockchain, this is Genesis :)"))),
    )

    //    Verify the correctness of the mining
    noException should be thrownBy genesisBlock.verifyThisHasBeenMinedProperly()
  }

  it should "mine the next block with a valid nonce" in {
    val index = 1
    val parentHash = Sha256.ZeroHash
    val transactions = Seq(Transaction("Transaction 1"), Transaction("Transaction 2"))
    val miningTargetNumber = Miner.StdMiningTargetNumber

    val block = Miner.mineNextBlock(index, parentHash, transactions, miningTargetNumber)

    block must have(
      Symbol("index")(index),
      Symbol("parentHash")(parentHash),
      Symbol("transactions")(transactions),
      Symbol("miningTargetNumber")(miningTargetNumber),
    )

    noException should be thrownBy block.verifyThisHasBeenMinedProperly()
  }
}
