package com.example.cbasample

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class MySampleRunner(testClass: Class<*>?) : RobolectricTestRunner(testClass) {
    override fun buildGlobalConfig(): Config {
        return Config.Builder.defaults().setApplication(CbaSampleAppTest::class.java).setShadows().build()
    }
}