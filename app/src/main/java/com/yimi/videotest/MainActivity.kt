package com.yimi.videotest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yimi.videotest.pagelist.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {

    private val urls : Array<String> = arrayOf(
        "https://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4",
        "https://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4",
        "https://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4",
        "https://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4",
        "https://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4",
        "https://vfx.mtime.cn/Video/2019/03/19/mp4/190319104618910544.mp4",
        "https://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4",
        "https://vfx.mtime.cn/Video/2019/03/17/mp4/190317150237409904.mp4",
        "https://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4",
        "https://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4",
        "https://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4",
        "https://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4",
        "https://vfx.mtime.cn/Video/2019/03/12/mp4/190312083533415853.mp4",
        "https://vfx.mtime.cn/Video/2019/03/09/mp4/190309153658147087.mp4",
        "https://vfx.mtime.cn/Video/2019/01/15/mp4/190115161611510728_480.mp4")
    private var url: String = "https://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    private var userName:String = "游客"
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        video_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter(urls)
        }

        findViewById<Button>(R.id.main_button_send).setOnClickListener {
            var editMain = findViewById<EditText>(R.id.main_edit).text;
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
//            System.out.println(" C DATE is "+currentDate)
            var appendText:String = "\n" + currentDate + " $userName：" + editMain
            Toast.makeText(getApplicationContext(), "发送成功：" + appendText, Toast.LENGTH_SHORT).show();
            editMain?.let {
                findViewById<EditText>(R.id.main_edit).text = null
                findViewById<TextView>(R.id.text_chat).append(appendText)
            }
        }

        PlayerManager.initPlayer(this@MainActivity)

        PlayerManager.getPlayer()?.run {
            player_view_small.player = this
            url?.let {
                PlayerManager.playWithUrl(this@MainActivity, url)
            }
//            intent.extras?.getString("url")?.let {
//                Toast.makeText(getApplicationContext(), "开播~",
//                        Toast.LENGTH_SHORT).show();
//                PlayerManager.playWithUrl(this@MainActivity, "https://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4")
//            }
        }


        startTimer()

    }

    private fun startTimer() {
        timer = fixedRateTimer("", false, 0, 30000) {
            toast("磕盐汪专属APP正在实时监控，严禁情侣使用！(￢︿̫̿￢☆)")
            Log.d("MainActivity", "磕盐汪专属APP正在实时监控，严禁情侣使用！(￢︿̫̿￢☆)")
        }
    }

    private fun toast(text: String) {
        runOnUiThread {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        PlayerManager.releasePlayer()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.getItemId() === R.id.menu_set_video) {
            //todo
            Toast.makeText(getApplicationContext(), "请设置视频播放源",
                Toast.LENGTH_SHORT).show();
            //________________设置视频播放源_______________________
            val setVideoDialog = SetVideoDialog(this,"重新设置视频播放源","视频播放源地址",object :SetVideoDialog.clickCallBack{
                override fun yesClick(dialog: SetVideoDialog) {
                    val input:String =dialog.editVideo?.text.toString()
                    if (input==null||input .equals("")) {
                        Toast.makeText(getApplicationContext(), "视频播放源不能为空！", Toast.LENGTH_LONG).show()
                    } else {
                        //此处写后续逻辑
//                        dialog.editVideo?.text?:url
//                        if(url != input){
//                            val nametxt = findViewById (R.id.edit_video) as EditText
//                            nametxt.text = url

                            url = input
                            PlayerManager.releasePlayer()
                            PlayerManager?.let{
                                PlayerManager.initPlayer(this@MainActivity)
                                PlayerManager.getPlayer()?.run {
                                    player_view_small.player = this
                                    url?.let {
                                        PlayerManager.playWithUrl(this@MainActivity, url)
                                    }
                                }
                            }
//                        dialog.editVideo?.text ?:  Editable.Factory.getInstance().newEditable(url)  //不报错，但没起作用？
                        Toast.makeText(getApplicationContext(), "视频播放源已设置为：" + url, Toast.LENGTH_LONG).show()
                        dialog?.dismiss()
                    }
                }
            },true)
            setVideoDialog.setOnDismissListener {
                //关闭监听逻辑
            }
            setVideoDialog.show();
            false
        } else if (item.getItemId() === R.id.menu_set_info) {
            //todo
            Toast.makeText(getApplicationContext(), "请设置身份",
                Toast.LENGTH_SHORT).show();
            val setInfoDialog =SetVideoDialog(this,"设置用户昵称","用户昵称",object :SetVideoDialog.clickCallBack{
                override fun yesClick(dialog: SetVideoDialog) {
                    val input:String =dialog.editVideo?.text.toString()
                    if (input==null||input .equals("")) {
                        Toast.makeText(getApplicationContext(), "昵称不能为空！", Toast.LENGTH_LONG).show()
                    } else {
                        //此处写后续逻辑
//                        dialog.editVideo?.text?:url
//                        if(url != input){
//                            val nametxt = findViewById (R.id.edit_video) as EditText
//                            nametxt.text = url

                        userName = input
                        Toast.makeText(getApplicationContext(), "昵称已设置为：" + userName, Toast.LENGTH_SHORT).show()
                        dialog?.dismiss()
                    }
                }
            },true)
            setInfoDialog.setOnDismissListener {
                //关闭监听逻辑
            }
            setInfoDialog.show();
            false
        } else if (item.getItemId() === R.id.menu_secret) {
            //todo
            Toast.makeText(getApplicationContext(), "磕盐汪专属神秘通道！只有聪明可爱的开发者大大才可以进，嗯哼(￢︿̫̿￢☆)",
                Toast.LENGTH_LONG).show();
            false
        } else if (item.getItemId() === R.id.menu_glasses) {
            //todo
            Toast.makeText(getApplicationContext(), "磕盐汪专属APP正在实时监控，严禁情侣使用！(￢︿̫̿￢☆)",
                Toast.LENGTH_LONG).show();
            false
        } else super.onOptionsItemSelected(item)
    }

}