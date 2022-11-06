package com.yellowsunn.rxkotlin.chap0702

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class SchedulerTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun multiple_observeOn_test() {
        // observeOn() 을 여러개 지정하면 지정한 다음의 데이터 처리를 각각 개별 쓰레드에서 진행한다
        Observable.range(1, 100)
            .doOnNext { log.info("{}", it) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .filter { it % 2 == 0 }
            .doOnNext { log.info("filter() - {}", it) }
            .observeOn(Schedulers.computation())
            .map { it * 10 }
            .doOnNext { log.info("map() - {}", it) }
            .observeOn(Schedulers.computation())
            .subscribe { log.info("subscribe() - {}", it) }

        TimeUnit.SECONDS.sleep(1L)
    }

    @Test
    fun computation_test() {
        val observable1 = Observable.fromIterable(getRandomList())
        val observable2 = Observable.fromIterable(getRandomList())
        val observable3 = Observable.fromIterable(getRandomList())

        val source = Observable
            .zip(observable1, observable2, observable3) { o1, o2, o3 ->
                maxOf(o1, o2, o3)
            }

        source.subscribeOn(Schedulers.computation())
            .subscribe { log.info("{}", it) }

        source.subscribeOn(Schedulers.computation())
            .subscribe { log.info("{}", it) }

        TimeUnit.MILLISECONDS.sleep(500)
    }

    @Test
    fun newThread_test() {
        // 쓰레드 생성 비용이 들고, 재사용되지 않으므로 권장되지 않는 방법
        val observable = Observable.range(1, 5)
        observable.subscribeOn(Schedulers.newThread())
            .map { "##${it}##" }
            .subscribe { log.info("{}", it) }

        observable.subscribeOn(Schedulers.newThread())
            .map { "$$${it}$$" }
            .subscribe { log.info("{}", it) }

        TimeUnit.MILLISECONDS.sleep(300L)
    }

    @Test
    fun trampoline_test() {
        // 대기 큐에 등록되는 순서대로 작업을 처리한다 (메인쓰레드에서 실행)
        val observable = Observable.range(1, 5)

        observable.subscribeOn(Schedulers.trampoline())
            .map { "##${it}##" }
            .subscribe { log.info("{}", it) }

        observable.subscribeOn(Schedulers.trampoline())
            .map { "$$${it}$$" }
            .subscribe { log.info("{}", it) }
    }

    @Test
    fun single_test() {
        // 쓰레드 하나를 사용
        val observable = Observable.range(1, 5)

        observable.subscribeOn(Schedulers.single())
            .map { "##${it}##" }
            .subscribe { log.info("{}", it) }

        observable.subscribeOn(Schedulers.single())
            .map { "$$${it}$$" }
            .subscribe { log.info("{}", it) }

        TimeUnit.MILLISECONDS.sleep(300L)
    }

    private fun getRandomList(): List<Int> {
        val list = mutableListOf<Int>()
        for (i in 1..100) {
            list.add((1..100).random())
        }
        return list
    }
}