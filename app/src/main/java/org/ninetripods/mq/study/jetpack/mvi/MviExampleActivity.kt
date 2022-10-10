package org.ninetripods.mq.study.jetpack.mvi

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.ninetripods.lib_viewpager2.MVPager2
import org.ninetripods.mq.study.BaseActivity
import org.ninetripods.mq.study.R
import org.ninetripods.mq.study.jetpack.mvi.adapter.RankAdapter
import org.ninetripods.mq.study.jetpack.mvi.base.*
import org.ninetripods.mq.study.kotlin.ktx.flowWithLifecycle2
import org.ninetripods.mq.study.kotlin.ktx.id

/**
 * MVI示例
 */
class MviExampleActivity : BaseMviActivity() {

    private val mBtnQuest: Button by id(R.id.btn_request)
    private val mToolBar: Toolbar by id(R.id.toolbar)
    private val mContentView: ViewGroup by id(R.id.cl_content_view)
    private val mViewPager2: MVPager2 by id(R.id.mvp_pager2)
    private val mRvRank: RecyclerView by id(R.id.rv_view)

    private val mViewModel: MViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.activity_wan_android_mvi
    }

    override fun initViews() {
        initToolBar(mToolBar, "Jetpack MVI", true, true, BaseActivity.TYPE_BLOG)
        mRvRank.layoutManager = GridLayoutManager(this, 2)
    }

    override fun initEvents() {
        registerEvent()
        mBtnQuest.setOnClickListener {
            mViewModel.dispatch(MviEvent.Toast)
            //请求数据
            mViewModel.dispatch(MviEvent.Banner)
            mViewModel.dispatch(MviEvent.Detail)
        }
    }

    override fun getVModel(): BaseViewModel {
        return mViewModel
    }

    private fun registerEvent() {
        /**
         * 一次性消费事件
         */
        mViewModel.singleUiState.flowWithLifecycle2(this,
            prop1 = MviSingleState::singleUiState) { singleUiState ->
            when (singleUiState) {
                is HomeSingleUiState.ShowToast ->
                    Toast.makeText(this@MviExampleActivity,
                        singleUiState.message, Toast.LENGTH_LONG).show()
            }

        }
        mViewModel.viewState.flowWithLifecycle2(this, prop1 = MviState::bannerUiState) { state ->
            when (state) {
                is BannerUiState.INIT -> {}
                is BannerUiState.SUCCESS -> {
                    mViewPager2.visibility = View.VISIBLE
                    mBtnQuest.visibility = View.GONE
                    val imgs = mutableListOf<String>()
                    for (model in state.models) {
                        imgs.add(model.imagePath)
                    }
                    mViewPager2.setIndicatorShow(true).setModels(imgs).start()
                }
            }

        }

        mViewModel.viewState.flowWithLifecycle2(this,
            prop1 = MviState::detailUiState) { state ->
            when (state) {
                is DetailUiState.INIT -> {}
                is DetailUiState.SUCCESS -> {
                    mRvRank.visibility = View.VISIBLE
                    val list = state.detail.datas
                    mRvRank.adapter = RankAdapter().apply { setModels(list) }
                }
            }

        }
    }

    override fun retryRequest() {
        //点击屏幕重试
        mViewModel.dispatch(MviEvent.Toast)
        mViewModel.dispatch(MviEvent.Banner)
        mViewModel.dispatch(MviEvent.Detail)
    }

    /**
     * 展示Loading、Empty、Error视图等
     */
    override fun getStatusOwnerView(): View? {
        return mContentView
    }

}