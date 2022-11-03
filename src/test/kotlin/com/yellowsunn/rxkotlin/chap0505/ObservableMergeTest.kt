package com.yellowsunn.rxkotlin.chap0505

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ObservableMergeTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun merge_test() {
        val observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
            .take(5)

        val observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
            .take(5)
            .map { it + 1000 }

        Observable.merge(observable1, observable2)
            .subscribe { log.info("{}", it) }

        TimeUnit.SECONDS.sleep(4L)
    }

    @Test
    fun concat_test() {
        val observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
            .take(5)

        val observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
            .take(5)
            .map { it + 1000 }

        Observable.concat(observable1, observable2)
            .subscribe { log.info("{}", it)}

        TimeUnit.SECONDS.sleep(4L)
    }

    // 데이터의 개수가 더 적은 observable 에 맞춰서 종료
    @Test
    fun zip_test() {
        val observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
            .take(5)

        val observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
            .take(7)
            .map { it + 1000 }

        Observable.zip(observable1, observable2) { o1, o2 ->
            "$o1, $o2"
        }.subscribe { log.info("{}", it)}

        TimeUnit.SECONDS.sleep(4L)
    }

    @Test
    fun combine_latest() {
        val observable1 = Observable.interval(200L, TimeUnit.MILLISECONDS)
            .take(5)

        val observable2 = Observable.interval(400L, TimeUnit.MILLISECONDS)
            .take(7)
            .map { it + 1000 }

        Observable.combineLatest(observable1, observable2) { o1, o2 ->
            "$o1, $o2"
        }.subscribe { log.info("{}", it)}

        TimeUnit.SECONDS.sleep(4L)
    }

}