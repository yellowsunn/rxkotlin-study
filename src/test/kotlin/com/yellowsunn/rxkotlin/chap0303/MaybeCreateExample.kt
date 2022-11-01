package com.yellowsunn.rxkotlin.chap0303

import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

class MaybeCreateExample {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun create_test() {
        Maybe.create { emitter: MaybeEmitter<ZonedDateTime> ->
//            emitter.onSuccess(ZonedDateTime.now())
            emitter.onComplete()
        }.subscribe(
            { log.info("# 시각: {}", it) },
            { e -> log.error("message={}", e.message, e) },
            { log.info("onComplete") }  // 전달된 데이터가 없으면 호출됨
        )
    }

    @Test
    fun just_test() {
//        Maybe.just(ZonedDateTime.now())
//            .subscribe(
//                { log.info("# 시각: {}", it) },
//                { e -> log.error("message={}", e.message, e) },
//                { log.info("onComplete") }
//            )

        Maybe.empty<ZonedDateTime>()
            .subscribe(
                { log.info("# 시각: {}", it) },
                { e -> log.error("message={}", e.message, e) },
                { log.info("onComplete") }
            )
    }
}