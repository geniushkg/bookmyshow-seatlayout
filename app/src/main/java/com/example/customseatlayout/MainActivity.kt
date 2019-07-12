package com.example.customseatlayout

import android.content.Context
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.customseatlayout.model.RowData
import com.example.customseatlayout.model.SeatData
import com.example.customseatlayout.model.SeatsResponse
import com.example.customseatlayout.zoomlayout.HelperUtils
import com.example.customseatlayout.zoomlayout.ZoomLayout
import com.example.customseatlayout.zoomlayout.ZoomListener
import com.otaliastudios.zoom.ZoomEngine

class MainActivity : AppCompatActivity() {


    private fun hidePreviewLayoutsAftersometime() {
         Handler().postDelayed({
             ivGradientBg.visibility=View.GONE
             ivPreview.visibility=View.GONE
         },300)
    }

    private var apiResponse:SeatsResponse = getDummyData()
    private  lateinit var tblContainer:TableLayout
    private lateinit var zlContainer:ZoomLayout
    private lateinit var ivPreview:ImageView
    private lateinit var ivGradientBg:ImageView
    private var context:Context=this
    private lateinit var zllib:com.otaliastudios.zoom.ZoomLayout

    private fun getDummyData(): SeatsResponse {

        var rowList = ArrayList<RowData>()

        for (i in 0..5){

            var seatList = ArrayList<SeatData>()

            for (j in 0..30){
                val eachSeat = SeatData("single","empty","$i-$j")
                seatList.add(eachSeat)
            }
            val rowData = RowData(seatList,"00")
            rowList.add(rowData)
        }

        var response = SeatsResponse(rowList,"1")

        return response

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        ivPreview = findViewById(R.id.ivPreview)
        ivGradientBg = findViewById(R.id.ivGradientBg)
        tblContainer = findViewById(R.id.tblSeatsContainer)
        //zlContainer = findViewById(R.id.zlContainer)
        //zlContainer.setListener(zoomListener)
        zllib = findViewById(R.id.zllib)
        zllib.engine.addListener(zlliblistener)
        setupSeatLayout(apiResponse)
    }

    private fun setupSeatLayout(apiResponse: SeatsResponse) {

        for (eachRow in apiResponse.rowList){
            val tRow:TableRow = TableRow(context)
            val layoutParam = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            layoutParam.setMargins(10,10,10,10)
            tRow.layoutParams=layoutParam
            tRow.gravity=Gravity.CENTER_HORIZONTAL
            val eachRowContainer = LinearLayout(this);
            val layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
            eachRowContainer.setOrientation(LinearLayout.HORIZONTAL);
            eachRowContainer.setLayoutParams(layoutParams)

            for (eachSeat in eachRow.seatList){
                val seatbtn = TextView(this)
                seatbtn.tag = eachSeat
                val lp1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                val layoutParams1 = LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(10, 25, 10, 25);
                //  seatbtn.textSize = 12f
                // seatbtn.minWidth = 70
                seatbtn.layoutParams = layoutParams1
                seatbtn.gravity = Gravity.CENTER
                seatbtn.setBackgroundResource(R.drawable.ic_your_seats)
                seatbtn.setOnClickListener {
                    Toast.makeText(context,eachSeat.areaCode,Toast.LENGTH_SHORT).show()
                }
                eachRowContainer.addView(seatbtn)
            }
            tRow.addView(eachRowContainer)
            tblContainer.addView(tRow)
        }
    }

    private var zlliblistener:ZoomEngine.Listener = object : ZoomEngine.Listener{
        override fun onIdle(engine: ZoomEngine) {

        }

        override fun onUpdate(engine: ZoomEngine, matrix: Matrix) {


            val calculatedHeight = tblContainer.height/4
            val calculatedWidth = tblContainer.width/4
            val locOnScreen = IntArray(2)
            tblContainer.getLocationOnScreen(locOnScreen)
            val rectOfTbl = HelperUtils.locateViewWithZoom(tblContainer,engine.realZoom)
            val rectOfZLayout = HelperUtils.locateView(zllib)

            Log.d("CustomListener","Child left ${tblContainer.left} right ${tblContainer.right}")
           // Log.d("CustomListener","visible left ${reactActualVisible.left} right ${reactActualVisible.right}")
            Log.d("CustomListener","loc x ${locOnScreen[0]} y ${locOnScreen[1]}")
            Log.d("CustomListener","loc left  ${rectOfTbl.left} loc right ${rectOfTbl.right}")
            Log.d("CustomListener","loc top  ${rectOfTbl.top} loc bottom ${rectOfTbl.bottom}")

            Log.d("CustomListener"," loc zl left ${rectOfZLayout.left}  loc zl right ${rectOfZLayout.right}")
            Log.d("CustomListener"," loc zl top ${rectOfZLayout.top}  loc zl bottom ${rectOfZLayout.bottom}")

            val rectOnPreview = Rect()

            val bitmap = HelperUtils.getBitmapFromView(tblContainer)

            ivPreview.layoutParams.height=calculatedHeight
            ivPreview.layoutParams.width=calculatedWidth
            ivGradientBg.layoutParams.height=calculatedHeight
            ivGradientBg.layoutParams.width=calculatedWidth
            ivPreview.setImageBitmap(bitmap)
            ivGradientBg.visibility=View.VISIBLE
            ivPreview.visibility=View.VISIBLE
            hidePreviewLayoutsAftersometime()
        }
    }

    private var zoomListener:ZoomListener = object : ZoomListener{
        override fun onChildUpdated(child: View) {

            val reactActualVisible = Rect()
            child.getGlobalVisibleRect(reactActualVisible)
            val widthOfDraw = reactActualVisible.right-reactActualVisible.left
            val heightOfDraw = reactActualVisible.bottom-reactActualVisible.top
            val bitmap = HelperUtils.getBitmapFromView(child)
            val calculatedHeight = child.height/2
            val calculatedWidth = child.width/2
            val locOnScreen = IntArray(2)
            child.getLocationOnScreen(locOnScreen)

            Log.d("CustomListener","Child left ${child.left} right ${child.right}")
            Log.d("CustomListener","visible left ${reactActualVisible.left} right ${reactActualVisible.right}")
            Log.d("CustomListener","loc x ${locOnScreen[0]} y ${locOnScreen[1]}")

            ivPreview.layoutParams.height=calculatedHeight+10
            ivPreview.layoutParams.width=calculatedWidth+10
            ivGradientBg.layoutParams.height=calculatedHeight+10
            ivGradientBg.layoutParams.width=calculatedWidth+10
            ivPreview.setImageBitmap(bitmap)
            ivGradientBg.visibility=View.VISIBLE
            ivPreview.visibility=View.VISIBLE
            hidePreviewLayoutsAftersometime()

        }
    }
}
