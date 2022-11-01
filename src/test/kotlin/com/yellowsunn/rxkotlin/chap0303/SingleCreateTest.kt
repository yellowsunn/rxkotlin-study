package com.yellowsunn.rxkotlin.chap0303

import io.reactivex.Single
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

class SingleCreateTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun create_test() {
        Single.create { emitter -> emitter.onSuccess(ZonedDateTime.now()) }
            .subscribe(
                { log.info("# 시각: {}", it) },
                { e -> log.error("message={}", e.message, e) }
            )
    }

    @Test
    fun just_test() {
        Single.just(ZonedDateTime.now())
            .subscribe(
                { log.info("# 시각: {}", it) },
                { e -> log.error("message={}", e.message, e) }
            )
    }
}