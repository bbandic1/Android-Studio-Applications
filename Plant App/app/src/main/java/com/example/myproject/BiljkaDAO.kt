package com.example.myproject

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
interface BiljkaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBiljka(biljka: Biljka): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBiljka(biljka: Biljka): Boolean {
        return try {
            addBiljka(biljka)
            true
        } catch (e: Exception) {
            false
        }
    }

    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        val statickeBiljke = getAllBiljkas()
        var brojac = 0
        val ispravljeneBiljke: MutableList<Biljka> = mutableListOf()
        val trefleDAO = TrefleDAO()
        for (biljka in statickeBiljke) {
            try {
                val biljkaCopy = biljka.copy()
                biljkaCopy.onlineChecked = true
                val izlaznaBiljka = withContext(Dispatchers.IO) {
                    trefleDAO.fixData(biljka)
                }
                if (izlaznaBiljka != biljkaCopy) {
                    brojac++
                }
                ispravljeneBiljke.add(izlaznaBiljka)
            } catch (e: Exception) {
                ispravljeneBiljke.add(biljka)
            }
        }
        return brojac
    }

    @Query("DELETE FROM Biljka")
    fun brišiBiljke()

    @Query("DELETE FROM BiljkaBitmap")
    fun brišiBitmape()

    @Transaction
    fun clearData() {
        brišiBiljke()
        brišiBitmape()
    }

    @Query("SELECT CASE WHEN EXISTS " +
            "(SELECT 1 FROM Biljka " +
            "WHERE id = :id)" +
            "THEN 1 " +
            "ELSE 0 " +
            "END")
    suspend fun pronađiBiljkaID(id: Long?): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBiljkaBitmap(biljkeBitmap: BiljkaBitmap)

    @Query("SELECT CASE WHEN EXISTS " +
            "(SELECT 1 FROM BiljkaBitmap, Biljka " +
            "WHERE idBiljke = :id)" +
            "THEN 1 " +
            "ELSE 0 " +
            "END")
    suspend fun pronađiBitmapID(id: Long?): Int

    private fun cropBitmap(srcBmp: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val srcWidth = srcBmp.width
        val srcHeight = srcBmp.height

        val aspectRatio = srcWidth.toFloat() / srcHeight.toFloat()
        val targetAspectRatio = targetWidth.toFloat() / targetHeight.toFloat()

        val scaledWidth: Int
        val scaledHeight: Int

        if (targetAspectRatio > aspectRatio) {
            scaledWidth = targetWidth
            scaledHeight = (targetWidth / aspectRatio).toInt()
        } else {
            scaledHeight = targetHeight
            scaledWidth = (targetHeight * aspectRatio).toInt()
        }

        val scaledBitmap = Bitmap.createScaledBitmap(srcBmp, scaledWidth, scaledHeight, true)

        val xOffset = (scaledWidth - targetWidth) / 2
        val yOffset = (scaledHeight - targetHeight) / 2

        return Bitmap.createBitmap(scaledBitmap, xOffset, yOffset, targetWidth, targetHeight)
    }

    @Transaction
    suspend fun addImage(biljkaID: Long?, biljkaBitmap: Bitmap): Boolean{

        val provjeraBiljke = pronađiBiljkaID(biljkaID) == 1
        if(!provjeraBiljke)
            return false

        val provjeraBitmape = biljkaID?.let { getBitmap(it) }
        if(provjeraBitmape != null)
            return false

        val croppedBitmap = cropBitmap(biljkaBitmap, 400, 400)
        val bitmapConvert = BitmapConvert()
        val biljkaBit = BiljkaBitmap(idBiljke = biljkaID, bitmap = bitmapConvert.fromBitmap(croppedBitmap))
        insertBiljkaBitmap(biljkaBit)
        return true
    }


    @Query("SELECT COUNT(id) FROM Biljka")
    suspend fun brojBiljaka(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBiljka(biljke: List<Biljka>)

    @Query("SELECT bitmap " +
            "FROM BiljkaBitmap" +
            " WHERE bitmap = :biljkaID")
    suspend fun getBitmap(biljkaID: Long): Bitmap?

    @Query("SELECT idBiljke " +
            "FROM BiljkaBitmap")
    suspend fun getIDBiljke(): MutableList<Long?>

}