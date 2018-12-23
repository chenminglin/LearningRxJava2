package com.bethena.learningrxjava2;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlatMapTest {

    final CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void flapMapTest() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("----111------" + Thread.currentThread().getName());
                emitter.onNext("11111");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(final String s) throws Exception {
                        System.out.println("----222----" + Thread.currentThread().getName());
                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                                for (int i = 1; i < 1000; i++) {
                                emitter.onNext(222222 + Integer.valueOf(s));
//                                }

                                emitter.onComplete();
                                System.out.println("----333----" + Thread.currentThread().getName());
                            }
                        });
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("----doOnNext----" + Thread.currentThread().getName());
                        System.out.println("-----doOnNext----" + integer);
                    }
                }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("----doOnComplete----" + Thread.currentThread().getName());
            }
        })
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
                        latch.countDown();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("----onComplete----" + Thread.currentThread().getName());
                        latch.countDown();
                    }
                });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void flatMapIterableTest() {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1234568");
                emitter.onComplete();
            }
        }).flatMapIterable(new Function<String, Iterable<Character>>() {
            @Override
            public Iterable<Character> apply(String s) throws Exception {
                char[] chars = s.toCharArray();
                List<Character> characterList = new ArrayList<>();
                for (int i = 0; i < chars.length; i++) {
                    characterList.add(chars[i]);
                }
                return characterList;
            }
        }).subscribe(new Observer<Character>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("----onSubscribe----" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Character o) {
                System.out.println("----onNext----" + Thread.currentThread().getName());
                System.out.println("onNext----" + o);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("----onError----" + Thread.currentThread().getName() + "----" + e.getMessage());
                latch.countDown();
            }

            @Override
            public void onComplete() {
                System.out.println("----onComplete----" + Thread.currentThread().getName());
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void flatMapMaybeTest() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 4; i++) {
                    emitter.onNext(i);
                }

                emitter.onComplete();
            }
        }).flatMapMaybe(new Function<Integer, MaybeSource<String>>() {
            @Override
            public MaybeSource<String> apply(final Integer integer) throws Exception {
                return new MaybeSource<String>() {
                    @Override
                    public void subscribe(MaybeObserver<? super String> observer) {
                        System.out.println("----subscribe-----");
                        if (integer != 4) {
                            observer.onSuccess(integer + "11111");
                        }

                        observer.onComplete();

                    }
                };
            }
        }).subscribe(new RxObserver<String>(latch));

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
