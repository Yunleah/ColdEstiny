package me.yunleah.plugin.coldestiny.internal.manager

import me.yunleah.plugin.coldestiny.ColdEstiny
import me.yunleah.plugin.coldestiny.util.FileUtil.saveResourceNotWarn
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit
import java.io.File

object FileManager {
    @Awake(LifeCycle.LOAD)
    fun createFile() {
        ColdEstiny.plugin.saveResourceNotWarn("workspace${File.separator}drop${File.separator}ExampleDrop.yml", false)
        ColdEstiny.plugin.saveResourceNotWarn("workspace${File.separator}redeem${File.separator}ExampleRedeem.yml", false)
        ColdEstiny.plugin.saveResourceNotWarn("workspace${File.separator}ExampleConfig.yml", false)

    }

}