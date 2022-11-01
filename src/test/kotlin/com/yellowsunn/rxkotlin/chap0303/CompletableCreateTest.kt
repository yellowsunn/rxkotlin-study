package com.yellowsunn.rxkotlin.chap0303

import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class CompletableCreateTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun test() {
        Completable.create { emitter: CompletableEmitter ->
            var sum = 0
            for (i: Int in 1..100) {
                sum += i
            }
            log.info("# 합계: {}", sum)

            emitter.onComplete()
        }
            .subscribeOn(Schedulers.computation())
            .subscribe(
                { log.info("onComplete") },
                { e -> log.error("message={}", e.message, e) }
            )

        TimeUnit.MILLISECONDS.sleep(100L)
    }

}