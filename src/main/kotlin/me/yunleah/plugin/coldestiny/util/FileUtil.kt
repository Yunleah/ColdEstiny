package me.yunleah.plugin.coldestiny.util

import taboolib.common.platform.function.getDataFolder
import taboolib.platform.BukkitPlugin
import java.io.*

object FileUtil {
    /**
     * 保存默认文件(不进行替换)
     */
    @JvmStatic
    fun BukkitPlugin.saveResourceNotWarn(resourcePath: String, overwrite: Boolean) {
        this.getResource(resourcePath.replace('\\', '/'))?.let { inputStream ->
            val outFile = File(this.dataFolder, resourcePath)
            val lastIndex: Int = resourcePath.lastIndexOf(File.separator)
            val outDir = File(this.dataFolder, resourcePath.substring(0, if (lastIndex >= 0) lastIndex else 0))
            if (!outDir.exists()) {
                outDir.mkdirs()
            }
            if (overwrite || !outFile.exists()) {
                try {
                    var len: Int
                    val fileOutputStream = FileOutputStream(outFile)
                    val buf = ByteArray(1024)
                    while (inputStream.read(buf).also { len = it } > 0) {
                        (fileOutputStream as OutputStream).write(buf, 0, len)
                    }
                    fileOutputStream.close()
                    inputStream.close()
                } catch (_: IOException) {}
            }
        }
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param dir 待获取文件夹
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(dir: File): ArrayList<File> {
        val list = ArrayList<File>()
        val files = dir.listFiles() ?: arrayOf<File>()
        for (file: File in files) {
            if (file.isDirectory) {
                list.addAll(getAllFiles(file))
            } else {
                list.add(file)
            }
        }
        return list
    }
    /**
     * 获取文件夹内文件(不存在时创建文件)
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrCreate(file: String): File {
        return File(getDataFolder(), File.separator + file).also {
            if (!it.exists()) {
                val parent = it.parentFile
                if (!parent.exists()) parent.mkdirs()
                it.createNewFile()
            }
        }
    }

    /**
     * 解析文件编码
     *
     * @param file 待解析文件
     * @return 编码类型
     */
    @JvmStatic
    fun charset(file: File): String {
        var charset = "GBK"
        val first3Bytes = ByteArray(3)
        try {
            var checked = false
            val bis = BufferedInputStream(FileInputStream(file))
            bis.mark(0)
            var read = bis.read(first3Bytes, 0, 3)
            if (read == -1) {
                bis.close()
                return charset
            } else if (first3Bytes[0] == 0xFF.toByte() && first3Bytes[1] == 0xFE.toByte()) {
                charset = "UTF-16LE"
                checked = true
            } else if (first3Bytes[0] == 0xFE.toByte() && first3Bytes[1] == 0xFF.toByte()) {
                charset = "UTF-16BE"
                checked = true
            } else if (first3Bytes[0] == 0xEF.toByte() && first3Bytes[1] == 0xBB.toByte() && first3Bytes[2] == 0xBF.toByte()) {
                charset = "UTF-8"
                checked = true
            }
            bis.reset()
            if (!checked) {
                while (bis.read().also { read = it } != -1) {
                    if (read >= 0xF0) break
                    if (read in 0x80..0xBF)
                        break
                    if (read in 0xC0..0xDF) {
                        read = bis.read()
                        if (read in 0x80..0xBF)
                            continue else break
                    } else if (read in 0xE0..0xEF) {
                        read = bis.read()
                        if (read in 0x80..0xBF) {
                            read = bis.read()
                            if (read in 0x80..0xBF) {
                                charset = "UTF-8"
                                break
                            } else break
                        } else break
                    }
                }
            }
            bis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return charset
    }

}