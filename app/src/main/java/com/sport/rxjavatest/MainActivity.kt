package com.sport.rxjavatest

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sport.rxjavatest.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val observable = Observable.just(1, 2, 3)

        val single = Single.just(1)

        val flowable = Flowable.just(1, 2, 3)

        val result: Disposable = Observable.just(1, 2, 3, 4, 5)
            .delay(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("TAG", "Value is $it")
            }, {

            })

        disposeBag.add(result)

        Handler().postDelayed({
            Log.d("TAG", "Disposed")
            result.dispose()
        }, 2000)

        binding.btnClick.setOnClickListener {
            Log.d("TAG", "clicked!")
        }

        val dispose: Disposable = dataSource()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {

            })
    }

    private fun dataSource(): Single<List<Int>> {
        return Single.create { subscriber ->
            val list = listOf<Int>(1, 2, 3, 4, 4, 5, 6, 7, 8)
            subscriber.onSuccess(list)
        }
    }
}