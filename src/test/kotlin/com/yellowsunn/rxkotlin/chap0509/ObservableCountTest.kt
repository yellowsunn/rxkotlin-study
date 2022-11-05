package com.yellowsunn.rxkotlin.chap0509

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ObservableCountTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun count_test() {
        Observable.just(1, 2, 3, 4, 5, 6)
            .count()
            .subscribe { count: Long -> log.info("{}", count) }
    }

    @Test
    fun reduce_test() {
        Observable.range(1, 10)
            .reduce { a, b ->
                log.info("{}, {}", a, b)
                a + b
            }
            .subscribe { log.info("{}", it) }
    }
}