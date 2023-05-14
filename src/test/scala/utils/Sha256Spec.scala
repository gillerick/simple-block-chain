package io.iohk.atala.swetest
package utils

import models.Hash

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper
import org.scalatest.matchers.should.Matchers

import java.security.MessageDigest

class Sha256Spec extends AnyFlatSpec with Matchers {

  "Sha256" should "compute the correct SHA-256 hash" in {
    val sampleBytesOne = Array[Byte](1, 2, 3)
    val sampleBytesTwo = Array[Byte](4, 5, 6)
    val expectedHash = Hash(Array[Byte](113, -110, 56, 92, 60, 6, 5, -34, 85, -69, -108, 118, -50, 29, -112, 116, -127, -112, -20, -77, 42, -114, -19, 127, 82, 7, -77, 12, -10, -95, -2, -119))

    val hash = Sha256(sampleBytesOne, sampleBytesTwo)

    hash.bytes mustEqual expectedHash.bytes
  }

  it should "compute the correct SHA-256 hash for empty input" in {
    val expectedHash = Hash(Array[Byte](-29, -80, -60, 66, -104, -4, 28, 20, -102, -5, -12, -56, -103, 111, -71, 36, 39, -82, 65, -28, 100, -101, -109, 76, -92, -107, -103, 27, 120, 82, -72, 85))

    val hash = Sha256()

    hash.bytes mustEqual expectedHash.bytes
  }

  it should "compute the correct SHA-256 hash for a single input" in {
    val bytes = Array[Byte](1, 2, 3)
    val expectedHash = Hash(Array[Byte](3, -112, 88, -58, -14, -64, -53, 73, 44, 83, 59, 10, 77, 20, -17, 119, -52, 15, 120, -85, -52, -50, -43, 40, 125, -124, -95, -94, 1, 28, -5, -127))

    val hash = Sha256(bytes)

    hash.bytes mustEqual expectedHash.bytes
  }

  "ZeroHash" should "be a hash of all zeros" in {
    val zeroHash = Sha256.ZeroHash
    val expectedBytes = Array.fill[Byte](32)(0)
    zeroHash.bytes shouldEqual expectedBytes
  }

  "TheDigest" should "be an instance of SHA-256" in {
    val sha256Algorithm = MessageDigest.getInstance("SHA-256")
    val theDigest = Sha256.TheDigest
    theDigest.getAlgorithm shouldEqual sha256Algorithm.getAlgorithm
  }
}
