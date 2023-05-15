package io.iohk.atala.swetest
package models

import utils.Base.{Nonce, Number}
import utils.Sha256

/**
 * This is the fundamental building block, which we use to build the chain, and holds many transactions.
 * Each block links back to the previous (parent) block and the block at index 0 is called the Genesis block.
 *
 * @param index              The index or position of the block in the chain
 * @param parentHash         The hash of the parent block
 * @param transactions       The transactions included in the block
 * @param miningTargetNumber The target number used in the PoW algorithm
 * @param nonce              A random value used in the PoW algorithm
 */
case class Block(
                  index: Int,
                  parentHash: Hash,
                  transactions: Seq[Transaction],
                  miningTargetNumber: Number,
                  nonce: Nonce,
                ) {

  /**
   * Computes the cryptographic hash of the block.
   * The cryptographic hash is calculated by combining the block components,
   * including the index, parent hash, transactions, mining target number, and nonce.
   *
   * @return The cryptographic hash of the block.
   */
  def cryptoHash: Hash = {
    val indexBytes = index.toString.getBytes
    val transactionBytes = transactions.flatMap(_.data.getBytes)
    val combinedBytes = indexBytes ++ parentHash.bytes ++ transactionBytes ++ miningTargetNumber.toByteArray ++ nonce.toString.getBytes
    Sha256(combinedBytes)
  }

  /**
   * Verifies that the block has been mined properly.
   * The essence of the PoW algorithm is that the computed hash of the block should be less than the mining target number.
   *
   * @throws BlockNotMinedProperlyException if the block's hash is not less than the mining target number.
   */
  def verifyThisHasBeenMinedProperly(): Unit =
    if (cryptoHash.toNumber >= miningTargetNumber) {
      throw BlockNotMinedProperlyException(index)
    }

}

object Block {
  /**
   * Initializes an empty block with the specified index. Useful for tests and experiments
   *
   * @param index The index of the block
   * @return A new empty block
   */
  def initEmptyBlock(index: Int): Block = Block(index, Hash(Array[Byte](0)), Seq.empty, BigInt(0), 0)
}

/**
 * A custom exception thrown when a block has not been properly mined.
 *
 * @param blockIndex The index of the block involved.
 */
case class BlockNotMinedProperlyException(blockIndex: Int) extends Exception(s"Block at index $blockIndex has not been mined properly.")



