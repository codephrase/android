package com.codephrase.android.common.security

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.codephrase.android.helper.ApplicationHelper
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal

@Suppress("DEPRECATION")
class SecureKeyWrapper(private val context: Context) {
    private val KEYSTORE_PROVIDER = "AndroidKeyStore"
    private val KEY_ALIAS = context.packageName
    private val KEY_SIZE = 2048

    private val ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA
    private val BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
    private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1
    private val RSA_CIPHER = ALGORITHM + "/" + BLOCK_MODE + "/" + ENCRYPTION_PADDING

    private val cipher: Cipher
    private val keyPair: KeyPair

    init {
        cipher = Cipher.getInstance(RSA_CIPHER)

        if (ApplicationHelper.isApiLevel23Supported())
            keyPair = generateKeyPairApi23()
        else if (ApplicationHelper.isApiLevel19Supported())
            keyPair = generateKeyPairApi19()
        else
            keyPair = generateKeyPair()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun generateKeyPairApi23(): KeyPair {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val spec = KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setCertificateSubject(generateCertificateSubject())
                    .setCertificateSerialNumber(BigInteger.ONE)
                    .setKeySize(KEY_SIZE)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(ENCRYPTION_PADDING)
                    .build()

            val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER)
            keyGenerator.initialize(spec)
            keyGenerator.generateKeyPair()
        }

        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        return KeyPair(entry.certificate.publicKey, entry.privateKey)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun generateKeyPairApi19(): KeyPair {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val startDate = GregorianCalendar()
            val endDate = GregorianCalendar()
            endDate.add(Calendar.YEAR, 100)

            val spec = android.security.KeyPairGeneratorSpec.Builder(context)
                    .setAlias(KEY_ALIAS)
                    .setSubject(generateCertificateSubject())
                    .setSerialNumber(BigInteger.ONE)
                    .setKeyType(KeyProperties.KEY_ALGORITHM_RSA)
                    .setKeySize(KEY_SIZE)
                    .setStartDate(startDate.time)
                    .setEndDate(endDate.time)
                    .build()

            val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER)
            keyGenerator.initialize(spec)
            keyGenerator.generateKeyPair()
        }

        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        return KeyPair(entry.certificate.publicKey, entry.privateKey)
    }

    private fun generateKeyPair(): KeyPair {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val startDate = GregorianCalendar()
            val endDate = GregorianCalendar()
            endDate.add(Calendar.YEAR, 100)

            val spec = android.security.KeyPairGeneratorSpec.Builder(context)
                    .setAlias(KEY_ALIAS)
                    .setSubject(generateCertificateSubject())
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(startDate.time)
                    .setEndDate(endDate.time)
                    .build()

            val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER)
            keyGenerator.initialize(spec)
            keyGenerator.generateKeyPair()
        }

        val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        return KeyPair(entry.certificate.publicKey, entry.privateKey)
    }

    private fun generateCertificateSubject(): X500Principal {
        return X500Principal("CN=$KEY_ALIAS")
    }

    fun wrap(key: SecretKey): ByteArray {
        cipher.init(Cipher.WRAP_MODE, keyPair.public)
        return cipher.wrap(key)
    }

    fun unwrap(blob: ByteArray): SecretKey {
        cipher.init(Cipher.UNWRAP_MODE, keyPair.private)
        return cipher.unwrap(blob, "AES", Cipher.SECRET_KEY) as SecretKey
    }
}