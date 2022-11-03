package com.yellowsunn.rxkotlin.chap0507

import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

class ObservableDelayTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun delay_test() {
        Observable.just(1, 3, 4, 6)
            .doOnNext { log.info("{}", it) }
            .delay(2L, TimeUnit.SECONDS)
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(3L)
    }

    @Test
    fun delay_test2() {
        Observable.just(1, 3, 4, 6)
            .delay {
                TimeUnit.SECONDS.sleep(1L)
                Observable.just(it)
            }.subscribe { log.info("{}", it) }
    }

    @Test
    fun delaySubscription_test() {
        log.info("start")
        // 2초 후에 구독 시작
        Observable.interval(200L, TimeUnit.MILLISECONDS)
            .doOnNext { log.info("{}", it) }
            .delaySubscription(2L, TimeUnit.SECONDS)
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(3L)
    }

    @Test
    fun timeout_test() {
        Observable.range(1, 5)
            .doOnNext {
                val time = if (it == 4) 1500L else 1000L
                TimeUnit.MILLISECONDS.sleep(time)
            }
            .timeout(1200L, TimeUnit.MILLISECONDS)
            .subscribe(
                { log.info("{}", it) },
                { e -> log.error("message={}", e.message, e) }
            )
    }

    @Test
    fun timeInterval_test() {
        Observable.just(1, 3, 5, 7, 9)
            .delay {
                TimeUnit.MILLISECONDS.sleep((100L..1000L).random())
                Observable.just(it)
            }
            .timeInterval()
            .subscribe { log.info("time={}, value={}", it.time(), it.value()) }
    }

    @Test
    fun materialize_dematerialize_test() {
        Observable.just(1, 3, 5, 7, 9)
            .doOnNext { if (it == 5) throw IllegalArgumentException() }
            .materialize()
            .doOnNext { notification: Notification<Int> ->
                if (notification.isOnError) {
                    log.warn("error 발생")
                }
            }
            .filter { !it.isOnError }
            .dematerialize { it }
            .subscribeOn(Schedulers.io())
            .subscribe { log.info("{}", it) }
    }
}