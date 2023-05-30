package io.iohk.atala.swetest
package models


import models.Block.initEmptyBlock

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers


class BlockchainSpec extends AnyFlatSpec with Matchers {

  "FastBlockchain" should "append a block and retrieve it by index" in {
    val sampleBlockChain = new FastBlockchain()
    val blockchainIndex = 45

    val block = Block.initEmptyBlock(blockchainIndex)
    sampleBlockChain.append(block)
    sampleBlockChain.append(Block.initEmptyBlock(567))

    val retrievedBlock = sampleBlockChain.findByIndex(blockchainIndex)
    retrievedBlock shouldEqual Some(block)

    //    Verify that all two blocks were correctly appended
    sampleBlockChain.length mustBe 2
  }

  it should "return None for a block that does not exist" in {
    val emptyBlockchain = new FastBlockchain()
    val randomIndex = 43

    //    Test findByIndex
    val findByIndexMaybeBlock = emptyBlockchain.findByIndex(randomIndex)
    findByIndexMaybeBlock should be(None)

    //    Test findByHash
    val randomHash = Hash(Array[Byte](0))
    val findByHashMaybeBlock = emptyBlockchain.findByHash(randomHash)
    findByHashMaybeBlock should be(None)
  }

  it should "retrieve a block by its hash" in {
    val blockchain = new FastBlockchain()

    val block = Block.initEmptyBlock(2)
    blockchain.append(block)
    val blockHash = block.cryptoHash

    val retrievedBlock = blockchain.findByHash(blockHash)
    retrievedBlock shouldEqual Some(block)
  }

  it should "find a common ancestor block with another blockchain" in {
    val blockchain1 = new FastBlockchain()
    val blockchain2 = new FastBlockchain()

    val commonBlock = Block(1, Hash(Array[Byte](1)), Seq.empty, BigInt(0), 0)
    val uniqueBlock1 = Block(2, Hash(Array[Byte](2)), Seq.empty, BigInt(0), 0)
    val uniqueBlock2 = Block(3, Hash(Array[Byte](3)), Seq.empty, BigInt(0), 0)

    blockchain1.append(commonBlock)
    blockchain1.append(uniqueBlock1)

    blockchain2.append(commonBlock)
    blockchain2.append(uniqueBlock2)

    val commonAncestor = blockchain1.commonAncestor(blockchain2)
    commonAncestor shouldEqual Some(commonBlock)
  }

  it should "return None for common ancestor when compared with a different blockchain type" in {
    val fastBlockchain = new FastBlockchain()
    val block1 = initEmptyBlock(1)
    val block2 = initEmptyBlock(2)
    val block3 = initEmptyBlock(3)

    fastBlockchain.append(block1)
    fastBlockchain.append(block2)
    fastBlockchain.append(block3)

    val genericBlockchain = new GenericBlockchain()


    val commonAncestor = fastBlockchain.commonAncestor(genericBlockchain)
    commonAncestor must be(None)
  }
}



