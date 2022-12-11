package com.mor.asiorv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mor.asiorv.model.PairTrashBag
import com.mor.asiorv.model.TrashBag
import com.mor.asiorv.ui.BagListAdapter
import com.mor.asiorv.util.TopSpacingItemDecoration
import com.mor.asiorv.util.visiBool

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),BagListAdapter.Interaction {

    //    list
    private val list2Kg: ArrayList<TrashBag> = arrayListOf()
    private val list1Kg: ArrayList<TrashBag> = arrayListOf()
    private var totalList: MutableList<PairTrashBag> = mutableListOf()

    //adapter
    private lateinit var listAdapter:BagListAdapter

    //    views
    lateinit var layoutEnterDetails:ConstraintLayout
    lateinit var rvLayout:ConstraintLayout
    lateinit var idContent:TextView
    lateinit var roundsRemains:TextView
    lateinit var weightContent:TextView
    lateinit var btnAddItem:FloatingActionButton
    lateinit var btnApproveItem:FloatingActionButton
    lateinit var rv:RecyclerView
    lateinit var pb: ProgressBar
    lateinit var btnCalc: Button
    lateinit var btnTest: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUI()
    }

    private fun setUI() {
        setPointer()
        setClickListener()
        setRv()
        testList()
    }

    private fun addItemToList() {
        if (!weightContent.text.isNullOrEmpty() && !idContent.text.isNullOrEmpty()){
            val weight = weightContent.text.toString().toDouble()
            val title = idContent.text.toString()


            if (weight in 1.01..1.99){
                val index = calcIndex(weight)
                list1Kg.add(index,TrashBag(weight = weight, title = title))

                //updateUI
                changeUi(false)
                Toast.makeText(this, "Bag Added", Toast.LENGTH_SHORT).show()
            }else if (weight > 1.99 && weight <=3){
                list2Kg.add(TrashBag(weight = weight, title = title))

                //updateUI
                changeUi(false)
                Toast.makeText(this, "Bag Added", Toast.LENGTH_SHORT).show()
            }else{
                weightContent.error = "The weight must to be more than 1.01 and less than 3"
            }

        }else{
            Toast.makeText(this, "please enter values", Toast.LENGTH_SHORT).show()
        }

        weightContent.text = ""
        idContent.text = ""

    }


    fun testList(){

        val list2 = arrayListOf<TrashBag>(
            TrashBag("3",2.5),
            TrashBag("5",2.08),
            TrashBag("9",2.2),
            TrashBag("10",2.4)
        )
        val list1 = arrayListOf<TrashBag>(
            TrashBag("1",1.01),
            TrashBag("2",1.08),
            TrashBag("4",1.4),
            TrashBag("6",1.01),
            TrashBag("7",1.8),
            TrashBag("8",1.5),
        )
        prepareDataForList(list1 = list1, list2 = list2)
        totalList.forEach{
            Log.e(TAG, "testList: 1 ${it.trash1} 2 ${it.trash2}  " )
        }
    }

    private fun prepareDataForList(list1:ArrayList<TrashBag>,list2:ArrayList<TrashBag>){
        pb.visiBool(true)

        if(list2.isNotEmpty() || list1.isNotEmpty()){
            list1.sortedBy { it.weight }
            Toast.makeText(this, "calculating...", Toast.LENGTH_SHORT).show()

            var end: Int = list1.size-1
            var start = 0
            while (end>start){
                val current = PairTrashBag(list1[start], list1[end])
                if (current.check()){
                    totalList.add(current)
                    start ++
                    end --
                }else{
                    list2.add(list1[end])
                    end --
                }
            }

            list2.forEach {
                totalList.add(PairTrashBag(it))
            }
            updateAndShowRv(true)
        }else{
            Toast.makeText(this, "please add bags to the list", Toast.LENGTH_SHORT).show()
        }
        pb.visiBool(false)
    }

    private fun updateAndShowRv(isVisible: Boolean) {
        listAdapter.submitList(totalList.toMutableList())
        rvLayout.visiBool(isVisible)
        rv.visiBool(isVisible)
        roundsRemains.text = totalList.size.toString()
    }

    private fun calcIndex(weight: Double): Int {
        return (((weight - 1)*list1Kg.size).toInt())
    }



    //implement interaction
    override fun onItemSelected(position: Int, item: PairTrashBag) {
        totalList.removeAt(position)
        updateAndShowRv(true)
    }


    // ui pointers
    private fun setPointer() {
        btnAddItem = findViewById(R.id.btn_add_item)
        layoutEnterDetails = findViewById(R.id.insert_details_layout)
        idContent = findViewById(R.id.et_insert_id)
        weightContent = findViewById(R.id.et_insert_weight)
        rv = findViewById(R.id.trash_bag_list)
        btnCalc = findViewById(R.id.btn_calculate_now)
        pb = findViewById(R.id.progress_bar)
        rvLayout = findViewById(R.id.rv_layout)
        btnApproveItem = findViewById(R.id.btn_approve_item)
        btnTest = findViewById(R.id.btn_test)
        roundsRemains = findViewById(R.id.remains_rounds_num)
        pb.visiBool(false)
    }

    private fun setClickListener() {
        btnAddItem.setOnClickListener {
            changeUi(true)
        }

        btnApproveItem.setOnClickListener {
            addItemToList()
        }

        btnCalc.setOnClickListener {
            prepareDataForList(list1Kg,list2Kg)
            btnCalc.visiBool(false)
        }

        btnTest.setOnClickListener {
            addFakeData()
        }
    }

    private fun setRv() {
        rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = BagListAdapter(this@MainActivity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            listAdapter = BagListAdapter(this@MainActivity)
            adapter = listAdapter
        }
    }

    private fun addFakeData() {
        for (i in 0..10){
            list2Kg.add(TrashBag("$i",2.0))
        }
        for (i in 11..20){
            list1Kg.add(TrashBag("$i",1.7))
        }
        for (i in 21..30){
            list1Kg.add(TrashBag("$i",1.2))
        }
    }

    private fun changeUi(openTheLayout:Boolean) {
        //1. make the option to add item visible
        layoutEnterDetails.visiBool(openTheLayout)
        btnApproveItem.visiBool(openTheLayout)
        btnAddItem.visiBool(!openTheLayout)
        if (list2Kg.size>0 || list1Kg.size>0){
            btnCalc.visiBool(!openTheLayout)
        }
    }

}
