package com.solang.maprecord.ui

import android.app.PendingIntent.getActivity
import com.solang.maprecord.R
import com.solang.maprecord.base.BaseActivity
import android.nfc.FormatException

import com.solang.maprecord.utils.NfcUtils

import android.nfc.NdefRecord

import android.nfc.NdefMessage

import android.nfc.NfcAdapter

import android.os.Parcelable

import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.solang.maprecord.utils.toast
import java.io.IOException
import java.io.UnsupportedEncodingException


class NfcActivity : BaseActivity() {
    var TAG = "22222"
    override fun getLayoutId(): Int {
        return R.layout.activity_nfc
    }

    override fun initView() {
        super.initView()
        NfcUtils(this)
    }
    override fun onResume() {
        super.onResume()
        //设定intentfilter和tech-list。如果两个都为null就代表优先接收任何形式的TAG action。也就是说系统会主动发TAG intent。
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processIntent(intent!!)
    }

    override fun onPause() {
        super.onPause()
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        NfcUtils.mNfcAdapter = null;
    }

    //  这块的processIntent() 就是处理卡中数据的方法
    fun processIntent(intent: Intent) {
//        val rawmsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
//        val msg = rawmsgs[0] as NdefMessage
//        val records = msg.records
//        val resultStr = String(records[0].payload)
//        // 返回的是NFC检查到卡中的数据
//        Log.e(TAG, "processIntent: $resultStr")
        try {
            // 检测卡的id
            val id = NfcUtils.readNFCId(intent)
            Log.e(TAG, "processIntent--id: $id")
            // NfcUtils中获取卡中数据的方法
            val result = NfcUtils.readNFCFromTag(intent)
            Log.e(TAG, "processIntent--result: $result")
            // 往卡中写数据
            toast(result)
            val data = "this.is.write"
//            NfcUtils.writeNFCToTag(data, intent)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}