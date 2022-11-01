package com.yellowsunn.rxkotlin.chap0303

import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

class MaybeFromSingleTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        val single = Single.just(ZonedDateTime.now())

        Maybe.fromSingle(single)
            .subscribe(
                { log.info("# 시각: {}", it) },
                { e -> log.error("message={}", e.message, e) },
                { log.info("onComplete") }
            )
    }
}