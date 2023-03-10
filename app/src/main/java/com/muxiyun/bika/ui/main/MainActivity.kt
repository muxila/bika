package com.muxiyun.bika.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.muxiyun.bika.BR
import com.muxiyun.bika.R
import com.muxiyun.bika.adapter.CategoriesAdapter
import com.muxiyun.bika.base.BaseActivity
import com.muxiyun.bika.bean.CategoriesBean
import com.muxiyun.bika.databinding.ActivityMainBinding
import com.muxiyun.bika.ui.account.AccountActivity
import com.muxiyun.bika.ui.apps.AppsActivity
import com.muxiyun.bika.ui.collections.CollectionsActivity
import com.muxiyun.bika.ui.comiclist.ComicListActivity
import com.muxiyun.bika.ui.comment.CommentsActivity
import com.muxiyun.bika.ui.games.GamesActivity
import com.muxiyun.bika.ui.history.HistoryActivity
import com.muxiyun.bika.ui.leaderboard.LeaderboardActivity
import com.muxiyun.bika.ui.mycomments.MyCommentsActivity
import com.muxiyun.bika.ui.notifications.NotificationsActivity
import com.muxiyun.bika.ui.search.SearchActivity
import com.muxiyun.bika.ui.settings.SettingsActivity
import com.muxiyun.bika.utils.*
import com.yalantis.ucrop.UCrop
import me.jingbin.library.skeleton.ByRVItemSkeletonScreen
import me.jingbin.library.skeleton.BySkeleton


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private var cList = ArrayList<CategoriesBean.Category>()
    private lateinit var adapter_categories: CategoriesAdapter
    private lateinit var skeletonScreen: ByRVItemSkeletonScreen

    private val ERROR_PROFILE = 0
    private val ERROR_CATEGORIES = 1
    private var ERROR = this.ERROR_PROFILE //????????????????????????????????????

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.toolbar.title = "??????"
        setSupportActionBar(binding.toolbar)

        val cTitle = arrayOf(
            "??????",
            "?????????",
            "????????????",
            "???????????????",
            "?????????",
            "????????????",
            "????????????"
        )
        val cRes = arrayOf(
            R.drawable.logo_round,
            R.drawable.cat_leaderboard,
            R.drawable.cat_game,
            R.drawable.cat_love_pica,
            R.drawable.cat_forum,
            R.drawable.cat_latest,
            R.drawable.cat_random
        )

        for (index in 0..5) {
            val category = CategoriesBean.Category(
                "",
                false,
                "",
                false,
                "",
                thumb = CategoriesBean.Category.Thumb("", "", ""),
                cTitle[index],
                cRes[index]
            )
            cList.add(index, category)
        }
        adapter_categories = CategoriesAdapter()
        binding.mainRv.layoutManager = GridLayoutManager(
            this@MainActivity,
            if (getWindowWidth() > getWindowHeight()) 6 else 3
        )//??????????????????????????????
        skeletonScreen = BySkeleton
            .bindItem(binding.mainRv)
            .adapter(adapter_categories)// ????????????adapter??????????????????????????????adapter
            .load(R.layout.item_categories_skeleton)// item?????????
            .duration(2000)// ????????????????????????
            .count(18)// item??????
            .show()

        initListener()

        viewModel.getUpdate()

        if (cList.size <= 6) {
            //?????????????????? TODO ???????????? ???????????????
            showProgressBar(true, "??????????????????...")
            viewModel.getProfile() //?????????????????????
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mainNavView.setCheckedItem(R.id.drawer_menu_home)
    }

    //toolbar??????
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                startActivity(SearchActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initListener() {
        //??????
        binding.drawerLayout.addDrawerListener(
            ActionBarDrawerToggle(
                this@MainActivity,
                binding.drawerLayout,
                binding.toolbar,
                R.string.drawer_show,
                R.string.drawer_hide
            )
        )
        (binding.mainNavView.getHeaderView(0)
            .findViewById(R.id.main_drawer_modify) as TextView).setOnClickListener {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show()

        }
        (binding.mainNavView.getHeaderView(0)
            .findViewById(R.id.main_drawer_character) as ImageView).setOnClickListener {
            if (viewModel.userId != "") {
                PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isCameraForegroundService(true)
                    .setSelectionMode(1)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setCropEngine { fragment, srcUri, destinationUri, dataSource, requestCode ->
                        UCrop.of(srcUri, destinationUri, dataSource)
                            .withAspectRatio(1f, 1f)
                            .withMaxResultSize(200, 200) //???????????????????????????
                            .start(fragment.requireActivity(), fragment, requestCode)
                    }
                    .forResult(object : OnResultCallbackListener<LocalMedia> {
                        override fun onResult(result: ArrayList<LocalMedia>) {
                            GlideApp.with(this@MainActivity)
                                .load(R.drawable.placeholder_avatar_2)
                                .into(
                                    binding.mainNavView.getHeaderView(0)
                                        .findViewById(R.id.main_drawer_imageView) as ImageView
                                )
                            viewModel.putAvatar(Base64Util().getBase64(result[0].cutPath))
                        }
                        override fun onCancel() {}
                    })
            }
        }
        binding.mainNavView.setNavigationItemSelectedListener {
            binding.mainNavView.setCheckedItem(it)
            when (it.itemId) {
                R.id.drawer_menu_history -> {
                    startActivity(HistoryActivity::class.java)
                }
                R.id.drawer_menu_bookmark -> {
                    val intent = Intent(this@MainActivity, ComicListActivity::class.java)
                    intent.putExtra("tag", "favourite")
                    intent.putExtra("title", "????????????")
                    intent.putExtra("value", "????????????")
                    startActivity(intent)

                }
                R.id.drawer_menu_mail -> {
                    startActivity(NotificationsActivity::class.java)

                }
                R.id.drawer_menu_chat -> {
                    startActivity(MyCommentsActivity::class.java)

                }
                R.id.drawer_menu_settings -> {
                    startActivity(SettingsActivity::class.java)
                }

            }
            true
        }

        binding.mainRv.setOnItemClickListener { v, position ->
            val intent = Intent(this, ComicListActivity::class.java)
            val datas = adapter_categories.getItemData(position)
            when (position) {
                0 -> {
                    startActivity(Intent(this, CollectionsActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this, LeaderboardActivity::class.java))
                }
                2 -> {
                    startActivity(Intent(this, GamesActivity::class.java))
                }
                3 -> {
                    startActivity(Intent(this, AppsActivity::class.java))
                }
                4 -> {
                    val intentComments = Intent(this, CommentsActivity::class.java)
                    intentComments.putExtra("id", "5822a6e3ad7ede654696e482")
                    intentComments.putExtra("comics_games", "comics")
                    startActivity(intentComments)
                }
                5 -> {
                    intent.putExtra("tag", "latest")
                    intent.putExtra("title", datas.title)
                    intent.putExtra("value", datas.title)
                    startActivity(intent)
                }
                6 -> {
                    intent.putExtra("tag", "random")
                    intent.putExtra("title", datas.title)
                    intent.putExtra("value", datas.title)
                    startActivity(intent)
                }
                else -> {
                    if (datas.isWeb) {
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(
                            "${datas.link}/?token=${
                                SPUtil.get(
                                    this,
                                    "token",
                                    ""
                                )
                            }&secret=pb6XkQ94iBBny1WUAxY0dY5fksexw0dt"
                        )
                        startActivity(intent)
                    } else {
                        intent.putExtra("tag", "categories")
                        intent.putExtra("title", datas.title)
                        intent.putExtra("value", datas.title)
                        startActivity(intent)
                    }
                }
            }

        }
        //??????????????????????????????
        binding.mainLoadLayout.setOnClickListener {
            skeletonScreen.show()
            if (ERROR == ERROR_PROFILE) {
                showProgressBar(true, "??????????????????...")
                viewModel.getProfile()
            } else {
                showProgressBar(true, "??????????????????...")
                viewModel.getCategories()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initViewObservable() {
        //user??????
        viewModel.liveData_profile.observe(this) {
            if (it.code == 200) {
                var fileServer = ""
                var path = ""
                var character = ""
                viewModel.userId=it.data.user._id

                if (it.data.user.avatar != null) {//??????
                    fileServer = it.data.user.avatar.fileServer
                    path = it.data.user.avatar.path
                    GlideApp.with(this@MainActivity)
                        .load(
                            GlideUrlNewKey(
                                fileServer,
                                path
                            )
                        )
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_avatar_2)
                        .into(
                            binding.mainNavView.getHeaderView(0)
                                .findViewById(R.id.main_drawer_imageView) as ImageView
                        )
                }
                if (it.data.user.character != null) { //????????? ???????????????

                    character = it.data.user.character
                    GlideApp.with(this@MainActivity)
                        .load(character)
                        .into(
                            binding.mainNavView.getHeaderView(0)
                                .findViewById(R.id.main_drawer_character) as ImageView
                        )
                }

                val name = it.data.user.name
                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_name) as TextView).text = name //?????????

                val level = it.data.user.level//??????
                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_user_ver) as TextView).text =
                    "Lv.${it.data.user.level}(${it.data.user.exp}/${exp(it.data.user.level)})"//??????

                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_title) as TextView).text = it.data.user.title//??????

                //??????
                val gender = it.data.user.gender
                (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_gender) as TextView).text =
                    when (it.data.user.gender) {
                        "m" -> "(??????)"
                        "f" -> "(??????)"
                        else -> "(?????????)"
                    }

                //????????????
                val text_slogan = (binding.mainNavView.getHeaderView(0)
                    .findViewById(R.id.main_drawer_slogan) as TextView)
                text_slogan.text =
                    if (it.data.user.slogan.isNullOrBlank()) {
                        resources.getString(R.string.slogan)
                    } else {
                        it.data.user.slogan
                    }

                if (!it.data.user.isPunched) {//????????????????????????
                    //????????????????????????
                    if (SPUtil.get(this, "setting_punch", true) as Boolean) {
                        viewModel.punch_In()
                    }
                }

                //??????????????????????????? ????????????????????????
                SPUtil.put(this, "user_fileServer", fileServer)
                SPUtil.put(this, "user_path", path)
                SPUtil.put(this, "user_character", character)
                SPUtil.put(this, "user_name", name)
                SPUtil.put(this, "user_birthday", it.data.user.birthday)
                SPUtil.put(this, "user_gender", gender)
                SPUtil.put(this, "user_level", level)
                SPUtil.put(this, "user_exp", it.data.user.exp)
                SPUtil.put(
                    this,
                    "user_slogan",
                    if (it.data.user.slogan.isNullOrBlank()) "" else it.data.user.slogan
                )
                SPUtil.put(this, "user_title", it.data.user.title)
                SPUtil.put(this, "user_id", it.data.user._id)
                SPUtil.put(this, "user_verified", it.data.user.verified)

                if (cList.size <= 10) {
                    //??????????????????????????????????????? ??????????????????
                    showProgressBar(true, "??????????????????...")
                    viewModel.getCategories() //??????????????????
                }

            } else if (it.code == 401) {
                if (it.error == "1005") {
                    //token ?????? ??????????????????
                    showProgressBar(true, "??????????????????????????????????????????...")
                    viewModel.getSignIn()//???????????? ????????????token
                }
            } else {
                ERROR = this.ERROR_PROFILE
                showProgressBar(
                    false,
                    "???????????????????????????\ncode=${it.code} error=${it.error} message=${it.message}"
                )
            }
        }

        //???????????? ??????????????????????????????????????????????????????
        viewModel.liveData_signin.observe(this) {
            if (it.code == 200) {
                //???????????? ??????token
//                MmkvUtils.putSet("token", bean.data.token)
                SPUtil.put(this, "token", it.data.token)
                showProgressBar(true, "?????????????????????????????????...")
                viewModel.getProfile() //??????????????????

            } else if (it.code == 400) {
                if (it.error == "1004") {
                    //???????????? ????????????????????? ?????????????????????AccountActivity
                    Toast.makeText(this, "?????????????????? ?????????????????????", Toast.LENGTH_SHORT).show()
                    startActivity(AccountActivity::class.java)
                    finish()
                }
            } else {
                //???????????? ???????????? ?????????????????????AccountActivity
                Toast.makeText(
                    this,
                    "??????????????????code=${it.code} error=${it.error} message=${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(AccountActivity::class.java)
                finish()
            }
        }


        //????????????
        viewModel.liveData.observe(this) {
            skeletonScreen.hide()
            if (it.code == 200) {
                binding.mainRv.loadMoreEnd()
                binding.mainLoadLayout.visibility = ViewGroup.GONE
                //TODO ???BUG ????????????
                cList.addAll(it.data.categories)
                adapter_categories.addData(cList)
            } else {
                ERROR = this.ERROR_CATEGORIES
                showProgressBar(
                    false,
                    "???????????????????????????\ncode=${it.code} error=${it.error} message=${it.message}"
                )
            }
        }


        //????????????
        viewModel.liveData_punch_in.observe(this) {
            if (it.code == 200) {
                //???????????? ????????????????????????
                var exp = SPUtil.get(this, "user_exp", 0) as Int
                var level = SPUtil.get(this, "user_level", 1) as Int
                exp += 10
                if (exp > exp(level)) {
                    level += 1
                }
                //??????
                SPUtil.put(this, "user_level", level)
                SPUtil.put(this, "user_exp", exp)
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "????????????message=${it.message} code=${it.code} error=${it.error}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //??????
        viewModel.liveData_update.observe(this) {
            if (it != null && it.code > AppVersion().code()) {
                MaterialAlertDialogBuilder(this)
                    .setTitle("????????? v${it.name}")
                    .setMessage(it.des)
                    .setPositiveButton("??????") { _, _ ->
                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(it.url)
                        startActivity(intent)
                    }
                    .setNegativeButton("??????", null)
                    .show()
            }
        }

        //????????????
        viewModel.liveData_avatar.observe(this) {
            if (it.code == 200) {
                //?????????????????????????????????
                viewModel.getProfile()
            } else {
                Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show()
                //??????????????????????????????
                val fileServer = SPUtil.get(this, "user_fileServer", "") as String
                val path = SPUtil.get(this, "user_path", "") as String
                GlideApp.with(this)
                    .load(
                        if (path != "") { GlideUrlNewKey(fileServer, path) } else R.drawable.placeholder_avatar_2
                    )
                    .placeholder(R.drawable.placeholder_avatar_2)
                    .into(binding.mainNavView.getHeaderView(0)
                        .findViewById(R.id.main_drawer_imageView) as ImageView)
            }
        }
    }

    private fun showProgressBar(show: Boolean, string: String) {
        binding.mainLoadProgressBar.visibility = if (show) ViewGroup.VISIBLE else ViewGroup.GONE
        binding.loadCategoriesError.visibility = if (show) ViewGroup.GONE else ViewGroup.VISIBLE
        binding.mainLoadText.text = string
        binding.mainLoadLayout.isEnabled = !show
    }

    private fun exp(i: Int): Int {
        //???????????????????????????????????????
        return 100 * i * i + (100 * i)
    }

    private fun getWindowHeight(): Int {
        return resources.displayMetrics.heightPixels
    }

    private fun getWindowWidth(): Int {
        return resources.displayMetrics.widthPixels
    }

//    private var expand = "[??????]"
//    private var collapse = "[??????]"
//
//    private fun expandText(contentTextView: TextView) {
//        val text: CharSequence = contentTextView.text
//        val width: Int = contentTextView.width
//        val paint: TextPaint = contentTextView.paint
//        val layout: Layout = contentTextView.layout
//        val line: Int = layout.lineCount
//        if (line > 4) {
//            val start: Int = layout.getLineStart(3)
//            val end: Int = layout.getLineVisibleEnd(3)
//            val lastLine = text.subSequence(start, end)
//            val expandWidth = paint.measureText(expand)
//            val remain = width - expandWidth
//            val ellipsize = TextUtils.ellipsize(
//                lastLine,
//                paint,
//                remain,
//                TextUtils.TruncateAt.END
//            )
//            val clickableSpan: ClickableSpan = object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    collapseText(contentTextView,text)
//                }
//            }
//            val ssb = SpannableStringBuilder()
//            ssb.append(text.subSequence(0, start))
//            ssb.append(ellipsize)
//            ssb.append(expand)
//            ssb.setSpan(
//                ForegroundColorSpan(-0x8c9608),
//                ssb.length - expand.length, ssb.length,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//            )
//            ssb.setSpan(
//                clickableSpan,
//                ssb.length - expand.length, ssb.length,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//            )
//            contentTextView.movementMethod = LinkMovementMethod.getInstance()
//            contentTextView.text = ssb
//        }
//    }
//
//    private fun collapseText(contentTextView: TextView,text: CharSequence) {
//
//        // ?????????????????????????????????????????????????????????????????????
//        val clickableSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                expandText(contentTextView)
//            }
//        }
//        val ssb = SpannableStringBuilder()
//        ssb.append(text.toString().replace(expand, ""))
//        ssb.append(collapse)
//        ssb.setSpan(
//            ForegroundColorSpan(-0x8c9608),
//            ssb.length - collapse.length, ssb.length,
//            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//        )
//        ssb.setSpan(
//            clickableSpan,
//            ssb.length - collapse.length, ssb.length,
//            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//        )
//        contentTextView.text = ssb
//    }
}