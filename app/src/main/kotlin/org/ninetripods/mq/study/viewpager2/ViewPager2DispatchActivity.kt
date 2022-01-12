package org.ninetripods.mq.study.viewpager2

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import org.ninetripods.mq.study.BaseActivity
import org.ninetripods.mq.study.R
import org.ninetripods.mq.study.kotlin.ktx.id
import org.ninetripods.mq.study.util.NavitateUtil

/**
 * ViewPager2
 */
class ViewPager2DispatchActivity : BaseActivity() {

    private val mTvBaseVp2: TextView by id(R.id.tv_vp2_base)
    private val mTvNestedScrollVp2: TextView by id(R.id.tv_vp2_nested_scroll)
    private val mTvVerticalRoll: TextView by id(R.id.tv_vp2_vertical)
    private val mToolBar: Toolbar by id(R.id.toolbar)

    override fun setContentView() {
        setContentView(R.layout.activity_view_pager2_dispatch)
    }

    override fun initViews() {
        initToolBar(mToolBar, "ViewPager2", true, false)
    }

    override fun initEvents() {
        mTvBaseVp2.setOnClickListener(this)
        mTvNestedScrollVp2.setOnClickListener(this)
        mTvVerticalRoll.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_vp2_nested_scroll -> NavitateUtil.startActivity(
                this,
                ViewPager2Activity::class.java
            )
        }
    }
}