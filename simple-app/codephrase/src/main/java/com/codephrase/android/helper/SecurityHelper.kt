package com.codephrase.android.helper

import android.util.Base64
import com.codephrase.android.common.SecureKeyWrapper
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityHelper private constructor() {
    companion object {
        private const val HASH_ALGORITHM = "SHA-256"
        private const val AES_ALGORITHM = "AES/GCM/NoPadding"

        private val IV by lazy {
            byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        }

        fun getEncryptionKey(): String {
            val context = ApplicationHelper.getContext();
            val file = File(context.filesDir, context.packageName)
            val wrapper = SecureKeyWrapper(context)

            if (!file.exists()) {
                val bytes = ByteArray(16)
                SecureRandom().nextBytes(bytes)
                val key = generateSecretKey(bytes)
                val wrappedKey = wrapper.wrap(key)
                file.writeBytes(wrappedKey)
            }

            val wrappedKey = file.readBytes()
            val key = wrapper.unwrap(wrappedKey)

            return Base64.encodeToString(key.encoded, Base64.NO_WRAP)
        }

        fun hash(str: String): String {
            val messageDigest = MessageDigest.getInstance(HASH_ALGORITHM)
            val bytes = str.toByteArray()
            messageDigest.update(bytes, 0, bytes.size)
            val hash = messageDigest.digest()
            return Base64.encodeToString(hash, Base64.NO_WRAP)
        }

        fun encrypt(str: String): String {
            val key = SecurityHelper.getEncryptionKey().toByteArray()
            val cipher = Cipher.getInstance(AES_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, generateSecretKey(key), generateIV())
            val cipherText = cipher.doFinal(str.toByteArray())
            return Base64.encodeToString(cipherText, Base64.NO_WRAP);
        }

        fun decrypt(str: String): String {
            val key = SecurityHelper.getEncryptionKey().toByteArray()
            val cipher = Cipher.getInstance(AES_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, generateSecretKey(key), generateIV())
            return String(cipher.doFinal(Base64.decode(str, Base64.NO_WRAP)))
        }

        private fun generateSecretKey(key: ByteArray): SecretKey {
            return SecretKeySpec(key, "AES")
        }

        private fun generateIV(): IvParameterSpec {
            return IvParameterSpec(IV)
        }
    }
}