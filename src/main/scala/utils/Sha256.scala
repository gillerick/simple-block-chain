package io.iohk.atala.swetest
package utils

import models.Hash
import utils.Base.Bytes

import java.security.MessageDigest

object Sha256 {
  private val algorithm = "SHA-256"
  val NumberOfBytes = 32
  val TheDigest: MessageDigest = MessageDigest.getInstance(algorithm)

  val ZeroHash: Hash = Hash(Array.fill[Byte](NumberOfBytes)(0))


  /**
   * Applies the SHA-256 hash algorithm to the given byte arrays and returns the resulting hash.
   *
   * @param bytes The byte arrays to hash.
   * @return The computed SHA-256 hash.
   */
  def apply(bytes: Bytes*): Hash = {
    val digest = MessageDigest.getInstance(algorithm)

    //    We are using foldLeft to increase readability and conciseness
    val hash = bytes.foldLeft(digest) { (accumulated, byte) =>
      accumulated.update(byte)
      accumulated
    }.digest()

    //    Here, instead of an assertion, we are throwing an explicit exception with more information
    if (hash.length != NumberOfBytes) {
      throw new IllegalArgumentException("Hash length is invalid.")
    }

    Hash(hash)
  }
}
