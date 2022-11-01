package com.yellowsunn.rxkotlin.chap0501

import io.reactivex.Observable
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ObservableOperatorsTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun interval_test() {
        // interval은 호출한 스레드와는 별도의 스레드에서 실행됨
        Observable.interval(0L, 1L, TimeUnit.SECONDS)
            .map { "$it count" }
            .subscribe { log.info(it) }

        TimeUnit.SECONDS.sleep(3L)
    }

    @Test
    fun range_test() {
        Observable.range(0, 5)
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun timer_test() {
        Observable.timer(2L, TimeUnit.MILLISECONDS)
            .map { "Do work! $it" }
            .subscribe { log.info(it) }

        TimeUnit.MILLISECONDS.sleep(5L)
    }

    @Test
    fun defer_test() {
        // subscribe()가 호출될 때마다 새로운 Observable을 생성한다
        val observable = Observable.defer {
            Observable.just(ZonedDateTime.now())
        }
        val observableJust = Observable.just(ZonedDateTime.now())

        observable.subscribe { log.info("# defer()의 구독 시간: $it") }
        observableJust.subscribe { log.info("# just()의 구독 시간: $it") }

        TimeUnit.SECONDS.sleep(3L)

        observable.subscribe { log.info("# defer()의 구독 시간: $it") }
        observableJust.subscribe { log.info("# just()의 구독 시간: $it") }
    }

    @Test
    fun fromIterable_test() {
        val countries = listOf("Korea", "Canada", "USA", "Italy")

        Observable.fromIterable(countries)
            .subscribe { log.info(it) }
    }

    @Test
    fun fromFuture_test() {
        val future: Future<ZonedDateTime> = CompletableFuture.supplyAsync {
            log.info("Processing....")
            TimeUnit.SECONDS.sleep(3L)
            ZonedDateTime.now()
        }

        Observable.fromFuture(future)
            .subscribe { log.info("# end time $it") }
    }
}