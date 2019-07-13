package com.example.customseatlayout

import android.content.Context
import android.graphics.Matrix
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
import com.otaliastudios.zoom.ZoomEngine

class MainActivity : AppCompatActivity() {




    private var apiResponse:SeatsResponse = getDummyData()
    private  lateinit var tblContainer:TableLayout
    private lateinit var ivPreview:ImageView
    private lateinit var ivGradientBg:ImageView
    private var context:Context=this
    private lateinit var zllib:com.otaliastudios.zoom.ZoomLayout
    private var counter = 0
    /**
     * this method is for populating data, in real project
     * data should be coming from api response
     */
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
            counter++
            if (counter>1000){
                counter=0
                return
            }
            if (counter%3==0){
                return
            }


            val calculatedHeight = tblContainer.height/4
            val calculatedWidth = tblContainer.width/4
            val rectOfTbl = HelperUtils.locateViewWithZoom(tblContainer,engine.realZoom)
            val rectVisibleArea = HelperUtils.locateView(zllib)

            // this comments are for understanding purpose only
            Log.d("CustomListener","loc left  ${rectOfTbl.left} loc right ${rectOfTbl.right}")
            Log.d("CustomListener","loc top  ${rectOfTbl.top} loc bottom ${rectOfTbl.bottom}")
            Log.d("CustomListener"," loc zl left ${rectVisibleArea.left}  loc zl right ${rectVisibleArea.right}")
            Log.d("CustomListener"," loc zl top ${rectVisibleArea.top}  loc zl bottom ${rectVisibleArea.bottom}")


            var leftDif=0
            var topDif=0
            var rightDif=0
            var bottomDif=0

            if (rectOfTbl.left<rectVisibleArea.left){
                leftDif=rectVisibleArea.left-rectOfTbl.left
            }else{
                leftDif=0
            }

            if (rectOfTbl.top<rectVisibleArea.top){
                topDif=rectVisibleArea.top-rectOfTbl.top
            }else{
                topDif=0
            }

            if (rectOfTbl.right>rectVisibleArea.right){
                rightDif=rectOfTbl.right-rectVisibleArea.right
            }else{
                rightDif=0
            }

            if (rectOfTbl.bottom>rectVisibleArea.bottom){
                bottomDif=rectOfTbl.bottom-rectVisibleArea.bottom
            }else{
                bottomDif=0
            }

            //scale back to original values
            if (engine.realZoom>1.0) {
                if (leftDif!=0) leftDif = Math.round(leftDif / engine.realZoom)
                if (topDif!=0)  topDif = Math.round(topDif / engine.realZoom)
                if (rightDif!=0) rightDif = Math.round(rightDif / engine.realZoom)
                if (bottomDif !=0) bottomDif = Math.round(bottomDif/engine.realZoom)
            }

            // genarate bitmap image of layout with rectangle
            val bitmap = HelperUtils.getBitmapFromView(tblContainer,
                leftDif,topDif,rightDif,bottomDif)

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

    /**
     * This method will hide preview layout after specified time
     */
    private fun hidePreviewLayoutsAftersometime() {
        Handler().postDelayed({
            ivGradientBg.visibility=View.GONE
            ivPreview.visibility=View.GONE
        },900)
    }
}
