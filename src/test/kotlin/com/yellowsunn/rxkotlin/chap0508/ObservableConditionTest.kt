package com.yellowsunn.rxkotlin.chap0508

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ObservableConditionTest {
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun all_test() {
        Observable.fromArray(1, 3, 5, 7)
            .doOnNext { log.info("{}", it) }
            .all { it % 2 != 0 }
            .subscribe { isSuccess: Boolean -> log.info("{}", isSuccess) }
    }

    @Test
    fun amb_test() {
        // 여러개의 Observable 중에서 최초 통지 시점이 가장 빠른 Observable의 데이터만 통지되고, 나머지 Observable은 무시된다.
        val observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
            .take(3)
            .doOnComplete { log.info("observable1") }
        val observable2 = Observable.interval(100L, TimeUnit.MILLISECONDS)
            .take(3)
            .map { it * 2 }
            .doOnComplete { log.info("observable2") }
        val observable3 = Observable.interval(500L, TimeUnit.MILLISECONDS)
            .take(3)
            .map { it * 3 }
            .doOnComplete { log.info("observable3") }
        Observable.amb(listOf(observable1, observable2, observable3))
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(2L)
    }

    @Test
    fun contains_test() {
        Observable.fromArray(1, 3, 5, 7)
            .doOnNext { log.info("{}", it) }
            .contains(5)
            .subscribe { isSuccess: Boolean -> log.info("{}", isSuccess) }
    }

    @Test
    fun defaultIfEmpty_test() {
        Observable.just(1, 2, 3, 4, 5)
            .filter { it > 10 }
            .defaultIfEmpty(10)
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun sequenceEqual_test() {
        val observable1 = Observable.just(1, 3, 5, 7)
            .subscribeOn(Schedulers.computation())
            .doOnNext { log.info("{}", it) }
            .delay(200L, TimeUnit.MILLISECONDS)
        val observable2 = Observable.just(1, 3, 5, 7)
            .doOnNext { log.info("{}", it) }
            .delay(300L, TimeUnit.MILLISECONDS)

        Observable.sequenceEqual(observable1, observable2)
            .subscribe { isSame: Boolean -> log.info("{}", isSame) }

        TimeUnit.SECONDS.sleep(1L)
    }
}