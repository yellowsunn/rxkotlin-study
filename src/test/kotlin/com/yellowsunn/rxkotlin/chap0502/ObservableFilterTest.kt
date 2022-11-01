package com.yellowsunn.rxkotlin.chap0502

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ObservableFilterTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun filter_test() {
        Observable.range(0, 100)
            .filter { it >= 30 }
            .filter { it <= 50 }
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun distinct_test() {
        Observable.fromArray(1L, 2L, 1L, 2L, 3L)
            .distinct { it.toString() }
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun take_test() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .take(350L, TimeUnit.MILLISECONDS)
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(1L)
    }

    @Test
    fun takeUntil_test_when_Predicate_parameter() {
        Observable.range(0, 100)
            .takeUntil { it == 50 }
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun takeUntil_test_Observable_parameter() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .takeUntil(Observable.timer(550L, TimeUnit.MILLISECONDS))
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(1L)
    }

    @Test
    fun skip_test() {
        Observable.range(1, 15)
            .skip(3)
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun skip_test_when_time_parameter() {
        Observable.interval(100L, TimeUnit.MILLISECONDS)
            .skip(350L, TimeUnit.MILLISECONDS)
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(1L)
    }
}