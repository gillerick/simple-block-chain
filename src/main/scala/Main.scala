package io.iohk.atala.swetest

import java.security.MessageDigest
import io.iohk.atala.swetest.Base._

import scala.annotation.tailrec
import scala.collection.mutable


///////////////////////////////////////////////////////////////////////
// "mini-chain" SWE coding challenge in Scala, version 5
//
// COPYRIGHT © IOHK
///////////////////////////////////////////////////////////////////////
//
// We will be using this to have a technical discussion.
// A few focused hours should be enough but ultimately it is up to you.
// We definitely do not want you to spend many days of your personal
// time.
//
// Remember, this is a representation of your skill and ultimately the
// first point of contact with our technical recruitment team.
// If you think that something is worth doing - it's probably
// a good idea to show it off (eg. error handling, sensible unit test coverage, formatting).
//
// You can think about this code as virtual collaboration with a
// colleague.
//
// General requirements are:
//
//  - Your submission is an SBT project.
//
//  - Your submission is a git repository with a commit log that you would submit as a PR.
//
//  - Ultimately it is up to the reviewer to understand the code submitted.
//    They do their best but if the code is hard to follow
//    that will affect the evaluation.
//
//  - Review and complete the design and implementation.
//
//  - The coding language is Scala.
//
//  - You are free to come up with any solution you want but take into account
//    memory and time complexity.
//
//  - Comments stating "this could be done better with 'XYZ'" will be ignored.
//    We can speculate all day but at the end the code submitted is what is evaluated.
//
//  - If you come up with a solution for a piece of the code,
//    but it is inefficient/doesn't capture all scenarios
//    and it would be very time consuming to implement a better one,
//    please add a comment describing the improvement, in what way it is better
//    and why you think it would be a lot of work.
//
//  - You must devise some tests to cover the code
//    that would support its maintenance over time and in a team setting.
//    hint: this does not mean one test that shows one happy path engaging the whole application.
//
//  - Feel free to change any part of the design.
//
//  - The code is written in an OO imperative style. We strongly encourage an FP oriented solution.
//
//  - You are free to add any external libraries you need.
//
//  - You need to document the system requirements of the project (eg. major jvm version)
//    in a file called requirements.md.
//
//  - You must share your submission privately (i.e. not a public GitHub
//    repository). Typically, a zip archive with the local git repo project source code.
//
//  - Assume the code will run in a multi-threaded, multi-cpu environment.
//    You never know how concurrency (or is it parallelism?) will kick in.
//
//  - Whenever you are making changes to the supplied code base make a note with a reason/purpose
//    (perhaps as part of the commit message).
//
///////////////////////////////////////////////////////////////////////
// We build some ingredients of a mini blockchain. Concepts we will
// be dealing with are:
//
//   - Hash, which is the result of a cryptographic hash computation.
//     We use SHA-256.
//
//   - Transaction, which is the representation of data stored in the
//     "mini-chain". Currently, just a string message.
//
//   - Block, the fundamental building block, which we use to build
//     the chain, and holds many transactions.
//
//   - Blockchain, a singly-linked list of Blocks with some extra
//     API to build and query it.
//
//   - Proof-of-Work or PoW for short.
//     A computationally intensive algorithm whose role is to solve a
//     puzzle that allows the next block to appear in the blockchain.
//     We "mine" the next block by using PoW.
//
///////////////////////////////////////////////////////////////////////

// Some base definitions


// The idea behind any cryptographic hash representation in "mini-chain"
// is to treat it as an immutable array of bytes that can be also viewed
// as a number or a hex string. You will see that the number representation
// is used in the mining process. The hex representation is for logging
// purposes.
case class Hash(bytes: Bytes) {
  def toNumber: Number = Number(1, bytes)

  def toHexString: String = Base.toHexString(bytes)
}

object Sha256 {
  val NumberOfBytes = 32
  val TheDigest = MessageDigest.getInstance("SHA-256")

  val Zero_Hash: Hash = Sha256(Array.fill[Byte](32)(0))

  def apply(bytess: Bytes*): Hash = {
    for (bytes <- bytess) {
      TheDigest.update(bytes)
    }

    val hash = TheDigest.digest()
    assert(hash.length == NumberOfBytes)

    Hash(hash)
  }
}

// In "mini-chain", a transaction is just a message.
case class Transaction(data: String)

// Now we are ready to describe the Block.
// Every block has an index, starting from zero (0).
// The block at index 0 is called the Genesis block.
// A block links back to the previous (parent) block.
// Of course, we also record the transactions that this block introduces to our mini-chain.
// We'll see the meaning of the other fields as we move along.
case class Block(
                  index: Int,
                  parentHash: Hash,
                  transactions: Seq[Transaction],
                  miningTargetNumber: Number,
                  nonce: Nonce,
                ) {

  def cryptoHash: Hash = {
    val indexBytes = index.toString.getBytes
    val transactionBytes = transactions.flatMap(_.data.getBytes)
    val combinedBytes = indexBytes ++ parentHash.bytes ++ transactionBytes ++ miningTargetNumber.toByteArray ++ nonce.toString.getBytes
    Sha256(combinedBytes)
  }

  // The essence of PoW is that it is a problem whose solution is easy
  // (in computational resources) to verify but difficult to find.
  def verifyThisHasBeenMinedProperly(): Unit =
    assert(cryptoHash.toNumber < miningTargetNumber)
}

object Miner {
  // NOTE: A Hash is also a Number, we use the two interchangeably.
  //
  // Mining is about computing hashes until we get something that is less
  // than a given target number.
  // This target serves, in a way, as the maximum possible number that a
  // proof of work computation should produce.
  final val StdMiningTargetNumber = targetByLeadingZeros(1)

  // Whoa! We actually mine the Genesis block.
  // Normally, this is done by the system during bootstrapping
  // and every other block is mined by a miner.
  final val Genesis = Miner.mineNextBlock(
    index = 0, // The very first block
    parentHash = Sha256.Zero_Hash, // Let's assume this is by definition for the Genesis block.
    transactions = Seq(Transaction("Hello Blockchain, this is Genesis :)")),
    StdMiningTargetNumber,
  )

  // We basically create a target number with the requirement of having
  // some leading zeros. More leading zeros means smaller target number.
  //
  // NOTE: To actually solve the current coding challenge, would you choose a
  // small or a big number of leading zeros?
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

  // And now let's implement the actual "proof-of-work"-style computation.
  // Compare the parameters of this method with the fields of a Block and
  // you'll see that the only thing missing here is the nonce. Here is why.
  //
  // Initially we have all the fixed elements a block:
  //
  //  - index,
  //  - parentHash,
  //  - transactions,
  //  - miningTargetNumber
  //
  // and by varying the nonce we try to have a block hash that is below the
  // given miningTargetNumber.
  //
  // NOTE Remember that the block hash can be transformed to an actual number,
  //      so we can talk about hash and number interchangeably.
  def mineNextBlock(
                     index: Int,
                     parentHash: Hash,
                     transactions: Seq[Transaction],
                     miningTargetNumber: BigInt,
                   ): Block = {
    def findValidNonce(nonce: Nonce): Nonce = {
      val block = Block(index, parentHash, transactions, miningTargetNumber, nonce)
      if (block.cryptoHash.toNumber < miningTargetNumber) nonce else findValidNonce(nonce + 1)
    }

    val validNonce = findValidNonce(0)
    Block(index, parentHash, transactions, miningTargetNumber, validNonce)
  }

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
  }


  /**
   * A fast blockchain implementation that uses an indexing data structure internally
   * to avoid traversing the linked list of blocks when answering queries like findByIndex.
   */
  class FastBlockchain extends Blockchain {
    private val blockIndex: mutable.HashMap[Int, Block] = mutable.HashMap.empty
    private val blockHashes: mutable.HashMap[Hash, Block] = mutable.HashMap.empty

    def append(block: Block): Unit = {
      blockIndex.put(block.index, block)
      blockHashes.put(block.cryptoHash, block)
    }

    def findByIndex(index: Int): Option[Block] = blockIndex.get(index)

    def findByHash(hash: Hash): Option[Block] = blockHashes.get(hash)

    def commonAncestor(that: Blockchain): Option[Block] = that match {
      case blockchain: FastBlockchain =>
        val commonIndices = blockIndex.keySet.intersect(blockchain.blockIndex.keySet)
        commonIndices.flatMap(index => blockIndex.get(index)).headOption
      case _ => None
    }
  }
}

// Finally, please write some tests to validate some of the properties
// you have encountered in the above description.

// Fun idea just for the discussion: How is this "mini-chain" similar (or not) to Git?

