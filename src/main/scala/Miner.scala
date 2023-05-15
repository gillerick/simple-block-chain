package io.iohk.atala.swetest

import models.{Block, Hash, Transaction}
import utils.Base._
import utils.Sha256


object Miner {
  final val StdMiningTargetNumber = targetByLeadingZeros(1)

  final val Genesis = Miner.mineNextBlock(
    index = 0,
    parentHash = Sha256.ZeroHash,
    transactions = Seq(Transaction("Hello Blockchain, this is Genesis :)")),
    StdMiningTargetNumber,
  )

  def targetByLeadingZeros(zeros: Int): BigInt = {
    require(zeros < Sha256.NumberOfBytes)

    val bytes: Bytes =
      Array.tabulate[Byte](32) { n =>
        if (n < zeros) {
          0
        }
        else {
          0xff.toByte
        }
      }

    BigInt(1, bytes)
  }

  def mineNextBlock(
                     index: Int,
                     parentHash: Hash,
                     transactions: Seq[Transaction],
                     miningTargetNumber: BigInt,
                   ): Block = {
    def findValidNonce(nonce: Nonce): Nonce = {
      val block = models.Block(index, parentHash, transactions, miningTargetNumber, nonce)
      if (block.cryptoHash.toNumber < miningTargetNumber) nonce else findValidNonce(nonce + 1)
    }

    val validNonce = findValidNonce(0)
    models.Block(index, parentHash, transactions, miningTargetNumber, validNonce)
  }
}
