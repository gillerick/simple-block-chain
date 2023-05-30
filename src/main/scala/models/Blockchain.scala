package io.iohk.atala.swetest
package models

import scala.collection.mutable

sealed trait Blockchain {
  /**
   * Appends a block to the blockchain
   *
   * @param block The new block to be appended
   */
  def append(block: Block): Unit

  /**
   * Finds a block by its index. The index of a block is the index of its parent plus one.
   *
   * @param index The index of the block to find.
   * @return An `Option` containing the block if found, or `None` if not found.
   */
  def findByIndex(index: Int): Option[Block]

  /**
   * Finds a block by its hash.
   *
   * @param hash The hash of the block to find.
   * @return An `Option` containing the block if found, or `None` if not found.
   */
  def findByHash(hash: Hash): Option[Block]

  /**
   * Finds a common ancestor block between this blockchain and another blockchain.
   *
   * @param that The other blockchain to compare with.
   * @return An `Option` containing the common ancestor block if found, or `None` if not found.
   */
  def commonAncestor(that: Blockchain): Option[Block]

  /**
   * Finds the number of blocks in the blockchain
   *
   * @return The size of the blockchain
   */
  def length: Int
}


/**
 * A fast blockchain implementation that uses an indexing data structure internally
 * to avoid traversing the linked list of blocks when answering queries like findByIndex.
 */
class FastBlockchain extends Blockchain {
  private val blockIndex: mutable.HashMap[Int, Block] = mutable.HashMap.empty
  private val blockHashes: mutable.HashMap[Number, Block] = mutable.HashMap.empty

  def append(block: Block): Unit = {
    blockIndex.put(block.index, block)
    blockHashes.put(block.cryptoHash.toNumber, block)
  }

  def findByIndex(index: Int): Option[Block] = blockIndex.get(index)

  def findByHash(hash: Hash): Option[Block] = blockHashes.get(hash.toNumber)

  def commonAncestor(that: Blockchain): Option[Block] = that match {
    case blockchain: FastBlockchain =>
      val commonIndices = blockIndex.keySet.intersect(blockchain.blockIndex.keySet)
      commonIndices.flatMap(index => blockIndex.get(index)).headOption
    case _ => None
  }

  def length: Int = blockIndex.size
}

/**
 * Unimplemented Blockchain. Mainly for the purposes of testing
 */
class GenericBlockchain extends Blockchain {
  override def append(block: Block): Unit = ???

  override def findByIndex(index: Int): Option[Block] = ???

  override def findByHash(hash: Hash): Option[Block] = ???

  override def commonAncestor(that: Blockchain): Option[Block] = ???

  override def length: Int = ???
}

