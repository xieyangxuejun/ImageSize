package com.foretree.support.image

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL

/**
 *
 * Created by silen on 22/05/2018.
 */
class ImageSize {
    companion object {
        private val INSTANCE: ImageSize = lazy { ImageSize() }.value

        fun with(imagePath: String): ImageLoader {
            return ImageLoader(imagePath, INSTANCE)
        }
    }

    fun get(loader: ImageLoader): IntArray {
        val stream: InputStream = loader.inputStream.load(loader.imagePath) ?: return intArrayOf(0,0,0)
        return intArrayOf()
    }
}

class ImageLoader(val imagePath: String, val imageSize: ImageSize) {
    val inputStream: InputStreamProvider = DefaultInputStreamProvider()

    fun get(): IntArray {
        return imageSize.get(this)
    }

}

interface InputStreamProvider {
    fun load(imagePath: String): InputStream?
}

class DefaultInputStreamProvider : InputStreamProvider {

    override fun load(imagePath: String): InputStream? {
        //http
        if (imagePath.startsWith("http")) {
            return URL(imagePath).openConnection().let {
                it.connectTimeout = 1000
                it.readTimeout = 1000
                it.connect()
                it.getInputStream()
            }
        } else { //local
            File(imagePath).let {
                if (it.exists()) {
                    return FileInputStream(it)
                }
            }
        }
        return null
    }

}

