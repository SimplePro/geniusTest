package com.wotin.geniustest.activity.test

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.wotin.geniustest.AppStorage
import com.wotin.geniustest.R
import com.wotin.geniustest.activity.MainActivity
import com.wotin.geniustest.adapter.test.TestModeRecyclerViewAdapter
import com.wotin.geniustest.adapter.test.TestQuicknessSlidingUpPanelRecyclerViewAdapter
import com.wotin.geniustest.customClass.geniusTest.GeniusTestDataCustomClass
import com.wotin.geniustest.customClass.geniusTest.TestModeCustomClass
import com.wotin.geniustest.databinding.ActivityTestBinding
import com.wotin.geniustest.roomMethod.GetRoomMethod
import com.wotin.geniustest.roomMethod.UpdateRoomMethod
import com.wotin.geniustest.service.ConcentractionTestHeartManagementService
import com.wotin.geniustest.service.MemoryTestHeartManagementService
import com.wotin.geniustest.service.QuicknessTestHeartManagementService
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.timer

class TestActivity : AppCompatActivity(), TestModeRecyclerViewAdapter.ModeClickedInterface, QuicknessTestHeartManagementService.QuicknessTestHeartManagementIsSaved,
    ConcentractionTestHeartManagementService.ConcentractionTestHeartManagementIsSaved,
    MemoryTestHeartManagementService.memoryTestHeartManagementIsSaved, BillingProcessor.IBillingHandler {

    lateinit var recyclerViewAdapter: TestModeRecyclerViewAdapter
    lateinit var modeList: ArrayList<TestModeCustomClass>

    lateinit var geniusTestData: GeniusTestDataCustomClass

    lateinit var mBinding : ActivityTestBinding

    var bp : BillingProcessor? = null
    lateinit var storage : AppStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)


        storage = AppStorage(this)
        bp = BillingProcessor(this, getString(R.string.license_key), this)
        bp!!.initialize()

        setModeList()

        setBottomDragView()

        mBinding.closeTestDragLayoutButton.setOnClickListener {
            mBinding.testSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
        }

        // 3초마다 윈도우를 조정해주는 메소드 실행.
        controlWindowOnTimer()

        mBinding.testRefreshLayout.setOnRefreshListener {
            setModeList()
            mBinding.testRefreshLayout.isRefreshing = false
        }


        recyclerViewAdapter =
        TestModeRecyclerViewAdapter(
            modeList,
            this
        )
        mBinding.testModeRecyclerview.apply {
            adapter = recyclerViewAdapter
            layoutManager =
                LinearLayoutManager(this@TestActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        test_back_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mBinding.testShop.setOnClickListener { 
//            인앱 결제 구현하는 부분
            val dialog = AlertDialog.Builder(this)
            val EDialog = LayoutInflater.from(this)
            val MView = EDialog.inflate(R.layout.shop_dialog_layout, null)
            val builder = dialog.create()
            val noAdsLayout = MView.findViewById<CardView>(R.id.no_ads_layout)
            val unlimitedLayout = MView.findViewById<CardView>(R.id.unlimited_try_layout)
            val noAdsAndUnlimitedLayout = MView.findViewById<CardView>(R.id.no_ads_and_unlimited_try_layout)
            noAdsLayout.setOnClickListener { // 광고 제거 버튼을 눌렀을 때
                if(storage.purchasedNoAds()) Toast.makeText(applicationContext, "이미 구매하셨습니다!", Toast.LENGTH_LONG).show()
                else bp!!.purchase(this, "no_ads")
            }
            unlimitedLayout.setOnClickListener { // 테스트 제한시간 없애기 버튼을 눌렀을 때
                if(storage.purchasedUnlimitedTry()) Toast.makeText(applicationContext, "이미 구매하셨습니다!", Toast.LENGTH_LONG).show()
                else bp!!.purchase(this, "unlimited_try")
            }
            noAdsAndUnlimitedLayout.setOnClickListener { // 광고 제거 & 테스트 제한시간 없애기 버튼을 눌렀을 때
                if(storage.purchasedUnlimitedTry() || storage.purchasedNoAds()) Toast.makeText(applicationContext, "이미 구매하셨습니다!", Toast.LENGTH_LONG).show()
                else bp!!.purchase(this, "no_ads_and_unlimited_try")
            }
            builder.setView(MView)
            builder.show()
        }

    }

    //3초마다 윈도우를 조정해주는 메소드.
    private fun controlWindowOnTimer() {
        timer(period = 3000)
        {
            runOnUiThread {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun modeClicked(mode: String) {
        when (mode) {
            "기억력 테스트" -> {
                ContextCompat.startForegroundService(this, Intent(applicationContext, MemoryTestHeartManagementService::class.java))
                recyclerViewAdapter.notifyDataSetChanged()
                val service = MemoryTestHeartManagementService()
                service.setMemoryInterface(this)
            }
            "집중력 테스트" -> {
                ContextCompat.startForegroundService(this, Intent(applicationContext, ConcentractionTestHeartManagementService::class.java))
                recyclerViewAdapter.notifyDataSetChanged()
                val service = ConcentractionTestHeartManagementService()
                service.setConcentractionInterface(this)
            }
            "순발력 테스트" -> {
                mBinding.testSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setModeList() {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {

                geniusTestData = GetRoomMethod().getGeniusTestData(applicationContext)

                modeList = arrayListOf(
                    TestModeCustomClass(
                        "기억력 테스트",
                        geniusTestData.memoryScore.toInt(),
                        geniusTestData.memoryDifference + "%"
                    ),
                    TestModeCustomClass(
                        "집중력 테스트",
                        geniusTestData.concentractionScore.toInt(),
                        geniusTestData.concentractionDifference + "%"
                    ),
                    TestModeCustomClass(
                        "순발력 테스트",
                        geniusTestData.quicknessScore.toFloat().toInt(),
                        geniusTestData.quicknessDifference + "%"
                    )
                )

                val geniusTestModeData = GetRoomMethod().getTestModeData(applicationContext)
                geniusTestModeData[0].apply {
                    score = geniusTestData.memoryScore.toInt()
                    difference = geniusTestData.memoryDifference
                }
                geniusTestModeData[1].apply {
                    score = geniusTestData.concentractionScore.toInt()
                    difference = geniusTestData.concentractionDifference
                }
                geniusTestModeData[2].apply {
                    score = geniusTestData.quicknessScore.toFloat().toInt()
                    difference = geniusTestData.quicknessDifference
                }

                UpdateRoomMethod().updateTestModeData(applicationContext, geniusTestModeData[0])
                UpdateRoomMethod().updateTestModeData(applicationContext, geniusTestModeData[1])
                UpdateRoomMethod().updateTestModeData(applicationContext, geniusTestModeData[2])
            }
        }

        modeList = GetRoomMethod().getTestModeData(applicationContext)
        Log.d("TAG", "testModeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this@TestActivity)
        mBinding.testModeRecyclerview.adapter = recyclerViewAdapter

    }


    override fun onResume() {
        super.onResume()
        Thread.sleep(500)
        setModeList()
    }

    override fun memoryTestHeartManagement() {
        Log.d("TAG", "memoryTestHeartManagement: start memoryTestHeartManagement")
        modeList = GetRoomMethod().getTestModeData(this.applicationContext)
        Log.d("TAG", "testHeartManagement: modeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        mBinding.testModeRecyclerview.apply {
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun quicknessTestHeartManagement() {
        Log.d("TAG", "quicknessTestHeartManagement: start quicknessTestHeartManagement")
        modeList = GetRoomMethod().getTestModeData(this.applicationContext)
        Log.d("TAG", "testHeartManagement: modeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        mBinding.testModeRecyclerview.apply {
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    override fun concentractionTestHeartManagement() {
        Log.d("TAG", "concentractionTestHeartManagement: start concentractionTestHeartManagement")
        modeList = GetRoomMethod().getTestModeData(this.applicationContext)
        Log.d("TAG", "testHeartManagement: modeList is $modeList")
        recyclerViewAdapter = TestModeRecyclerViewAdapter(modeList, this)
        mBinding.testModeRecyclerview.apply {
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun setBottomDragView() {
        val brainModeList = arrayListOf<String>("우뇌형", "좌뇌형")
        val dragRecyclerViewAdapter = TestQuicknessSlidingUpPanelRecyclerViewAdapter(brainModeList)
        mBinding.testDragRecyclerView.apply {
            adapter = dragRecyclerViewAdapter
            layoutManager = LinearLayoutManager(this@TestActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onBillingInitialized() {
        // * 처음에 초기화됬을때.
    }

    override fun onPurchaseHistoryRestored() {
        // * 구매 정보가 복원되었을때 호출
        // bp.loadOwnedPurchasesFromGoogle() 하면 호출 가능
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        // * 구매 완료시 호출
        // productId: 구매한 sku (ex) no_ads)
        // details: 결제 관련 정보
        when (productId) {
            "no_ads" -> storage.setPurchasedNoAds()
            "unlimited_try" -> {
                val modeList = GetRoomMethod().getTestModeData(applicationContext)
                modeList[0].start = true
                modeList[1].start = true
                modeList[2].start = true
                UpdateRoomMethod().updateTestModeData(applicationContext, modeList[0])
                UpdateRoomMethod().updateTestModeData(applicationContext, modeList[1])
                UpdateRoomMethod().updateTestModeData(applicationContext, modeList[2])
                storage.setPurchasedUnlimitedTry()
            }
            "no_ads_and_unlimited_try" -> {
                val modeList = GetRoomMethod().getTestModeData(applicationContext)
                modeList[0].start = true
                modeList[1].start = true
                modeList[2].start = true
                UpdateRoomMethod().updateTestModeData(applicationContext, modeList[0])
                UpdateRoomMethod().updateTestModeData(applicationContext, modeList[1])
                UpdateRoomMethod().updateTestModeData(applicationContext, modeList[2])
                storage.setPurchasedNoAdsAndUnlimitedTry()
            }
        }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        // * 구매 오류시 호출
        // errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 일때는
        // 사용자가 단순히 구매 창을 닫은것임으로 이것 제외하고 핸들링하기.
        Toast.makeText(applicationContext, "결제가 취소되었습니다.", Toast.LENGTH_SHORT).show()
        Log.e("TAG", "errorCode is $errorCode, error is $error")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(!bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        if(bp != null) bp!!.release()
        super.onDestroy()
    }
}