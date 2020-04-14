package com.kostynchikoff.core_application.utils

import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE

class KoinLoger : org.koin.core.logger.Logger() {
    override fun log(level: Level, msg: MESSAGE) {
        print(msg)
    }
}