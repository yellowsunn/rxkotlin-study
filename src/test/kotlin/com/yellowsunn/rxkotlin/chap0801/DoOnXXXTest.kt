package com.yellowsunn.rxkotlin.chap0801

import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

// 디버깅을 위한 doXXX 함수
class DoOnXXXTest {

    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun doOnSubscribe_test() {
        // onSubscribe 이벤트 발생전에 호출되는 doOnSubscribe
        Observable.range(1, 7)
            .doOnSubscribe { log.info("doOnSubscribe()") }
            .subscribe(
                { log.info("onNext() - {}", it) },
                { e -> log.error("onError() - {}", e.message, e) },
                { log.info("onComplete()") },
                { log.info("onSubscribe()") }
            )
    }

    @Test
    fun doOnNext_test() {
        // 데이터 통지 시 마다 실행되는 doOnNext() 를 이용해 데이터의 상태를 확인 가능
        Observable.range(1, 15)
            .doOnNext { log.info("doOnNext() - {}", it) }
            .filter { it < 10 }
            .doOnNext { log.info("after filter() - {}", it) }
            .subscribe(
                { log.info("onNext() - {}", it) },
                { e -> log.error("onError() - {}", e.message, e) },
                { log.info("onComplete()") },
                { log.info("onSubscribe()") }
            )
    }

    @Test
    fun doOnComplete_test() {
        // onCompete 이벤트 발생 전에 호출되는 doOnComplete
        Observable.range(1, 5)
            .doOnComplete { log.info("doOnComplete()") }
            .subscribe(
                { log.info("onNext() - {}", it) },
                { e -> log.error("onError() - {}", e.message, e) },
                { log.info("onComplete()") },
                { log.info("onSubscribe()") }
            )
    }

    @Test
    fun doOnError_test() {
        // onCompete 이벤트 발생 전에 호출되는 doOnComplete
        Observable.range(1, 5)
            .zipWith(Observable.just(5, 4, 3, 0, 1)) { a, b -> a / b }
            .doOnError { e -> log.error("doOnError() - {}", e.message) }
            .subscribe(
                { log.info("onNext() - {}", it) },
                { e -> log.error("onError() - {}", e.message, e) },
                { log.info("onComplete()") },
                { log.info("onSubscribe()") }
            )
    }

    @Test
    fun doOnEach_test() {
        // doOnNext, doOnComplete, doOnError 를 한번에 처리할 수 있다.
        Observable.range(1, 5)
            .doOnEach { notification: Notification<Int> ->
                if (notification.isOnNext) {
                    log.info("doOnNext() - {}", notification.value)
                } else if (notification.isOnError) {
                    log.info("doOnError() - {}", notification.error?.message)
                } else if (notification.isOnComplete) {
                    log.info("doOnComplete()")
                }
            }
            .subscribe(
                { log.info("onNext() - {}", it) },
                { e -> log.error("onError() - {}", e.message, e) },
                { log.info("onComplete()") },
                { log.info("onSubscribe()") }
            )
    }

    @Test
    fun doOnDispose_test() {
        Observable.range(1, 5)
            .doOnDispose { log.info("doOnDispose()") }
            .subscribe(
                { log.info("onNext() - {}", it) },
                { e -> log.error("onError() - {}", e.message, e) },
                { log.info("onComplete()") },
                { disposable: Disposable ->
                    log.info("onSubscribe()")
                    disposable.dispose()
                }
            )
    }
}