package com.jinny.lottonum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val clearBtn: Button by lazy {
        findViewById(R.id.cleanBtn)
    }
    private val addBtn: Button by lazy {
        findViewById(R.id.addBtn)
    }
    private val runBtn: Button by lazy {
        findViewById(R.id.runBtn)
    }
    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numPicker)
    }
    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()
    private val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.numTextView1),
            findViewById(R.id.numTextView2),
            findViewById(R.id.numTextView3),
            findViewById(R.id.numTextView4),
            findViewById(R.id.numTextView5),
            findViewById(R.id.numTextView6)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initClearButton() {
        clearBtn.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun initAddButton() {
        addBtn.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호 입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumBackground(numberPicker.value,textView)
            pickNumberSet.add(numberPicker.value)
        }
    }
    private fun setNumBackground (number: Int, textView: TextView){
        when(number){
            in 1..10 -> textView.background = ContextCompat.getDrawable(this,R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this,R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this,R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this,R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this,R.drawable.circle_green)
        }
    }
    private fun initRunButton() {
        runBtn.setOnClickListener {
            val list = getRandomNum()
            didRun = true
            list.forEachIndexed {
                //forEach와 다르게 index와 number값 사용이 된다.
                    index, i ->
                val textView = numTextViewList[index]
                textView.text = i.toString()
                textView.isVisible = true
                setNumBackground(i,textView)
            }
        }
    }

    private fun getRandomNum(): List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }
        numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)
        //return newList.sorted() //오름차 순 정렬
        return newList
    }
}