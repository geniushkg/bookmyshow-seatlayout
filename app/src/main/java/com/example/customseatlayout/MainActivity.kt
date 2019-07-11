package com.example.customseatlayout

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.Helper
import com.example.customseatlayout.model.RowData
import com.example.customseatlayout.model.SeatData
import com.example.customseatlayout.model.SeatsResponse
import com.example.customseatlayout.zoomlayout.HelperUtils
import com.example.customseatlayout.zoomlayout.ZoomLayout
import com.example.customseatlayout.zoomlayout.ZoomListener

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

    private fun getDummyData(): SeatsResponse {

        var rowList = ArrayList<RowData>()

        for (i in 0..5){

            var seatList = ArrayList<SeatData>()

            for (j in 0..30){

                val eachSeat = SeatData("single","empty","00$j-$i")

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
        zlContainer = findViewById(R.id.zlContainer)
        zlContainer.setListener(zoomListener)
        setupSeatLayout(apiResponse)
      /*  val childItem = zlContainer.child()
        childItem.isDrawingCacheEnabled=true
        val bitmap:Bitmap = childItem.getDrawingCache(true)
            .copy(Bitmap.Config.ARGB_8888, false)
        childItem.destroyDrawingCache()
        ivPreview.setImageBitmap(bitmap)*/
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
                val layoutParams1 = LinearLayout.LayoutParams(30, LinearLayout.LayoutParams.WRAP_CONTENT);
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

    private var zoomListener:ZoomListener = object : ZoomListener{
        override fun onChildUpdated(child: View) {
            val bitmap = HelperUtils.getBitmapFromView(child)
            val calculatedWidth = (child.measuredWidth/child.height)*400
            ivPreview.layoutParams.width=calculatedWidth+20
            ivGradientBg.layoutParams.width=calculatedWidth+20
            ivPreview.setImageBitmap(bitmap)
            ivGradientBg.visibility=View.VISIBLE
            ivPreview.visibility=View.VISIBLE
            hidePreviewLayoutsAftersometime()
        }
    }
}
