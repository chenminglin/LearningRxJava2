package com.bethena.learningrxjava2;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxJava2Test {

    final String TAG = getClass().getSimpleName();

    final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void observableTest() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("----subscribe----" + Thread.currentThread().getName());


                emitter.onNext(1);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                        System.out.println("----accept----" + Thread.currentThread().getName());
                    }
                }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("----onSubscribe----" + Thread.currentThread().getName());


            }

            @Override
            public void onNext(Object o) {
                System.out.println("----onNext----" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("----onError----" + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                System.out.println("----onComplete----" + Thread.currentThread().getName());
            }
        });
    }

    @Test
    public void mapTest() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onComplete();
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + " ----";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("----onSubscribe----" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(String s) {
                System.out.println("----onNext----" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("----onError----" + Thread.currentThread().getName());
            }

            @Override
            public void onComplete() {
                System.out.println("----onComplete----" + Thread.currentThread().getName());
            }
        });
    }

    @Test
    public void concatTest() {

        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("observable1");
                emitter.onNext(1111);
                emitter.onComplete();//这里写了onComplete，observable2则继续走
            }
        });

        Observable observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("observable2");
                emitter.onNext(22222);
                emitter.onComplete();
            }
        });

        Observable.concat(observable1, observable2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("----onSubscribe----" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer o) {
                        System.out.println("----onNext----" + Thread.currentThread().getName());
                        System.out.println("onNext----" + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("----onError----" + Thread.currentThread().getName() + "----" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("----onComplete----" + Thread.currentThread().getName());
                    }
                });

    }

    @Test
    public void concatWithTest() {

        Observable observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("observable2");
                emitter.onNext(22222);
                emitter.onComplete();
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                System.out.println("observable1");
                emitter.onNext(1111);
//                emitter.onComplete();//这里写了onComplete，observable2则继续走
            }
        }).concatWith(observable2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("----onSubscribe----" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer o) {
                        System.out.println("----onNext----" + Thread.currentThread().getName());
                        System.out.println("onNext----" + o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("----onError----" + Thread.currentThread().getName() + "----" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("----onComplete----" + Thread.currentThread().getName());
                    }
                });
    }




}
